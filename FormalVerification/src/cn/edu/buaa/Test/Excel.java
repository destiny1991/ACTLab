package cn.edu.buaa.Test;

import java.io.File;
import java.io.IOException;

import jxl.Sheet;   
import jxl.Workbook;   
import jxl.read.biff.BiffException;

public class Excel {
	public static void main(String[] args) {
		Workbook readwb = null;
		
		try {
			readwb = Workbook.getWorkbook(new File("src/cn/edu/buaa/resources/Axiom.xls"));
			//获取第一张Sheet表
            Sheet readsheet = readwb.getSheet(0);
            //获取Sheet表中所包含的总行数
            int rsRows = readsheet.getRows();
            //获取Sheet表中所包含的总列数   
            int rsColumns = readsheet.getColumns();
            
            System.out.println(rsColumns + " " + rsRows);
            
            // getCell(int column, int row)
            int i = 1;
            while(i < rsRows) {
            	String name = readsheet.getCell(0, i).getContents().trim();
            	System.out.println(name);
            	while(i < rsRows) {
            		String tmp = readsheet.getCell(0, i).getContents();
            		if(null == tmp || tmp.equals("") || tmp.equals(name))  {
            			String premise = readsheet.getCell(1, i).getContents().trim();
            			String left = readsheet.getCell(2, i).getContents().trim();
            			String right = readsheet.getCell(3, i).getContents().trim();
            			
            			if(null == premise || premise.equals("")) premise = null;
            			if(null == left || left.equals("")) left = null;
            			if(null == right || right.equals("")) right = null;
            			
            			if(null == premise && null == left && null == right) {		
            			}else if(null == premise && null == right) {
        					System.out.println(left);
        				} else if(null == premise) {
        					System.out.println(left + " = " + right);
        				} else {
        					System.out.println(premise + " -> " 
        							+ left + " = " + right);
        				}
    	            	i++;
            			
            		} else {
            			break;
            		}
            	}
            	System.out.println();
            }
            
            //获取指定单元格的对象引用
//            for (int i=0; i<rsRows; i++) {   
//                for (int j = 0; j < rsColumns; j++) {   
//                    String content = readsheet.getCell(j, i).getContents();
//                    
//                    
//                }   
//                System.out.println();   
//            }
            
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(null != readwb) {
				readwb.close();
			}
		}
		
	}
}
