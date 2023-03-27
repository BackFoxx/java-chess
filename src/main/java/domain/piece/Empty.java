package domain.piece;

import domain.point.Direction;

import java.util.Map;

public class Empty extends Piece {
    @Override
    public String getSymbol() {
        return ".";
    }

    @Override
    public boolean equals(Object obj) {
        return getClass() == obj.getClass();
    }

    @Override
    public Map<Direction, Integer> getMovableDirectionAndRange() {
        return null;
    }

    @Override
    public boolean isBlack() {
        return false;
    }

    @Override
    public boolean isWhite() {
        return false;
    }
}
