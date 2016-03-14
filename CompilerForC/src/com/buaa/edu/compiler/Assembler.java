package com.buaa.edu.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * 编译成汇编语言
 * @author destiny
 *
 */
public class Assembler {
	private SyntaxTree tree;						// 最终生成的语法树
	private AssemblerFileHandler assFileHandler;	// 要生成的汇编文件管理器
	private Map<String, Map<String, String>> symbolTable;		// 符号表
	private List<String> sentenceType;				// 语法类型
	
	private Stack<String> operatorStack;			// 表达式中的符号栈
	private Stack<Map<String, String>> operandStack;// 表达式中的操作符栈
	private int labelCnt;							// 已经声明了多少个label
	private Map<String, String> labelsIfelse;		// ifelse中的标签
	
	public Assembler(SyntaxTree tree) {
		super();
		this.tree = tree;
		assFileHandler = new AssemblerFileHandler();
		symbolTable = new HashMap<>();
		sentenceType = new ArrayList<String>(){
			private static final long serialVersionUID = 1L;
			{
				add("Sentence"); add("Include"); add("FunctionStatement");
				add("Statement"); add("FunctionCall"); add("Assignment");
				add("Control"); add("Expression"); add("Return");
			}
		};
		
		operatorStack = new Stack<>();
		operandStack = new Stack<>();
		labelCnt = 0;
		labelsIfelse = new HashMap<>();
	}

	public SyntaxTree getTree() {
		return tree;
	}

	public void setTree(SyntaxTree tree) {
		this.tree = tree;
	}

	public AssemblerFileHandler getAssFileHandler() {
		return assFileHandler;
	}

	public void setAssFileHandler(AssemblerFileHandler assFileHandler) {
		this.assFileHandler = assFileHandler;
	}

	public Map<String, Map<String, String>> getSymbolTable() {
		return symbolTable;
	}

	public void setSymbolTable(Map<String, Map<String, String>> symbolTable) {
		this.symbolTable = symbolTable;
	}

	public List<String> getSentenceType() {
		return sentenceType;
	}

	public void setSentenceType(List<String> sentenceType) {
		this.sentenceType = sentenceType;
	}

	public Stack<String> getOperatorStack() {
		return operatorStack;
	}

	public void setOperatorStack(Stack<String> operatorStack) {
		this.operatorStack = operatorStack;
	}

	public Stack<Map<String, String>> getOperandStack() {
		return operandStack;
	}

	public void setOperandStack(Stack<Map<String, String>> operandStack) {
		this.operandStack = operandStack;
	}

	public int getLabelCnt() {
		return labelCnt;
	}

	public void setLabelCnt(int labelCnt) {
		this.labelCnt = labelCnt;
	}

	public Map<String, String> getLabelsIfelse() {
		return labelsIfelse;
	}

	public void setLabelsIfelse(Map<String, String> labelsIfelse) {
		this.labelsIfelse = labelsIfelse;
	}
	
	// include句型
	private void _include(SyntaxTreeNode node) {
		// 不用处理，不会生成对应的汇编代码
	}
	
	// 函数定义句型
	private void _functionStatement(SyntaxTreeNode node) throws Exception {
		// 第一个儿子
		SyntaxTreeNode currentNode = node.getFirstSon();
		String funcName = null;
		while(currentNode != null) {
			if(currentNode.getValue().equals("FunctionName")) {
				if(!currentNode.getFirstSon().getValue().equals("main")) {
					throw new Exception("other function statement except for main is not supported");
				} else {
					funcName = "main";
					assFileHandler.insert("	.align 2", "TEXT");
					assFileHandler.insert("	.globl main", "TEXT");
					assFileHandler.insert("	.type main, @function", "TEXT");
					assFileHandler.insert("main:", "TEXT");
					assFileHandler.insert("	stwu 1,-16(1)", "TEXT");
					assFileHandler.insert("	stw 31,12(1)", "TEXT");
					assFileHandler.insert("	mr 31,1", "TEXT");
					assFileHandler.insert("", "TEXT");
				}
			} else if(currentNode.getValue().equals("Sentence")) {
				traverse(currentNode.getFirstSon());
			}
			currentNode = currentNode.getRight();
		}
		if(null !=funcName && funcName.equals("main")) {
			assFileHandler.insert("	addi 11,31,16", "TEXT");
			assFileHandler.insert("	lwz 31,-4(11)", "TEXT");
			assFileHandler.insert("	mr 1,11", "TEXT");
			assFileHandler.insert("	blr", "TEXT");
			assFileHandler.insert("	.size	main, .-main", "TEXT");
		}
	}
	
