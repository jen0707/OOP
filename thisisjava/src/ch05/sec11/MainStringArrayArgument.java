package ch05.sec11;

public class MainStringArrayArgument {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length != 2) {
			System.out.println("프로그램 입력 값이 부족"); // 설정 이전에는 프로그램 입력 값이 부족 출력
			System.exit(0); 
		}
		
		String strNum1 = args[0];
		String strNum2 = args[1];
		
		int num1 = Integer.parseInt(strNum1);
		int num2 = Integer.parseInt(strNum2);
		
		int result = num1 + num2;
		System.out.println(num1 + " + " + num2 + " = " + result);

	} // Arguments 설정 이후에는 값이 나온다.

}
