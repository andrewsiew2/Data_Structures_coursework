package chess.bots;

import java.util.Comparator;


import cse332.chess.interfaces.Board;
import cse332.chess.interfaces.Evaluator;
import cse332.chess.interfaces.Move;

public class Optimizer<M extends Move<M>, B extends Board<M, B>> implements Comparator<M>{
	@Override
	public int compare(M o1, M o2) {
		if(!o1.isCapture() && o2.isCapture()) {
			return 1;
		}else if(o1.isCapture() && !o2.isCapture()) {
			return -1;
		}
		return 0;
	}
}