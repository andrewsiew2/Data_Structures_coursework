package experiment;

import chess.board.ArrayBoard;
import chess.board.ArrayMove;
import chess.game.SimpleEvaluator;
import cse332.chess.interfaces.Searcher;

//Class which returns the best move 
public class ExpStartingPosition {

    public  ArrayMove getBestMove(String fen, Searcher<ArrayMove, ArrayBoard> searcher, int depth, int cutoff) { 
        searcher.setDepth(depth);
        searcher.setCutoff(cutoff);
        searcher.setEvaluator(new SimpleEvaluator()); 
        return searcher.getBestMove(ArrayBoard.FACTORY.create().init(fen), 0, 0);
    }
    
    public void printMove(String fen, Searcher<ArrayMove, ArrayBoard> searcher, int depth, int cutoff) {
        getBestMove(fen, searcher, depth, cutoff);
    }
    
    public long main(String startingPosition) {
    		// change the bot here
    		ParallelSearcherCopy dumb = new ParallelSearcherCopy<>();
        // change depth here
    		// cutoff depth/2 for any parallel methods
        printMove(startingPosition, dumb, 4, 2);
        return dumb.nodeCount;
    }
}
