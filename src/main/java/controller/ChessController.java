package controller;

import domain.Board;
import domain.Turn;
import exception.GameFinishedException;
import view.Command;
import view.InputView;
import view.OutputView;

public class ChessController {
    private final Board board;
    private final OutputView outputView;
    private final InputView inputView;

    public ChessController(Board board, InputView inputview, OutputView outputView) {
        this.board = board;
        this.inputView = inputview;
        this.outputView = outputView;
    }

    public void initializeBoard() {
        try {
            outputView.printAskingBootingCommandMessage();
            Command command = inputView.getGameCommand();
            if (command.isStarting()) {
                printBoardStatus();
            }
            if (command.isEnding()) {
                throw new GameFinishedException();
            }
        } catch (IllegalArgumentException e) {
            outputView.printExceptionMessage(e.getMessage());
            initializeBoard();
        }
    }

    public void executeByCommand(Turn turn) {
        try {
            Command command = inputView.getGameCommand();
            command.execute(board, turn, outputView);
            printBoardStatus();
        } catch (IllegalArgumentException e) {
            outputView.printExceptionMessage(e.getMessage());
            executeByCommand(turn);
        }
    }

    private void printBoardStatus() {
        outputView.printStatus(board.findCurrentStatus());
    }
}
