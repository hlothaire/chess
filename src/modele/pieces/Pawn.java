package modele.pieces;

import com.google.common.collect.ImmutableList;
import modele.Alliance;
import modele.board.Board;
import modele.board.BoardUtils;
import modele.board.Move;
import modele.board.Move.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATE = {8,16,7,9};

    public Pawn(final int piecePosition, final Alliance pieceAlliance){
        super(PieceType.Pawn,piecePosition, pieceAlliance,true);
    }


    public Pawn(final int piecePosition, final Alliance pieceAlliance,final boolean isFirstMove){
        super(PieceType.Pawn,piecePosition, pieceAlliance,isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE){
            int candidateDestinationCoordinate  = this.piecePosition + (this.getPieceAlliance().getDirection() *
                    currentCandidateOffset);
            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                continue;
            }
            if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
            }else if(currentCandidateOffset == 16 && this.isFirstMove() &&
                    ((BoardUtils.SEVEN_RANK[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
                    (BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceAlliance().isWhite()))){
                final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                        !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    legalMoves.add(new PawnJump(board,this,candidateDestinationCoordinate));
                }
            } else if(currentCandidateOffset == 7 &&
                    !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                    (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack() )))){
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                        legalMoves.add(new PawnAttackMove(board,this,candidateDestinationCoordinate,pieceOnCandidate));
                    }
                }

            } else if (currentCandidateOffset == 9 &&
                    !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                    (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack() )))){
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                        legalMoves.add(new PawnAttackMove(board,this,candidateDestinationCoordinate,pieceOnCandidate));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Piece movePiece(final Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().pieceAlliance);
    }

    @Override
    public String toString(){
        return PieceType.Pawn.toString();
    }

}
