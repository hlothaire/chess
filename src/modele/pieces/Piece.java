package modele.pieces;

import modele.Alliance;
import modele.board.Board;
import modele.board.Move;

import java.util.Collection;

public abstract class Piece {

    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final boolean isFirstMove;
    protected final Alliance pieceAlliance;

    Piece(final PieceType pieceType, final int piecePosition, final Alliance pieceAlliance) {
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = false;
    }

    public PieceType getPieceType() {
        return this.pieceType;
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

        Pawn("P") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        Knight("N") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        Bishop("B") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        Rook("R") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        Queen("Q") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        King("K") {
            @Override
            public boolean isKing() {
               return true;
            }
        };
        private String pieceName;
       PieceType(final String pieceName){
           this.pieceName  = pieceName;
        }

        @Override
        public String toString(){
           return this.pieceName;
        }

        public abstract boolean isKing();
    }
}
