package com.buaa.edu.compiler;

import java.util.List;

/**
 * 语法分析器
 * @author destiny
 *
 */
public class Parser {
	private List<Token> tokens;	// 要分析的tokens
	private int index;			// tokens下标
	private SyntaxTree tree;	// 最终生成的语法树
	
	public Parser(List<Token> tokens) {
		super();
		this.tokens = tokens;
		index = 0;
		tree = new SyntaxTree();
	}
	
	public List<Token> getTokens() {
		return tokens;
	}

	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public SyntaxTree getTree() {
		return tree;
	}

	public void setTree(SyntaxTree tree) {
		this.tree = tree;
	}
	
	// 判断是否是控制关键字
	private boolean isControl(String word) {
		for(String str : CDefine.keywords[1]) {
			if(str.equals(word)) return true;
		}
		return false;
	}
	
	private boolean isDataType(String word) {
		for(String str : CDefine.keywords[0]) {
			if(str.equals(word)) return true;
		}
		return false;
	}
	
	// 根据一个句型的句首判断句型
	private String judgeSentencePattern() {
		String tokenValue = tokens.get(index).getValue();
		String tokenType = tokens.get(index).getType();
		
		// include句型
		if(tokenType.equals("SHARP") && tokens.get(index + 1).getType().equals("INCLUDE")) {
			return "INCLUDE";
		// 控制句型
		} else if(isControl(tokenValue)) {
			return "CONTROL";
		} else if(isDataType(tokenValue) && tokens.get(index + 1).getType().equals("IDENTIFIER")) {
			
		}
		
	}
	
	public void runParser() throws Exception {
		// 根节点
		tree.setRoot(new SyntaxTreeNode("Sentence"));
		tree.setCurrent(tree.getRoot());
		
		while(index < tokens.size()) {
			String sentencePattern = judgeSentencePattern();
			
			// 如果是include句型
			if(sentencePattern.equals("INCLUDE")) {
				include();
			// 函数声明语句
			} else if(sentencePattern.equals("FUNCTION_STATEMENT")) {
				functionStatement();
			// 声明语句
			} else if(sentencePattern.equals("STATEMENT")) {
				statement();
			// 函数调用
			} else if(sentencePattern.equals("FUNCTION_CALL")) {
				functionCall();
			} else {
				throw new Exception("main error");
			}
		}
	}
}
