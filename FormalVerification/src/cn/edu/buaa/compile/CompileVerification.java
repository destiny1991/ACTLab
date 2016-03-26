package cn.edu.buaa.compile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * 形式验证程序
 * @author destiny
 */
public class CompileVerification {
	private Map<String, Semantic> axiomSet;
	private List<Instruction> codeSet;
	private List<Semantic> semanSrc;
	private List<Semantic> result;
	
		
	/**
	 * 
	 * @param item1		未加入semanSet
	 * @param item2		已加入semanSet
	 * @return
	 */
	public Item solveTwoItem(Item item1, Item item2) {
		if(null == item2.getPremise() && null == item2.getRight()) {
			return item1;
		} else if(null == item2.getPremise()) {
			if(null == item1.getPremise() && null == item1.getRight()) {
				return item1;
			} else if(null == item1.getPremise()) {
				if(!item2.getLeft().equals("PC")) {
					item1.setRight(item1.getRight().replace(item2.getLeft(), item2.getRight()));
				}
				if(item1.getLeft().equals(item2.getLeft())) {
					if(!item2.getLeft().equals("PC")) {
						return null;
					} else {
						return item1;
					}
				} else {
					return item1;
				}
			} else {
				if(!item2.getLeft().equals("PC")) {
					item1.setRight(item1.getRight().replace(item2.getLeft(), item2.getRight()));
				}
				String str = item2.getLeft() + " = " + item2.getRight();
				if(item1.getPremise().equals(str)) {
					item1.setPremise(null);
					if(item1.getLeft().equals(item2.getLeft())) {
						return null;
					} else {
						return item1;
					}
				} else {
					return item1;
				}
			}
		} else {
			if(null == item1.getPremise() && null == item1.getRight()) {
				return item1;
			} else if(null == item1.getPremise()) {
				if(item1.getLeft().equals(item2.getLeft())) {
					if(!item2.getLeft().equals("PC")) {
						item2.setRight(item1.getRight());
					}
				}
				return item1;
			} else {
				String str = item2.getLeft() + " = " + item2.getRight();
				if(item1.getPremise().equals(str)) {
					item1.setPremise(item2.getPremise());
					return null;
				} else {
					return item1;
				}
			}
		}
	}
	
	/**
	 * 
	 * @param smt1	code当前遍历到的语义
	 * @param smt2	semanSet已存在的语义
	 * @return
	 */
	public void solveTwoSemantic(Semantic smt1, Semantic smt2) {
		//用新加入的语义对已存在的语义进行化简
		for(int i=0; i<smt1.getSemanSet().size(); i++) {
			Item item1 = smt1.getSemanSet().get(i);
			for(int j=0; j<smt2.getSemanSet().size(); j++) {
				Item item2 = smt2.getSemanSet().get(j);
				//null, 已存在的语义删去
				if(null == solveTwoItem(item1, item2)) {
					smt2.getSemanSet().remove(j);
					j--;
				}
			}
		}
				
		//去掉三选一的情况
		for(int i=0; i<smt1.getSemanSet().size(); i++) {
			Item a = smt1.getSemanSet().get(i);
			if(null != a.getPremise()) {
				for(int j=0; j<smt1.getSemanSet().size(); j++) {
					Item b = smt1.getSemanSet().get(j);
					if(null == b.getPremise() && a.getLeft().equals(b.getLeft())) {
						smt1.getSemanSet().remove(i);
						i--;
					}
				}
			}
		}
		
	}
	
	public List<Semantic> verificationProcess(List<Semantic> semanSrc) {
		List<Semantic> semanSet = new ArrayList<Semantic>();
		
		for(int i=0; i<semanSrc.size(); i++) {
			Semantic smt1 = semanSrc.get(i);		//code当前遍历到的语义
			for(int j=0; j<semanSet.size(); j++) {
				Semantic smt2 = semanSet.get(j);	//已存在semanSet中的语义
				solveTwoSemantic(smt1, smt2);
				if(0 == smt2.getSemanSet().size()) {
					semanSet.remove(j);
					j--;
				}
			}
			if(0 != smt1.getSemanSet().size()) semanSet.add(smt1);	//增加			
		}
				
		return semanSet;
	}
	
