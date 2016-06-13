package cn.edu.buaa.pojo;

import java.util.Map;


public class Instruction {
	
	private String name;					// 指令名称
	private Map<String, String> paras;		// 指令参数
	private Proposition proposition;		// 指令映射成的命题
	private String content;					// 指令内容
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Map<String, String> getParas() {
		return paras;
	}
	
	public void setParas(Map<String, String> paras) {
		this.paras = paras;
	}
	
	public Proposition getProposition() {
		return proposition;
	}
	
	public void setProposition(Proposition proposition) {
		this.proposition = proposition;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
}
