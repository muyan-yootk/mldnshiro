package cn.mldn.action.oauth;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.mldn.service.IClientService;
import cn.mldn.util.cache.shiro.RedisCache;
import cn.mldn.vo.Client;

@Controller
@PropertySource("classpath:config/oauth.properties")
public class TokenAction {
	@Value("${oauth.token.expire}") 
	private String expire ; // 该内容的配置通过配置文件定义
	@Reference
	private IClientService clientService ;	// 因为需要继续验证client_id、client_secret
	private RedisCache<Object,Object> redisCacheAuthcode ;
	private RedisCache<Object,Object> redisCacheToken ;
	@ResponseBody
	@RequestMapping(value="/accessToken" ,method=RequestMethod.POST)
	public Object accessToken(HttpServletRequest request) {
		try {	// 现在应该构建的是一个OAuthToken请求，如果发现你的请求模式不是post会出错
			OAuthTokenRequest tokenRequest = new OAuthTokenRequest(request) ;
			String mid = null; // 要通过authcode来获得用户名
			// 如果要想获得Token，那么这个时候需要获得两个重要的信息比较：authCode是否存在、client是否合法
			if (tokenRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())) {
				// 在之前获取authcode的时候曾经将用户名保存在了authcode之中，于是此时获得用户名
				String authCode = tokenRequest.getParam(OAuth.OAUTH_CODE); // 获取authCode
				try { // 需要根据authcode查询出保存的用户名
					mid = (String) this.redisCacheAuthcode.get(authCode); // 进行数据的查询
				} catch (Exception e) {
				}
			}
			if (mid == null) { // 此时的authcode不合法
				OAuthResponse response = OAuthResponse
						.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
						.setError(OAuthError.TokenResponse.INVALID_GRANT) 
						.setErrorDescription("授权码错误！")
						.buildJSONMessage() ; // 使用JSON数据进行返回
				return new ResponseEntity<String>(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
			} // 如果现在可以继续向下执行，那么就应该对Client的信息进行完整判断
			String clientId = tokenRequest.getClientId() ; // 取得发送来的client_id参数内容
			Client client = this.clientService.getByClientId(clientId) ;	
			String secret = tokenRequest.getClientSecret() ; // 获取密码
			if (client == null || (!secret.equals(client.getClientSecret()))) {	// 客户非法 
				OAuthResponse oauthResponse = OAuthASResponse
						.errorResponse(HttpServletResponse.SC_BAD_REQUEST) 
						.setError(OAuthError.TokenResponse.INVALID_CLIENT)
						.setErrorDescription("无效的客户端ID信息")
						.buildJSONMessage() ;
				return new ResponseEntity<String>(oauthResponse.getBody(),
						HttpStatus.valueOf(oauthResponse.getResponseStatus()));
			} // 现在一切的检测都到位之后，后面需要处理的部分就是要进行accessToken的生成
			// 设置一个OAuth的编码指派器，此编码继续使用MD5的加密处理形式
			OAuthIssuer oauthIssuer = new OAuthIssuerImpl(new MD5Generator()) ;
			String accessToken = oauthIssuer.accessToken() ;  // 生成一个accessToken
			// 当存在有token之后，该token肯定是要发送给客户端服务器的，但是随后客户端服务器需要通过此token获得内容
			// 本处的程序是将Token作为了Key，而mid作为了value，如果要想做的更加丰富，则可以存储json结构，
			// 例如：{mid:用户编号,name:姓名,deptno:部门编号} 
			this.redisCacheToken.putEx(accessToken, mid, this.expire); 
			// 随后需要将这个Token的信息发送给客户端，需要创建一个响应处理
			OAuthResponse response = OAuthASResponse
					.tokenResponse(HttpServletResponse.SC_OK)
					.setAccessToken(accessToken)
					.setExpiresIn(this.expire)
					.buildJSONMessage() ; 
			return new ResponseEntity<String>(response.getBody(), HttpStatus.valueOf(response.getResponseStatus())); 
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("服务器内部错误，请稍后重试！",
					HttpStatus.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
		}
	} 
	@Resource(name="cacheManager")
	public void setCacheManager(CacheManager cacheManager) { // 获得缓存操作
		this.redisCacheAuthcode = (RedisCache<Object,Object>) cacheManager.getCache("authcodeCache") ;
		this.redisCacheToken = (RedisCache<Object,Object>) cacheManager.getCache("tokenCache") ;
	}
}
