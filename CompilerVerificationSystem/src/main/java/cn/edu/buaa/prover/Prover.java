package cn.edu.buaa.prover;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
	// 中间过程输出结果
	public BufferedWriter bufferedWriter;

	public Prover() {
		loadAxioms("src/main/resources/axiom/ppcAxiom.xls");
		// showAxioms();

		loadAllObjectCodePatterns("src/main/resources/statement/");
		// showAllObjectCodePatterns();
	}

	/**
	 * 对输入的目标模式进行证明： 1) 目标码映射 2) 推理证明 3) 获得语义
	 * 
	 * @param objectCodePatterns
	 * @throws IOException
	 */
	public void proveProcess(List<String> objectCodePatterns) {

		if (bufferedWriter != null) {
			try {
				bufferedWriter.write("目标码模式 :\n");
				saveAllString(objectCodePatterns);
				bufferedWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// 命题映射
		List<Proposition> propositions = PropositionMappingAlgorithm.process(objectCodePatterns, axioms);
		showAllProposition(propositions);
		if (bufferedWriter != null) {
			try {
				bufferedWriter.write("目标码模式命题 :\n");
				saveAllProposition(propositions);
				bufferedWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 命题推导
		System.out.println("\n=============================\n");
		List<Proposition> simplifiedPropositions = AutomaticDerivationAlgorithm.process(propositions);
		showAllProposition(simplifiedPropositions);
		if (bufferedWriter != null) {
			try {
				bufferedWriter.write("命题推理结果 :\n");
				saveAllProposition(simplifiedPropositions);
				bufferedWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 获得语义
		System.out.println("\n=============================\n");
		List<Proposition> semantemes = obtainSemantemeFromProposition(simplifiedPropositions);
		showAllProposition(semantemes);
		
		// 循环交互证明算法
		

	}
	
	public List<Proposition> obtainSemantemeFromProposition(List<Proposition> simplifiedPropositions) {
		
		List<Proposition> semantemes = new ArrayList<>();
		for (Proposition proposition : simplifiedPropositions) {
			Proposition tmp = ProverHelper.cloneProposition(proposition);
			if (tmp.size() == 3) {
				ProverHelper.reducePropositionOfThree(tmp);
			}
			semantemes.add(tmp);
		}
				
		// 从含有多条语句的命题开始处理
		for (int i = 0; i < semantemes.size(); i++) {
			Proposition pro = semantemes.get(i);
			if (pro.size() > 1) {
				solveMultiple(pro, i, semantemes);
			}
		}
		
		// 清理无用的命题
		for (int i = 0; i < semantemes.size(); i++) {
			Proposition pro = semantemes.get(i);
			if (pro.size() == 1){
				if (pro.getItems().get(0).getPremise() == null 
						&& pro.getItems().get(0).getLeft() != null
						&& pro.getItems().get(0).getRight() == null 
						&& pro.getItems().get(0).getLeft().matches("\\.L(\\d)+:")) {
					pro.getItems().clear();
				} else if (pro.getItems().get(0).getPremise() == null 
						&& pro.getItems().get(0).getLeft() != null
						&& pro.getItems().get(0).getRight() != null) {
					if (pro.getItems().get(0).getLeft().equals("PC") && pro.getItems().get(0).getRight().contains("PC")) {
						pro.getItems().clear();
					}
				}
			}
			if (pro.size() == 0) {
				semantemes.remove(i);
				i--;
			}
		}
		
		return semantemes;
	}

	public void solveMultiple(Proposition pro, int i, List<Proposition> semantemes) {
		
		for (int j = 0; j < pro.size(); j++) {
			Item item = pro.getItems().get(j);
			if (item.getLeft().equals("PC")) {
				List<Proposition> S = new ArrayList<>(); 
				// 左右两边含有PC
				if (item.getRight().contains("PC")) {
					if (item.getRight().contains("PC + 4")) {
						
						obtainsProposition(S, semantemes, i + 1, semantemes.size());
						
					} else if (item.getRight().contains("@")) {
						String address = item.getRight().substring(item.getRight().indexOf("@") + 1).trim();
					
						for (int k = 0; k < semantemes.size(); k++) {
							if (i == k) continue;
							if (semantemes.get(k).size() == 1 
									&& semantemes.get(k).getItems().get(0).getLeft().contains(address)) {
								if (k < i) {
									obtainsProposition(S, semantemes, k + 1, i);
								} else {
									obtainsProposition(S, semantemes, k + 1, semantemes.size());
								}
								break;
							}
						}
					}
				}
				
				if (S.size() > 0) {
					item.setRight(null);
					StringBuffer sb = new StringBuffer();
//					sb.append("{");
					sb.append(S.get(0).getItems().get(0).toString());
					for (int k = 1; k < S.size(); k++) {
						sb.append("; " + S.get(k).getItems().get(0).toString());
					}
//					sb.append("}");
					
					item.setLeft(sb.toString().trim());
					
					for (int k = 0; k < S.size(); k++) {
						S.get(k).getItems().clear();
					}
				} else {
					item.setLeft(null);
					item.setRight(null);
				}
			}
			
		}
	}

	public void obtainsProposition(List<Proposition> S, List<Proposition> semantemes, int i, int limit) {
		
		for (; i < limit; i++) {
			if (semantemes.get(i).size() == 1) {
				Item item = semantemes.get(i).getItems().get(0);
				if (item.getPremise() == null && item.getLeft() != null && item.getRight() == null) {
					if (item.getLeft().contains("<") && item.getLeft().contains(">")) {
						S.add(semantemes.get(i));
					} else {
						continue;
					}
				} else if (item.getPremise() == null && item.getLeft() != null && item.getRight() != null) {
					if (item.getLeft().equals("PC") && item.getRight().contains("PC") && item.getRight().contains("@")) {
						String address = item.getRight().substring(item.getRight().indexOf("@") + 1).trim();
						
						for (int k = 0; k < semantemes.size(); k++) {
							if (i == k) continue;
							if (semantemes.get(k).size() == 1 
									&& semantemes.get(k).getItems().get(0).getLeft().contains(address)) {
								if (k < i) {
									obtainsProposition(S, semantemes, k + 1, i);
								} else {
									obtainsProposition(S, semantemes, k + 1, semantemes.size());
								}
								semantemes.get(i).getItems().clear();
								return;
							}
						}
					} else {
						S.add(semantemes.get(i));
					}
				} else {
					return;
				}
			}  else {
				return;
			} 
		}
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
		for (String name : axioms.keySet()) {
			Proposition proposition = axioms.get(name);
			System.out.println(name + ":");
			System.out.println(proposition);
		}
	}

	public void loadAllObjectCodePatterns(String dirName) {
		allObjectCodePatterns = new HashMap<>();

		File dir = new File(dirName);
		for (String fileName : dir.list()) {
			String path = dirName;
			if (!path.endsWith("/"))
				path += "/";
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
			while (null != (line = reader.readLine())) {
				line = line.trim();
				if (line.length() == 0)
					continue;
				objectCodePatterns.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
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

		for (String key : allObjectCodePatterns.keySet()) {
			List<String> objectCodePatterns = allObjectCodePatterns.get(key);
			System.out.println(key + "::");
			for (String line : objectCodePatterns) {
				System.out.println(line);
			}
			System.out.println();
		}

	}

	public void showAllProposition(List<Proposition> propositions) {
		for (Proposition proposition : propositions) {
			System.out.println(proposition);
		}
	}

	public void createOutputFile(String key) {
		try {
			bufferedWriter = new BufferedWriter(new FileWriter("src/main/resources/output/" + key + ".txt"));
			bufferedWriter.write("======================结果输出=======================\n");
			bufferedWriter.write("语句 : " + key + "\n");
			bufferedWriter.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void saveAllProposition(List<Proposition> propositions) throws IOException {
		for (Proposition proposition : propositions) {
			bufferedWriter.write(proposition.toString());
		}
		bufferedWriter.newLine();
	}

	public void saveAllString(List<String> objectCodePatterns) throws IOException {
		for (String str : objectCodePatterns) {
			bufferedWriter.write(str);
			bufferedWriter.newLine();
		}
		bufferedWriter.newLine();
	}

	public static void main(String[] args) {
		Prover prover = new Prover();
		String key = "for";
		List<String> objectCodePatterns = prover.getObjectCodePatterns(key);
		prover.createOutputFile(key);
		prover.proveProcess(objectCodePatterns);
	}
}
