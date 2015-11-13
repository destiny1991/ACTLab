package cn.edu.buaa.Test;

public class BinaryCut {
	public static void main(String[] args) {
		int a = 29;
		String b = Integer.toBinaryString(a);
		System.out.println(b.substring(0, 3));
		
		System.out.println((a>>2) & 7);
	}
}
