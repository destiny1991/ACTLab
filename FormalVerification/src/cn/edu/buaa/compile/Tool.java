package cn.edu.buaa.compile;

import java.util.ArrayList;
import java.util.List;
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
	 * 输出codeSet的内容
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
}
