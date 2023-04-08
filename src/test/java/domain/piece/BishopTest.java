package domain.piece;

import dao.Movement;
import domain.Board;
import domain.Turn;
import domain.piece.bishop.BlackBishop;
import domain.piece.bishop.WhiteBishop;
import domain.piece.pawn.WhitePawn;
import domain.piece.rook.BlackRook;
import domain.point.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import util.ExceptionMessages;

import java.util.Map;

import static domain.point.File.*;
import static domain.point.Rank.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class BishopTest {
    @Nested
    @DisplayName("비숍이 이동하는 경우")
    class moveCase {
        @ParameterizedTest(name = "{displayName} - {0}")
        @ValueSource(strings = {"b4", "a5", "b2", "a1", "d4", "e5", "d2", "e1"})
        @DisplayName("주위에 어떤 장기말도 없을 때, 룩은 양 대각선 방향으로 무한히 이동할 수 있다.")
        void rookFirstMove(String toPoint) {
            // given
            Board board = Textures.makeBoard(Map.of(
                    new Point(C, THREE), new WhiteBishop()
            ));

            // when & then
            assertDoesNotThrow(() -> board.move(new Movement(new Point(C, THREE), Point.fromSymbol(toPoint)), Turn.WHITE));
        }

        @ParameterizedTest(name = "{displayName} - {1}")
        @CsvSource(value = {"b1,아래 이동 불가", "c2,오른쪽 이동 불가", "b3,위 이동 불가", "a2,왼쪽 이동 불가"})
        @DisplayName("룩을 가로세로 방향으로 이동하려는 경우 예외가 발생한다.")
        void pawnMoveToInvalidDirection(String destination, String description) {
            // given
            Board board = Textures.makeBoard(Map.of(
                    new Point(B, TWO), new WhiteBishop()
            ));

            // when & then
            assertThatThrownBy(() -> board.move(new Movement(Point.fromSymbol("b2"), Point.fromSymbol(destination)), Turn.WHITE))
                    .as(description)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ExceptionMessages.INVALID_DESTINATION);
        }

        @Test
        @DisplayName("이동하려는 경로 사이에 다른 기물이 막고있을 경우, 전진하지 못하고 예외가 발생한다.")
        void givenPieceBetWeenTwoPoint_whenPawnMoveToPoint() {
            // given
            Board board = Textures.makeBoard(Map.of(
                    new Point(B, TWO), new WhitePawn(),
                    new Point(A, THREE), new BlackBishop(),
                    new Point(B, THREE), new BlackRook()
            ));

            // when & then
            assertThatThrownBy(() -> board.move(new Movement(Point.fromSymbol("a3"), Point.fromSymbol("c1")), Turn.BLACK))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ExceptionMessages.INVALID_DESTINATION);
        }

        @Test
        @DisplayName("이동하려는 위치에 우리 편의 기물이 있다면 이동이 불가능하다.")
        void givenTeamOnPoint_whenPawnMoveToPoint() {
            // given
            Board board = Textures.makeBoard(Map.of(
                    new Point(C, ONE), new BlackRook(),
                    new Point(A, THREE), new BlackBishop()
            ));

            // when & then
            assertThatThrownBy(() -> board.move(new Movement(Point.fromSymbol("a3"), Point.fromSymbol("c1")), Turn.BLACK))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ExceptionMessages.INVALID_DESTINATION);
        }
    }
}
