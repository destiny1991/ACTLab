package cn.edu.buaa.compile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
				if(" " == s || "," == s || "" == s ||
						"(" == s || ")" == s) continue;
				tmp.add(s);
			}
		}
		return (String[]) tmp.toArray();
	}
	
	public static Map<String, String> generateParas(String[] lines) {
		Map<String, String> paras = new HashMap<String, String>();
		switch (lines[0]) {
		case "cmpi":
			paras.put("crfD", lines[1]);
			paras.put("rA", lines[3]);
			paras.put("SIMM", lines[4]);
			break;
		case "bc":
			paras.put("BO", lines[1]);
			paras.put("BI", lines[2]);
			paras.put("BD", lines[3]);
			paras.put("AA", "0");
			paras.put("LK", "0");
			break;
		case "b":
			paras.put("LI", lines[1]);
			paras.put("AA", "0");
			paras.put("LK", "0");
			break;
		default:
			break;
		}
		return paras;
	}
	
	public static Semantic generateSemantic(String name, Map<String, String> paras) {
		Semantic seman = new Semantic();
		List<Item> semanSet = new ArrayList<Item>();
		seman.setSemanSet(semanSet);
		String premise;
		String left;
		String right;
		Item item;
		
		switch (name) {
		case "cmpi":
			premise = "GPR[" + paras.get("rA") + "] < {16{" 
							+ paras.get("SIMM") + "[16]}, " + paras.get("SIMM") + "}";
			left = "CR[" + paras.get("crfD") + "]";
			right = "{3'b100, XER.SO}";
			item = new Item(premise, left, right);
			semanSet.add(item);
			
			premise = "GPR[" + paras.get("rA") + "] > {16{" 
					+ paras.get("SIMM") + "[16]}, " + paras.get("SIMM") + "}";
			left = "CR[" + paras.get("crfD") + "]";
			right = "{3'b010, XER.SO}";
			item = new Item(premise, left, right);
			semanSet.add(item);
			
			premise = "GPR[" + paras.get("rA") + "] = {16{" 
					+ paras.get("SIMM") + "[16]}, " + paras.get("SIMM") + "}";
			left = "CR[" + paras.get("crfD") + "]";
			right = "{3'b001, XER.SO}";
			item = new Item(premise, left, right);
			semanSet.add(item);
			
			break;
		case "bc":
			premise = "CR[" + paras.get("BI") + "] = {3'b001, XER.SO}";
			left = "PC";
			right = "(PC + {16{" + paras.get("BD") + "[0]}, " + paras.get("BD") + ", 2'b0})";
			item = new Item(premise, left, right);
			semanSet.add(item);
			
			premise = "CR[" + paras.get("BI") + "] = {3'b100, XER.SO}";
			left = "PC";
			right = "PC + 4";
			item = new Item(premise, left, right);
			semanSet.add(item);
			
			premise = "CR[" + paras.get("BI") + "] = {3'b010, XER.SO}";
			left = "PC";
			right = "PC + 4";
			item = new Item(premise, left, right);
			semanSet.add(item);
			break;
		case "b":
			premise = null;
			left = "PC";
			right = "PC + {6{" + paras.get("LI") + "[0]}, " + paras.get("LI") + ", 2'b0}";
			item = new Item(premise, left, right);
			semanSet.add(item);
			break;
		default:
			break;
		}
		return seman;
	}
	
	
}
