package com.nowcoder.community;

import java.io.IOException;

public class WkTests {

	public static void main(String[] args) {
		String cmd = "C:/wkhtmltopdf/bin/wkhtmltoimage https://baidu.com C:/Users/LJY/Desktop/NowcoderProject/upload/6.png";
		try {
			Runtime.getRuntime().exec(cmd);
			System.out.println("ok");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
