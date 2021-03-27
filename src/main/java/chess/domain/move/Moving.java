package chess.domain.move;

import chess.domain.board.Board;
import chess.domain.pieces.Piece;
import chess.domain.position.Position;

import java.util.List;

public interface Moving {
    List<Position> movablePositions(final Piece piece, final Board board, final int[] rowDirections, final int[] colDirections);
}
