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
	private int bssPointer;
	private int textPointer;
	
	public AssemblerFileHandler() {
		super();
		result = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			{
				add(".data");
				add(".bss");
				add(".lcomm bss_tmp, 4");
				add(".text");
			}
		};
		dataPointer = 1;
		bssPointer = 3;
		textPointer = 4;
	}
	
	public void insert(String value, String type) throws Exception {
		System.out.println(value + "\t" + type);
		// 插入到data域
		if(type.equals("DATA")) {
			result.add(dataPointer, value);
			dataPointer++;
			bssPointer++;
			textPointer++;
		// 插入到bss域	
		} else if(type.equals("BSS")) {
			result.add(bssPointer, value);
			bssPointer++;
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
	public void generateAssFile(String src) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(src));
		for(String item : result) {
			writer.write(item);
			writer.newLine();
		}
		writer.close();
	}
}
