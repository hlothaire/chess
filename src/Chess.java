import modele.board.Board;
import view.Table;

public class Chess {

    public static void main(String[] args){
        Board board = Board.createStandardBoard();
        System.out.println(board);
        Table table = new Table();
    }
}
