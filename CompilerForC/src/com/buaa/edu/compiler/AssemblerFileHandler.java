package com.buaa.edu.compiler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 维护生成的汇编文件
 * @author destiny
 *
 */
public class AssemblerFileHandler {
	private List<String> result;
	private int dataPointer;
	private int textPointer;
	
	public AssemblerFileHandler() {
		super();
		result = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			{
				add("	.section .rodata");
				add("	.section \".text\"");
			}
		};
		dataPointer = 1;
		textPointer = 2;
	}
	
	public void insert(String value, String type) throws Exception {
		System.out.println(value + "\t" + type);
		// 插入到data域
		if(type.equals("DATA")) {
			result.add(dataPointer, value);
			dataPointer++;
			textPointer++;
		// 插入到代码段
		} else if(type.equals("TEXT")) {
			result.add(textPointer, value);
			textPointer++;
		} else {
			throw new Exception("Error generate assembler");
		}
	}
	
	// 将结果保存到文件中
	public void generateAssFile(String src, String filename) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(src));
		writer.write("	.file	\"" + filename + "\"");
		writer.newLine();
		for(String item : result) {
			writer.write(item);
			writer.newLine();
		}
		writer.write("	.ident	\"powerpc-e500v2-linux-gnuspe-gcc\"");
		writer.newLine();
		writer.close();
	}
}
