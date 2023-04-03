package domain.board;

import domain.Board;
import domain.exception.InvalidDestinationPointException;
import domain.piece.pawn.WhitePawn;
import domain.piece.queen.BlackQueen;
import domain.piece.queen.WhiteQueen;
import domain.piece.rook.WhiteRook;
import domain.point.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static domain.point.File.B;
import static domain.point.Rank.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class QueenTest {
    @Nested
    @DisplayName("룩이 이동하는 경우")
    class moveCase {
        @ParameterizedTest(name = "{displayName} - {0}")
        @ValueSource(strings = {"b4", "b5", "c3", "b2", "b1", "a3", "c4", "c2", "a2", "a4"})
        @DisplayName("주위에 어떤 장기말도 없을 때, 퀸은 가로와 세로, 대각선 방향으로 무한히 이동할 수 있다.")
        void rookFirstMove(String toPoint) {
            // given
            Board board = Textures.makeBoard(Map.of(
                    new Point(B, THREE), new BlackQueen()
            ));

            // when & then
            assertDoesNotThrow(() -> board.move("b3", toPoint));
        }

        @Test
        @DisplayName("이동하려는 경로 사이에 다른 기물이 막고있을 경우, 전진하지 못하고 예외가 발생한다.")
        void givenPieceBetWeenTwoPoint_whenPawnMoveToPoint() {
            // given
            Board board = Textures.makeBoard(Map.of(
                    new Point(B, TWO), new WhiteRook(),
                    new Point(B, FOUR), new WhiteQueen()
            ));

            // when & then
            assertThatThrownBy(() -> board.move("b4", "b1"))
                    .isInstanceOf(InvalidDestinationPointException.class);
        }

        @Test
        @DisplayName("이동하려는 위치에 우리 편의 기물이 있다면 이동이 불가능하다.")
        void givenTeamOnPoint_whenPawnMoveToPoint() {
            // given
            Board board = Textures.makeBoard(Map.of(
                    new Point(B, TWO), new WhitePawn(),
                    new Point(B, FOUR), new WhiteQueen()
            ));

            // when & then
            assertThatThrownBy(() -> board.move("b4", "b2"))
                    .isInstanceOf(InvalidDestinationPointException.class);
        }
    }
}
