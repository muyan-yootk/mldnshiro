package cn.mldn.action.oauth;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.shiro.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.mldn.util.cache.shiro.RedisCache;

@Controller
public class MemberAction {
	private RedisCache<Object,Object> redisCacheToken ;
	@RequestMapping("/memberInfo")
	public Object memberInfo(HttpServletRequest request) {
		try {	// 用户现在所发送来的信息一定是带有token的信息，将采用地址重写的方式来进行token的发送
			OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.QUERY);
			String accessToken = oauthRequest.getAccessToken() ; // 获得用户发来的token信息
			String mid = null ; // 该用户信息是保存在Redis之中
			try {// 进行指定Token对应用户信息的获取
				mid = (String) this.redisCacheToken.get(accessToken) ; 
			} catch (Exception ew) {}
			if (mid == null) {	// Token有错误，或者Token失效了
				OAuthResponse response = OAuthResponse
						.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
						.setError(OAuthError.ResourceResponse.INVALID_TOKEN) 
						.setErrorDescription("Token信息已失效！")
						.buildJSONMessage() ; // 使用JSON数据进行返回
				return new ResponseEntity<String>(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
			}
			return new ResponseEntity<String>(mid, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("服务器内部错误，请稍后重试！",
					HttpStatus.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
		}
	}
	@Resource(name="cacheManager")
	public void setCacheManager(CacheManager cacheManager) { // 获得缓存操作
		this.redisCacheToken = (RedisCache<Object,Object>) cacheManager.getCache("tokenCache") ;
	}
}
