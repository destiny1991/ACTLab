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
 * 形式验证主体函数
 * @author destiny
 */
public class CompileVerification {
	private List<Instruction> codeSet;
	
	public CompileVerification() {
		codeSet = new ArrayList<Instruction>();
	}
	
	public  Instruction createInstruction(BufferedReader reader, String regex) throws IOException {
		String str;
		Instruction inst = null;
		while(null != (str = reader.readLine())) {
			str = str.trim();
			if(0 == str.length()) continue;
			
			String[] lines = str.split(regex);
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
				Map<String, String> paras = Tool.generateParas(lines);
				Semantic seman = Tool.generateSemantic(lines[0], paras);
				inst.setParas(paras);
				inst.setSeman(seman);
			}
		}
		return inst;
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
			Instruction inst = createInstruction(reader, regex);
			codeSet.add(inst);
			
			/**
			 * 基于指称语义进行推导
			 */
			
			
			
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
		String regex = " |,|\\(|\\)";
		cv.runApp(inputPath, regex);
	}
}
