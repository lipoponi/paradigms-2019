"use strict";

const {
    Const, Variable, Add, Subtract,
    Multiply, Divide, Negate, ArcTan, ArcTan2,
    Min3, Max5, Sinh, Cosh, parse
} = (function () {

    function equalsConst(obj, value) {
        return obj instanceof Const && obj.evaluate() === value;
    }


    const Primary = new Function();

    Primary.prototype.simplify = function () {
        return this;
    };


    function Const(value) {
        Primary.call(this);

        this.evaluate = function () {
            return value.valueOf();
        };
        this.toString = function () {
            return value.toString();
        };
    }

    Const.prototype = Object.create(Primary.prototype);
    Const.prototype.diff = () => new Const(0);


    function Variable(name) {
        Primary.call(this);

        this.evaluate = function (...args) {
            return args['xyz'.indexOf(name)];
        };
        this.toString = function () {
            return name.toString();
        };
        this.diff = function (variable) {
            return new Const(name === variable ? 1 : 0);
        };
    }

    Variable.prototype = Object.create(Primary.prototype);


    function Operation(operands) {
        this.evaluate = function (...variables) {
            return this.apply(...operands.map((e) => e.evaluate(...variables)));
        };
        this.toString = function () {
            return operands.map((e) => e.toString()).concat(this.str).join(' ');
        };
        this.diff = function (variable) {
            if (this.applyDiffRules === undefined) {
                return new Const(0);
            }
            return this.applyDiffRules(...operands.map((e) => e.diff(variable)));
        };
        this.simplify = function () {
            operands.forEach((e, index, array) => array[index] = array[index].simplify());

            if (operands.every((e) => e instanceof Const)) {
                return new Const(this.evaluate());
            } else if (this.specialSimplify !== undefined) {
                return this.specialSimplify();
            } else {
                return this;
            }
        };
    }


    function Add(...operands) {
        Operation.call(this, operands);

        this.specialSimplify = function () {
            if (equalsConst(operands[0], 0)) {
                return operands[1];
            } else if (equalsConst(operands[1], 0)) {
                return operands[0];
            } else {
                return this;
            }
        };
    }

    Add.operandsCount = 2;
    Add.prototype = Object.create(Operation.prototype);
    Add.prototype.apply = (left, right) => left + right;
    Add.prototype.str = '+';
    Add.prototype.applyDiffRules = (leftDiff, rightDiff) => new Add(leftDiff, rightDiff);


    function Subtract(...operands) {
        Operation.call(this, operands);

        this.specialSimplify = function () {
            if (equalsConst(operands[0], 0)) {
                return new Negate(operands[1]);
            } else if (equalsConst(operands[1], 0)) {
                return operands[0];
            } else {
                return this;
            }
        };
    }

    Subtract.operandsCount = 2;
    Subtract.prototype = Object.create(Operation.prototype);
    Subtract.prototype.apply = (left, right) => left - right;
    Subtract.prototype.str = '-';
    Subtract.prototype.applyDiffRules = (leftDiff, rightDiff) => new Subtract(leftDiff, rightDiff);


    function Multiply(...operands) {
        Operation.call(this, operands);

        this.applyDiffRules = function (leftDiff, rightDiff) {
            return new Add(
                new Multiply(operands[0], rightDiff),
                new Multiply(operands[1], leftDiff)
            );
        };
        this.specialSimplify = function () {
            if (equalsConst(operands[0], 0) || equalsConst(operands[1], 0)) {
                return new Const(0);
            } else if (equalsConst(operands[0], 1)) {
                return operands[1];
            } else if (equalsConst(operands[1], 1)) {
                return operands[0];
            } else {
                return this;
            }
        };
    }

    Multiply.operandsCount = 2;
    Multiply.prototype = Object.create(Operation.prototype);
    Multiply.prototype.apply = (left, right) => left * right;
    Multiply.prototype.str = '*';


    function Divide(...operands) {
        Operation.call(this, operands);

        this.applyDiffRules = function (leftDiff, rightDiff) {
            return new Divide(
                new Subtract(
                    new Multiply(leftDiff, operands[1]),
                    new Multiply(operands[0], rightDiff)
                ),
                new Multiply(operands[1], operands[1])
            );
        };
        this.specialSimplify = function () {
            if (equalsConst(operands[0], 0)) {
                return new Const(0);
            } else if (equalsConst(operands[1], 1)) {
                return operands[0];
            } else {
                return this;
            }
        };
    }

    Divide.operandsCount = 2;
    Divide.prototype = Object.create(Operation.prototype);
    Divide.prototype.apply = (left, right) => left / right;
    Divide.prototype.str = '/';


    function Negate(...operands) {
        Operation.call(this, operands);
    }

    Negate.operandsCount = 1;
    Negate.prototype = Object.create(Operation.prototype);
    Negate.prototype.apply = inner => -inner;
    Negate.prototype.str = 'negate';
    Negate.prototype.applyDiffRules = (innerDiff) => new Negate(innerDiff);


    function ArcTan(...operands) {
        Operation.call(this, operands);

        this.applyDiffRules = function (innerDiff) {
            return new Divide(
                innerDiff,
                new Add(
                    new Const(1),
                    new Multiply(operands[0], operands[0])
                )
            );
        };
    }

    ArcTan.operandsCount = 1;
    ArcTan.prototype = Object.create(Operation.prototype);
    ArcTan.prototype.apply = Math.atan;
    ArcTan.prototype.str = 'atan';


    function ArcTan2(...operands) {
        Operation.call(this, operands);

        this.applyDiffRules = function (yDiff, xDiff) {
            return new Divide(
                new Subtract(
                    new Multiply(yDiff, operands[1]),
                    new Multiply(operands[0], xDiff)
                ),
                new Add(
                    new Multiply(operands[0], operands[0]),
                    new Multiply(operands[1], operands[1])
                )
            );
        };
    }

    ArcTan2.operandsCount = 2;
    ArcTan2.prototype = Object.create(Operation.prototype);
    ArcTan2.prototype.apply = Math.atan2;
    ArcTan2.prototype.str = 'atan2';


    function Min3(...operands) {
        Operation.call(this, operands);
    }

    Min3.operandsCount = 3;
    Min3.prototype = Object.create(Operation.prototype);
    Min3.prototype.apply = Math.min;
    Min3.prototype.str = 'min3';


    function Max5(...operands) {
        Operation.call(this, operands);
    }

    Max5.operandsCount = 5;
    Max5.prototype = Object.create(Operation.prototype);
    Max5.prototype.apply = Math.max;
    Max5.prototype.str = 'max5';


    function Sinh(...operands) {
        Operation.call(this, operands);

        this.applyDiffRules = function (innerDiff) {
            return new Multiply(innerDiff, new Cosh(operands[0]));
        };
    }

    Sinh.operandsCount = 1;
    Sinh.prototype = Object.create(Operation.prototype);
    Sinh.prototype.apply = Math.sinh;
    Sinh.prototype.str = 'sinh';


    function Cosh(...operands) {
        Operation.call(this, operands);

        this.applyDiffRules = function (innerDiff) {
            return new Multiply(innerDiff, new Sinh(operands[0]));
        };
    }

    Cosh.operandsCount = 1;
    Cosh.prototype = Object.create(Operation.prototype);
    Cosh.prototype.apply = Math.cosh;
    Cosh.prototype.str = 'cosh';


    const operations = {
        '+': Add,
        '-': Subtract,
        '*': Multiply,
        '/': Divide,
        'negate': Negate,
        'atan': ArcTan,
        'atan2': ArcTan2,
        'min3': Min3,
        'max5': Max5,
        'sinh': Sinh,
        'cosh': Cosh
    };

    function parse(rpn) {
        let stack = Array();
        let units = rpn.trim().split(/\s+/);

        units.forEach((unit) => {
            let result;

            if (!isNaN(Number(unit))) {
                result = new Const(Number(unit));
            } else if (operations.hasOwnProperty(unit)) {
                let count = operations[unit].operandsCount;
                let args = stack.splice(stack.length - count, count);
                result = new operations[unit](...args);
            } else {
                result = new Variable(unit);
            }

            stack.push(result);
        });

        return stack.pop();
    }

    return {
        Const: Const,
        Variable: Variable,
        Add: Add,
        Subtract: Subtract,
        Multiply: Multiply,
        Divide: Divide,
        Negate: Negate,
        ArcTan: ArcTan,
        ArcTan2: ArcTan2,
        Min3: Min3,
        Max5: Max5,
        Sinh: Sinh,
        Cosh: Cosh,
        parse: parse
    };
})();