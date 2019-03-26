"use strict";

const cnst = value => () => Number(value);

const variable = name => (...args) => args['xyz'.indexOf(name)];

const operation = f => (...inners) => (...args) => f(...inners.map(inner => inner(...args)));

const negate = operation(value => -value);

const add = operation((l, r) => l + r);

const subtract = operation((l, r) => l - r);

const multiply = operation((l, r) => l * r);

const divide = operation((l, r) => l / r);

const avg5 = operation((...args) => args.reduce((a, c) => a + c, 0) / 5);

const med3 = operation((...args) => args.sort((a, b) => a - b)[1]);

const abs = operation((x) => Math.abs(x));

const iff = operation((a, b, c) => a >= 0 ? b : c);


const pi = cnst(Math.PI);
const e = cnst(Math.E);
const one = cnst(1);
const two = cnst(2);


const operations = {
    '+': {
        arguments: 2,
        f: add
    },
    '-': {
        arguments: 2,
        f: subtract
    },
    '*': {
        arguments: 2,
        f: multiply
    },
    '/': {
        arguments: 2,
        f: divide
    },
    'negate': {
        arguments: 1,
        f: negate
    },
    'avg5': {
        arguments: 5,
        f: avg5
    },
    'med3': {
        arguments: 3,
        f: med3
    },
    'abs': {
        arguments: 1,
        f: abs
    },
    'iff': {
        arguments: 3,
        f: iff
    }
};

const constants = {
    'pi': pi,
    'e': e,
    'one': one,
    'two': two
};

function parse(rpn) {
    let stack = Array();
    let units = rpn.trim().split(/\s+/);

    units.forEach((unit) =>  {
        let result;

        if (!isNaN(Number(unit))) {
            result = cnst(unit);
        } else if (operations.hasOwnProperty(unit)) {
            let count = operations[unit].arguments;
            let args = stack.splice(stack.length - count, count);
            result = operations[unit].f(...args);
        } else if (constants.hasOwnProperty(unit)) {
            result = constants[unit];
        } else {
            result = variable(unit);
        }

        stack.push(result);
    });

    return stack.pop();
}