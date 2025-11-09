package ch06.sec08.exam03;

public class CarExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Car myCar = new Car();
		
		myCar.SetGas(5);
		
		if(myCar.isLeftGas()) {
			System.out.println("출발합니다.");
			
			myCar.run();
		}
		System.out.println("가스를 주입하세요.");

	}

}
