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
    private final int  cachedHashCode;

    Piece(final PieceType pieceType, final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = isFirstMove;
        this.cachedHashCode = computeHashCode();
    }

    private int computeHashCode(){
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Piece)){
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() &&
                pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove;
    }

    @Override
    public int hashCode(){
        return this.cachedHashCode;
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
    public abstract Piece movePiece(final Move move);

    public enum PieceType{

        Pawn("P") {
            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isKing() {
                return false;
            }
        },
        Knight("N") {
            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isKing() {
                return false;
            }
        },
        Bishop("B") {
            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isKing() {
                return false;
            }
        },
        Rook("R") {
            @Override
            public boolean isRook() {
                return true;
            }

            @Override
            public boolean isKing() {
                return false;
            }
        },
        Queen("Q") {
            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isKing() {
                return false;
            }
        },
        King("K") {
            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isKing() {
               return true;
            }
        };
        private final String pieceName;
       PieceType(final String pieceName){
           this.pieceName  = pieceName;
        }

        @Override
        public String toString(){
           return this.pieceName;
        }
        public abstract boolean isRook();
        public abstract boolean isKing();
    }
}