	// 变量声明语句
	private void _statement(SyntaxTreeNode node) throws Exception {
		// 对应的汇编代码中的声明语句
		String line = null;
		// 变量数据类型
		String variableFieldType = null;
		// 变量类型，是数组还是单个变量
		String variableType = null;
		// 变量名
		String variableName = null;
		SyntaxTreeNode currentNode = node.getFirstSon();
		
		while(null != currentNode) {
			// 类型
			if(currentNode.getValue().equals("Type")) {
				variableFieldType = currentNode.getFirstSon().getValue();
			// 变量名
			} else if(currentNode.getType() != null && currentNode.getType().equals("IDENTIFIER")) {
				variableName = currentNode.getValue();
				variableType = currentNode.getExtraInfo().get("type");	
			// 数组元素
			} else if(currentNode.getValue().equals("ConstantList")) {
				line = "	.align 2";
				assFileHandler.insert(line, "DATA");
				line = "." + variableName + ":";
				assFileHandler.insert(line, "DATA");
				
				SyntaxTreeNode tmpNode = currentNode.getFirstSon();
				while(null != tmpNode) {
					line = "." + variableFieldType + "	" + tmpNode.getValue();
					assFileHandler.insert(line, "DATA");
					tmpNode = tmpNode.getRight();
				}
			}
			currentNode = currentNode.getRight();
		}
		// 将该变量存入符号表
		Map<String, String> tmpMap = new HashMap<String, String>();
		tmpMap.put("type", variableType);
		tmpMap.put("field_type", variableFieldType);
		symbolTable.put(variableName, tmpMap);
	}
	
	// 函数调用
	private void _functionCall(SyntaxTreeNode node) throws Exception {
		SyntaxTreeNode currentNode = node.getFirstSon();
		String funcName = null;
		List<String> parameterList = new ArrayList<>();
		
		while(null != currentNode) {
			// 函数名字
			if(currentNode.getType() != null && currentNode.getType().equals("FUNCTION_NAME")) {
				funcName = currentNode.getValue();
				if(!funcName.equals("scanf") && !funcName.equals("printf")) {
					throw new Exception("function call except scanf and printf not supported yet");
				}
			// 函数参数
			} else if(currentNode.getValue().equals("CallParameterList")) {
				SyntaxTreeNode tmpNode = currentNode.getFirstSon();
				while(null != tmpNode) {
					// 是常数
					if(tmpNode.getType().equals("DIGIT_CONSTANT") 
							|| tmpNode.getType().equals("STRING_CONSTANT")) {
						// 汇编中该参数的名称
						String label = ".LC" + labelCnt;
						labelCnt++;
						if(tmpNode.getType().equals("STRING_CONSTANT")) {
							// 添加数据段中该参数定义
							String line = "	.align 2";
							assFileHandler.insert(line, "DATA");
							line = label + ":";
							assFileHandler.insert(line, "DATA");
							line = "	.string	\"" + tmpNode.getValue() + ":\"";
							assFileHandler.insert(line, "DATA");
						} else {
							throw new Exception("in functionc_call digital constant parameter is not supported yet");
						}
						Map<String, String> tmpMap = new HashMap<>();
						tmpMap.put("type", "STRING_CONSTANT");
						tmpMap.put("value", tmpNode.getValue());
						symbolTable.put(label, tmpMap);
						parameterList.add(label);
					// 是某个变量
					} else if(tmpNode.getType().equals("IDENTIFIER")) {
						parameterList.add(tmpNode.getValue());
					} else if(tmpNode.getType().equals("ADDRESS")) {
						// 暂不处理
					} else {
						throw new Exception("parameter type is not supported yet");
					}
					tmpNode = tmpNode.getRight();
				}
			}
			currentNode = currentNode.getRight();
		}
		
		// 添加到代码段
		if(funcName.equals("printf")) {
			int num = 3;
			//for(int i = parameterList.size() - 1 ; i >= 0; i--) {
			for(int i = 0; i < parameterList.size(); i++) {
				String parameter = parameterList.get(i);
				// 如果该参数的类型是字符串常量
				if(symbolTable.get(parameter).get("type").equals("STRING_CONSTANT")) {
					String line = "	lis 0," + parameter + "@ha";
					assFileHandler.insert(line, "TEXT");
					line = "	addic 0,0," + parameter + "@l";
					assFileHandler.insert(line, "TEXT");
					line =  "	mr " + num + ",0";
					num++;
					assFileHandler.insert(line, "TEXT");
				} else if(symbolTable.get(parameter).get("type").equals("VARIABLE")) {
					String fieldType = symbolTable.get(parameter).get("field_type");
					if(fieldType.equals("int") || fieldType.equals("long")) {
						String line = "	lwz " + num +  "," + parameter + "(31)";
						num++;
						assFileHandler.insert(line, "TEXT");
					} else if(fieldType.equals("float")) {						
//						String line = "	lwz 9," + parameter + "(31)";
//						assFileHandler.insert(line, "TEXT");
//						num++;
//						line = "	evmergehi 7,9,9";
//						assFileHandler.insert(line, "TEXT");
//						line = "	mr 8,9";
//						assFileHandler.insert(line, "TEXT");
//						line = "	stw " + num + "," + (4*num) + "(31)";
//						assFileHandler.insert(line, "TEXT");
//						line =  "	mr " + num + ",9";
//						assFileHandler.insert(line, "TEXT");
//						num ++;
						throw new Exception("Sorry, printf not support float type!");
					} else {
						throw new Exception("other type in printf is not supported yet!");
					}
				} else {
					throw new Exception("parameter type not supported in printf yet");
				}
			}
			String line = "	crxor 6,6,6";
			assFileHandler.insert(line, "TEXT");
			line = "	bl printf";
			assFileHandler.insert(line, "TEXT");
		} else if(funcName.equals("scanf")) {
			int num = 3;
			for(int i = 0; i < parameterList.size(); i++) {
				String parameter = parameterList.get(i);
				String parameterType = symbolTable.get(parameter).get("type");
				if(parameterType.equals("STRING_CONSTANT")) {
					String line = "	lis 0," + parameter + "@ha";
					assFileHandler.insert(line, "TEXT");
					line = "	addic " + (num + 7) + ",0," + parameter + "@l";
					assFileHandler.insert(line, "TEXT");
					line = "	mr " + num + "," + (num + 7);
					num++;
					assFileHandler.insert(line, "TEXT");
				} else if(parameterType.equals("VARIABLE")) {
					String fieldType = symbolTable.get(parameter).get("field_type");
					if(fieldType.equals("int") || fieldType.equals("long")) {
						String line = "	addi " + (num + 7) + ",31," + parameter;
						assFileHandler.insert(line, "TEXT");
						line = "	mr " + num + "," + (num + 7);
						num++;
						assFileHandler.insert(line, "TEXT");
					} else if(fieldType.equals("float")) {
						// 浮点数加入后，代码变得复杂，所以暂未实现
						throw new Exception("Sorry, scanf not support float type!");
					} else {
						throw new Exception("other type in scanf is not supported yet!");
					}
				} else {
					throw new Exception("parameter type not supported in scanf");
				}
			}
			String line = "	crxor 6,6,6";
			assFileHandler.insert(line, "TEXT");
			line = "	bl __isoc99_scanf";
			assFileHandler.insert(line, "TEXT");
		}
		assFileHandler.insert("", "TEXT");	// 增加一个空行
	}
	
