package chess.bots;

import java.util.List;

import cse332.chess.interfaces.AbstractSearcher;
import cse332.chess.interfaces.Board;
import cse332.chess.interfaces.Evaluator;
import cse332.chess.interfaces.Move;
import cse332.exceptions.NotYetImplementedException;

/**
 * This class should implement the minimax algorithm as described in the
 * assignment handouts.
 */
public class SimpleSearcher<M extends Move<M>, B extends Board<M, B>> extends
        AbstractSearcher<M, B> {
	
    public M getBestMove(B board, int myTime, int opTime) {
        /* Calculate the best move */
        BestMove<M> best = minimax(this.evaluator, board, ply);
        return best.move;
        
    }

    static <M extends Move<M>, B extends Board<M, B>> BestMove<M> minimax(Evaluator<B> evaluator, B board, int depth) {
        if (depth > 0) {
	    		List<M> potentialMoves = board.generateMoves();
            if (potentialMoves.isEmpty()) {
            		if (board.inCheck()) {
            			return new BestMove(null, -evaluator.mate() - depth);
            		} else {
            			return new BestMove(null, evaluator.stalemate()).negate();
            		}
            }
            
            // Now we know there are potential moves here!
            BestMove<M> currentBestMove = new BestMove<M>(null, evaluator.infty()).negate();
            for (M move: potentialMoves) {
            		board.applyMove(move);
            		BestMove<M> apparentBestMove = minimax(evaluator, board, depth - 1);
            		board.undoMove();
            		apparentBestMove = apparentBestMove.negate();
            		if (apparentBestMove.value > currentBestMove.value) {
            			currentBestMove = new BestMove<M>(move, apparentBestMove.value);
            		}
            }
            return currentBestMove;
        }
        return new BestMove<M>(null, evaluator.eval(board));
    }     
    
}