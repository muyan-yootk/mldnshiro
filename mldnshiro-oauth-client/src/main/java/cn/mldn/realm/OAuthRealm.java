package cn.mldn.realm;

import java.util.Map;
import java.util.Set;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.mldn.filter.token.OAuthToken;
import cn.mldn.service.IMemberAuthorizationService;

public class OAuthRealm extends AuthorizingRealm {
	@Reference
	private IMemberAuthorizationService authzService ;
	private String clientId ;	// 应该由客户服务器申请获得
	private String clientSecret ;	// 应该由客户服务器申请获得
	private String redirectUri ; // 返回地址
	private String accessTokenUrl ; // 进行Token操作的地址定义
	private String memberInfoUrl ; // 获得用户信息的路径
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("***** 1、【OAuth-Client】通过OAuth-Server进行登录认证");
		OAuthToken oAuthToken = (OAuthToken) token ; // 强制转型为自定义的OAuthToken，里面有code
		String authCode = (String) oAuthToken.getCredentials() ; // 获取OAuth返回的Code数据
		String mid = this.getMemberInfo(authCode) ;
		return new SimpleAuthenticationInfo(mid, authCode, "memberRealm");
	}
	private String getMemberInfo(String code) {	// 获取用户的信息
		String mid = null ;
		try {
			OAuthClient oauthClient = new OAuthClient(new URLConnectionClient()) ;
			OAuthClientRequest accessTokenRequest = OAuthClientRequest
				.tokenLocation(this.accessTokenUrl) // 设置Token的访问地址
				.setGrantType(GrantType.AUTHORIZATION_CODE)
				.setClientId(this.clientId)
				.setClientSecret(this.clientSecret)
				.setRedirectURI(this.redirectUri)
				.setCode(code) 
				.buildQueryMessage() ;
			// 构建了一个专门用于进行Token数据回应处理的操作类对象，获得Token的请求是POST
			OAuthJSONAccessTokenResponse oauthResponse = oauthClient.accessToken(accessTokenRequest,
					OAuth.HttpMethod.POST);
			String accessToken = oauthResponse.getAccessToken() ; // 获得Token
			// 获得AccessToken设计目的是为了能够通过此Token获得mid的信息，所以此时应该继续构建第二次请求
			// 如果要想获得请求操作一定要设置有accessToken处理信息
			OAuthClientRequest memberInfoRequest = new OAuthBearerClientRequest(this.memberInfoUrl)
					.setAccessToken(accessToken).buildQueryMessage(); // 创建一个请求操作
			// 要进行指定用户信息请求的回应处理项
			OAuthResourceResponse resouceResponse = oauthClient.resource(memberInfoRequest, OAuth.HttpMethod.GET,
					OAuthResourceResponse.class);
			mid = resouceResponse.getBody() ; // 获取mid的信息
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return mid ;
	} 
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("***** 2、【OAuth-Client】客户端本机实现授权管理");
		String mid = (String) principals.getPrimaryPrincipal() ; // 获取用户名
		Map<String, Object> map = this.authzService.listByMember(mid) ; // 获取全部的授权信息（角色与权限）
		SimpleAuthorizationInfo authz = new SimpleAuthorizationInfo() ;
		// 实际的开发应该通过业务层获取所有的角色信息
		authz.setRoles((Set<String>) map.get("allRoles")); // 保存角色，保存到Shiro自己的管理机制里
		authz.setStringPermissions((Set<String>) map.get("allActions")); // 保存权限信息
		return authz ; 
	}

	public void setMemberInfoUrl(String memberInfoUrl) {
		this.memberInfoUrl = memberInfoUrl;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	public void setAccessTokenUrl(String accessTokenUrl) {
		this.accessTokenUrl = accessTokenUrl;
	}

}
