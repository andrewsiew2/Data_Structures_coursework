package experiment;

import java.util.ArrayList;
import java.util.List;

public class BotNodeCounter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExpGame newGame = new ExpGame();
		ArrayList<String> list = newGame.main();
		long size = list.size(); 
		long total = 0;
		ExpStartingPosition printer = new ExpStartingPosition();
		for(int i = 0; i < size; i++) {
			long number = printer.main(list.get(i));
			total += number/size;
			System.out.println(number/size);
		}
		System.out.println("Average : " + total);
	}

}
