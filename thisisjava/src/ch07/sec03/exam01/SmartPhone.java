package ch07.sec03.exam01;

public class SmartPhone extends Phone {
	
	public SmartPhone(String model, String color) {
		super(); // 컴파일 시 자동 추가
		this.model = model;
		this.color = color;
		System.out.println("SmartPhone(String model, String color) 생성자 실행 됨");
	}

}
