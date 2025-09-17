package ch02.sec08;

public class CastingExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int var1 = 10;
		byte var2 = (byte) var1; //강제 타입 변환 (10 그대로 유지)
		System.out.println(var2);
		
		long var3 = 300;
		int var4 = (int) var3; //강제 타입 변환 (300 그대로 유지)
		System.out.println(var4);
		
		int var5 = 65;
		char var6 = (char) var5; //강제 타입 변환
		System.out.println(var6); //'A' 출력
		
		double var7 = 3.14;
		int var8 = (int) var7; //강제 타입 변환
		System.out.println(var8); //3 출력

	}

}
