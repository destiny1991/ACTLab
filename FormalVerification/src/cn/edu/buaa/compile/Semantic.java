package cn.edu.buaa.compile;

import java.util.List;

/**
 * 存储每条汇编指令的指称语义
 * 
 * @author destiny
 */
public class Semantic {
	private List<Item> semanSet;

	public Semantic() {
	}

	public Semantic(List<Item> semanSet) {
		this.semanSet = semanSet;
	}

	public List<Item> getSemanSet() {
		return semanSet;
	}

	public void setSemanSet(List<Item> semanSet) {
		this.semanSet = semanSet;
	}
}
