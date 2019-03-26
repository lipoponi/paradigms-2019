package expression;

public class Const implements CommonExpression {
    private final Number value;

    public Const(int value) {
        this.value = value;
    }

    public Const(double value) {
        this.value = value;
    }

    public int evaluate(int x) {
        return this.value.intValue();
    }

    public double evaluate(double x) {
        return this.value.doubleValue();
    }

    public int evaluate(int x, int y, int z) {
        return this.value.intValue();
    }
}