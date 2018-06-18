package cn.mldn.action.oauth;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.mldn.service.IClientService;
import cn.mldn.vo.Client;
@Controller
public class AuthorizeAction {
	@Reference
	private IClientService clientService ;	// 注入IClientService接口对象
	
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
			} else {	// 此时存在有Client信息
				String authCode = null ; // 保存authcode信息
				String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE) ; // 获取OAuth的回应信息类型
				// 明确的描述当前可以处理的responseType类型为code
				if(responseType.equals(ResponseType.CODE.toString())) {
					// 定义一个用于分配认证码的处理程序类，这个类生成的认证码需要设置一个加密处理模式
					OAuthIssuerImpl oauthIssuer = new OAuthIssuerImpl(new MD5Generator()) ;
					authCode = oauthIssuer.authorizationCode() ; // 生成认证码
				}
				return new ResponseEntity<String>(authCode, HttpStatus.OK) ;
			} 
			 
		} catch (Exception e) {	// 如果这个时候程序上出现了异常操作
			e.printStackTrace();
			return new ResponseEntity<String>("服务器内部错误，请稍后重试！",
					HttpStatus.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
		}
	}
}
