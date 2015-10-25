package cn.edu.buaa.compile;

/**
 * 存储一行指称语义
 * @author destiny
 *
 */
public class Item {
	private String premise;		//存储前提
	private String left;		//存储语句
	private String right;		
	
	public Item() {
	}
	
	public Item(String body) {
		left = body;
	}
	
	public Item(String premise, String left, String right) {
		this.premise = premise;
		this.left = left;
		this.right = right;
	}

	public String getPremise() {
		return premise;
	}

	public void setPremise(String premise) {
		this.premise = premise;
	}

	public String getLeft() {
		return left;
	}

	public void setLeft(String left) {
		this.left = left;
	}

	public String getRight() {
		return right;
	}

	public void setRight(String right) {
		this.right = right;
	}
}
