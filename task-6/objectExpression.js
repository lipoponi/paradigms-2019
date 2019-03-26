"use strict";

const {
    Const, Variable, Add, Subtract,
    Multiply, Divide, Negate, ArcTan, ArcTan2,
    Min3, Max5, Sinh, Cosh, Sum, Avg,
    Sumexp, Softmax, Sumsq, Length, parse, parsePostfix, parsePrefix
} = (function () {

    const variables = Array.from('xyz');
    const ops = [];
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
        'cosh': Cosh,
        'sum': Sum,
        'avg': Avg,
        'sumexp': Sumexp,
        'softmax': Softmax,
        'sumsq': Sumsq,
        'length': Length
    };

    function equalsConst(obj, value) {
        return obj instanceof Const && obj.evaluate() === value;
    }


    const Primary = new Function();

    Primary.prototype.simplify = function () {
        return this;
    };


    function Const(value) {
        Primary.call(this);
        value = Number(value);

        this.evaluate = function () {
            return value.valueOf();
        };
        this.toString = this.postfix = this.prefix = function () {
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
        this.toString = this.postfix = this.prefix = function () {
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
        this.postfix = function () {
            return `(${operands.map((e) => e.postfix()).join(' ')} ${this.str})`;
        };
        this.prefix = function () {
            return `(${this.str} ${operands.map((e) => e.prefix()).join(' ')})`;
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


    function Sum(...operands) {
        Operation.call(this, operands);
    }

    Sum.operandsCount = -1;
    Sum.prototype = Object.create(Operation.prototype);
    Sum.prototype.apply = (...args) => args.reduce((a, b) => a + b, 0);
    Sum.prototype.str = 'sum';
    Sum.prototype.applyDiffRules = (...args) => new Sum(...args);


    function Avg(...operands) {
        Operation.call(this, operands);
    }

    Avg.operandsCount = -1;
    Avg.prototype = Object.create(Operation.prototype);
    Avg.prototype.apply = (...args) => args.reduce((a, b) => a + b, 0) / args.length;
    Avg.prototype.str = 'avg';
    Avg.prototype.applyDiffRules = (...args) => new Avg(...args);


    function Sumexp(...operands) {
        Operation.call(this, operands);

        this.applyDiffRules = function (...diffs) {
            return new Sum(...diffs.map((diff, index) => new Multiply(new Sumexp(operands[index]), diff)));
        }
    }

    Sumexp.operandsCount = -1;
    Sumexp.prototype = Object.create(Operation.prototype);
    Sumexp.prototype.apply = (...args) => args.reduce((a, b) => a + Math.exp(b), 0);
    Sumexp.prototype.str = 'sumexp';


    function Softmax(...operands) {
        Operation.call(this, operands);

        this.diff = function (variable) {
            return new Divide(new Sumexp(operands[0]), new Sumexp(...operands)).diff(variable);
        }
    }

    Softmax.operandsCount = -1;
    Softmax.prototype = Object.create(Operation.prototype);
    Softmax.prototype.apply = (...args) => Math.exp(args[0]) / args.reduce((a, b) => a + Math.exp(b), 0);
    Softmax.prototype.str = 'softmax';


    function Sumsq(...operands) {
        Operation.call(this, operands);

        this.diff = function (variable) {
            return new Sum(...operands.map((operand) => new Multiply(new Const(2), new Multiply(operand.diff(variable), operand))));
        };
    }

    Sumsq.operandsCount = -1;
    Sumsq.prototype = Object.create(Operation.prototype);
    Sumsq.prototype.apply = (...args) => args.reduce((a, b) => a + b * b, 0);
    Sumsq.prototype.str = 'sumsq';


    function Length(...operands) {
        Operation.call(this, operands);

        this.diff = function (variable) {
            if (operands.length === 0) {
                return new Const(0);
            }
            return new Divide(
                new Divide(
                    new Sumsq(...operands).diff(variable),
                    new Length(...operands)
                ),
                new Const(2)
            );
        }
    }

    Length.operandsCount = -1;
    Length.prototype = Object.create(Operation.prototype);
    Length.prototype.apply = (...args) => Math.sqrt(args.reduce((a, b) => a + b * b, 0));
    Length.prototype.str = 'length';


    function EndOfInputError() {
        Error.call(this);
        this.name = "EndOfInputError";
        this.message = 'End of input';

        if (Error.hasOwnProperty('captureStackTrace')) {
            Error.captureStackTrace(this, EndOfInputError);
        } else {
            this.stack = (new Error()).stack;
        }
    }

    EndOfInputError.prototype = Object.create(Error.prototype);


    function UnknownOperationError(operation, position) {
        Error.call(this);
        this.name = "UnknownOperationError";
        this.message = `Unknown operation '${operation}' at ${position}`;

        if (Error.hasOwnProperty('captureStackTrace')) {
            Error.captureStackTrace(this, EndOfInputError);
        } else {
            this.stack = (new Error()).stack;
        }
    }

    UnknownOperationError.prototype = Object.create(Error.prototype);


    function UnexpectedSymbolError(symbol, position) {
        Error.call(this);
        this.name = "UnexpectedSymbolError";
        this.message = `Unexpected symbol '${symbol}' at ${position}`;

        if (Error.hasOwnProperty('captureStackTrace')) {
            Error.captureStackTrace(this, EndOfInputError);
        } else {
            this.stack = (new Error()).stack;
        }
    }

    UnexpectedSymbolError.prototype = Object.create(Error.prototype);


    function ArgumentCountError(operation, args) {
        Error.call(this);
        this.name = "ArgumentCountError";
        this.message = `Invalid arguments count for '${operation.str}' operation expected ${operation.operandsCount}, actual ${args.length}`;

        if (Error.hasOwnProperty('captureStackTrace')) {
            Error.captureStackTrace(this, ArgumentCountError);
        } else {
            this.stack = (new Error()).stack;
        }
    }

    ArgumentCountError.prototype = Object.create(Error.prototype);


    function Tokenizer(expression) {
        let position = 0;
        let lastPosition = 0;

        function skipWhitespace() {
            while (position < expression.length && expression[position] === ' ') {
                position++;
            }
        }

        this.hasNext = function () {
            skipWhitespace();
            return position < expression.length;
        };
        this.next = function () {
            skipWhitespace();
            if (!this.hasNext()) {
                throw new EndOfInputError();
            }

            lastPosition = position;
            let result = expression[position++];
            if (!'()'.includes(result)) {
                while (position < expression.length && !'( )'.includes(expression[position])) {
                    result += expression[position++];
                }
            }

            return result;
        };
        this.getLastPosition = function () {
            return lastPosition;
        };
        this.peek = function (c) {
            if (this.hasNext()) {
                let a = lastPosition;
                let result = this.next();
                position = lastPosition;
                lastPosition = a;
                return result;
            } else {
                return '\0';
            }
        }
    }


    function parse(rpn) {
        let stack = Array();
        let tokens = rpn.trim().split(/\s+/);

        tokens.forEach((token) => {
            let result;

            if (!isNaN(Number(token))) {
                result = new Const(Number(token));
            } else if (operations.hasOwnProperty(token)) {
                let count = operations[token].operandsCount;
                if (count === -1) {
                    count = stack.length;
                }
                let args = stack.splice(stack.length - count, count);
                result = new operations[token](...args);
            } else {
                result = new Variable(token);
            }

            stack.push(result);
        });

        return stack.pop();
    }

    function parsePostfix(rpn) {
        let tokenizer = new Tokenizer(rpn);

        function parseOperation() {
            let stack = [];

            while (!operations.hasOwnProperty(tokenizer.peek())) {
                stack.push(parsePrimary());
            }

            let operation = operations[tokenizer.next()];
            if (operation.operandsCount !== -1 && operation.operandsCount !== stack.length) {
                throw new ArgumentCountError(operation, stack.length);
            }

            let rb = tokenizer.next();
            if (rb !== ')') {
                throw new UnexpectedSymbolError(rb, tokenizer.getLastPosition());
            }
            return new operation(...stack);
        }

        function parsePrimary() {
            let token = tokenizer.next();

            if (!isNaN(Number(token))) {
                return new Const(token);
            } else if (variables.includes(token)) {
                return new Variable(token);
            } else if (token === '(') {
                return parseOperation();
            } else {
                throw new UnexpectedSymbolError(token, tokenizer.getLastPosition());
            }
        }

        let result = parsePrimary();
        if (tokenizer.hasNext()) {
            throw new UnexpectedSymbolError(tokenizer.next(), tokenizer.getLastPosition());
        }
        return result;
    }

    function parsePrefix(pn) {
        let tokenizer = new Tokenizer(pn);

        function parseOperation() {
            let token = tokenizer.next();

            if (operations.hasOwnProperty(token)) {
                let count = operations[token].operandsCount;
                let operands = [];
                while (operands.length < count || (count === -1 && tokenizer.peek() !== ')')) {
                    operands.push(parsePrimary());
                }
                let rb = tokenizer.next();
                if (rb !== ')') {
                    throw new UnexpectedSymbolError(rb, tokenizer.getLastPosition());
                }
                return new operations[token](...operands);
            } else {
                throw new UnknownOperationError(token, tokenizer.getLastPosition());
            }
        }

        function parsePrimary() {
            let token = tokenizer.next();

            if (!isNaN(Number(token))) {
                return new Const(token);
            } else if (variables.includes(token)) {
                return new Variable(token);
            } else if (token === '(') {
                return parseOperation();
            } else {
                throw new UnexpectedSymbolError(token, tokenizer.getLastPosition());
            }
        }

        let result = parsePrimary();
        if (tokenizer.hasNext()) {
            throw new UnexpectedSymbolError(tokenizer.next(), tokenizer.getLastPosition());
        }
        return result;
    }

    return Object.values(operations).reduce((exports, operation) => {
        exports[operation.name] = operation;
        return exports;
    }, {
        Const: Const,
        Variable: Variable,
        parse: parse,
        parsePostfix: parsePostfix,
        parsePrefix: parsePrefix
    });
})();
