package team.bank.account;

import java.util.Scanner;

public class BankApplication {
	private static Account[] accounts = new Account[100];
	private static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean run = true;
        int balance = 0;

        while (run) {
            System.out.println("--------------------------------------------");
            System.out.println("1.계좌생성 | 2.계좌목록 | 3.예금 | 4.출금 | 5.종료");
            System.out.println("-------------------------------------------");
            System.out.print("선택> ");

            int menu = scanner.nextInt();

            if (menu == 1) {
                createAccount();
            } else if (menu == 2) {
            	accountList();
            } else if (menu == 3) {
            	deposit();
            } else if (menu == 4) {
                withdraw();
            } else if (menu == 5) {
            	run = false;
            } else {
            	System.out.println("잘못된 입력입니다.");
            }
        }

        System.out.println("프로그램 종료");
    }

	private static void createAccount() {
		System.out.println("---------");
		System.out.println("계좌생성");
		System.out.println("---------");

		System.out.print("계좌번호: ");
		String num = scanner.nextLine();
		System.out.print("계좌주: ");
		String owner = scanner.nextLine();
		System.out.print("초기입금액: ");
		int balance = Integer.parseInt(scanner.nextLine());

		Account newAccount = new Account(num, owner, balance);

		for (int i = 0; i < accounts.length; i++) {
			if (accounts[i] == null) {
				accounts[i] = newAccount;
				System.out.println("결과: 계좌가 생성되었습니다.");
				break;
			}
		}
	}

	private static void accountList() {
		System.out.println("---------");
		System.out.println("계좌목록");
		System.out.println("---------");

		for (int i = 0; i < accounts.length; i++) {
			Account account = accounts[i];
			if (account != null) {
				System.out.println(account.getNum() + "   " + account.getOwner() + "   " + account.getBalance());
			}   
		}
	}

     private static void deposit() {
          System.out.println("---------");
          System.out.println("예금");
          System.out.println("---------");

          System.out.print("계좌번호: ");
          String num = scanner.nextLine();
          Account account = findAccount(num);

          if (account == null) {
              System.out.println("결과: 계좌가 없습니다.");
              return;
          }
          
          System.out.print("예금액: ");
          int money = Integer.parseInt(scanner.nextLine());
          account.setBalance(account.getBalance() + money);
          System.out.println("결과: 예금이 성공되었습니다.");
      }
     
     private static void withdraw() {
         System.out.println("---------");
         System.out.println("출금");
         System.out.println("---------");

         System.out.print("계좌번호: ");
         String num = scanner.nextLine();
         Account account = findAccount(num);

         if (account == null) {
             System.out.println("결과: 계좌가 없습니다.");
             return;
         }
         
         System.out.print("출금액: ");
         int money = Integer.parseInt(scanner.nextLine());
		     if(money <= account.getBalance()) {
			    	 account.setBalance(account.getBalance() - money);
			    	 System.out.println("결과: 출금이 성공되었습니다.");
    	 
		     }else {
		    	 System.out.println("결과: 출금불가. 현재 잔액은 " + account.getBalance() + "원 입니다.");		     }
     }
     
     private static Account findAccount(String num) {
         for (int i = 0; i < accounts.length; i++) {
             if (accounts[i] != null && accounts[i].getNum().equals(num)) {
                 return accounts[i];
             }
         }
         return null;
     }
 }