package buaa.main;
import java.io.*;
import java.util.*;
import buaa.data.*;

public class Main {
	private List<Instruction> sourceCode = null;

	public List<Instruction> getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(List<Instruction> sourceCode) {
		this.sourceCode = sourceCode;
	}
	
	public List<Instruction> readSourceFile(String fileName) {
		List<Instruction> codeList = new ArrayList<Instruction>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
			try {
				String str = null;
				while((str = in.readLine()) != null) {
					if(str.length() == 0) continue;
					Instruction ins = new Instruction();
					ins.resolve(str);
					codeList.add(ins);
				}
				
			} finally {
				in.close();
			}
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
		return codeList;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Main program = new Main();
		List<Instruction> codeList = program.readSourceFile("powerCode.txt");
		program.setSourceCode(codeList);
		
		
		//���ָ����ȷ��
		//if(!Tool.check(program.getSourceCode())) {
			//throw new RuntimeException("Դ�ļ���ָ����ִ���");
		//}
		
		
		Iterator<Instruction> iter = program.getSourceCode().iterator();
		while(iter.hasNext()) {
			Instruction str = iter.next();
			System.out.println(str);
		}
		
		
		
	}


}
