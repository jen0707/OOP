package ch05.sec05;

public class CharAtExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String ssn = "010624123013";
		char sex = ssn.charAt(6);
		switch (sex) {
			case '1':
			case '3':
				System.out.println("남자입니다.");
				break;
			case '2':
			case '4':
				System.out.println("여자입니다.");
				break;
		}
	}

}
