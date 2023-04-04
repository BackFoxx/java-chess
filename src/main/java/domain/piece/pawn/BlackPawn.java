package domain.piece.pawn;

import domain.piece.Piece;
import domain.point.Direction;

import java.util.HashMap;
import java.util.Map;

public class BlackPawn extends Piece {
    @Override
    public String getSymbol() {
        return "P";
    }

    @Override
    public boolean equals(Object obj) {
        return getClass() == obj.getClass();
    }

    @Override
    public Map<Direction, Integer> getMovableDirectionAndRange() {
        Map<Direction, Integer> movableRange = new HashMap<>();
        movableRange.put(Direction.DOWN, 2);
        return movableRange;
    }

    @Override
    public boolean isBlack() {
        return true;
    }

    @Override
    public boolean isWhite() {
        return false;
    }

    @Override
    public boolean isBlackPawn() {
        return true;
    }

    @Override
    public boolean isWhitePawn() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isKing() {
        return false;
    }
}