	// 赋值语句
	private void _assignment(SyntaxTreeNode node) throws Exception {
		SyntaxTreeNode currentNode = node.getFirstSon();
		if(currentNode.getType().equals("IDENTIFIER") 
				&& currentNode.getRight().getValue().equals("Expression")) {
			Map<String, String> expres = _expression(currentNode.getRight());
			// 该变量的类型
			String fieldType = symbolTable.get(currentNode.getValue()).get("field_type");
			if(fieldType.equals("int")) {
				// 常数
				if(expres.get("type").equals("CONSTANT")) {
					// movl $0, temp
					//String line = "movl $" + expres.get("value") + ", " + currentNode.getValue();
					//assFileHandler.insert(line, "TEXT");
					
					String line = "	li 0," + expres.get("value");
					assFileHandler.insert(line, "TEXT");
					line = "	stw 0," + currentNode.getValue() + "(31)";
					assFileHandler.insert(line, "TEXT");
				// 变量
				} else if(expres.get("type").equals("VARIABLE")) {
					// 把数放到r0中，再把r0总的数转到目标寄存器中, 同float
					String line = "	lwz 0," + expres.get("value") + "(31)";
					assFileHandler.insert(line, "TEXT");
					line = "	stw 0," + currentNode.getValue() + "(31)";
					assFileHandler.insert(line, "TEXT");
				} else {
					throw new Exception("_assignment : only support constant and varivale!");
				}
			} else if(fieldType.equals("float")) {
				if(expres.get("type").equals("CONSTANT")) {					
					// data域
					String line = "	.align 2";
					assFileHandler.insert(line, "DATA");
					String label = ".LC" + labelCnt;
					labelCnt++;
					line = label + ":";
					assFileHandler.insert(line, "DATA");
					int fTob = Float.floatToIntBits(Float.parseFloat(expres.get("value")));
					line = "	.long " + fTob;
					assFileHandler.insert(line, "DATA");
					
					// text域
					line = "	lis 0," + label + "@ha";
					assFileHandler.insert(line, "TEXT");
					line = "	mr 9,0";
					assFileHandler.insert(line, "TEXT");
					line = "	lwz 0," + label + "@l(9)";
					assFileHandler.insert(line, "TEXT");
					line  = "	stw 0," + currentNode.getValue() + "(31)";
				} else {
					// 把数放到r0中，再把r0总的数转到目标寄存器中
					String line = "	lwz 0," + expres.get("value") + "(31)";
					assFileHandler.insert(line, "TEXT");
					line = "	stw 0," + currentNode.getValue() + "(31)";
					assFileHandler.insert(line, "TEXT");
				}
			} else {
				throw new Exception("field type except int and float not supported");
			}
		} else {
			throw new Exception("assignment wrong");
		}
		assFileHandler.insert("", "TEXT");
	}
	
	// if else语句
	private void _controlIf(SyntaxTreeNode node) throws Exception {
		SyntaxTreeNode currentNode = node.getFirstSon();
		labelsIfelse.put("label_else", ".L" + labelCnt);
		labelCnt++;
		labelsIfelse.put("label_end", ".L" + labelCnt);
		labelCnt++;
		
		while(null != currentNode) {
			if(currentNode.getValue().equals("IfControl")) {
				if(!currentNode.getFirstSon().getValue().equals("Expression") 
						|| !currentNode.getFirstSon().getRight().getValue().equals("Sentence")) {
					throw new Exception("control_if error");
				}
				_expression(currentNode.getFirstSon());
				String line = "	cmpi 7,0,0,0";
				assFileHandler.insert(line, "TEXT");
				line = "	bc 4,29," + labelsIfelse.get("label_else");
				traverse(currentNode.getFirstSon().getRight().getFirstSon());
				line = "	b " + labelsIfelse.get("label_end");
				assFileHandler.insert(line, "TEXT");
				line = labelsIfelse.get("label_else") + ":";
				assFileHandler.insert(line, "TEXT");
			} else if(currentNode.getValue().equals("ElseControl")) {
				traverse(currentNode.getFirstSon());
				String line = labelsIfelse.get("label_end") + ":";
				assFileHandler.insert(line, "TEXT");
			}
			currentNode = currentNode.getRight();
		}
	}
	
