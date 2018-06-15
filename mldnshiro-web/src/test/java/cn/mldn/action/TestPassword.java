package cn.mldn.action;

import cn.mldn.util.encrypt.EncryptUtil;

public class TestPassword {
	public static void main(String[] args) {
		String pwd = "java" ;
		System.out.println(EncryptUtil.encrypt(pwd));
	}
}
