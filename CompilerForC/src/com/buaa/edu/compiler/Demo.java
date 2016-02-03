package com.buaa.edu.compiler;

import java.io.BufferedReader;
import java.io.FileReader;

public class Demo {
	public static void main(String[] args) throws Exception {
		String src = "src/input/source.c";
		BufferedReader reader = new BufferedReader(new FileReader(src));
		String line = null;
		StringBuffer sb = new StringBuffer();
		while(null != (line = reader.readLine())) {
			sb.append(line).append("\n");
		}
		reader.close();
		
		//System.out.println(sb.toString());
		
		System.out.println("dasddasd".substring(2, 5));
		
//		Lexer lexer = new Lexer(sb.toString());
		
	}
}
