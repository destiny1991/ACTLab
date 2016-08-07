package cn.edu.buaa.assembler;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import cn.edu.buaa.constant.AssemblerDefine;
import cn.edu.buaa.lexer.Lexer;
import cn.edu.buaa.parser.Parser;
import cn.edu.buaa.pojo.SyntaxTree;
import cn.edu.buaa.pojo.SyntaxTreeNode;

public class Assembler {
	
	// 语法分析传过来的语法树
	private SyntaxTree tree;
	
	// 要生成的汇编文件管理器
	private AssemblerFileHandler assFileHandler;
	
	// 符号表
	private Map<String, Map<String, String>> symbolTable;
	
	// 表达式中的操作符栈
	private Stack<String> operatorStack;
	
	// 表达式中的操作数栈
	private Stack<Map<String, String>> operandStack;
	
	// 操作符和操作数栈
	private Stack<Map<String, String>> optAndOpdStack;
	
	// 已经声明了多少个label
	private int labelCnt;
	
	// 已经使用了多少个相对地址
	private int memAdress;							
	
	// 控制生成的汇编代码中，变量是以数字还是原始名称出现，
	// 默认false，为原始名称出现
	private boolean isVariableSymbolOrNumber = true;
	
	public Assembler(SyntaxTree tree) {
		this.tree = tree;
		this.assFileHandler = new AssemblerFileHandler();
		this.symbolTable = new HashMap<>();
		this.operatorStack = new Stack<>();
		this.operandStack = new Stack<>();
		this.optAndOpdStack = new Stack<>();
		this.labelCnt = 0;
		this.memAdress = 8;		// 以8号地址起始
		
	}
	
	public void runAssembler() {
		// 从语法树的根节点开始遍历
		traverse(tree.getRoot());
		
	}
	
	// 从左向右遍历某一层的全部节点
	private void traverse(SyntaxTreeNode node) {		
		while(node != null) {
			handlerSentenceblock(node);
			node = node.getRight();
		}
	}	
	
	// 处理某一种句型
	private void handlerSentenceblock(SyntaxTreeNode node) {
		if (node == null) {
			return;
		}
		
		// 将要遍历的节点
		if (AssemblerUtils.isSentenceType(node.getValue())) {
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
					
				} else if (node.getType().equals("DoWhileControl")) {
					_controlDoWhile(node);
					
				} else {
					try {
						throw new Exception("control type not supported" + node.getType());
					} catch (Exception e) {
						e.printStackTrace();
						System.exit(1);
					}
					
				}
				
			// 表达式语句
			} else if(node.getValue().equals("Expression")) {
				_expression(node);
							
			// return语句
			} else if(node.getValue().equals("Return")) {
				_return(node);
			
			} else {
				try {
					throw new Exception("sentenct type not supported yet : " + node.getValue());
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
				
			}
			
		} else {
			try {
				throw new Exception("Unsupport sentence type : " + node.getValue());
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
	}
	
	// include句型
	private void _include(SyntaxTreeNode node) {
		// TODO Auto-generated method stub
		// 不用处理，不会生成对应的汇编代码
		
	}

	
	
	private void generateAssemblerFile(String fileName) {
		assFileHandler.generateAssemblerFile(fileName);
		
	}
	
	
	private void generateSymbolTableFile() {
		assFileHandler.generateSymbolTableFile(symbolTable);
		
	}
	
	private void outputAssembler() {
		System.out.println("===================Assembler==================");
		assFileHandler.dispalyResult();
		
	}
	
	public static void main(String[] args) {
		String fileName = "evenSum.c";
		Lexer lexer = new Lexer(fileName);
		lexer.runLexer();
		lexer.labelSrc(fileName);
		
		Parser parser = new Parser(lexer.getTokens());
		parser.runParser();
		
		Assembler assembler = new Assembler(parser.getTree());
		assembler.runAssembler();
		assembler.generateAssemblerFile(fileName);
		assembler.generateSymbolTableFile();
		assembler.outputAssembler();
		
	}

	

	
	
}
