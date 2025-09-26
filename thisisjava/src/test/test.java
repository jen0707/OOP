package test;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int x = 1000000;
		int y = 1000000;
		int z = x*y;  // overflow
		
		System.out.println(z);
		
		System.out.println("-----------------");
		
		long x1 = 1000000;
		long y1 = 1000000;
		long z1 = (long)x*(long)y;
		
		System.out.println(z1);
	}

}
