package cn.edu.buaa.lexer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import cn.edu.buaa.constant.LexerDefine;
import cn.edu.buaa.pojo.Token;

public class Lexer {
	
	private List<Token> tokens;
	
	public Lexer() {
		tokens = new ArrayList<Token>();
	}
	
	public void runLexer(String fileName) {
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			String line = null;
			Stack<Integer> stack = new Stack<>();
			stack.push(1);
			while ((line = reader.readLine()) != null) {
				
				line = line.trim();

				if (line.contains("}")) {
					stack.pop();
					int tmp = stack.pop();
					stack.push(tmp + 1);
				}

				if (line.length() != 0) {
					String label = generateLabel(stack);
					solveLine(line, label);
				}
				
				if (line.contains("{")) {
					stack.push(0);
				}

				if (line.length() != 0) {
					int tmp = stack.pop();
					stack.push(tmp + 1);
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private void solveLine(String line, String label) {

		int i = 0;
		Token token = null;
		while (i < line.length()) {
			i = skipBlank(i, line);
			
			// 如果是引入头文件
			if (i == 0 &&line.charAt(i) == '#') {
				token = new Token(4, line.charAt(i), label);
				tokens.add(token);
				
				i = skipBlank(i, line);
				// 匹配和处理"include"
				if ((i + 7) <= line.length() && line.substring(i, i+7).equals("include")) {
					token = new Token(0, "include", label);
					tokens.add(token);
					
					i = skipBlank(i, line);
					if (line.charAt(i) == '\"' || line.charAt(i) == '<') {
						token = new Token(4, line.charAt(i), label);
						tokens.add(token);
						
						char close_flag = line.charAt(i) == '\"'? '\"' : '>';
						i = skipBlank(i + 1, line);
						
						// 找到include的头文件
						String lib = "";
						while(line.charAt(i) != close_flag) {
							lib += line.charAt(i);
							i++;
						}
						
						token = new Token(1, lib, label);
						tokens.add(token);
						token = new Token(4, close_flag, label);
						tokens.add(token);
						
						i = skipBlank(i + 1, line);
					} else {
						
						try {
							throw new Exception("include error [" + label + "] : " + line.charAt(i));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				} else {
					try {
						throw new Exception("Error include [" + label + "] : #");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			// 如果是字母或者是以下划线开头
			} else if (Character.isLetter(line.charAt(i)) || line.charAt(i) == '_') {
				
			
			// 如果是数字开头
			} else if (Character.isDigit(line.charAt(i))) {
			
			// 如果是分隔符
			} else if (isDelimiter(line.charAt(i))) {
				
			// 如果是运算符
			} else if (isOperator(line.charAt(i))) {
				
			} else {
				try {
					throw new Exception("Unrecognized symbol [" + label + " ] : " 
							+ line.charAt(i));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	private int skipBlank(int i, String line) {
		
		while (i < line.length() && isBlank(i, line)) {
			i++;
		}
		
		return i;
	}

	private boolean isBlank(int i, String line) {
		char ch = line.charAt(i);
		return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r';
	}
	
	// 判断是否是分隔符
	private boolean isDelimiter(String word) {
		for(String str : LexerDefine.delimiters) {
			if(str.equals(word)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isDelimiter(char ch) {
		return isDelimiter(ch + "");
	}
	
	// 判断是否是运算符
	private boolean isOperator(String word) {
		for(String str : LexerDefine.operators) {
			if(str.equals(word)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isOperator(char ch) {
		return isOperator(ch + "");
	}
	
	public String generateLabel(Stack<Integer> stack) {
		String v = "";

		for (int i = 0; i < stack.size(); i++) {
			if(i == 0) {
				continue;
			} else if (i == 1) {
				v += stack.get(i);
			} else {
				v += "." + stack.get(i);
			}
		}

		return v;
	}
	
	public String lTrim(String str) {

		int i = str.length() - 1;
		while (i >= 0) {
			if (str.charAt(i) != ' ' || str.charAt(i) != '\t' || str.charAt(i) != '\n') {
				break;
			}
		}

		return str.substring(0, i + 1);
	}
	
	public static void main(String[] args) {
		
		String fileName = "src/main/resources/source/evenSum.c";
		Lexer lexer = new Lexer();
		lexer.runLexer(fileName);
		
		
	}
	
}
