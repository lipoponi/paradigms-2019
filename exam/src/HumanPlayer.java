import java.util.Scanner;

public class HumanPlayer extends AbstractPlayer {
    public HumanPlayer(String id) {
        super(id);
    }

    @Override
    protected Point makeDecision(BoardConfiguration configuration) {
        System.out.println("Введите ход для игрорка " + this.toString() + " (x, y)");

        Scanner scan = new Scanner(System.in);
        int x = scan.nextInt() - 1;
        int y = scan.nextInt() - 1;

        return new Point(x, y);
    }
}
