package com.buaa.edu.compiler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

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
	
	// 判断是否是分隔符
	private boolean isDelimiter(String word) {
		for(String str : CDefine.delimiters) {
			if(str.equals(word)) {
				return true;
			}
		}
		return false;
	}
	
	// 判断是否是运算符
	private boolean isOperator(String word) {
		for(String str : CDefine.operators) {
			if(str.equals(word)) {
				return true;
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
				// 匹配和处理"include"
				if(content.substring(i, i+7).equals("include")) {
					tokens.add(new Token(0, "include"));
					i = skipBlank(i + 7);
					if(content.charAt(i) == '\"' || content.charAt(i) == '<') {
						tokens.add(new Token(4, content.charAt(i)));
						char close_flag = content.charAt(i) == '\"'? '\"' : '>';
						i = skipBlank(i + 1);
						// 找到include的头文件
						String lib = "";
						while(content.charAt(i) != close_flag) {
							lib += content.charAt(i);
							i++;
						}
						tokens.add(new Token(1, lib));
						tokens.add(new Token(4, close_flag));
						i = skipBlank(i + 1);
					} else {
						throw new Exception("include error");
					}
				}
			// 如果是字母或者是以下划线开头
			} else if(Character.isLetter(content.charAt(i)) || content.charAt(i) == '_') {
				String tmp = "";
				while(i < content.length() && (Character.isLetterOrDigit(content.charAt(i)) || content.charAt(i) == '_')) {
					tmp += content.charAt(i);
					i++;
				}
				// 关键字
				if(isKeyword(tmp)) {
					tokens.add(new Token(0, tmp));
				// 标识符
				} else {
					tokens.add(new Token(1, tmp));
				}
				i = skipBlank(i);
			// 如果是数字开头
			} else if(Character.isDigit(content.charAt(i))) {
				String tmp = "";
				while(i < content.length()) {
					if(Character.isDigit(content.charAt(i)) 
							|| (content.charAt(i) == '.' && Character.isDigit(content.charAt(i + 1)))) {
						tmp += content.charAt(i);
						i++;
					} else {
						if(content.charAt(i) == '.') {
							throw new Exception("float number error");
						} else break;
					}
				}
				// 常量
				tokens.add(new Token(2, tmp));
				i = skipBlank(i);
			// 如果是分隔符
			} else if(isDelimiter(content.charAt(i) + "")) {
				tokens.add(new Token(4, content.charAt(i)));
				// 如果是字符串常量
				if(content.charAt(i) == '\"') {
					i += 1;
					String tmp = "";
					while(i < content.length()) {
						if(content.charAt(i) != '\"') {
							tmp += content.charAt(i);
							i += 1;
						} else break;
					}
					tokens.add(new Token(5, tmp));
					tokens.add(new Token(4, '\"'));
				}
				i = skipBlank(i + 1);
			// 如果是运算符
			} else if(isOperator(content.charAt(i) + "")) {
				// 如果是++或者--
				if((content.charAt(i) == '+' || content.charAt(i) == '-') 
						&& (content.charAt(i) == content.charAt(i + 1))) {
					tokens.add(new Token(3, content.substring(i, i + 2)));
					i = skipBlank(i + 2);
				// 如果是>=或者<=
				} else if((content.charAt(i) == '>' || content.charAt(i) == '<') 
						&& content.charAt(i + 1) == '=') {
					tokens.add(new Token(3, content.charAt(i) + "="));
					i = skipBlank(i + 2);
				} else {
					tokens.add(new Token(3, content.charAt(i)));
					i = skipBlank(i + 1);
				}
			} else {
				throw new Exception("Unrecognized symbol");
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		String src = "src/input/source.c";
		BufferedReader reader = new BufferedReader(new FileReader(src));
		String line = null;
		StringBuffer sb = new StringBuffer();
		while(null != (line = reader.readLine())) {
			sb.append(line).append("\n");
		}
		reader.close();
		
		Lexer lexer = new Lexer(sb.toString());
		lexer.runLexer();
		List<Token> tokens = lexer.getTokens();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter("src/output/lexer.txt"));
		for(Token e : tokens) {
			writer.write("(" + e.getType() + ", " + e.getValue() + ")\n");
			System.out.println("(" + e.getType() + ", " + e.getValue() + ")");
		}
		writer.close();
	}
}
