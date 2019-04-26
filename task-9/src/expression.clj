(defprotocol IExpression
  (^Number evaluate [this vars])
  (^String toString [this])
  (^IExpression diff [this variable]))


(deftype Constant [^Number x]
  IExpression
  (evaluate [this vars] x)
  (toString [this] (str x))
  (diff [this variable] (Constant. 0)))

(deftype Variable [^String name]
  IExpression
  (evaluate [this vars] (vars name))
  (toString [this] name)
  (diff [this variable] (Constant. (if (= name variable) 1 0))))


(defn evaluate [expression vars] (.evaluate expression vars))
(defn toString [expression] (.toString expression))
(defn diff [expression variable] (.diff expression variable))


(println (evaluate (diff (Variable. "x") "y") {}))