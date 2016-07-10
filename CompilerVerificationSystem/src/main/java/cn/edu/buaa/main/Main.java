package cn.edu.buaa.main;

import cn.edu.buaa.labeler.Labeler;

/**
 * 程序运行入口
 * @author destiny
 *
 */
public class Main {
	
	public static void main(String[] args) {
		
		String fileName = "src/main/resources/source/evenSum.c";
		Labeler labeler = new Labeler();
		labeler.runLabeler(fileName);
		
		
		
	}
}
