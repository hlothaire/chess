package modele.player;

public enum MoveStatus {

    Done() {
        @Override
        boolean isDone() {
            return true;
        }
    },
    ILLEGAL_MOVE(){
        @Override
        boolean isDone() {
            return false;
        }
    },
    LEAVES_PLAYER_IN_CHECK(){
        @Override
        boolean isDone() {
            return false;
        }
    };

    abstract boolean isDone();
}
