package domain.board;

import domain.Board;
import domain.exception.InvalidDestinationPointException;
import domain.piece.Empty;
import domain.piece.Piece;
import domain.piece.king.BlackKing;
import domain.piece.king.WhiteKing;
import domain.piece.pawn.BlackPawn;
import domain.piece.pawn.OnceMovedBlackPawn;
import domain.piece.pawn.OneMovedWhitePawn;
import domain.piece.pawn.WhitePawn;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class PawnTest {
    @Nested
    @DisplayName("검정색 폰을 움직이는 경우")
    class BlackPawnCase {
        @Test
        @DisplayName("폰을 처음 움직이는 경우, 한 번에 두 칸씩 이동할 수 있다.")
        void pawnFirstMove() {
            List<List<Piece>> boardStatus = Arrays.asList(
                    Arrays.asList(new Empty(), new Empty(), new Empty()), // a1, b1, c1
                    Arrays.asList(new Empty(), new Empty(), new Empty()), // a2, b2, c2
                    Arrays.asList(new BlackPawn(), new Empty(), new Empty()) // a3, b3, c3
            );
            Board board = new Board(boardStatus);

            board.move("a3", "a1");

            assertThat(boardStatus.get(0).get(0)).isEqualTo(new OnceMovedBlackPawn());
        }

        @Test
        @DisplayName("폰을 처음 움직인 이후에는, 한 번에 한 칸씩 전진할 수 있다.")
        void pawnMoveAfterFirst() {
            List<List<Piece>> boardStatus = Arrays.asList(
                    Arrays.asList(new Empty(), new Empty(), new Empty()), // a1, a2, a3
                    Arrays.asList(new Empty(), new Empty(), new Empty()), // a2, b2, c2
                    Arrays.asList(new Empty(), new Empty(), new Empty()), // a3, b3, c3
                    Arrays.asList(new Empty(), new Empty(), new Empty()), // a4, b4, c4
                    Arrays.asList(new BlackPawn(), new Empty(), new Empty()) // a5, b5, c5
            );
            Board board = new Board(boardStatus);
            board.move("a5", "a3");

            assertThatThrownBy(() -> board.move("a3", "a1"))
                    .as("최초의 이동이 아닌데 두 칸을 한번에 전진하려는 경우 예외가 발생한다.")
                    .isInstanceOf(InvalidDestinationPointException.class);
            assertDoesNotThrow(() -> board.move("a3", "a2"));
        }

        @Test
        @DisplayName("주위에 장기말이 없을 때, 폰을 위쪽 방향 외의 다른 방향으로 이동하려는 경우 예외가 발생한다.")
        void pawnMoveToInvalidDirection() {
            List<List<Piece>> boardStatus = Arrays.asList(
                    Arrays.asList(new Empty(), new Empty(), new Empty()), // a1, b1, c1
                    Arrays.asList(new Empty(), new BlackPawn(), new Empty()), // a2, b2, c2
                    Arrays.asList(new Empty(), new Empty(), new Empty()) // a3, b3, c3
            );
            Board board = new Board(boardStatus);

            assertAll(
                    () -> assertThatThrownBy(() -> board.move("b2", "a1"))
                            .as("왼쪽 아래 이동 불가").isInstanceOf(InvalidDestinationPointException.class),
                    () -> assertThatThrownBy(() -> board.move("b2", "c1"))
                            .as("오른쪽 아래 이동 불가").isInstanceOf(InvalidDestinationPointException.class),
                    () -> assertThatThrownBy(() -> board.move("b2", "c2"))
                            .as("오른쪽 이동 불가").isInstanceOf(InvalidDestinationPointException.class),
                    () -> assertThatThrownBy(() -> board.move("b2", "c3"))
                            .as("오른쪽 위 이동 불가").isInstanceOf(InvalidDestinationPointException.class),
                    () -> assertThatThrownBy(() -> board.move("b2", "b3"))
                            .as("위 이동 불가").isInstanceOf(InvalidDestinationPointException.class),
                    () -> assertThatThrownBy(() -> board.move("b2", "a3"))
                            .as("왼쪽 위 이동 불가").isInstanceOf(InvalidDestinationPointException.class),
                    () -> assertThatThrownBy(() -> board.move("b2", "a2"))
                            .as("왼쪽 이동 불가").isInstanceOf(InvalidDestinationPointException.class)
            );
        }

        @Test
        @DisplayName("이동하려는 경로 사이에 다른 기물이 막고있을 경우, 전진하지 못하고 예외가 발생한다.")
        void givenPieceBetWeenTwoPoint_whenPawnMoveToPoint() {
            List<List<Piece>> boardStatus = Arrays.asList(
                    Arrays.asList(new Empty(), new Empty(), new Empty()), // a1, b1, c1
                    Arrays.asList(new Empty(), new BlackPawn(), new Empty()), // a2, b2, c2
                    Arrays.asList(new Empty(), new BlackPawn(), new Empty()), // a3, b3, c3
                    Arrays.asList(new Empty(), new Empty(), new Empty()) // a4, b4, c4
            );
            Board board = new Board(boardStatus);

            assertThatThrownBy(() -> board.move("b3", "b1"))
                    .isInstanceOf(InvalidDestinationPointException.class);
        }

        @Test
        @DisplayName("이동하려는 위치에 우리 편의 기물이 있다면 이동이 불가능하다.")
        void givenTeamOnPoint_whenPawnMoveToPoint() {
            List<List<Piece>> boardStatus = Arrays.asList(
                    Arrays.asList(new Empty(), new Empty(), new Empty()), // a1, b1, c1
                    Arrays.asList(new Empty(), new BlackPawn(), new Empty()), // a2, b2, c2
                    Arrays.asList(new Empty(), new Empty(), new Empty()), // a3, b3, c3
                    Arrays.asList(new Empty(), new BlackPawn(), new Empty()) // a4, b4, c4
            );
            Board board = new Board(boardStatus);

            assertThatThrownBy(() -> board.move("b4", "b2"))
                    .isInstanceOf(InvalidDestinationPointException.class);
        }

        @ParameterizedTest(name = "{displayName} - {0}")
        @ValueSource(strings = {"a1", "c1"})
        @DisplayName("검은색 기물의 전진 방향 대각선으로 상대 기물이 있다면, 상대 기물이 있는 위치로 이동할 수 있다.")
        void enemyOnDiagonal(String target) {
            List<List<Piece>> boardStatus = Arrays.asList(
                    Arrays.asList(new WhiteKing(), new Empty(), new WhitePawn()), // a1, b1, c1
                    Arrays.asList(new Empty(), new BlackPawn(), new Empty()), // a2, b2, c2
                    Arrays.asList(new Empty(), new Empty(), new Empty()), // a3, b3, c3
                    Arrays.asList(new Empty(), new Empty(), new Empty()) // a4, b4, c4
            );
            Board board = new Board(boardStatus);

            assertDoesNotThrow(() -> board.move("b2", target));
        }
    }

    @Nested
    @DisplayName("하얀색 폰을 움직이는 경우")
    class WhitePawnCase {
        @Test
        @DisplayName("폰을 처음 움직이는 경우, 한 번에 두 칸씩 이동할 수 있다.")
        void pawnFirstMove() {
            List<List<Piece>> boardStatus = Arrays.asList(
                    Arrays.asList(new Empty(), new WhitePawn(), new Empty()), // a1, b1, c1
                    Arrays.asList(new Empty(), new Empty(), new Empty()), // a2, b2, c2
                    Arrays.asList(new Empty(), new Empty(), new Empty()) // a3, b3, c3
            );
            Board board = new Board(boardStatus);

            board.move("b1", "b3");

            assertThat(boardStatus.get(2).get(1)).isEqualTo(new OneMovedWhitePawn());
        }

        @Test
        @DisplayName("폰을 처음 움직인 이후에는, 한 번에 한 칸씩 전진할 수 있다.")
        void pawnMoveAfterFirst() {
            List<List<Piece>> boardStatus = Arrays.asList(
                    Arrays.asList(new Empty(), new WhitePawn(), new Empty()), // a1, b1, c1
                    Arrays.asList(new Empty(), new Empty(), new Empty()), // a2, b2, c2
                    Arrays.asList(new Empty(), new Empty(), new Empty()), // a3, b3, c3
                    Arrays.asList(new Empty(), new Empty(), new Empty()), // a4, b4, c4
                    Arrays.asList(new Empty(), new Empty(), new Empty()) // a5, b5, c5
            );
            Board board = new Board(boardStatus);
            board.move("b1", "b3");

            assertThatThrownBy(() -> board.move("b3", "b5"))
                    .as("최초의 이동이 아닌데 두 칸을 한번에 전진하려는 경우 예외가 발생한다.")
                    .isInstanceOf(InvalidDestinationPointException.class);
            assertDoesNotThrow(() -> board.move("b3", "b4"));
        }

        @Test
        @DisplayName("주위에 장기말이 없을 때, 폰을 위 방향 외의 다른 방향으로 이동하려는 경우 예외가 발생한다.")
        void pawnMoveToInvalidDirection() {
            List<List<Piece>> boardStatus = Arrays.asList(
                    Arrays.asList(new Empty(), new Empty(), new Empty()), // a1, b1, c1
                    Arrays.asList(new Empty(), new WhitePawn(), new Empty()), // a2, b2, c2
                    Arrays.asList(new Empty(), new Empty(), new Empty()) // a3, b3, c3
            );
            Board board = new Board(boardStatus);

            assertAll(
                    () -> assertThatThrownBy(() -> board.move("b2", "b1"))
                            .as("아래 이동 불가").isInstanceOf(InvalidDestinationPointException.class),
                    () -> assertThatThrownBy(() -> board.move("b2", "a1"))
                            .as("왼쪽 아래 이동 불가").isInstanceOf(InvalidDestinationPointException.class),
                    () -> assertThatThrownBy(() -> board.move("b2", "c1"))
                            .as("오른쪽 아래 이동 불가").isInstanceOf(InvalidDestinationPointException.class),
                    () -> assertThatThrownBy(() -> board.move("b2", "c2"))
                            .as("오른쪽 이동 불가").isInstanceOf(InvalidDestinationPointException.class),
                    () -> assertThatThrownBy(() -> board.move("b2", "c3"))
                            .as("오른쪽 위 이동 불가").isInstanceOf(InvalidDestinationPointException.class),
                    () -> assertThatThrownBy(() -> board.move("b2", "a3"))
                            .as("왼쪽 위 이동 불가").isInstanceOf(InvalidDestinationPointException.class),
                    () -> assertThatThrownBy(() -> board.move("b2", "a2"))
                            .as("왼쪽 이동 불가").isInstanceOf(InvalidDestinationPointException.class)
            );
        }

        @Test
        @DisplayName("이동하려는 경로 사이에 다른 기물이 막고있을 경우, 전진하지 못하고 예외가 발생한다.")
        void givenPieceBetWeenTwoPoint_whenPawnMoveToPoint() {
            List<List<Piece>> boardStatus = Arrays.asList(
                    Arrays.asList(new Empty(), new Empty(), new Empty()), // a1, b1, c1
                    Arrays.asList(new Empty(), new WhitePawn(), new Empty()), // a2, b2, c2
                    Arrays.asList(new Empty(), new WhitePawn(), new Empty()), // a3, b3, c3
                    Arrays.asList(new Empty(), new Empty(), new Empty()) // a4, b4, c4
            );
            Board board = new Board(boardStatus);

            assertThatThrownBy(() -> board.move("b2", "b4"))
                    .isInstanceOf(InvalidDestinationPointException.class);
        }

        @Test
        @DisplayName("이동하려는 위치에 우리 편의 기물이 있다면 이동이 불가능하다.")
        void givenTeamOnPoint_whenPawnMoveToPoint() {
            List<List<Piece>> boardStatus = Arrays.asList(
                    Arrays.asList(new Empty(), new Empty(), new Empty()), // a1, b1, c1
                    Arrays.asList(new Empty(), new WhitePawn(), new Empty()), // a2, b2, c2
                    Arrays.asList(new Empty(), new Empty(), new Empty()), // a3, b3, c3
                    Arrays.asList(new Empty(), new WhitePawn(), new Empty()) // a4, b4, c4
            );
            Board board = new Board(boardStatus);

            assertThatThrownBy(() -> board.move("b2", "b4"))
                    .isInstanceOf(InvalidDestinationPointException.class);
        }

        @ParameterizedTest(name = "{displayName} - {0}")
        @ValueSource(strings = {"a3", "c3"})
        @DisplayName("검은색 기물의 전진 방향 대각선으로 상대 기물이 있다면, 상대 기물이 있는 위치로 이동할 수 있다.")
        void enemyOnDiagonal(String target) {
            List<List<Piece>> boardStatus = Arrays.asList(
                    Arrays.asList(new Empty(), new Empty(), new Empty()), // a1, b1, c1
                    Arrays.asList(new Empty(), new WhitePawn(), new Empty()), // a2, b2, c2
                    Arrays.asList(new BlackKing(), new Empty(), new BlackPawn()), // a3, b3, c3
                    Arrays.asList(new Empty(), new Empty(), new Empty()) // a4, b4, c4
            );
            Board board = new Board(boardStatus);

            assertDoesNotThrow(() -> board.move("b2", target));
        }
    }
}
