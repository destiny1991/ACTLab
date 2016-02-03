package com.buaa.edu.compiler;

/**
 * 记录分析出来的单词
 * @author destiny
 *
 */
public class Token {
	private String type;
	private String value;
	
	public Token(String type, String value) {
		super();
		this.type = type;
		this.value = value;
	}
	
	public Token(int type_index, String value) {
		super();
		if(type_index == 0 || type_index == 3 || type_index == 4) {
			this.type = CDefine.DETAIL_TOKEN_STYLE.get(value);
		} else {
			this.type = CDefine.TOKEN_STYLE[type_index];
		}
		this.value = value;
	}
	
	public Token(int type_index, char value) {
		this(type_index, value + "");
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
}
