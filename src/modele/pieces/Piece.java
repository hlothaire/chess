package modele.pieces;

import modele.Alliance;
import modele.board.Board;
import modele.board.Move;

import java.util.Collection;

public abstract class Piece {

    protected final int piecePosition;
    protected final boolean isFirstMove;
    protected final Alliance pieceAlliance;

    Piece(final int piecePosition, final Alliance pieceAlliance) {
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = false;
    }

    public int getPiecePosition(){
        return this.piecePosition;
    }

    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }

    public boolean isFirstMove() { return this.isFirstMove; }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public enum PieceType{

        Pawn("P"),
        Knight("N"),
        Bishop("B"),
        Rook("R"),
        Queen("Q"),
        King("K");
        private String pieceName;
       PieceType(final String pieceName){
           this.pieceName  = pieceName;
        }

        @Override
        public String toString(){
           return this.pieceName;
        }
    }
}
