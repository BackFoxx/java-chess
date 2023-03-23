package domain.piece;

import domain.point.Direction;

import java.util.Map;

public class WhiteRook extends Piece {
    @Override
    public String getSymbol() {
        return "r";
    }

    @Override
    public boolean equals(Object obj) {
        return getClass() == obj.getClass();
    }

    @Override
    public Map<Direction, Integer> getMovableRange() {
        return null;
    }

    @Override
    public boolean isBlack() {
        return false;
    }

    @Override
    public boolean isWhite() {
        return true;
    }
}
