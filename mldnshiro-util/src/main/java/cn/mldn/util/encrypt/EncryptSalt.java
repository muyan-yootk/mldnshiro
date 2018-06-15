package cn.mldn.util.encrypt;

import java.util.Base64;

public class EncryptSalt {
	private static final String SALT = "mldnjava" ;	// 盐值的基本处理
	private static final int REPEAT = 5 ; 			// 加密的处理次数
	private EncryptSalt() {}
	/**
	 * 得到进行加密处理的盐值
	 * @return 处理后的盐值
	 */
	public static String getSalt() {
		byte saltData [] = SALT.getBytes() ; // 加密需要通过字节数组
		for (int x = 0 ; x < REPEAT ; x ++) {
			saltData = Base64.getEncoder().encode(saltData) ;
		}
		return new String(saltData) ; 
	}
}
