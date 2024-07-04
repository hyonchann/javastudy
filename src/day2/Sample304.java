package day2;

import java.util.Scanner;

public class Sample304 {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		System.out.println("1-7までの数字を入力");

		        int dice = sc.nextInt();
				System.out.println("さいころの目:"+dice);
		if(1 <=dice && dice <=6) {
			if(dice==2 || dice ==4 ||dice == 6) {

				System.out.println("丁です。");

			}else {
				System.out.println("半です。");
			}
		}else {
			System.out.println("範囲外の数値です。");
		}

	}

}
