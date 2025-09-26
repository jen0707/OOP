package ch03.sec04;

public class AccruracyExample1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int apple = 1;
		double pieceUnit = 0.1;
		
		int number = 7;
		
		double result = apple - number*pieceUnit;
		System.out.println("사과 1개에서 남은 양: " + result);
		// 정확한 계산을 위해서는 실수가 아닌 정수를 사용하자
	}

}
