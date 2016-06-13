package cn.edu.buaa.pojo;

import java.util.List;

public class Proposition {
	
	private List<Item> proposition;		// 每个命题可能包含多项
	
	public int size() {
		return proposition.size();
	}
	
	public List<Item> getProposition() {
		return proposition;
	}

	public void setProposition(List<Item> proposition) {
		this.proposition = proposition;
	}
	
}
