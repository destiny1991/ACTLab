package cn.edu.buaa.prover;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.buaa.pojo.Item;
import cn.edu.buaa.pojo.Proposition;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Prover {
	
	// 专用公理集
	private Map<String, Proposition> axioms;
	// 所有语句的目标码模式
	private Map<String, List<String>> allObjectCodePatterns;

	public Prover() {
		loadAxioms("src/main/resources/axiom/ppcAxiom.xls");
//		showAxioms();
		
		loadAllObjectCodePatterns("src/main/resources/statement/");
//		showAllObjectCodePatterns();
	}
	
	/**
	 * 对输入的目标模式进行证明：
	 * 1) 目标码映射
	 * 2) 推理证明
	 * 3) 获得语义
	 * @param objectCodePatterns
	 */
	public void proveProcess(List<String> objectCodePatterns) {
		
		List<Proposition> propositions = PropositionMappingAlgorithm.process(objectCodePatterns, axioms);
		showAllProposition(propositions);
		
//		List<Proposition> semantemes = AutomaticDerivationAlgorithm.process(propositions);
		
		
	}
	
	public List<String> getObjectCodePatterns(String key) {
		return allObjectCodePatterns.get(key);
	}
	
	public void loadAxioms(String path) {
		axioms = new HashMap<>();
		
		Workbook readwb = null;
		try {
			readwb = Workbook.getWorkbook(new File(path));
			Sheet readsheet = readwb.getSheet(0);
			
			// 读取xls文档
			int rsRows = readsheet.getRows();
			int i = 1;
			while (i < rsRows) {
				String name = readsheet.getCell(0, i).getContents().trim();
				if (name == null || name.length() == 0)
					continue;
				
				List<Item> items = new ArrayList<Item>();
				Proposition proposition = new Proposition(items);
				while (i < rsRows) {
					String tmp = readsheet.getCell(0, i).getContents().trim();
					if (null == tmp || tmp.length() == 0 || tmp.equals(name)) {
						String premise = readsheet.getCell(1, i).getContents().trim();
						String left = readsheet.getCell(2, i).getContents().trim();
						String right = readsheet.getCell(3, i).getContents().trim();

						if (null == premise || premise.length() == 0)
							premise = null;
						if (null == left || left.length() == 0)
							left = null;
						if (null == right || right.length() == 0)
							right = null;

						if (null != premise || null != left || null != right) {
							Item item = new Item(premise, left, right);
							items.add(item);
						}
						
						i++;
					} else {
						break;
					}
				}
				
				axioms.put(name, proposition);
				name = null;
			}

		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showAxioms() {
		for(String name : axioms.keySet()) {
			Proposition proposition = axioms.get(name);
			System.out.println(name + ":");
			System.out.println(proposition);
		}
	}
	
	public void loadAllObjectCodePatterns(String dirName) {
		allObjectCodePatterns = new HashMap<>();
		
		File dir = new File(dirName);
		for(String fileName : dir.list()) {
			String path = dirName;
			if(!path.endsWith("/")) path += "/";
			path += fileName;
			
			List<String> objectCodePatterns = loadSingleObjectCodePatterns(path);
			String key = fileName.substring(0, fileName.indexOf('.'));			
			allObjectCodePatterns.put(key, objectCodePatterns);
		}
	}
	
	public List<String> loadSingleObjectCodePatterns(String path) {
		
		List<String> objectCodePatterns = new ArrayList<>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(path));
			String line = null;
			while(null != (line = reader.readLine())) {
				line = line.trim();
				if(line.length() == 0) continue;
				objectCodePatterns.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return objectCodePatterns;
	}
	
	public void showAllObjectCodePatterns() {

		for(String key : allObjectCodePatterns.keySet()) {
			List<String> objectCodePatterns = allObjectCodePatterns.get(key);
			System.out.println(key + "::");
			for(String line : objectCodePatterns) {
				System.out.println(line);
			}
			System.out.println();
		}
		
	}
	
	public void showAllProposition(List<Proposition> propositions) {
		for(Proposition proposition : propositions) {
			System.out.println(proposition);
		}
	}
	
	public static void main(String[] args) {
		Prover prover = new Prover();
		List<String> objectCodePatterns = prover.getObjectCodePatterns("while");
		prover.proveProcess(objectCodePatterns);
	}
}
