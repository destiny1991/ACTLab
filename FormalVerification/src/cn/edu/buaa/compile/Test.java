package cn.edu.buaa.compile;

import java.util.ArrayList;
import java.util.List;

public class Test {
	public static List<Semantic> test1() {
		List<Semantic> data = new ArrayList<Semantic>();
		
		Item item1 = new Item("GPR[0] = {16{0[16]}, 0}", "CR[7]", "{3'b001, XER.SO}");
		List<Item> semanSet1 = new ArrayList<Item>();
		semanSet1.add(item1);
		Semantic seman1 = new Semantic(semanSet1);
		data.add(seman1);
		
		Item item2 = new Item("CR[7] = {3'b001, XER.SO}", "PC", "PC + 4");
		List<Item> semanSet2 = new ArrayList<Item>();
		semanSet2.add(item2);
		Semantic seman2 = new Semantic(semanSet2);
		data.add(seman2);
		
		return data;
	}
	
	public static List<Semantic> test2() {
		List<Semantic> data = new ArrayList<Semantic>();
		Item item;
		List<Item> semanSet;
		Semantic seman;
		
		semanSet = new ArrayList<Item>();
		item = new Item("GPR[0]", "{16{0[16]}, 0}");
		semanSet.add(item);
		seman = new Semantic(semanSet);
		data.add(seman);
		
		semanSet = new ArrayList<Item>();
		item = new Item("GPR[0] < {16{0[16]}, 0}", "CR[7]", "{3'b100, XER.SO}");	
		semanSet.add(item);
		item = new Item("GPR[0] = {16{0[16]}, 0}", "CR[7]", "{3'b001, XER.SO}");
		semanSet.add(item);
		item = new Item("GPR[0] > {16{0[16]}, 0}", "CR[7]", "{3'b010, XER.SO}");
		semanSet.add(item);
		seman = new Semantic(semanSet);
		data.add(seman);
		
		return data;
	}
	
	public static void main(String[] args) {
		List<Semantic> semanTest = test2();
		CompileVerification cv = new CompileVerification();
		Tool.printSemanticList(semanTest);
		System.out.println("******************************\n\n");
		List<Semantic> result = cv.verificationProcess(semanTest);
		Tool.printSemanticList(result);
	}
}
