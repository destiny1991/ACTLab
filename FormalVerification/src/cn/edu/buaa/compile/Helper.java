package cn.edu.buaa.compile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用来帮助生成指令的相关属性，随着后续更多代码的增加，此文件会不断被修改
 * 
 * @author destiny
 *
 */
public class Helper {
	public static Map<String, String> generateParas(String[] lines) throws Exception {
		Map<String, String> paras = new HashMap<String, String>();
		switch (lines[0]) {
		case "cmp":
			paras.put("crfD", lines[1]);
			paras.put("L", lines[2]);
			paras.put("rA", lines[3]);
			paras.put("rB", lines[4]);
			break;
		case "cmpi":
			paras.put("crfD", lines[1]);
			paras.put("L", lines[2]);
			paras.put("rA", lines[3]);
			paras.put("SIMM", lines[4]);
			break;
		case "b":
			paras.put("LI", lines[1]);
			paras.put("AA", "0");
			paras.put("LK", "0");
			break;
		case "beq":
			paras.put("crfD", lines[1]);
			paras.put("target", lines[2]);
			break;
		case "bne":
			paras.put("crfD", lines[1]);
			paras.put("target", lines[2]);
			break;
		case "li":
			paras.put("rD", lines[1]);
			paras.put("SIMM", lines[2]);
			break;
		case "lwz":
			paras.put("rD", lines[1]);
			paras.put("D", lines[2]);
			if(lines.length > 3) paras.put("rA", lines[3]);
			break;
		case "stw":
			paras.put("rS", lines[1]);
			paras.put("D", lines[2]);
			paras.put("rA", lines[3]);
			break;
		case "addi":	// rD,rA,SIMM
		case "mulli":
			paras.put("rD", lines[1]);
			paras.put("rA", lines[2]);
			paras.put("SIMM", lines[3]);
			break;
		case "divw":
		case "mullw":
		case "subf":
		case "add":
			paras.put("rD", lines[1]);
			paras.put("rA", lines[2]);
			paras.put("rB", lines[3]);
			paras.put("OE", "0");
			paras.put("Rc", "0");
			break;
		case "isel":
			paras.put("rD", lines[1]);
			paras.put("rA", lines[2]);
			paras.put("rB", lines[3]);
			paras.put("crfD", Integer.toString(Integer.parseInt(lines[4]) / 4));
			paras.put("crb", lines[4]);
			break;
		case "xori":
		case "andi.":
		case "ori":
		case "slwi":
		case "srawi":
			paras.put("rA", lines[1]);
			paras.put("rS", lines[2]);
			paras.put("UIMM", lines[3]);
			break;
		case "xor":
		case "and":
		case "or":
		case "slw":
		case "sraw":
		case "nor":
			paras.put("rA", lines[1]);
			paras.put("rS", lines[2]);
			paras.put("rB", lines[3]);
			break;
		default:
			throw new Exception("generateParams 中没法找到指令： " + lines[0]);
		}
		return paras;
	}

	public static void pretreat(List<Item> semanSet, String name, List<Item> ts, Map<String, String> paras) {
		Item item = null;
		switch (name) {
		case "isel":
			if(paras.get("crb").equals("28")) {
				Item e = ts.get(0);
				e.setRight("GPR[rA]");
				if(paras.get("rA").equals("0")) e.setRight("0");
				semanSet.add(e);
				e = ts.get(1);
				e.setRight("GPR[rB]");
				semanSet.add(e);
				e = ts.get(2);
				e.setRight("GPR[rB]");
				semanSet.add(e);
			} else if(paras.get("crb").equals("29")) {
				Item e = ts.get(0);
				e.setRight("GPR[rB]");
				semanSet.add(e);
				e = ts.get(1);
				e.setRight("GPR[rA]");
				if(paras.get("rA").equals("0")) e.setRight("0");
				semanSet.add(e);
				e = ts.get(2);
				e.setRight("GPR[rB]");
				semanSet.add(e);
			} else {
				for (Item e : ts) {
					try {
						item = (Item) e.clone();
						if(paras.get("rA").equals("0")) {
							if(item.getRight().equals("GPR[rA]")) {
								item.setRight("0");
							}
						}			
						semanSet.add(item);
					} catch (CloneNotSupportedException e1) {
						e1.printStackTrace();
					}
				}
			}
			break;
		case "addi":
			for (Item e : ts) {
				try {
					item = (Item) e.clone();
					if(paras.get("rA").equals("0") && item.getRight().contains("rA")) {
						item.setRight("SIMM");
					}			
					semanSet.add(item);
				} catch (CloneNotSupportedException e1) {
					e1.printStackTrace();
				}
			}
			break;
		default:
			for (Item e : ts) {
				try {
					item = (Item) e.clone();
					semanSet.add(item);
				} catch (CloneNotSupportedException e1) {
					e1.printStackTrace();
				}
			}
			break;
		}
	}

	public static Semantic generateSemantic(String name, Map<String, String> paras, Map<String, Semantic> axiomSet) throws Exception {
		List<Item> semanSet = new ArrayList<Item>();
		Semantic seman = new Semantic(semanSet);
		if(!axiomSet.containsKey(name)) {
			throw new Exception("缺少指称语义：" + name);
		}
		List<Item> ts = axiomSet.get(name).getSemanSet();

		pretreat(semanSet, name, ts, paras);

		Set<String> set = paras.keySet();
		for (String key : set) {
			String value = paras.get(key);
			for (Item item : seman.getSemanSet()) {
				String nkey = "\\b" + key + "\\b";
				if (null != item.getPremise()) {
					String tmp = item.getPremise().replaceAll(nkey, value);
					item.setPremise(tmp);
				}
				if (null != item.getLeft()) {
					String tmp = item.getLeft().replaceAll(nkey, value);
					item.setLeft(tmp);
				}
				if (null != item.getRight()) {
					String tmp = item.getRight().replaceAll(nkey, value);
					item.setRight(tmp);
				}
			}
		}
		return seman;
	}
}
