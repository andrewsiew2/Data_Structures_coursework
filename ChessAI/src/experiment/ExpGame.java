package experiment;

import java.awt.List;
import java.util.ArrayList;

import chess.board.ArrayBoard;
import chess.board.ArrayMove;
import chess.bots.AlphaBetaSearcher;
import chess.bots.SimpleSearcher;
import chess.game.SimpleEvaluator;
import cse332.chess.interfaces.Searcher;

//Class which deals with generating a list of fens
public class ExpGame {
    public Searcher<ArrayMove, ArrayBoard> whitePlayer;
    public Searcher<ArrayMove, ArrayBoard> blackPlayer;
    public static final String STARTING_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    
    private ArrayBoard board;
    
    public ArrayList<String> main(){
        ExpGame game = new ExpGame();
        return game.play();
    }

    public ExpGame() {
        setupWhitePlayer(new SimpleSearcher<ArrayMove, ArrayBoard>(), 3, 3);
        setupBlackPlayer(new AlphaBetaSearcher<ArrayMove, ArrayBoard>(), 4, 4);
    }
    
    public ArrayList<String> play() {
       this.board = ArrayBoard.FACTORY.create().init(STARTING_POSITION);
       Searcher<ArrayMove, ArrayBoard> currentPlayer = this.blackPlayer;
       ArrayList<String> list = new ArrayList<String>();

       /* Note that this code does NOT check for stalemate... */
       while (!board.inCheck() || board.generateMoves().size() > 0) {
           currentPlayer = currentPlayer.equals(this.whitePlayer) ? this.blackPlayer : this.whitePlayer;
           list.add(board.fen());
           this.board.applyMove(currentPlayer.getBestMove(board, 1000, 1000));

       }
       
       return list;
    }
    
    public Searcher<ArrayMove, ArrayBoard> setupPlayer(Searcher<ArrayMove, ArrayBoard> searcher, int depth, int cutoff) {
        searcher.setDepth(depth);
        searcher.setCutoff(cutoff);
        searcher.setEvaluator(new SimpleEvaluator());
        return searcher; 
    }
    public void setupWhitePlayer(Searcher<ArrayMove, ArrayBoard> searcher, int depth, int cutoff) {
        this.whitePlayer = setupPlayer(searcher, depth, cutoff);
    }
    public void setupBlackPlayer(Searcher<ArrayMove, ArrayBoard> searcher, int depth, int cutoff) {
        this.blackPlayer = setupPlayer(searcher, depth, cutoff);
    }
}
	