	public  List<Instruction> createCodeSet(String inputFile, Map<String, Semantic> axiomSet) {	
		BufferedReader reader = null;
		List<Instruction> instructionSet = new ArrayList<Instruction>();
		String regex = "\t| |,|\\(|\\)";
		
		/**
		 * 获得输入汇编代码文件
		 */
		try {
			reader = new BufferedReader(
						new InputStreamReader(
								new FileInputStream(inputFile)
								)
						);
			
			String str;
			Instruction inst = null;
			while(null != (str = reader.readLine())) {
				str = str.trim();
				if(0 == str.length()) continue;
				
				String[] lines = str.split(regex);	//separator不作为任何数组元素的部分返回
				lines = Tool.filterOtherSignal(lines);
				if(1 == lines.length) {		//特殊处理单项
					inst = new Instruction(lines[0]);
					List<Item> semanSet = new ArrayList<Item>();
					Semantic seman = new Semantic();
					Item item = new Item(lines[0]);		//单项语义放在Item.left中
					semanSet.add(item);
					seman.setSemanSet(semanSet);
					inst.setSeman(seman);
				} else if(lines.length > 1) {
					inst = new Instruction(lines[0]);
					Map<String, String> paras = Helper.generateParas(lines);
					Semantic seman = Helper.generateSemantic(lines[0], paras, axiomSet);
					inst.setParas(paras);
					inst.setSeman(seman);
				} else {
					inst = null;
				}
				if(null != inst) instructionSet.add(inst);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return instructionSet;
	}
	
	public Map<String, Semantic> loadAxiom() {
		Map<String, Semantic> axiom = new HashMap<String, Semantic>(); 
		Workbook readwb = null;
		
		try {
			readwb = Workbook.getWorkbook(new File("src/cn/edu/buaa/resources/Axiom.xls"));
            Sheet readsheet = readwb.getSheet(0);
            int rsRows = readsheet.getRows();
            int i = 1;
            while(i < rsRows) {
            	String name = readsheet.getCell(0, i).getContents().trim();
            	if(null == name || name.equals("")) continue;
            	List<Item> semanSet = new ArrayList<Item>();
				Semantic seman = new Semantic(semanSet);
            	while(i < rsRows) {
            		String tmp = readsheet.getCell(0, i).getContents();
            		if(null == tmp || tmp.equals("") || tmp.equals(name))  {
            			String premise = readsheet.getCell(1, i).getContents().trim();
            			String left = readsheet.getCell(2, i).getContents().trim();
            			String right = readsheet.getCell(3, i).getContents().trim();
            			
            			if(null == premise || premise.equals("")) premise = null;
            			if(null == left || left.equals("")) left = null;
            			if(null == right || right.equals("")) right = null;
            			
            			if(null != premise || null != left || null != right) {
            				Item item = new Item(premise, left, right);
            				semanSet.add(item);
            			}
    	            	i++;
            		} else {
            			break;
            		}
            	}
            	axiom.put(name, seman);
				name = null;
            }
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(null != readwb) {
				readwb.close();
			}
		}
		
		return axiom;
	}
		
	public void runApp(String inputFile) {
		/**
		 * 载入程序所需的公理
		 */
		axiomSet = loadAxiom();
		
		long start = System.currentTimeMillis();
		
		/**
		 * 把输入的汇编代码翻译成对应的指称语义形式
		 */
		codeSet = createCodeSet(inputFile, axiomSet);
		if (null == codeSet || 0 == codeSet.size())
			return;

		/**
		 * 基于指称语义进行推导
		 */
		semanSrc = Tool.cloneSemanFromCodeSet(codeSet);
		result = verificationProcess(semanSrc);
		long end = System.currentTimeMillis();
//		Tool.saveResult(inputFile, result);

		Tool.printCodeSemantic(codeSet);
		System.out.println("**************************************************");
		Tool.printSemanticList(result);
		System.out.println("\n计算和推导耗时：" + (end - start) + " ms");
	}

	public static void main(String[] args) {
		CompileVerification cv = new CompileVerification();
		String inputPath = "src/input/if.txt";
		cv.runApp(inputPath);
	}
}
