public abstract class AbstractPlayer implements Player {
    private String id;
    private CellFilling role;

    protected abstract Point makeDecision(BoardConfiguration configuration);

    public AbstractPlayer(String id) {
        this.id = id;
    }

    @Override
    public void setRole(CellFilling role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public void makeMove(BoardConfiguration configuration) {
        Point decision = makeDecision(configuration);
        configuration.put(decision, role);
    }
}
