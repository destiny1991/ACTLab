package com.buaa.edu.compiler;

import java.beans.Statement;
import java.util.HashMap;
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
	
	// 判断是否是数据类型
	private boolean isDataType(String word) {
		for(String str : CDefine.keywords[0]) {
			if(str.equals(word)) return true;
		}
		return false;
	}
	
	// include句型
	private void include(SyntaxTreeNode father) {
		if(null == father) father = tree.getRoot();
		
		SyntaxTree includeTree = new SyntaxTree();
		includeTree.setRoot(new SyntaxTreeNode("Include"));
		includeTree.setCurrent(includeTree.getRoot());
		tree.addChildNode(includeTree.getRoot(), father);
		
		// include语句中双引号的个数
		int cnt = 0;
		// include语句是否结束
		boolean flag = true;
		while(flag) {
			if(tokens.get(index).getValue().equals("\"")) cnt++;
			if(index >= tokens.size() || cnt >= 2 
					|| tokens.get(index).getValue().equals(">")) {
				flag = false;
			}
			includeTree.addChildNode(
					new SyntaxTreeNode(tokens.get(index).getValue()), includeTree.getRoot());
			index++;
		}
	}
	
	// 声明语句
	private void statement(SyntaxTreeNode father) {
		if(null == father) father = tree.getRoot();
		
		SyntaxTree statementTree = new SyntaxTree();
		statementTree.setRoot(new SyntaxTreeNode("Statement"));
		statementTree.setCurrent(statementTree.getRoot());
		tree.addChildNode(statementTree.getRoot(), father);
		
		// 暂时用来保存当前声明语句的类型，以便于识别多个变量的声明
		String tmpVariableType = null;
		while(index < tokens.size() && !tokens.get(index).getType().equals("SEMICOLON")) {
			// 变量类型
			if(isDataType(tokens.get(index).getValue())) {
				tmpVariableType = tokens.get(index).getValue();
				SyntaxTreeNode variableType = new SyntaxTreeNode("Type");
				statementTree.addChildNode(variableType, null);
				HashMap<String, String> extraInfo = new HashMap<>();
				extraInfo.put("type", tokens.get(index).getValue());
				statementTree.addChildNode(
						new SyntaxTreeNode(
								tokens.get(index).getValue(), 
								"FIELD_TYPE", 
								extraInfo), 
						null);
			// 变量名
			} else if(tokens.get(index).getType().equals("IDENTIFIER")) {
				HashMap<String, String> extraInfo = new HashMap<>();
				extraInfo.put("type", "VARIABLE");
				extraInfo.put("variable_type", tmpVariableType);
				statementTree.addChildNode(
						new SyntaxTreeNode(
								tokens.get(index).getValue(), 
								"IDENTIFIER", 
								extraInfo), 
						statementTree.getRoot());
			// 数组大小
			} else if(tokens.get(index).getType().equals("DIGIT_CONSTANT")) {
				statementTree.addChildNode(
						new SyntaxTreeNode(
								tokens.get(index).getValue(), 
								"DIGIT_CONSTANT", 
								null), 
						statementTree.getRoot());
				HashMap<String, String> extraInfo = new HashMap<>();
				extraInfo.put("type", "LIST");
				extraInfo.put("list_type", tmpVariableType);
				statementTree.getCurrent().getLeft().setExtraInfo(extraInfo);
			// 数组元素
			} else if(tokens.get(index).getType().equals("LB_BRACKET")) {
				index++;
				SyntaxTreeNode constantList = new SyntaxTreeNode("ConstantList");
				statementTree.addChildNode(constantList, statementTree.getRoot());
				while(!tokens.get(index).getType().equals("RB_BRACKET")) {
					if(tokens.get(index).getType().equals("DIGIT_CONSTANT")) {
						statementTree.addChildNode(
								new SyntaxTreeNode(
										tokens.get(index).getValue(), 
										"DIGIT_CONSTANT", 
										null), 
								constantList);
					}
					index++;
				}
			// 多个变量声明
			} else if(tokens.get(index).getType().equals("COMMA")) {
				while(!tokens.get(index).getType().equals("SEMICOLON")) {
					if(tokens.get(index).getType().equals("IDENTIFIER")) {
						SyntaxTree tmpTree = new SyntaxTree();
						tmpTree.setRoot(new SyntaxTreeNode("Statement"));
						tmpTree.setCurrent(tmpTree.getRoot());
						tree.addChildNode(tmpTree.getRoot(), father);
						// 类型
						SyntaxTreeNode variableType = new SyntaxTreeNode("Type");
						tmpTree.addChildNode(variableType, null);
						// extra_info
						HashMap<String, String> extraInfo = new HashMap<>();
						extraInfo.put("type", tmpVariableType);
						tmpTree.addChildNode(
								new SyntaxTreeNode(
										tmpVariableType, 
										"FIELD_TYPE", 
										extraInfo), 
								null);
						extraInfo = new HashMap<>();
						extraInfo.put("type", "VARIABLE");
						extraInfo.put("variable_type", tmpVariableType);
						tmpTree.addChildNode(
								new SyntaxTreeNode(
										tokens.get(index).getValue(), 
										"IDENTIFIER", 
										extraInfo), 
								tmpTree.getRoot());
					}
				}
				break;
			}
			index++;
		}
		index++;
	}
	
	// 表达式
	private void expression() {
		
	}
	
	// 赋值语句
	private void assignment(SyntaxTreeNode father) {
		if(null == father) father = tree.getRoot();
		
		SyntaxTree assignTree = new SyntaxTree();
		assignTree.setRoot(new SyntaxTreeNode("Assignment"));
		assignTree.setCurrent(assignTree.getRoot());
		tree.addChildNode(assignTree.getRoot(), father);
		
		while(!tokens.get(index).getType().equals("SEMICOLON")) {
			// 被赋值的变量
			if(tokens.get(index).getType().equals("IDENTIFIER")) {
				assignTree.addChildNode(
						new SyntaxTreeNode(
								tokens.get(index).getValue(), 
								"IDENTIFIER", 
								null), 
						null);
				index++;
			} else if(tokens.get(index).getType().equals("ASSIGN")) {
				index++;
				expression(assignTree.getRoot());
			}
		}
		index++;
	}
	
	// 处理大括号里的部分
	private void block(SyntaxTree fatherTree) throws Exception {
		index++;
		SyntaxTree sentenceTree = new SyntaxTree();
		sentenceTree.setRoot(new SyntaxTreeNode("Sentence"));
		sentenceTree.setCurrent(sentenceTree.getRoot());
		fatherTree.addChildNode(sentenceTree.getRoot(), fatherTree.getRoot());
		
		while(true) {
			String sentencePattern = judgeSentencePattern();
			
			// 声明语句
			if(sentencePattern.equals("STATEMENT")) {
				statement(sentenceTree.getRoot());
			// 赋值语句
			} else if(sentencePattern.equals("ASSIGNMENT")) {
				assignment(sentenceTree.getRoot());
			// 函数调用
			} else if(sentencePattern.equals("FUNCTION_CALL")) {
				functionCall(sentenceTree.getRoot());
			// 控制流语句
			} else if(sentencePattern.equals("CONTROL")) {
				control(sentenceTree.getRoot());
			// return语句
			} else if(sentencePattern.equals("RETURN")) {
				_return(sentenceTree.getRoot());
			// 右大括号，函数结束
			} else if(sentencePattern.equals("RB_BRACKET")) {
				break;
			} else {
				throw new Exception("Block Error");
			}
		}
		
	}
	
	// 函数声明
	private void functionStatement(SyntaxTreeNode father) throws Exception {
		if(null == father) father = tree.getRoot();
		
		SyntaxTree funcStatementTree = new SyntaxTree();
		funcStatementTree.setRoot(new SyntaxTreeNode("FunctionStatement"));
		funcStatementTree.setCurrent(funcStatementTree.getRoot());
		tree.addChildNode(funcStatementTree.getRoot(), father);
		
		// 函数声明语句什么时候结束
		boolean flag = true;
		while(flag && index < tokens.size()) {
			// 如果是函数返回类型
			if(isDataType(tokens.get(index).getValue())) {
				SyntaxTreeNode returnType = new SyntaxTreeNode("Type");
				funcStatementTree.addChildNode(returnType, null);
				HashMap<String, String> extraInfo = new HashMap<>();
				extraInfo.put("type", tokens.get(index).getValue());
				funcStatementTree.addChildNode(
						new SyntaxTreeNode(tokens.get(index).getValue(), 
								"FIELD_TYPE", 
								extraInfo), 
						null);
				index++;
			// 如果是函数名
			} else if(tokens.get(index).getType().equals("IDENTIFIER")) {
				SyntaxTreeNode funcName = new SyntaxTreeNode("FunctionName");
				funcStatementTree.addChildNode(funcName, funcStatementTree.getRoot());
				// extra_info
				HashMap<String, String> extraInfo = new HashMap<>();
				extraInfo.put("type", "FUNCTION_NAME");
				funcStatementTree.addChildNode(
						new SyntaxTreeNode(
								tokens.get(index).getValue(), 
								"IDENTIFIER", 
								extraInfo), 
						null);
				index++;
			// 如果是参数序列
			} else if(tokens.get(index).getType().equals("LL_BRACKET")) {
				SyntaxTreeNode paramsList = new SyntaxTreeNode("StateParameterList");
				funcStatementTree.addChildNode(paramsList, funcStatementTree.getRoot());
				index++;
				while(!tokens.get(index).getType().equals("RL_BRACKET")) {
					if(isDataType(tokens.get(index).getValue())) {
						SyntaxTreeNode param = new SyntaxTreeNode("Parameter");
						funcStatementTree.addChildNode(param, paramsList);
						
						// extra_info
						HashMap<String, String> extraInfo = new HashMap<>();
						extraInfo.put("type", tokens.get(index).getValue());
						funcStatementTree.addChildNode(
								new SyntaxTreeNode(tokens.get(index).getValue(), 
										"FIELD_TYPE", 
										extraInfo), 
								param);
						if(tokens.get(index + 1).getType().equals("IDENTIFIER")) {
							extraInfo = new HashMap<>();
							extraInfo.put("type", "VARIABLE");
							extraInfo.put("variable_type", tokens.get(index).getValue());
							funcStatementTree.addChildNode(
									new SyntaxTreeNode(
											tokens.get(index + 1).getValue(), 
											"IDENTIFIER", 
											extraInfo), 
									param);
						} else {
							throw new Exception("函数定义参数错误");
						}
						index++;
					}
					index++;
				}
				index++;
			// 如果是遇见了左大括号
			} else if(tokens.get(index).getType().equals("LB_BRACKET")) {
				block(funcStatementTree);
			}
		}
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
		// 有可能是声明语句或者函数声明语句
		} else if(isDataType(tokenValue) && tokens.get(index + 1).getType().equals("IDENTIFIER")) {
			String index2TokenType = tokens.get(index + 2).getType();
			if(index2TokenType.equals("LL_BRACKET")) {
				return "FUNCTION_STATEMENT";
			} else if (index2TokenType.equals("SEMICOLON") || index2TokenType.equals("LM_BRACKET")
					|| index2TokenType.equals("COMMA")) {
				return "STATEMENT";
			} else {
				return "ERROR";
			}
		// 可能为函数调用或者赋值语句	
		} else if(tokenType.equals("IDENTIFIER")) {
			String index1TokenType = tokens.get(index + 1).getType();
			if(index1TokenType.equals("LL_BRACKET")) {
				return "FUNCTION_CALL";
			} else if(index1TokenType.equals("ASSIGN")) {
				return "ASSIGNMENT";
			} else {
				return "ERROR";
			}
		// return语句
		} else if(tokenType.equals("RETURN")) {
			return "RETURN";
		// 右大括号，表明函数的结束
		} else if (tokenType.equals("RB_BRACKET")) {
			index++;
			return "RB_BRACKET";
		} else {
			return "ERROR";
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
				include(null);
			// 函数声明语句
			} else if(sentencePattern.equals("FUNCTION_STATEMENT")) {
				functionStatement(null);
			// 声明语句
			} else if(sentencePattern.equals("STATEMENT")) {
				statement(null);
			// 函数调用
			} else if(sentencePattern.equals("FUNCTION_CALL")) {
				functionCall(null);
			} else {
				throw new Exception("main error");
			}
		}
	}
}
