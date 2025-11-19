package team.bank.account;

public class Account {
	private String num;
	private String owner;
	private int balance;
	
	public Account(String num, String owner, int balance){
		this.num = num;
		this.owner = owner;
		this.balance = balance;
	}
	
	public String getNum() {
		return num;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public int getBalance() {
		return balance;
	}
	
	public void setBalance(int balance) {
		this.balance = balance;
	}

}
