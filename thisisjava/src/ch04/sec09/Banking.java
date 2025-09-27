package ch04.sec09;

import java.util.Scanner;

public class Banking {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean run = true;
        int balance = 0;

        while (run) {
            System.out.println("----------------------------");
            System.out.println("1.예금 | 2.출금 | 3.잔고 | 4.종료");
            System.out.println("----------------------------");
            System.out.print("선택> ");

            int menu = scanner.nextInt();

            if (menu == 1) {
                System.out.print("예금액> ");
                int money = scanner.nextInt();
                balance += money;

            } else if (menu == 2) {
                System.out.print("출금액> ");
                int money = scanner.nextInt();
                if (balance >= money) {
                    balance -= money;
                } else {
                    System.out.println("잔고가 부족합니다!");
                }

            } else if (menu == 3) {
                System.out.println("잔고> " + balance);

            } else if (menu == 4) {
                run = false;
            }
        }

        System.out.println("프로그램 종료");
    }
}