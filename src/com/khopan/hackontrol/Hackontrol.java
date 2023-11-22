package com.khopan.hackontrol;

public class Hackontrol {
	static {
		System.load("D:\\GitHub Repository\\Hackontrol\\Native Library\\x64\\Debug\\Token.dll");
	}

	private static native String getBotToken();

	public static void main(String[] args) {
		System.out.println(Hackontrol.getBotToken());
	}
}
