package ch02.sec07;

public class PromotionExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		byte byteValue = 10;
		int intValue = byteValue; //자동형변환
		System.out.println("intValue: " + intValue);
		
		char charValue = '가';
		intValue = charValue; //자동형변환
		System.out.println("가의 유니코드: " + intValue);
		
		intValue = 50;
		long longValue = intValue; //자동형변환
		System.out.println("longValue: " + longValue);
		
		longValue = 100;
		float floatValue = longValue; //자동형변환
		System.out.println("floatValue: " + floatValue);
		
		floatValue = 100.5F;
		double doubleValue = floatValue; //자동형변환
		System.out.println("doubleValue: " + doubleValue);

	}

}
