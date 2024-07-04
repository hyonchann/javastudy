package day1;

public class Loop {

	public static void main(String[] args) {

		// ■
		// ■■
		// ■■■
		// ■■■■
		// ■■■■■
		for(int i = 5; i >=1; i--) {
			for(int j = 5; j >= 5-i; j--) {
				System.out.print("■");
			}
		}

		// ■■■■■
		// ■■■■
		// ■■■
		// ■■
		// ■

		// 2~9段まで九九処理を作成


		for(int i = 2; i <=9; i++) {
			System.out.println("["+i+"段]");
			for(int j = 1; j <= 9; j++)
			{
				System.out.println(i+"*"+j+"="+i*j);
			}
		    }

            System.out.println();






}


































	}


