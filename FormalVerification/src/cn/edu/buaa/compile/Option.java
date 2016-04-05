package cn.edu.buaa.compile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Option {

	public static Map<String, Semantic> loadAxiom1() {
		BufferedReader reader = null;
		Map<String, Semantic> axiom = new HashMap<String, Semantic>();
		try {
			reader = new BufferedReader(
					new InputStreamReader(new FileInputStream("src/cn/edu/buaa/resources/axiom.txt")));

			String line;
			String name = null;
			while (null != (line = reader.readLine())) {
				line = line.trim();
				if (0 == line.length())
					continue;
				if (null == name) {
					name = line;
					List<Item> semanSet = new ArrayList<Item>();
					Semantic seman = new Semantic(semanSet);
					while (!name.equals(line = reader.readLine().trim())) {
						if (0 == line.length())
							continue;
						String[] lines = line.split("\t");
						lines = Tool.filterOtherSignal(lines);
						Item item = null;
						if (1 == lines.length) {
							item = new Item(lines[0]);
						} else if (2 == lines.length) {
							item = new Item(lines[0], lines[1]);
						} else if (3 == lines.length) {
							item = new Item(lines[0], lines[1], lines[2]);
						} else {
							item = null;
						}
						if (null != item)
							semanSet.add(item);
					}
					axiom.put(name, seman);
					name = null;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return axiom;
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
			premise = "GPR[" + paras.get("rA") + "] < {16{" + paras.get("SIMM") + "[16]}, " + paras.get("SIMM") + "}";
			left = "CR[" + paras.get("crfD") + "]";
			right = "{3'b100, XER.SO}";
			item = new Item(premise, left, right);
			semanSet.add(item);

			premise = "GPR[" + paras.get("rA") + "] > {16{" + paras.get("SIMM") + "[16]}, " + paras.get("SIMM") + "}";
			left = "CR[" + paras.get("crfD") + "]";
			right = "{3'b010, XER.SO}";
			item = new Item(premise, left, right);
			semanSet.add(item);

			premise = "GPR[" + paras.get("rA") + "] = {16{" + paras.get("SIMM") + "[16]}, " + paras.get("SIMM") + "}";
			left = "CR[" + paras.get("crfD") + "]";
			right = "{3'b001, XER.SO}";
			item = new Item(premise, left, right);
			semanSet.add(item);

			break;
		case "bc":
			int bo = Integer.parseInt(paras.get("BO"));
			if ((bo & 1 << 2) > 0 && 0 == (bo & 1 << 3)) {
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
			} else if ((bo & 1 << 2) > 0 && (bo & 1 << 3) > 0) {
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
			if (0 == rA) {
				right = "MEM({16{" + paras.get("D") + "[16]}, " + paras.get("D") + "}, 4)";
			} else {
				right = "MEM(GPR[" + paras.get("rA") + "] + {16{" + paras.get("D") + "[16]}, " + paras.get("D")
						+ "},4)";
			}
			item = new Item(premise, left, right);
			semanSet.add(item);
			break;
		default:
			break;
		}
		return seman;
	}

	public static void pretreat(List<Item> semanSet, String name, List<Item> ts, Map<String, String> paras) {
		switch (name) {
		case "bc":
			int bo = Integer.parseInt(paras.get("BO"));
			if ((bo & 1 << 2) > 0 && 0 == (bo & 1 << 3)) {
				Item item = new Item("CR[BI] = {3'b100, XER.SO}", "PC", "PC + 4");
				semanSet.add(item);
				item = new Item("CR[BI] = {3'b010, XER.SO}", "PC", "PC + 4");
				semanSet.add(item);
				item = new Item("CR[BI] = {3'b001, XER.SO}", "PC", "(PC + {16{BD[0]}, BD, 2'b0})");
				semanSet.add(item);
			} else if ((bo & 1 << 2) > 0 && (bo & 1 << 3) > 0) {
				Item item = new Item("CR[BI] = {3'b100, XER.SO}", "PC", "PC + 4");
				semanSet.add(item);
				item = new Item("CR[BI] = {3'b010, XER.SO}", "PC", "(PC + {16{BD[0]}, BD, 2'b0})");
				semanSet.add(item);
				item = new Item("CR[BI] = {3'b001, XER.SO}", "PC", "PC + 4");
				semanSet.add(item);
			}
			break;
		case "b":
			int AA = Integer.parseInt(paras.get("AA"));
			for (Item e : ts) {
				if (e.getPremise().equals("AA = " + AA)) {
					Item item = new Item(null, e.getLeft(), e.getRight());
					semanSet.add(item);
				}
			}
			break;
		case "lwz":
			int rA = Integer.parseInt(paras.get("rA"));
			for (Item e : ts) {
				if (e.getPremise().equals("rA = " + rA)) {
					Item item = new Item(null, e.getLeft(), e.getRight());
					semanSet.add(item);
				}
			}
			break;
		default:
			for (Item e : ts) {
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
}