	// for语句
	private void _controlFor(SyntaxTreeNode node) throws Exception {
		SyntaxTreeNode currentNode = node.getFirstSon();
		// 遍历的是for循环中的那个部分
		int cnt = 2;
		SyntaxTreeNode forCondition = null;
		while(null != currentNode) {
			// for第一部分
			if(currentNode.getValue().equals("Assignment")) {
				_assignment(currentNode);
			// for第二、三部分
			} else if(currentNode.getValue().equals("Expression")) {
				// 第2部分
				if(cnt == 2) {
					cnt++;
					String label = ".L" + labelCnt;
					labelCnt++;
					String line = "	b " + label;
					assFileHandler.insert(line, "TEXT");
					label = ".L" + labelCnt;
					labelCnt++;
					line = label + ":";
					assFileHandler.insert(line, "TEXT");
					
					forCondition = currentNode;
					//_expression(currentNode);
				// 第3部分
				} else {
					_expression(currentNode);
				}
			// for语句块的部分
			} else if(currentNode.getValue().equals("Sentence")) {
				traverse(currentNode.getFirstSon());
			}
			currentNode = currentNode.getRight();
		}
		
		String line = ".L" + (labelCnt - 2);
		assFileHandler.insert(line, "TEXT");
		if(forCondition != null) _expression(forCondition);
		line = "	lwz 0,relational_tmp(31)";
		assFileHandler.insert(line, "TEXT");
		line = "	cmpi 7,0,0,0";
		assFileHandler.insert(line, "TEXT");
		line = "	bc	12,29," + ".L" + (labelCnt - 1);
		assFileHandler.insert(line, "TEXT");
		assFileHandler.insert("", "TEXT");	// 增加一个空行
	}
	
	// while语句
	private void _controlWhile(SyntaxTreeNode node) throws Exception {
		SyntaxTreeNode currentNode = node.getFirstSon();
		SyntaxTreeNode whileCondition = null;
		while(null != currentNode) {
			// while第一部分
			if(currentNode.getValue().equals("Expression")) {
				String label = ".L" + labelCnt;
				labelCnt++;
				String line = "	b " + label;
				assFileHandler.insert(line, "TEXT");
				label = ".L" + labelCnt;
				labelCnt++;
				line = label + ":";
				assFileHandler.insert(line, "TEXT");
				whileCondition = currentNode;
				//_expression(currentNode);
			// while循环体
			} else if(currentNode.getValue().equals("Sentence")) {
				traverse(currentNode.getFirstSon());
			}
			currentNode = currentNode.getRight();
		}
		String line = ".L" + (labelCnt - 2);
		assFileHandler.insert(line, "TEXT");
		if(whileCondition != null) _expression(whileCondition);
		line = "	lwz 0,relational_tmp(31)";
		assFileHandler.insert(line, "TEXT");
		line = "	cmpi 7,0,0,0";
		assFileHandler.insert(line, "TEXT");
		line = "	bc	12,29," + ".L" + (labelCnt - 1);
		assFileHandler.insert(line, "TEXT");
		assFileHandler.insert("", "TEXT");	// 增加一个空行
	}
	
	// return语句
	private void _return(SyntaxTreeNode node) throws Exception {
		SyntaxTreeNode currentNode = node.getFirstSon();
		if(!currentNode.getValue().equals("return") 
				|| !currentNode.getRight().getValue().equals("Expression")) {
			throw new Exception("return error");
		} else {
			currentNode = currentNode.getRight();
			Map<String, String> expres = _expression(currentNode);
			if(expres.get("type").equals("CONSTANT")) {
				String line = "	li 0," + expres.get("value");
				assFileHandler.insert(line, "TEXT");
				line = "	mr 3,0";
				assFileHandler.insert(line, "TEXT");
			} else {
				throw new Exception("return type not supported");
			}
		}
	}
	
	// 遍历表达式
	private void _traverseExpression(SyntaxTreeNode node) {
		if(null == node) return;
		
		if(node.getType().equals("_Variable")) {
			Map<String, String> item = new HashMap<>();
			item.put("type", "VARIABLE");
			item.put("operand", node.getValue());
			operandStack.push(item);
		} else if(node.getType().equals("_Constant")) {
			Map<String, String> item = new HashMap<>();
			item.put("type", "CONSTANT");
			item.put("operand", node.getValue());
			operandStack.push(item);
		} else if(node.getType().equals("_Operator")) {
			operatorStack.push(node.getValue());
		} else if(node.getType().equals("_ArrayName")) {
			Map<String, String> item = new HashMap<>();
			item.put("type", "ARRAY_ITEM");
			item.put("operand0", node.getValue());
			item.put("operand1", node.getRight().getValue());
			operandStack.push(item);
			return;
		}
		
		SyntaxTreeNode currentNode = node.getFirstSon();
		while(currentNode != null) {
			_traverseExpression(currentNode);
			currentNode = currentNode.getRight();
		}
	}
	
