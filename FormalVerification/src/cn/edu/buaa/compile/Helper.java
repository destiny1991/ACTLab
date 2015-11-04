package cn.edu.buaa.compile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		
	public static void pretreat(List<Item> semanSet, String name, List<Item> ts, 
			Map<String, String> paras) {
		switch (name) {
		case "bc":
			int bo =  Integer.parseInt(paras.get("BO"));
			if((bo & 1<<2) > 0 && 0 == (bo & 1<<3)) {
				Item item = new Item("CR[BI] = {3'b100, XER.SO}", ts.get(6).getLeft(), ts.get(6).getRight());
				semanSet.add(item);
				item = new Item("CR[BI] = {3'b010, XER.SO}", ts.get(6).getLeft(), ts.get(6).getRight());
				semanSet.add(item);
				item = new Item("CR[BI] = {3'b001, XER.SO}", ts.get(5).getLeft(), ts.get(5).getRight());
				semanSet.add(item);
			} else if((bo & 1<<2) > 0 && (bo & 1<<3) > 0) {
				Item item = new Item("CR[BI] = {3'b100, XER.SO}", ts.get(6).getLeft(), ts.get(6).getRight());
				semanSet.add(item);
				item = new Item("CR[BI] = {3'b010, XER.SO}", ts.get(5).getLeft(), ts.get(5).getRight());
				semanSet.add(item);
				item = new Item("CR[BI] = {3'b001, XER.SO}",  ts.get(6).getLeft(), ts.get(6).getRight());
				semanSet.add(item);
			}			
			break;
		case "b":
			int AA = Integer.parseInt(paras.get("AA"));
			for(Item e : ts) {
				if(e.getPremise().equals("AA = " + AA)) {
					Item item = new Item(null, e.getLeft(), e.getRight());
					semanSet.add(item);
				}
			}
			break;
		case "lwz":
			int rA = Integer.parseInt(paras.get("rA"));
			if(0 != rA) rA = 1;
			for(Item e : ts) {
				if(e.getPremise().equals("rA = " + rA)) {
					Item item = new Item(null, e.getLeft(), e.getRight());
					semanSet.add(item);
					return;
				}
			}
			break;
		default:
			for(Item e : ts) {	
				try {
					Item item = (Item) e.clone();
					semanSet.add(item);
				} catch (CloneNotSupportedException e1) {
					e1.printStackTrace();
				}
			}
			break;
		}
	}
	
	public static Semantic generateSemantic(String name, Map<String, String> paras, 
			Map<String, Semantic> axiomSet) {
		List<Item> semanSet = new ArrayList<Item>();
		Semantic seman = new Semantic(semanSet);
		List<Item> ts = axiomSet.get(name).getSemanSet();
		
		pretreat(semanSet, name, ts, paras);
		
		Set<String> set = paras.keySet();
		for(String key : set) {
			String value = paras.get(key);
			for(Item item : seman.getSemanSet()) {
				String nkey = "\\b" + key + "\\b";
				if(null != item.getPremise()) {
					String tmp = item.getPremise().replaceAll(nkey, value);
					item.setPremise(tmp);
				}
				if(null != item.getLeft()) {
					String tmp = item.getLeft().replaceAll(nkey, value);
					item.setLeft(tmp);
				}
				if(null != item.getRight()) {
					String tmp = item.getRight().replaceAll(nkey, value);
					item.setRight(tmp);
				}
			}
		}
		return seman;
	}
}
