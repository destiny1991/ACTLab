package cn.edu.buaa.compile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 形式验证程序
 * @author destiny
 */
public class CompileVerification {
	private List<Instruction> codeSet;
	private List<Semantic> semanSrc;
	private List<Semantic> result;
	
	public CompileVerification() {
	}
	
	public  List<Instruction> createInstructionSet(BufferedReader reader, String regex) throws IOException {
		List<Instruction> instructionSet = new ArrayList<Instruction>();
		String str;
		Instruction inst = null;
		while(null != (str = reader.readLine())) {
			str = str.trim();
			if(0 == str.length()) continue;
			
			String[] lines = str.split(regex);	//separator不作为任何数组元素的部分返回
			if(1 == lines.length) {		//特殊处理单项
				inst = new Instruction(lines[0]);
				List<Item> semanSet = new ArrayList<Item>();
				Semantic seman = new Semantic();
				Item item = new Item(lines[0]);		//单项语义放在Item.left中
				semanSet.add(item);
				seman.setSemanSet(semanSet);
				inst.setSeman(seman);
			} else {
				lines = Tool.filterOtherSignal(lines); 
				inst = new Instruction(lines[0]);
				Map<String, String> paras = Helper.generateParas(lines);
				Semantic seman = Helper.generateSemantic(lines[0], paras);
				inst.setParas(paras);
				inst.setSeman(seman);
			}
			instructionSet.add(inst);
		}
		return instructionSet;
	}
	
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
					item2.setRight(item1.getRight());
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
	
	public List<Semantic>  cloneSemanFromCodeSet(List<Instruction> codeSet) {
		List<Semantic> ts = new ArrayList<Semantic>();
		for(Instruction inst : codeSet) {
			Semantic t = inst.getSeman();
			ts.add(t);
		}
		return ts;
	}
	
	public void runApp(String inputFile, String regex) {
		File file = new File(inputFile);
		
		BufferedReader reader = null;
		
		try {
			/**
			 * 获得输入汇编代码文件
			 */
			reader = new BufferedReader(
						new InputStreamReader(
								new FileInputStream(file)
								)
						);
			
			/**
			 * 把输入的汇编代码翻译成对应的指称语义形式
			 */
			codeSet = createInstructionSet(reader, regex);
			if(null == codeSet || 0 == codeSet.size()) return;
//			Tool.printCodeSet(codeSet);
//			Tool.printCodeSemantic(codeSet);
			
			/**
			 * 基于指称语义进行推导
			 */
			semanSrc = cloneSemanFromCodeSet(codeSet);
			Tool.printSemanticList(semanSrc);
			result = verificationProcess(semanSrc);
			System.out.println("******************************\n\n");
			Tool.printSemanticList(result);
			
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
	}
		
	public static void main(String[] args) {
		CompileVerification cv = new CompileVerification();
		String inputPath = "src/cn/edu/buaa/resources/assembly.txt";
		String regex = "\t| |,|\\(|\\)";
		cv.runApp(inputPath, regex);
	}
}
