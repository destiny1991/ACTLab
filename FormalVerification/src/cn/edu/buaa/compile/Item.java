package cn.edu.buaa.compile;

/**
 * 存储一行指称语义
 * @author destiny
 *
 */
public class Item implements Cloneable {
	private String premise;		//存储前提
	private String left;		//存储语句
	private String right;		
	
	public Item() {
	}
	
	public Item(String body) {
		this(null, body, null);
	}
	
	public Item(String left, String right) {
		this(null, left, right);
	}
	
	public Item(String premise, String left, String right) {
		this.premise = premise;
		this.left = left;
		this.right = right;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		Object obj = super.clone(); 
		
		//添加如下代码实现深复制
		Item s = (Item) obj;
		s.premise = this.premise;
		s.left = this.left;
		s.right = this.right;
		
		return obj;
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
