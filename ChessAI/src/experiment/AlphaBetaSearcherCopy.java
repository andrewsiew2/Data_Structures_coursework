package experiment;

import java.util.List;

import chess.bots.BestMove;
import cse332.chess.interfaces.AbstractSearcher;
import cse332.chess.interfaces.Board;
import cse332.chess.interfaces.Evaluator;
import cse332.chess.interfaces.Move;

public class AlphaBetaSearcherCopy<M extends Move<M>, B extends Board<M, B>> extends AbstractSearcher<M, B> {
	protected static int nodeCount;
    public M getBestMove(B board, int myTime, int opTime) {
    		nodeCount = 0;
    		BestMove<M> best = aplhabeta(this.evaluator, board, ply, -this.evaluator.infty(), this.evaluator.infty());
        return best.move;
    }
    
    static <M extends Move<M>, B extends Board<M, B>> BestMove<M> aplhabeta(Evaluator<B> evaluator, B board, 
    																			int depth, int alpha, int beta) {
    		if(depth > 0) {
			List<M> moves = board.generateMoves();
			if(moves.isEmpty()) {
				if(board.inCheck()) {
					return new BestMove<M>(-evaluator.mate()-depth);
				}else {
					return new BestMove<M>(-evaluator.stalemate());
				}
			}

			M bestMove = null;
		    	for(M move : moves) {
		    		board.applyMove(move);
		    		nodeCount++;
		    		int value = aplhabeta(evaluator, board, depth - 1, -beta, -alpha).negate().value;
		    		board.undoMove();
		    		
		    		if (value > alpha) {
		    			bestMove = move;
		    			alpha = value;
		    		}
		    		if (alpha >= beta) {
		    			return new BestMove(move, alpha);
		    		}
		    	}
	
		    	return new BestMove(bestMove, alpha);
    		}
	    	return new BestMove<M>(null, evaluator.eval(board));
	}
    		
}
