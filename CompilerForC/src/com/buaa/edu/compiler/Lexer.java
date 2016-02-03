package com.buaa.edu.compiler;

import java.util.ArrayList;
import java.util.List;

import javax.print.DocFlavor.STRING;

/**
 * 词法分析器
 * @author destiny
 *
 */
public class Lexer {
	private String content;
	private List<Token> tokens;

	public Lexer(String content) {
		this.content = content;
		tokens = new ArrayList<Token>();
	}

	public List<Token> getTokens() {
		return tokens;
	}
	
	// 判断是否是空白字符
	private boolean isBlank(int index) {
		char ch = content.charAt(index);
		return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r';
	}
	
	// 跳过空白字符
	private int skipBlank(int index) {
		while(index < content.length() && isBlank(index)) {
			index++;
		}
		return index;
	}
	
	// 判断是否是关键字
	private boolean isKeyword(String word) {
		for(int i = 0; i < CDefine.keywords.length ; i++) {
			for(int j = 0; j < CDefine.keywords[i].length; j++) {
				if(word.equals(CDefine.keywords[i][j])) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void runLexer() throws Exception {
		int i = 0;
		
		while(i < content.length()) {
			i = skipBlank(i);
			
			// 如果是引入头文件
			if(content.charAt(i) == '#') {
				tokens.add(new Token(4, content.charAt(i)));
				i = skipBlank(i + 1);
				// 匹配"include"
				if(content.substring(i, i+7).equals("include")) {
					tokens.add(new Token(0, "include"));
					i = skipBlank(i + 7);
				} else if(content.charAt(i) == '\"' || content.charAt(i) == '<') {
					tokens.add(new Token(4, content.charAt(i)));
					char close_flag = content.charAt(i) == '\"'? '\"' : '>';
					// 找到include的头文件
					String lib = "";
					while(content.charAt(i) != close_flag) {
						lib += content.charAt(i);
						i++;
					}
					tokens.add(new Token(1, lib.trim()));
					tokens.add(new Token(4, close_flag));
					i = skipBlank(i + 1);
					break;
				} else {
					throw new Exception("include error");
				}
			// 如果是字母或者是以下划线开头
			} else if(Character.isLetter(content.charAt(i)) || content.charAt(i) == '_') {
				String tmp = "";
				while(i < content.length() && (Character.isLetterOrDigit(content.charAt(i)) || content.charAt(i) == '_')) {
					tmp += content.charAt(i);
					i++;
				}
				if() {
					
				}
				
			}
			
		}
		
	}
}
