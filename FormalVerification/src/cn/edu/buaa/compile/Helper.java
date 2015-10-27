package cn.edu.buaa.compile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用来帮助生成指令的相关属性，随着后续更多代码的增加，此文件会不断被修改
 * @author destiny
 *
 */
public class Helper {
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
		case "lwz":
			paras.put("rD", lines[1]);
			paras.put("D", lines[2]);
			paras.put("rA", lines[3]);
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
			int bo =  Integer.parseInt(paras.get("BO"));
			if((bo & 1<<2) > 0 && 0 == (bo & 1<<3)) {
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
				
				premise = "CR[" + paras.get("BI") + "] = {3'b001, XER.SO}";
				left = "PC";
				right = "(PC + {16{" + paras.get("BD") + "[0]}, " + paras.get("BD") + ", 2'b0})";
				item = new Item(premise, left, right);
				semanSet.add(item);
			} else if((bo & 1<<2) > 0 && (bo & 1<<3) > 0) {
				premise = "CR[" + paras.get("BI") + "] = {3'b100, XER.SO}";
				left = "PC";
				right = "PC + 4";
				item = new Item(premise, left, right);
				semanSet.add(item);
				
				premise = "CR[" + paras.get("BI") + "] = {3'b010, XER.SO}";
				left = "PC";
				right = "(PC + {16{" + paras.get("BD") + "[0]}, " + paras.get("BD") + ", 2'b0})";
				item = new Item(premise, left, right);
				semanSet.add(item);
				
				premise = "CR[" + paras.get("BI") + "] = {3'b001, XER.SO}";
				left = "PC";
				right = "PC + 4";
				item = new Item(premise, left, right);
				semanSet.add(item);
			}

			break;
		case "b":
			premise = null;
			left = "PC";
			right = "PC + {6{" + paras.get("LI") + "[0]}, " + paras.get("LI") + ", 2'b0}";
			item = new Item(premise, left, right);
			semanSet.add(item);
			break;
		case "lwz":
			premise = null;
			left = "GPR[" + paras.get("rD") + "]";
			int rA = Integer.parseInt(paras.get("rA"));
			if(0 == rA) {
				right = "MEM({16{" + paras.get("D") + "[16]}, " + paras.get("D") + "}, 4)";
			} else {
				right = "MEM(GPR[" + paras.get("rA") + "] + {16{" 
						+ paras.get("D") + "[16]}, " + paras.get("D") + "},4)";
			}
			item = new Item(premise, left, right);
			semanSet.add(item);
			break;
		default:
			break;
		}
		return seman;
	}
}
