package cn.edu.buaa.compile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Tool {
	/**
	 * 过滤输入字符串数组中的指定字符串
	 * @param lines
	 * @return
	 */
	public static String[] filterOtherSignal(String[] lines) {
		List<String> tmp = new ArrayList<String>();
		if(null != lines && 0 != lines.length) {
			for(String s : lines) {
				if(0 == s.length() || "\t" == s || " " == s || "," == s || "" == s ||
						"(" == s || ")" == s) continue;
				tmp.add(s);
			}
		}
		
		String[] lists = new String[tmp.size()];
		tmp.toArray(lists);
		return lists;
	}
	
	/**
	 * 把codeSet中的语义放到List中, 使用clone函数新建和原对象一样的对象
	 * @param codeSet
	 * @return
	 */
	public static List<Semantic>  cloneSemanFromCodeSet(List<Instruction> codeSet) {
		List<Semantic> res = new ArrayList<Semantic>();
		for(Instruction inst : codeSet) {
			List<Item> ts = inst.getSeman().getSemanSet();
			List<Item> td = new ArrayList<Item>();
			
			for(Item i : ts) {
				try {
					Item t = (Item)i.clone();
					td.add(t);
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
			
			Semantic s = new Semantic(td);
			res.add(s);
		}
		return res;
	}
	
	/**
	 * 把推语义List写入文本文件
	 * @param inputFile
	 * @param seman
	 */
	public static void saveResult(String inputFile, List<Semantic> seman) {
		int index = inputFile.lastIndexOf('/');
		String name = inputFile.substring(index + 1);
		String outputName = "semantic_" + name;
		File file = new File("src/output/" + outputName);
		if(!file.exists()){  
		    try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}  
		}  
		
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(file)
							)
					);
			for(Semantic s : seman) {
				for(Item e : s.getSemanSet()) {
					if(null == e.getPremise() && null == e.getRight()) {
						writer.write(e.getLeft());
					} else if(null == e.getPremise()) {
						writer.write(e.getLeft() + " = " + e.getRight());
					} else {
						String str = e.getPremise() + " -> " + e.getLeft() + " = " + e.getRight();
						writer.write(str);
					}
					writer.newLine();
				}
				writer.newLine();
				writer.flush();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if(null != writer) {
				try {
					writer.close();
				} catch (IOException e) {
				e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 输出Item List
	 * @param result
	 */
	public static void printItemList(List<Item> result) {
		for(Item item : result) {
			if(null == item.getPremise() && null == item.getRight()) {
				System.out.println(item.getLeft());
			} else if(null == item.getPremise()) {
				System.out.println(item.getLeft() + " = " + item.getRight());
			} else {
				System.out.println(item.getPremise() + " -> " 
						+ item.getLeft() + " = " + item.getRight());
			}
		}
	}
	
	/**
	 * 输出所有的语义
	 * @param seman
	 */
	public static void printSemanticList(List<Semantic> seman) {
		for(Semantic sm : seman) {
			List<Item> semanSet = sm.getSemanSet();
			for(Item item : semanSet) {
				if(null == item.getPremise() && null == item.getRight()) {
					System.out.println(item.getLeft());
				} else if(null == item.getPremise()) {
					System.out.println(item.getLeft() + " = " + item.getRight());
				} else {
					System.out.println(item.getPremise() + " -> " 
							+ item.getLeft() + " = " + item.getRight());
				}
			}
			System.out.println();
		}
	}
	
	/**
	 * 输出codeSet的中指令的全部内容
	 * @param codeSet
	 */
	public static void printCodeSet(List<Instruction> codeSet) {
		for(Instruction ins : codeSet) {
			System.out.println("name: " + ins.getName());
			
			System.out.print("paras: ");
			if(null != ins.getParas()) {
				Set<String> keys = ins.getParas().keySet();
				for(String key : keys) {
					String value = ins.getParas().get(key);
					System.out.print(key + "--" + value + "\t");
				}
			} else {
				System.out.print("null");
			}
			System.out.println();
			
			System.out.println("Semantic: ");
			List<Item> semanSet = ins.getSeman().getSemanSet();
			for(Item item : semanSet) {
				if(null == item.getPremise() && null == item.getRight()) {
					System.out.println(item.getLeft());
				} else if(null == item.getPremise()) {
					System.out.println(item.getLeft() + " = " + item.getRight());
				} else {
					System.out.println(item.getPremise() + " -> " 
							+ item.getLeft() + " = " + item.getRight());
				}
			}
			System.out.println();
		}
	}
	
	/**
	 * 输出代码的指称语义
	 * @param codeSet
	 */
	public static void printCodeSemantic(List<Instruction> codeSet) {
		for(Instruction ins : codeSet) {
			List<Item> semanSet = ins.getSeman().getSemanSet();
			for(Item item : semanSet) {
				if(null == item.getPremise() && null == item.getRight()) {
					System.out.println(item.getLeft());
				} else if(null == item.getPremise()) {
					System.out.println(item.getLeft() + " = " + item.getRight());
				} else {
					System.out.println(item.getPremise() + " -> " 
							+ item.getLeft() + " = " + item.getRight());
				}
			}
			System.out.println();
		}
	}
	
	/**
	 * 输出公理所在的Map
	 */
	public static void printMap(Map<String, Semantic> axiomSet) {
		Set<String> set = axiomSet.keySet();
		for(String key : set) {
			Semantic seman = axiomSet.get(key);
			List<Item> semanSet = seman.getSemanSet();
			for(Item item : semanSet) {
				if(null == item.getPremise() && null == item.getRight()) {
					System.out.println(item.getLeft());
				} else if(null == item.getPremise()) {
					System.out.println(item.getLeft() + " = " + item.getRight());
				} else {
					System.out.println(item.getPremise() + " -> " 
							+ item.getLeft() + " = " + item.getRight());
				}
			}
			System.out.println();
		}
	}
}
