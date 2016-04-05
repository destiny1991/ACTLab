package cn.edu.buaa.compile;

import java.util.Map;

/**
 * Instruction持久化类
 * 
 * @author destiny
 */
public class Instruction {
	private String name;
	private Map<String, String> paras;
	private Semantic seman;

	public Instruction() {
	}

	public Instruction(String name) {
		this.name = name;
	}

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

	public Semantic getSeman() {
		return seman;
	}

	public void setSeman(Semantic seman) {
		this.seman = seman;
	}
}
