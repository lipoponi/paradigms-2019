import java.util.ArrayList;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        ArrayList<Player> players = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            players.add(new ComputerPlayer("C" + i));
        }

        players.set(0, new HumanPlayer("Human"));

        Collections.shuffle(players);

        for (int i = 0; i < 14; i += 2) {
            GameServer currentServer = new GameServer(players.get(i), players.get(i + 1));

            Player winner = null;
            while (winner == null) {
                BoardConfiguration board = new BoardConfiguration();
                winner = currentServer.play(board);
            }

            players.add(winner);
        }

        String winnerId = players.get(players.size() - 1).toString();
        System.out.println("-----");
        System.out.println("The winner is " + winnerId);
    }
}
