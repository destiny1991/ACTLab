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
		if(null == item2.getPremise() && null == item2.getRight()) return item1;
		else if(null == item2.getPremise()) {
			
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param smt1	code当前遍历到的语义
	 * @param smt2	对semanSet已存在的语义，只能增加和修改
	 * @return
	 */
	public Semantic solveTwoSemantic(Semantic smt1, Semantic smt2) {
		List<Item> tmp = new ArrayList<Item>();
		
		for(Item item2 : smt2.getSemanSet()) {
			for(Item item1 : smt1.getSemanSet()) {
				Item e = solveTwoItem(item1, item2);
				if(null != tmp) tmp.add(e);
			}
		}
		
		if(0 == tmp.size()) return null;
		else return new Semantic(tmp);
	}
	
	public List<Semantic> verificationProcess(List<Instruction> codeSet) {
		List<Semantic> semanSet = new ArrayList<Semantic>();
		
		for(int i=0; i<codeSet.size(); i++) {
			Semantic smt1 = codeSet.get(i).getSeman();	//code当前遍历到的语义
			for(int j=0; j<semanSet.size(); j++) {
				Semantic smt2 = semanSet.get(j);		//已存在semanSet中的语义
				Semantic e = solveTwoSemantic(smt1, smt2);
				if(null != e) semanSet.add(e);			//对semanSet已存在的语义，只能增加和修改
			}
		}
				
		return semanSet;
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
			Tool.printCodeSemantic(codeSet);
			
			/**
			 * 基于指称语义进行推导
			 */
//			result = verificationProcess(codeSet);
//			Tool.printSemanticList(result);
			
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
