package cn.mldn.action.oauth;

import java.net.URI;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
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
public class AuthorizeAction {
	@Reference
	private IClientService clientService ;	// 注入IClientService接口对象
	@Value("${oauth.authcode.expire}") 
	private String expire ; // 该内容的配置通过配置文件定义	
	private RedisCache<Object,Object> redisCache ; // 进行Redis缓存注册
	@Resource(name="cacheManager")
	public void setCacheManager(CacheManager cacheManager) {
		this.redisCache = (RedisCache<Object,Object>) cacheManager.getCache("authcodeCache") ;
	}
	@ResponseBody
	@RequestMapping(value="/authorize" ,method=RequestMethod.GET)
	public Object authorize(HttpServletRequest request) {
		try {// OAuth本身是一个处理的标准，那么既然是标准就需要通过标准的路径进行访问
			OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request) ;
			// 必须获得Client_ID，授权注册接入用户的编号（可变），最好利用OAuth标准的请求来处理
			String clientId = oauthRequest.getClientId() ; // 取得发送来的client_id参数内容
			// 获得了ClientID的目的是为了要对这个请求的合法性做出检测
			Client client = this.clientService.getByClientId(clientId) ;
			if (client == null) {	// 客户非法
				OAuthResponse oauthResponse = OAuthASResponse
						.errorResponse(HttpServletResponse.SC_BAD_REQUEST) 
						.setError(OAuthError.TokenResponse.INVALID_CLIENT)
						.setErrorDescription("无效的客户端ID信息")
						.buildJSONMessage() ;
				return new ResponseEntity<String>(oauthResponse.getBody(),
						HttpStatus.valueOf(oauthResponse.getResponseStatus()));
			} 
			// 如果执行此部分则意味着client_id的检测通过，检测通过之后需要跳转到登录页
			Subject subject = SecurityUtils.getSubject() ; // 获得当前用户的信息
			if (!subject.isAuthenticated()) { // 当前用户没有登录认证过
				WebUtils.saveRequest(request); // 登录之后还要回到这个页面
				HttpHeaders headers = new HttpHeaders() ; // 创建http请求信息头
				headers.setLocation(new URI(request.getContextPath() + "/loginShiro.action"));
				return new ResponseEntity<String>(headers,HttpStatus.TEMPORARY_REDIRECT) ;
			}
			// 如果完成认证的处理之后，应该继续生成authcode的内容
			String authCode = null ; // 现在就需要生成一个认证码
			if (client != null) {	// 现在的client_id合法，在登录认证之后执行
				// 要获得一个OAuth的responseType的信息，该信息一定是code
				String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE) ;
				// 明确的描述当前可以处理的responseType类型为code
				if(responseType.equals(ResponseType.CODE.toString())) {
					// 定义一个用于分配认证码的处理程序类，这个类生成的认证码需要设置一个加密处理模式
					OAuthIssuerImpl oauthIssuer = new OAuthIssuerImpl(new MD5Generator()) ;
					authCode = oauthIssuer.authorizationCode() ; // 生成认证码
					// 由于每一个不同的用户请求都会生成不同的authcode，那么将生成的authcode的数据与当前用户名一起保存
					this.redisCache.putEx(authCode, subject.getPrincipal(), this.expire) ;
				} 
			} 
			// 当登录完成之后应该跳转到redirect_url所给定的路径（接入客户服务器的地址）
			OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request,
					HttpServletResponse.SC_FOUND);	// 构建一个回应请求的构造器（code、redirect_url跳转）
			builder.setCode(authCode) ; // 设置authCode的信息
			String redirectUrl = oauthRequest.getRedirectURI() ; // 获得回应路径
			// 创建个回应地址：redirect_url?code=authcode
			OAuthResponse oauthResponse = builder.location(redirectUrl).buildQueryMessage() ;
			HttpHeaders headers = new HttpHeaders() ; // 定义要返回的头部处理信息
			headers.setLocation(new URI(oauthResponse.getLocationUri())); // 设置地址
			return new ResponseEntity<String>(headers, HttpStatus.valueOf(oauthResponse.getResponseStatus())) ; // 回应状态码 
		} catch (Exception e) {	// 如果这个时候程序上出现了异常操作
			e.printStackTrace();
			return new ResponseEntity<String>("服务器内部错误，请稍后重试！",
					HttpStatus.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
		}
	} 
}
