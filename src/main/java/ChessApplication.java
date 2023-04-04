import controller.ChessController;
import domain.Board;
import domain.Turn;
import exception.GameFinishedException;
import view.InputView;
import view.OutputView;
import view.ScannerInputReader;

public class ChessApplication {
    public static void main(String[] args) {
        ChessController controller = new ChessController(
                new Board(),
                new InputView(new ScannerInputReader()),
                new OutputView());

        try {
            controller.initializeBoard();
        } catch (GameFinishedException e) {
            //TODO : 게임이 끝날 때 동작
            return;
        }

        play(controller);
    }

    private static void play(ChessController controller) {
        Turn turn = Turn.BLACK;
        while (true) {
            try {
                controller.movePiece(turn);
                turn = turn.switchTurn();
            } catch (GameFinishedException e) {
                //TODO : 게임이 끝날 때 동작
                return;
            }
        }
    }
}
