package expression;

public class Variable implements CommonExpression {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    public int evaluate(int x) {
        return x;
    }

    public double evaluate(double x) {
        return x;
    }

    public int evaluate(int x, int y, int z) {
        switch (this.name) {
            case "x":
                return x;
            case "y":
                return y;
            case "z":
                return z;
        }

        return 0;
    }
}