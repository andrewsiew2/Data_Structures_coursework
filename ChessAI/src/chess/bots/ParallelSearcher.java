package chess.bots;

import cse332.chess.interfaces.AbstractSearcher;
import cse332.chess.interfaces.Board;
import cse332.chess.interfaces.Evaluator;
import cse332.chess.interfaces.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;


public class ParallelSearcher<M extends Move<M>, B extends Board<M, B>> extends
        AbstractSearcher<M, B>{
	
    private static final ForkJoinPool POOL = new ForkJoinPool();
    private static final int DIVIDE_CUTOFF = 3; // Splitting of moves in each board
    public M getBestMove(B board, int myTime, int opTime) {
    		return  (M)parallel(board, evaluator, ply, cutoff).move;
    }

    public static BestMove parallel(Board board, Evaluator eval, int ply, int cutoff) {
    		return POOL.invoke(new ParallelSplit(board, eval, ply, cutoff, 0, board.generateMoves().size(), null, board.generateMoves()));
    }

    public static class ParallelSplit extends RecursiveTask<BestMove> {    	

    		private Board board;
    		private final Evaluator eval;
    		private int ply;
    		private final int cutoff;
    		private int lo;
    		private int hi;
    		private List moves;
    		
    		public ParallelSplit(Board board, Evaluator eval, int ply, int cutoff, int lo, int hi, Move move, List moves) {
    			
    			if (move != null) {
    				this.board = (Board)board.copy();
    				this.board.applyMove(move);
    				this.moves = this.board.generateMoves();
    				this.ply = ply - 1;
    				this.lo = 0;
    				this.hi = this.moves.size();
    			} else {
    				this.board = board;
    				this.moves = moves;
        			this.ply = ply;
    				this.lo = lo;
    				this.hi = hi;
    			}
    			this.eval = eval;
    			this.cutoff = cutoff;
    		}
    	
		@Override
		protected BestMove compute() {
			
			if (ply <= cutoff) {// Do everything sequentially here
				SimpleSearcher cut = new SimpleSearcher();
				BestMove move = cut.minimax(this.eval, this.board, this.ply);
				return move;
			} else {
				
				if (moves.isEmpty()) {
					if (board.inCheck()) {
						return new BestMove(null, -eval.mate() - ply);
					} else {
						return new BestMove(null, -eval.stalemate());
					}
				}
				
				if (hi - lo <= DIVIDE_CUTOFF) { // SEQUENTIAL FORKING
					
					List<ParallelSplit> tasks = new ArrayList<>();
					
					BestMove currentBestMove = new BestMove(null, eval.infty()).negate();
					
					// Sequential forking
					for (int i = lo; i < hi; i++) {
						ParallelSplit finalSplit = new ParallelSplit(board, eval, ply , cutoff, 0, -1, (Move)moves.get(i), moves);
						if (i == hi - 1) {
							currentBestMove = finalSplit.compute().negate();
							currentBestMove.move = moves.get(i);
						} else {
							finalSplit.fork();
							tasks.add(finalSplit);
						}
					}
					
					for (int i = 0; i < tasks.size(); i++) {
						BestMove apparentBestMove = tasks.get(i).join().negate();
						if (apparentBestMove.value > currentBestMove.value) {
							currentBestMove = apparentBestMove;
							currentBestMove.move = moves.get(i + lo);
						}
					}
					return currentBestMove;
					
				} else {
					int middle = lo + ((hi - lo) / 2);
					ParallelSplit leftSplit = new ParallelSplit(board, eval, ply, cutoff, lo, middle, null, moves);
					ParallelSplit rightSplit = new ParallelSplit(board, eval, ply, cutoff, middle, hi, null, moves);
					leftSplit.fork();
					BestMove rightBestMove = rightSplit.compute();
					BestMove leftBestMove = leftSplit.join();
					// Compare their values and choose the better one TO BE ADDED
					if (rightBestMove.value > leftBestMove.value) {
						return rightBestMove;
					}
					return leftBestMove;
				}
				
			}
			
		}
		
		
		
		
    }
}