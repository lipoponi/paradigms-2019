import java.util.Random;

public class ComputerPlayer extends AbstractPlayer {
    private final Random generator;

    public ComputerPlayer(String id) {
        super(id);
        generator = new Random();
    }

    @Override
    protected Point makeDecision(BoardConfiguration configuration) {
        int x, y;

        Point result;
        do {
            x = generator.nextInt(configuration.getBoardWidth());
            y = generator.nextInt(configuration.getBoardHeight());
            result = new Point(x, y);
        } while (configuration.isFilled(result));

        return result;
    }
}
