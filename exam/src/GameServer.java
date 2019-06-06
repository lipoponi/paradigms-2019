public class GameServer {
    private Player playerA, playerB;

    GameServer(Player playerA, Player playerB) {
        this.playerA = playerA;
        this.playerB = playerB;
        playerA.setRole(CellFilling.CROSS);
        playerB.setRole(CellFilling.NOUGHT);
    }

    public Player play(BoardConfiguration configuration) {
        boolean playerAActive = true;

        while (!configuration.lastPutWasWinning() && configuration.hasFreeCells()) {
            if (playerAActive) {
                playerA.makeMove(configuration);
            } else {
                playerB.makeMove(configuration);
            }

            playerAActive = !playerAActive;
        }

        if (configuration.lastPutWasWinning()) {
            return playerAActive ? playerB : playerA;
        } else {
            return null;
        }
    }
}
