package cn.mldn.util.encrypt;


public class EncryptUtil {
	private static final int REPEAT = 5 ; 			// 加密的处理次数
	private EncryptUtil() {}
	/**
	 * 实现数据的加密处理
	 * @param password 用户输入的原始密码
	 * @return
	 */ 
	public static String encrypt(String password) {
		String pwd = null ; // 保存的是最终的加密密码
		// 使用MD5进行加密处理，联同盐值一起操作，格式“密码{SALT}”
		pwd = password + "{" + EncryptSalt.getSalt() + "}";
		MD5Code md5 = new MD5Code() ;
		for (int x = 0 ; x < REPEAT ; x ++) {
			pwd = md5.getMD5ofStr(pwd) ;
		}
		return pwd ;
	}
}
