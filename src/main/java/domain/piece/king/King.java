package domain.piece.king;

import domain.piece.Piece;
import domain.point.Direction;

import java.util.HashMap;
import java.util.Map;

public abstract class King extends Piece {

    public static final int SCORE = 0;

    @Override
    public Map<Direction, Integer> getMovableDirectionAndRange() {
        Map<Direction, Integer> movableRange = new HashMap<>();
        movableRange.put(Direction.UP, 1);
        movableRange.put(Direction.DOWN, 1);
        movableRange.put(Direction.LEFT, 1);
        movableRange.put(Direction.RIGHT, 1);
        movableRange.put(Direction.LEFT_UP, 1);
        movableRange.put(Direction.LEFT_DOWN, 1);
        movableRange.put(Direction.RIGHT_UP, 1);
        movableRange.put(Direction.RIGHT_DOWN, 1);
        return movableRange;
    }

    @Override
    public boolean isWhitePawn() {
        return false;
    }

    @Override
    public boolean isBlackPawn() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isKing() {
        return true;
    }

    @Override
    public float getScore() {
        return SCORE;
    }
}
