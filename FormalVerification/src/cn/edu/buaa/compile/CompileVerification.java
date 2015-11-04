package cn.edu.buaa.compile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
				if(item1.getLeft().equals(item2.getLeft())) {
					if(!item2.getLeft().equals("PC")) {
						item2.setRight(item1.getRight());
					} else {
						return item1;
					}
				} else {
					return item1;
				}
			} else {
				String str = item2.getLeft() + " = " + item2.getRight();
				if(item1.getPremise().equals(str)) {
					item1.setPremise(null);
					return item1;
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
					} else {
						return item1;
					}
				} else {
					return item1;
				}
			} else {
				String str = item2.getLeft() + " = " + item2.getRight();
				if(item1.getPremise().equals(str)) {
					item2.setLeft(item1.getLeft());
					item2.setRight(item1.getRight());
				} else {
					return item1;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param smt1	code当前遍历到的语义
	 * @param smt2	对semanSet已存在的语义，只能增加和修改
	 * @return
	 */
	public void solveTwoSemantic(Semantic smt1, Semantic smt2) {
		//用已经存在的语义对新加的语义进行化简
		for(int i=0; i<smt1.getSemanSet().size(); i++) {
			Item item1 = smt1.getSemanSet().get(i);
			boolean isAdd = true;
			for(int j=0; j<smt2.getSemanSet().size(); j++) {
				Item item2 = smt2.getSemanSet().get(j);
				if(null == solveTwoItem(item1, item2)) {
					isAdd = false;
				}
			}
			if(!isAdd) {
				smt1.getSemanSet().remove(i);
				i--;
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
			}
			if(0 != smt1.getSemanSet().size()) semanSet.add(smt1);	//增加或修改
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
		BufferedReader reader = null;
		Map<String, Semantic> axiom = new HashMap<String, Semantic>(); 
		try {
			reader = new BufferedReader(
						new InputStreamReader(
								new FileInputStream("src/cn/edu/buaa/resources/axiom.txt")
								)
						);
			
			String line;
			String name = null;
			while(null != (line = reader.readLine())) {
				line = line.trim();
				if(0 == line.length()) continue;
				if(null == name) {
					name = line;
					List<Item> semanSet = new ArrayList<Item>();
					Semantic seman = new Semantic(semanSet);
					while(!name.equals(line = reader.readLine().trim())) {
						if(0 == line.length()) continue;
						String[] lines = line.split("\t");
						lines = Tool.filterOtherSignal(lines);
						Item item = null;
						if(1 == lines.length) {
							item = new Item(lines[0]);
						} else if(2 == lines.length) {
							item = new Item(lines[0], lines[1]);
						} else if(3 == lines.length) {
							item = new Item(lines[0], lines[1], lines[2]);
						} else {
							item = null;
						}
						if(null != item) semanSet.add(item);
					}
					axiom.put(name, seman);
					name = null;
				}
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
		return axiom;
	}
	
	public void runApp(String inputFile) {
		long start = System.currentTimeMillis();

		/**
		 * 载入程序所需的公理
		 */
		axiomSet = loadAxiom();
		
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
		Tool.saveResult(inputFile, result);

		Tool.printCodeSemantic(codeSet);
		System.out.println("**************************************************");
		Tool.printSemanticList(result);
		System.out.println("\n加载和推导耗时：" + (end - start) + " ms");
	}
		
	public static void main(String[] args) {
		CompileVerification cv = new CompileVerification();
		String inputPath = "src/input/if.txt";
		cv.runApp(inputPath);
	}
}