	// 判断一个变量是不是float类型
	private boolean _isFloat(Map<String, String> operand) {
		return operand.get("type").equals("VARIABLE") 
				&& symbolTable.get(operand.get("operand")).get("field_type").equals("float");
	}
	
	// 判断两个操作数中是否含有float类型的数
	private boolean _containFloat(Map<String, String> operand_a, Map<String, String> operand_b) {
		return _isFloat(operand_a) || _isFloat(operand_b);
	}
	
	// 表达式
	private Map<String, String> _expression(SyntaxTreeNode node) throws Exception {
		if(node.getType().equals("Constant")) {
			Map<String, String> tmpMap = new HashMap<>();
			tmpMap.put("type", "CONSTANT");
			tmpMap.put("value", node.getFirstSon().getValue());
			return tmpMap;
		}
		
		// 先清空
		operandStack.clear();
		operatorStack.clear();
		
		// 遍历该表达式
		_traverseExpression(node);
		
		// 双目运算符
		List<String> doubleOperators = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			{
				add("+"); add("-"); add("*"); add("/");
				add(">"); add("<"); add(">="); add("<=");
			}
		};
		
		// 单目运算符
		List<String> singleOperators = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			{
				add("++"); add("--");
			}
		};
		
		
		while(!operatorStack.empty()) {
			String operator = operatorStack.pop();
			if(doubleOperators.contains(operator)) {
				Map<String, String> operand_b = operandStack.pop();
				Map<String, String> operand_a = operandStack.pop();
				boolean containFloat = _containFloat(operand_a, operand_b);
				
				if(operator.equals("+")) {
					if(containFloat) {
						throw new Exception("Sorry, only support int type!");
					} else {
						// 第一个操作数
						if(operand_a.get("type").equals("ARRAY_ITEM")) {
							
							throw new Exception("Not support array operation yet!");
							
						} else if(operand_a.get("type").equals("VARIABLE")) {
//							String line = "movl " + operand_a.get("operand") + ", %eax";
//							assFileHandler.insert(line, "TEXT");							
							String line = "	lwz 9," + operand_a.get("operand") + "(31)";
							assFileHandler.insert(line, "TEXT");
							
						} else if(operand_a.get("type").equals("CONSTANT")) {
//							String line = "movl $" + operand_a.get("operand") + ", %eax";
//							assFileHandler.insert(line, "TEXT");
							String line = "	li 9," + operand_a.get("operand");
							assFileHandler.insert(line, "TEXT");
						}
						
						// 加上第二个操作数
						if(operand_b.get("type").equals("ARRAY_ITEM")) {
//							String line = "movl " + operand_b.get("operand1") + ", %edi";
//							assFileHandler.insert(line, "TEXT");
//							line = "addl " + operand_b.get("operand0") + "(, %edi, 4), %eax";
//							assFileHandler.insert(line, "TEXT");
							throw new Exception("Not support array operation yet!");
						} else if(operand_b.get("type").equals("VARIABLE")) {
//							String line = "addl " + operand_b.get("operand") + ", %eax";
//							assFileHandler.insert(line, "TEXT");
							String line = "	lwz 0," + operand_b.get("operand") + "(31)";
							assFileHandler.insert(line, "TEXT");
							line = "	add 0,9,0";
							assFileHandler.insert(line, "TEXT");
						} else if(operand_b.get("type").equals("CONSTANT")) {
//							String line = "addl $" + operand_b.get("operand") + ", %eax";
//							assFileHandler.insert(line, "TEXT");
							String line = "	li 0," + operand_b.get("operand");
							assFileHandler.insert(line, "TEXT");
							line = "	add 0,9,0";
							assFileHandler.insert(line, "TEXT");
						}
						
						// 赋值给临时操作数
						String line = "	stw 0,bss_tmp(31)";
						assFileHandler.insert(line, "TEXT");
						// 计算结果压栈
						Map<String, String> tmpMap = new HashMap<>();
						tmpMap.put("type", "VARIABLE");
						tmpMap.put("operand", "bss_tmp");
						operandStack.add(tmpMap);
						// 记录到符号表中
						tmpMap = new HashMap<>();
						tmpMap.put("type", "IDENTIFIER");
						tmpMap.put("field_type", "int");
						symbolTable.put("bss_tmp", tmpMap);
					}
				} else if(operator.equals("-")) {
					if(containFloat) {
						throw new Exception("sub operator not suppor float type");
					} else {
						// 被减数
						if(operand_a.get("type").equals("ARRAY_ITEM")) {
							throw new Exception("not support array operation yet!");
						}else if(operand_a.get("type").equals("VARIABLE")) {						
							String line = "	lwz 9," + operand_a.get("operand") + "(31)";
							assFileHandler.insert(line, "TEXT");							
						} else if(operand_a.get("type").equals("CONSTANT")) {
							String line = "	li 9," + operand_a.get("operand");
							assFileHandler.insert(line, "TEXT");
						}
						
						// 减数
						if(operand_b.get("type").equals("ARRAY_ITEM")) {
							throw new Exception("Not support array operation yet!");
						} else if(operand_b.get("type").equals("VARIABLE")) {
							String line = "	lwz 0," + operand_b.get("operand") + "(31)";
							assFileHandler.insert(line, "TEXT");
							line = "	subf 0,9,0";
							assFileHandler.insert(line, "TEXT");
						} else if(operand_b.get("type").equals("CONSTANT")) {
							String line = "	li 0," + operand_b.get("operand");
							assFileHandler.insert(line, "TEXT");
							line = "	subf 0,9,0";
							assFileHandler.insert(line, "TEXT");
						}
						
						// 赋值给临时操作数
						String line = "	stw 0,bss_tmp(31)";
						assFileHandler.insert(line, "TEXT");
						// 计算结果压栈
						Map<String, String> tmpMap = new HashMap<>();
						tmpMap.put("type", "VARIABLE");
						tmpMap.put("operand", "bss_tmp");
						operandStack.add(tmpMap);
						// 记录到符号表中
						tmpMap = new HashMap<>();
						tmpMap.put("type", "IDENTIFIER");
						tmpMap.put("field_type", "int");
						symbolTable.put("bss_tmp", tmpMap);
					}
				// 整数乘法
				} else if(operator.equals("*")) {
					if(containFloat) {
						
						throw new Exception("mul operator not suppor float type");
						
					} else {
						if(operand_a.get("type").equals("ARRAY_ITEM")) {
							throw new Exception("* not support array operation yet!");
						} else if(operand_a.get("type").equals("VARIABLE")) {						
							String line = "	lwz 9," + operand_a.get("operand") + "(31)";
							assFileHandler.insert(line, "TEXT");							
						} else if(operand_a.get("type").equals("CONSTANT")) {
							String line = "	li 9," + operand_a.get("operand");
							assFileHandler.insert(line, "TEXT");
						}
						
						if(operand_b.get("type").equals("ARRAY_ITEM")) {
							throw new Exception("not support array operation yet!");
						} else if(operand_b.get("type").equals("VARIABLE")) {
							String line = "	lwz 0," + operand_b.get("operand") + "(31)";
							assFileHandler.insert(line, "TEXT");
							line = "	mullw 0,9,0";
							assFileHandler.insert(line, "TEXT");
						} else if(operand_b.get("type").equals("CONSTANT")) {
							String line = "	li 0," + operand_b.get("operand");
							assFileHandler.insert(line, "TEXT");
							line = "	mullw 0,9,0";
							assFileHandler.insert(line, "TEXT");
						}
						
						// 赋值给临时操作数
						String line = "	stw 0,bss_tmp(31)";
						assFileHandler.insert(line, "TEXT");
						// 计算结果压栈
						Map<String, String> tmpMap = new HashMap<>();
						tmpMap.put("type", "VARIABLE");
						tmpMap.put("operand", "bss_tmp");
						operandStack.add(tmpMap);
						// 记录到符号表中
						tmpMap = new HashMap<>();
						tmpMap.put("type", "IDENTIFIER");
						tmpMap.put("field_type", "int");
						symbolTable.put("bss_tmp", tmpMap);
					}
				// 整数除法
				} else if(operator.equals("/")) {
					if(containFloat) {
						
						throw new Exception("div not supported float yet");
						
					} else {
						if(operand_a.get("type").equals("ARRAY_ITEM")) {
							throw new Exception("not support array operation yet!");
						} else if(operand_a.get("type").equals("VARIABLE")) {						
							String line = "	lwz 9," + operand_a.get("operand") + "(31)";
							assFileHandler.insert(line, "TEXT");							
						} else if(operand_a.get("type").equals("CONSTANT")) {
							String line = "	li 9," + operand_a.get("operand");
							assFileHandler.insert(line, "TEXT");
						}
						
						if(operand_b.get("type").equals("ARRAY_ITEM")) {
							throw new Exception("not support array operation yet!");
						} else if(operand_b.get("type").equals("VARIABLE")) {
							String line = "	lwz 0," + operand_b.get("operand") + "(31)";
							assFileHandler.insert(line, "TEXT");
							line = "	divw 0,9,0";
							assFileHandler.insert(line, "TEXT");
						} else if(operand_b.get("type").equals("CONSTANT")) {
							String line = "	li 0," + operand_b.get("operand");
							assFileHandler.insert(line, "TEXT");
							line = "	divw 0,9,0";
							assFileHandler.insert(line, "TEXT");
						}
						
						// 赋值给临时操作数
						String line = "	stw 0,bss_tmp(31)";
						assFileHandler.insert(line, "TEXT");
						// 计算结果压栈
						Map<String, String> tmpMap = new HashMap<>();
						tmpMap.put("type", "VARIABLE");
						tmpMap.put("operand", "bss_tmp");
						operandStack.add(tmpMap);
						// 记录到符号表中
						tmpMap = new HashMap<>();
						tmpMap.put("type", "IDENTIFIER");
						tmpMap.put("field_type", "int");
						symbolTable.put("bss_tmp", tmpMap);
					}
				// 取余运算
				} else if(operator.equals("%")) {
					if(operand_a.get("type").equals("ARRAY_ITEM")) {
						throw new Exception("not support array operation yet!");
					} else if(operand_a.get("type").equals("VARIABLE")) {						
						String line = "	lwz 9," + operand_a.get("operand") + "(31)";
						assFileHandler.insert(line, "TEXT");							
					} else if(operand_a.get("type").equals("CONSTANT")) {
						String line = "	li 9," + operand_a.get("operand");
						assFileHandler.insert(line, "TEXT");
					}
					
					if(operand_b.get("type").equals("ARRAY_ITEM")) {
						throw new Exception("not support array operation yet!");
					} else if(operand_b.get("type").equals("VARIABLE")) {
						String line = "	lwz 11," + operand_b.get("operand") + "(31)";
						assFileHandler.insert(line, "TEXT");
						line = "	divw 0,11,9";
						assFileHandler.insert(line, "TEXT");
						line = "	mullw 0,9,0";
						assFileHandler.insert(line, "TEXT");
						line = "	subf 0,0,11";
						assFileHandler.insert(line, "TEXT");
					} else if(operand_b.get("type").equals("CONSTANT")) {
						String line = "li 11," + operand_b.get("operand");
						assFileHandler.insert(line, "TEXT");
						line = "	divw 0,11,9";
						assFileHandler.insert(line, "TEXT");
						line = "	mullw 0,9,0";
						assFileHandler.insert(line, "TEXT");
						line = "	subf 0,0,11";
						assFileHandler.insert(line, "TEXT");
					}
					
					// 赋值给临时操作数
					String line = "	stw 0,bss_tmp(31)";
					assFileHandler.insert(line, "TEXT");
					// 计算结果压栈
					Map<String, String> tmpMap = new HashMap<>();
					tmpMap.put("type", "VARIABLE");
					tmpMap.put("operand", "bss_tmp");
					operandStack.add(tmpMap);
					// 记录到符号表中
					tmpMap = new HashMap<>();
					tmpMap.put("type", "IDENTIFIER");
					tmpMap.put("field_type", "int");
					symbolTable.put("bss_tmp", tmpMap);
				} else if(operator.equals(">=")) {
					if(containFloat) {
						
						throw new Exception("<= not support float");
						
					} else {
						// int type
						if(operand_a.get("type").equals("ARRAY_ITEM")) {
							throw new Exception("not support array operation yet!");
						} else if(operand_a.get("type").equals("VARIABLE")) {						
							String line = "	lwz 0," + operand_a.get("operand") + "(31)";
							assFileHandler.insert(line, "TEXT");							
						} else if(operand_a.get("type").equals("CONSTANT")) {
							String line = "	li 0," + operand_a.get("operand");
							assFileHandler.insert(line, "TEXT");
						}
						
						if(operand_b.get("type").equals("ARRAY_ITEM")) {
							throw new Exception("not support array operation yet!");
						} else if(operand_b.get("type").equals("VARIABLE")) {
							String line = "	lwz 9," + operand_b.get("operand") + "(31)";
							assFileHandler.insert(line, "TEXT");
						} else if(operand_b.get("type").equals("CONSTANT")) {
							String line = "	li 9," + operand_b.get("operand");
							assFileHandler.insert(line, "TEXT");
						}
						
						String line = "	cmp 28,0,0,9";
						assFileHandler.insert(line, "TEXT");
						line = "	li 0,0";
						assFileHandler.insert(line, "TEXT");
						line = "	li 9,1";
						assFileHandler.insert(line, "TEXT");
						line = "	isel 0,9,0,28";
						assFileHandler.insert(line, "TEXT");
						// 赋值给临时操作数
						line = "	stw 0,bss_tmp(31)";
						assFileHandler.insert(line, "TEXT");
						// 计算结果压栈
						Map<String, String> tmpMap = new HashMap<>();
						tmpMap.put("type", "VARIABLE");
						tmpMap.put("operand", "bss_tmp");
						operandStack.add(tmpMap);
						// 记录到符号表中
						tmpMap = new HashMap<>();
						tmpMap.put("type", "IDENTIFIER");
						tmpMap.put("field_type", "int");
						symbolTable.put("bss_tmp", tmpMap);
					}
				} else if(operator.equals("<")) {
					if(containFloat) {
						
						throw new Exception("Sorry, < not support float type");
						
					} else {
//						String line = operand_a.get("type").equals("CONSTANT") ? "movl $" : "movl ";
//						line += operand_a.get("operand") + ", %edi";
//						assFileHandler.insert(line, "TEXT");
//						
//						line = operand_b.get("type").equals("CONSTANT") ? "movl $" : "movl ";
//						line += operand_b.get("operand") + ", %esi";
//						assFileHandler.insert(line, "TEXT");
//						
//						line = "cmpl %esi, %edi";
//						assFileHandler.insert(line, "TEXT");
//						
//						line = operatorMap.get("<") + " " + "label_" + labelCnt;
//						assFileHandler.insert(line, "TEXT");

						
						if(operand_a.get("type").equals("ARRAY_ITEM")) {
							throw new Exception("not support array operation yet!");
						} else if(operand_a.get("type").equals("VARIABLE")) {						
							String line = "	lwz 0," + operand_a.get("operand") + "(31)";
							assFileHandler.insert(line, "TEXT");							
						} else if(operand_a.get("type").equals("CONSTANT")) {
							String line = "	li 0," + operand_a.get("operand");
							assFileHandler.insert(line, "TEXT");
						}
						
						if(operand_b.get("type").equals("ARRAY_ITEM")) {
							throw new Exception("not support array operation yet!");
						} else if(operand_b.get("type").equals("VARIABLE")) {
							String line = "	lwz 9," + operand_b.get("operand") + "(31)";
							assFileHandler.insert(line, "TEXT");
						} else if(operand_b.get("type").equals("CONSTANT")) {
							String line = "	li 9," + operand_b.get("operand");
							assFileHandler.insert(line, "TEXT");
						}
						
						String line = "	cmp 28,0,0,9";
						assFileHandler.insert(line, "TEXT");
//						line = "	li 0,0";
//						assFileHandler.insert(line, "TEXT");
//						line = "	li 9,1";
//						assFileHandler.insert(line, "TEXT");
//						line = "	isel 0,9,0,28";
//						assFileHandler.insert(line, "TEXT");
						// 赋值给临时操作数
						line = "	stw 0,relational_tmp(31)";
						assFileHandler.insert(line, "TEXT");
						// 计算结果压栈
						Map<String, String> tmpMap = new HashMap<>();
						tmpMap.put("type", "VARIABLE");
						tmpMap.put("operand", "bss_tmp");
						operandStack.add(tmpMap);
						// 记录到符号表中
						tmpMap = new HashMap<>();
						tmpMap.put("type", "IDENTIFIER");
						tmpMap.put("field_type", "int");
						symbolTable.put("bss_tmp", tmpMap);
					}
				}
				
			} else if(singleOperators.contains(operator)) {
				Map<String, String> operand = operandStack.pop();
				if(operator.equals("++")) {
//					String line = "incl " + operand.get("operand");
//					assFileHandler.insert(line, "TEXT");
					
					String line = "	lwz 0," + operand.get("operand") + "(31)";
					assFileHandler.insert(line, "TEXT");
					line = "	addic 0,0,1";
					assFileHandler.insert(line, "TEXT");
					line = "	stw 0," + operand.get("operand")  + "(31)";
					assFileHandler.insert(line, "TEXT");
					
				} else if(operator.equals("--")) {
					String line = "	lwz 0," + operand.get("operand") + "(31)";
					assFileHandler.insert(line, "TEXT");
					line = "	addic 0,0,-1";
					assFileHandler.insert(line, "TEXT");
					line = "	stw 0," + operand.get("operand")  + "(31)";
					assFileHandler.insert(line, "TEXT");
				} else {
					throw new Exception("Only suport ++ and -- singleOperator");
				}
			} else {
				throw new Exception("other operator not support int expression");
			}
			assFileHandler.insert("", "TEXT");	// 增加一个空行
		}
		Map<String, String> result = new HashMap<>();
		if(!operandStack.empty()) {
			result.put("type", operandStack.get(0).get("type"));
			result.put("value", operandStack.get(0).get("operand"));
		} else {
			result.put("type", "");
			result.put("value", "");
		}
		return result;
	}
	
	// 处理某一种句型
	private void handlerSentenceblock(SyntaxTreeNode node) throws Exception {
		if(null == node) return;
		
		// 下一个将要遍历的节点
		if(sentenceType.contains(node.getValue())) {
			// 如果是根节点
			if(node.getValue().equals("Sentence")) {
				traverse(node.getFirstSon());
			// include语句
			} else if(node.getValue().equals("Include")) {
				_include(node);
			// 函数声明
			} else if(node.getValue().equals("FunctionStatement")) {
				_functionStatement(node);
			// 声明语句
			} else if(node.getValue().equals("Statement")) {
				_statement(node);
			// 函数调用
			} else if(node.getValue().equals("FunctionCall")) {
				_functionCall(node);
			// 赋值语句
			} else if(node.getValue().equals("Assignment")) {
				_assignment(node);
			// 控制语句
			} else if(node.getValue().equals("Control")) {
				if(node.getType().equals("IfElseControl")) {
					_controlIf(node);
				} else if(node.getType().equals("ForControl")) {
					_controlFor(node);
				} else if(node.getType().equals("WhileControl")) {
					_controlWhile(node);
				} else {
					throw new Exception("control type not supported");
				}
			// 表达式语句
			} else if(node.getValue().equals("Expression")) {
				_expression(node);
			// return语句
			} else if(node.getValue().equals("Return")) {
				_return(node);
			} else {
				new Exception("sentenct type not supported yet");
			}
		}
	}
	
	// 从左向右遍历某一层的全部节点
	public void traverse(SyntaxTreeNode node) throws Exception {		
		while(node != null) {
			handlerSentenceblock(node);
			node = node.getRight();
		}
	}
	
	public static void main(String[] args) throws Exception {
		String src = "src/input/sum_while.c";
		String filename = src.substring(src.lastIndexOf("/") + 1);
		Lexer lexer = new Lexer(Lexer.getContent(src));
		lexer.runLexer();
		List<Token> tokens = lexer.getTokens();
		System.out.println("========================词法分析===========================");
		Lexer.outputLexer(tokens);
		
		System.out.println("\n===========================语法分析========================");
		Parser parser = new Parser(tokens);
		parser.runParser();
		src = "src/output/parser.txt";
		Parser.outputParser(parser, src);
		
		System.out.println("\n==========================汇编代码生成==========================");
		Assembler assembler = new Assembler(parser.getTree());
		assembler.traverse(assembler.getTree().getRoot());
		src = "src/output/assembler.txt";
		assembler.getAssFileHandler().generateAssFile(src, filename);
	}
}
