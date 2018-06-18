package cn.mldn.action;

import cn.mldn.util.encrypt.EncryptUtil;

public class TestPassword {
	public static void main(String[] args) {
		String pwd = "hello" ;
		System.out.println(EncryptUtil.encrypt(pwd));
		System.out.println("78EBEA978031492D8D0717778601B47E".equals(EncryptUtil.encrypt(pwd)));
	}
}
