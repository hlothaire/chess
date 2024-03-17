package modele.pieces;

import com.google.common.collect.ImmutableList;
import modele.Alliance;
import modele.board.Board;
import modele.board.BoardUtils;
import modele.board.Move;
import modele.board.Move.*;
import modele.board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Queen extends Piece{

    private final static int[] CANDIDATE_MOVE_COORDINATE = {-9,-8,-7,-1,1,7,8,9};

    public Queen(final int piecePosition, final Alliance pieceAlliance){
        super(PieceType.Queen,piecePosition, pieceAlliance,true);
    }


    public Queen(final int piecePosition, final Alliance pieceAlliance,final boolean isFirstMove){
        super(PieceType.Queen,piecePosition, pieceAlliance,isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final int candidateCoordinateOffset : CANDIDATE_MOVE_COORDINATE){
            int candidateDestinationCoordinate = this.piecePosition;
            while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                if (isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
                        isEighthColumnExclusion(candidateDestinationCoordinate,candidateCoordinateOffset)) {
                    break;
                }
                candidateDestinationCoordinate += candidateCoordinateOffset;
                if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if(!candidateDestinationTile.isTileOccupied()){
                        legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
                    } else {
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                        if (this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }
                    }
                    break;
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Piece movePiece(final Move move) {
        return new Queen(move.getDestinationCoordinate(), move.getMovedPiece().pieceAlliance);
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1 || candidateOffset == -9 || candidateOffset == 7);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 9 || candidateOffset == 1);
    }

    @Override
    public String toString(){
        return PieceType.Queen.toString();
    }
}
