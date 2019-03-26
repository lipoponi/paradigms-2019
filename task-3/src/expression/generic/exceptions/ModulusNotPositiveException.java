package expression.generic.exceptions;

public class ModulusNotPositiveException extends EvaluationException {
    public ModulusNotPositiveException() {
        super("Modulus not positive in expression");
    }
}
