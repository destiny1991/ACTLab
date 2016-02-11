package com.buaa.edu.compiler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

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
	
	// 判断是否是运算符
	private boolean isOperator(String word) {
		for(String str : CDefine.operators) {
			if(str.equals(word)) {
				return true;
			}
		}
		return false;
	}
	
	// 判断是否包含在指定的容器中
	private boolean isContain(String[] strs, String word) {
		for(String str : strs) {
			if(str.equals(word)) 
				return true;
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
					index++;
				}
				break;
			}
			index++;
		}
		index++;
	}
	
	// 表达式
	private void expression(SyntaxTreeNode father, Integer ind) throws Exception {
		if(null == father) father = tree.getRoot();
		
		// 运算符优先级
		Map<String, Integer> operatorPriority = new HashMap<String, Integer>(){
			private static final long serialVersionUID = 1L;
			{
				put(">", 0); put("<", 0); put(">=", 0); put("<=", 0);
				put("+", 1); put("-", 1);
				put("*", 2); put("/", 2);
				put("++", 3); put("--", 3); put("!", 3);
			}
		};
		
		// 运算符栈
		Stack<SyntaxTree> operatorStack = new Stack<SyntaxTree>();
		// 转换成的逆波兰表达式结果
		List<SyntaxTree> reversePolishExpression = new ArrayList<SyntaxTree>();
		
		// 中缀表达式转为后缀表达式，即逆波兰表达
		while(!tokens.get(index).getType().equals("SEMICOLON")) {
			if(null != ind && index >= ind) break;
			
			// 如果是常量
			if(tokens.get(index).getType().equals("DIGIT_CONSTANT")) {
				SyntaxTree tmpTree = new SyntaxTree();
				tmpTree.setRoot(new SyntaxTreeNode("Expression", "Constant", null));
				tmpTree.setCurrent(tmpTree.getRoot());
				tmpTree.addChildNode(
						new SyntaxTreeNode(tokens.get(index).getValue(), "_Constant", null), null);
				reversePolishExpression.add(tmpTree);
			// 如果是变量或者数组的某元素
			} else if(tokens.get(index).getType().equals("IDENTIFIER")) {
				// 变量
				if(isOperator(tokens.get(index + 1).getValue()) 
						|| tokens.get(index + 1).getType().equals("SEMICOLON")) {
					SyntaxTree tmpTree = new SyntaxTree();
					tmpTree.setRoot(new SyntaxTreeNode("Expression", "Variable", null));
					tmpTree.setCurrent(tmpTree.getRoot());
					tmpTree.addChildNode(
							new SyntaxTreeNode(tokens.get(index).getValue(), "_Variable", null), null);
					reversePolishExpression.add(tmpTree);
				// 数组的某一个元素ID[i]
				} else if(tokens.get(index + 1).getType().equals("LM_BRACKET")) {
					SyntaxTree tmpTree = new SyntaxTree();
					tmpTree.setRoot(
							new SyntaxTreeNode("Expression", "ArrayItem", null));
					tmpTree.setCurrent(tmpTree.getRoot());
					// 数组的名字
					tmpTree.addChildNode(
							new SyntaxTreeNode(tokens.get(index).getValue(), "_ArrayName", null), null);
					index += 2;
					if(!tokens.get(index).getType().equals("DIGIT_CONSTANT") 
							&& !tokens.get(index).getType().equals("IDENTIFIER")) {
						throw new Exception("error: 数组下表必须为常量或标识符 " + tokens.get(index).getType());
					} else {
						// 数组下标
						tmpTree.addChildNode(
								new SyntaxTreeNode(tokens.get(index).getValue(), "_ArrayIndex", null), 
								tmpTree.getRoot());
						reversePolishExpression.add(tmpTree);
					}
				}
			// 如果是运算符
			} else if(isOperator(tokens.get(index).getValue()) 
					|| tokens.get(index).getType().equals("LL_BRACKET")
					|| tokens.get(index).getType().equals("RL_BRACKET")) {
				SyntaxTree tmpTree = new SyntaxTree();
				tmpTree.setRoot(
						new SyntaxTreeNode("Operator", "Operator", null));
				tmpTree.setCurrent(tmpTree.getRoot());
				tmpTree.addChildNode(
						new SyntaxTreeNode(tokens.get(index).getValue(), "_Operator", null), null);
				
				// 如果是左括号，直接压栈
				if(tokens.get(index).getType().equals("LL_BRACKET")) {
					operatorStack.push(tmpTree);
				// 如果是右括号，弹栈直到遇到左括号为止
				} else if(tokens.get(index).getType().equals("RL_BRACKET")) {
					while(!operatorStack.empty() && !operatorStack.peek().getCurrent().getType().equals("LL_BRACKET")) {
						reversePolishExpression.add(operatorStack.pop());
					}
					// 将左括号弹出来
					if(!operatorStack.empty()) operatorStack.pop();
				// 其他只能是运算符
				} else {
					while(!operatorStack.empty() 
							&& operatorPriority.get(tmpTree.getCurrent().getValue()) 
									< operatorPriority.get(operatorStack.peek().getCurrent().getValue())) {
						reversePolishExpression.add(operatorStack.pop());
					}
					operatorStack.add(tmpTree);
				}
			}
			index++;
		}
		
		// 最后将符号栈清空，最终得到逆波兰表达式reverse_polish_expression
		while(!operatorStack.empty()) {
			reversePolishExpression.add(operatorStack.pop());
		}
		
		
		// 操作数栈
		Stack<SyntaxTree> operandStack = new Stack<SyntaxTree>();
		String[][] childOperators = {
						{"!", "++", "--"},
						{"+", "-", "*", "/", ">", "<", ">=", "<="}
				};
		
		for(SyntaxTree item : reversePolishExpression) {
			if(!item.getRoot().getType().equals("Operator")) {
				operandStack.push(item);
			} else {
				// 处理单目运算符
				if(isContain(childOperators[0], item.getCurrent().getValue())) {
					SyntaxTree a = operandStack.pop();
					SyntaxTree newTree = new SyntaxTree();
					newTree.setRoot(new SyntaxTreeNode("Expression", "SingleOperand", null));
					newTree.setCurrent(newTree.getRoot());
					// 添加操作符
					newTree.addChildNode(item.getRoot(), null);
					// 添加操作数
					newTree.addChildNode(a.getRoot(), newTree.getRoot());
					operandStack.push(newTree);
				// 双目运算符
				} else if(isContain(childOperators[1], item.getCurrent().getValue())) {
					SyntaxTree b = operandStack.pop();
					SyntaxTree a = operandStack.pop();
					SyntaxTree newTree = new SyntaxTree();
					newTree.setRoot(new SyntaxTreeNode("Expression", "DoubleOperand", null));
					newTree.setCurrent(newTree.getRoot());
					// 第一个操作数
					newTree.addChildNode(a.getRoot(), null);
					// 操作符
					newTree.addChildNode(item.getRoot(), newTree.getRoot());
					// 第二个操作数
					newTree.addChildNode(b.getRoot(), newTree.getRoot());
					operandStack.push(newTree);
				} else {
					throw new Exception("operator " + item.getCurrent().getValue() + " is not supported!");
				}
			}
		}
		tree.addChildNode(operandStack.get(0).getRoot(), father);
	}
	
	// 赋值语句
	private void assignment(SyntaxTreeNode father) throws Exception {
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
				expression(assignTree.getRoot(), null);
			}
		}
		index++;
	}
	
	// return语句
	private void _return(SyntaxTreeNode father) throws Exception {
		if(null == father) father = tree.getRoot();
		
		SyntaxTree returnTree = new SyntaxTree();
		returnTree.setRoot(new SyntaxTreeNode("Return"));
		returnTree.setCurrent(returnTree.getRoot());
		tree.addChildNode(returnTree.getRoot(), father);
		
		while(!tokens.get(index).getType().equals("SEMICOLON")) {
			// 被赋值的变量
			if(tokens.get(index).getType().equals("RETURN")) {
				returnTree.addChildNode(
						new SyntaxTreeNode(tokens.get(index).getValue()), null);
				index++;
			} else {
				expression(returnTree.getRoot(), null);
			}
		}
		index++;
	}
	
	// while语句，没处理do-while的情况，只处理了while
	private void _while(SyntaxTreeNode father) throws Exception {
		SyntaxTree whileTree = new SyntaxTree();
		whileTree.setRoot(
				new SyntaxTreeNode("Control", "WhileControl", null));
		whileTree.setCurrent(whileTree.getRoot());
		tree.addChildNode(whileTree.getRoot(), father);
		
		index++;
		if(tokens.get(index).getType().equals("LL_BRACKET")) {
			int tmpIndex = index;
			while(!tokens.get(tmpIndex).getType().equals("RL_BRACKET")) {
				tmpIndex++;
			}
			expression(whileTree.getRoot(), tmpIndex);
			
			if(tokens.get(tmpIndex).getType().equals("LB_BRACKET")) {
				block(whileTree);
			}
		}
	}
	
	// if语句
	private void _if_else(SyntaxTreeNode father) throws Exception {
		SyntaxTree ifElseTree = new SyntaxTree();
		ifElseTree.setRoot(
				new SyntaxTreeNode("Control", "IfElseControl", null));
		ifElseTree.setCurrent(ifElseTree.getRoot());
		tree.addChildNode(ifElseTree.getRoot(), father);
		
		SyntaxTree ifTree = new SyntaxTree();
		ifTree.setRoot(new SyntaxTreeNode("IfControl"));
		ifTree.setCurrent(ifTree.getRoot());
		ifElseTree.addChildNode(ifTree.getRoot(), null);
		
		// if标志
		if(tokens.get(index).getType().equals("IF")) {
			index++;
			// 左小括号
			if(tokens.get(index).getType().equals("LL_BRACKET")) {
				index++;
				// 右小括号位置
				int tmpIndex = index;
				while(!tokens.get(tmpIndex).getType().equals("RL_BRACKET")) {
					tmpIndex++;
				}
				expression(ifTree.getRoot(), tmpIndex);
				index++;
			} else {
				throw new Exception("error: lack of left bracket!");
			}
			
			// 左大括号
			if(tokens.get(index).getType().equals("LB_BRACKET")) {
				block(ifTree);
			}
		}
		
		// 如果是else关键字
		if(tokens.get(index).getType().equals("ELSE")) {
			 index++;
			 SyntaxTree elseTree = new SyntaxTree();
			 elseTree.setRoot(new SyntaxTreeNode("ElseControl"));
			 elseTree.setCurrent(elseTree.getRoot());
			 ifElseTree.addChildNode(elseTree.getRoot(), ifElseTree.getRoot());
			 // 左大括号
			 if(tokens.get(index).getType().equals("LB_BRACKET")) {
				 block(elseTree);
			 }	 
		}
	}
	
	// for语句
	private void _for(SyntaxTreeNode father) throws Exception {
		SyntaxTree forTree = new SyntaxTree();
		forTree.setRoot(
				new SyntaxTreeNode("Control", "ForControl", null));
		tree.addChildNode(forTree.getRoot(), father);
		
		// 标记for语句是否结束
		while(true) {
			String tokenType = tokens.get(index).getType();
			// for标记
			if(tokenType.equals("FOR")) {
				index++;
			} else if(tokenType.equals("LL_BRACKET")) {
				index++;
				// 首先找到右小括号的位置
				int tmpIndex = index;
				while(!tokens.get(tmpIndex).getType().equals("RL_BRACKET")) {
					tmpIndex++;
				}
				// for语句中的第一个分号前的部分
				assignment(forTree.getRoot());
				// 两个分号中间的部分
				expression(forTree.getRoot(), null);
				index++;
				// 第二个分号后的部分
				expression(forTree.getRoot(), tmpIndex);
				index++;
			// 如果为左大括号
			} else if(tokenType.equals("LB_BRACKET")) {
				block(forTree);
				break;
			}
		}
		// 交换for语句下第三个子节点和第四个子节点
		SyntaxTreeNode currentNode = forTree.getRoot().getFirstSon().getRight().getRight();
		SyntaxTreeNode nextNode = currentNode.getRight();
		forTree.switchTwoSubTree(currentNode, nextNode);
	}
	
	// 处理控制语句
	private void control(SyntaxTreeNode father) throws Exception {
		String tokenType = tokens.get(index).getType();
		if(tokenType.equals("WHILE") || tokenType.equals("DO")) {
			_while(father);
		} else if(tokenType.equals("IF")) {
			_if_else(father);
		} else if(tokenType.equals("FOR")) {
			_for(father);
		} else {
			throw new Exception("error: control style not supported!");
		}
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
						new SyntaxTreeNode(
								tokens.get(index).getValue(), 
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
	
	// 函数调用
	private void functionCall(SyntaxTreeNode father) throws Exception {
		if(null == father) father = tree.getRoot();
		
		SyntaxTree funcCallTree = new SyntaxTree();
		funcCallTree.setRoot(new SyntaxTreeNode("FunctionCall"));
		funcCallTree.setCurrent(funcCallTree.getRoot());
		tree.addChildNode(funcCallTree.getRoot(), father);
		
		while(!tokens.get(index).getType().equals("SEMICOLON")) {
			// 函数名
			if(tokens.get(index).getType().equals("IDENTIFIER")) {
				funcCallTree.addChildNode(
						new SyntaxTreeNode(tokens.get(index).getValue(), "FUNCTION_NAME", null), null);
			// 左小括号
			} else if(tokens.get(index).getType().equals("LL_BRACKET")) {
				index++;
				SyntaxTreeNode paramsList = new SyntaxTreeNode("CallParameterList");
				funcCallTree.addChildNode(paramsList, funcCallTree.getRoot());
				while(!tokens.get(index).getType().equals("RL_BRACKET")) {
					if(tokens.get(index).getType().equals("IDENTIFIER")
							|| tokens.get(index).getType().equals("DIGIT_CONSTANT")
							|| tokens.get(index).getType().equals("STRING_CONSTANT")) {
						funcCallTree.addChildNode(
								new SyntaxTreeNode(tokens.get(index).getValue(), tokens.get(index).getType(), null), paramsList);
					} else if(tokens.get(index).getType().equals("DOUBLE_QUOTE")) {
						index++;
						funcCallTree.addChildNode(
								new SyntaxTreeNode(tokens.get(index).getValue(), tokens.get(index).getType(), null), paramsList);
						index++;
					} else if(tokens.get(index).getType().equals("ADDRESS")) {
						funcCallTree.addChildNode(
								new SyntaxTreeNode(tokens.get(index).getValue(), "ADDRESS", null), paramsList);
					}
					index++;
				}
			} else {
				throw new Exception("function call error");
			}
			index++;
		}
		index++;
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
	
	// DFS遍历语法树
	public static void display(SyntaxTreeNode node, BufferedWriter writer) throws IOException {
		if(null == node) return;
		
		System.out.printf("( self: %s %s, father: %s, left: %s, right: %s )\n", 
				node.getValue(), node.getType(), 
				node.getFather() == null ? null : node.getFather().getValue(), 
				node.getLeft() == null ? null : node.getLeft().getValue(), 
				node.getRight() == null ? null : node.getRight().getValue());
		writer.write("( self: " + node.getValue() + " " + node.getType()
							+ ", father: " + (node.getFather() == null ? null : node.getFather().getValue())
							+ ", left: " + (node.getLeft() == null ? null : node.getLeft().getValue())
							+ ", right: " + (node.getRight() == null ? null : node.getRight().getValue()));
		writer.newLine();
		SyntaxTreeNode child = node.getFirstSon();
		while(null != child) {
			display(child, writer);
			child = child.getRight();
		} 
	}
	
	public static void outputParser(Parser parser, String src) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(src));
		display(parser.getTree().getRoot(), writer);
		writer.close();
	}
	
	public static void main(String[] args) throws Exception {
		String src = "src/input/source.c";
		Lexer lexer = new Lexer(Lexer.getContent(src));
		lexer.runLexer();
		List<Token> tokens = lexer.getTokens();
		System.out.println("========================词法分析===========================");
		Lexer.outputLexer(tokens);
		
		System.out.println("\n===========================语法分析========================");
		Parser parser = new Parser(tokens);
		parser.runParser();
		src = "src/output/parser.txt";
		outputParser(parser, src);
	}
}
