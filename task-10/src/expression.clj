(defn -return [value tail] {:value value :tail tail})
(def -valid? boolean)
(def -value :value)
(def -tail :tail)


;-----


(defn _empty [value] (partial -return value))
(defn _char [p]
  (fn [[c & cs]]
    (if (and c (p c)) (-return c cs))))
(defn _map [f]
  (fn [result]
    (if (-valid? result)
      (-return (f (-value result)) (-tail result)))))
(defn _combine [f a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar)
        ((_map (partial f (-value ar))) ((force b) (-tail ar)))))))
(defn _either [a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar) ar ((force b) str)))))
(defn _parser [p]
  (fn [input]
    (-value ((_combine (fn [v _] v) p (_char #{\u0000})) (str input \u0000)))))


;-----


(defn +char [chars] (_char (set chars)))
(defn +char-not [chars] (_char (comp not (set chars))))
(defn +map [f parser] (comp (_map f) parser))
(def +parser _parser)
(def +ignore (partial +map (constantly 'ignore)))

(defn iconj [coll value]
  (if (= value 'ignore) coll (conj coll value)))
(defn +seq [& ps]
  (reduce (partial _combine iconj) (_empty []) ps))
(defn +seqf [f & ps] (+map (partial apply f) (apply +seq ps)))
(defn +seqn [n & ps] (apply +seqf (fn [& vs] (nth vs n)) ps))

(defn +or [p & ps]
  (reduce (partial _either) p ps))
(defn +opt [p]
  (+or p (_empty 'ignore)))
(defn +star [p]
  (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))] (rec)))
(defn +plus [p] (+seqf cons p (+star p)))
(defn +str [p] (+map (partial apply str) p))
(defn +string [chars] (+str (apply +seq (map #(+char %) (map str (seq chars))))))


;-----


(defn get-field [object key]
  (cond
    (contains? object key) (object key)
    (contains? object :prototype) (get-field (object :prototype) key)))
(defn call-method [object key & args] (apply (get-field object key) (cons object args)))



(declare Constant Variable Add Subtract Multiply Divide Negate Pow Log And Or Xor)

(defn evaluate [expression mapping] (call-method expression :evaluate mapping))
(defn toString [expression] (call-method expression :toString))
(defn toStringSuffix [expression] (call-method expression :toStringSuffix))
(defn toStringInfix [expression] (call-method expression :toStringInfix))
(defn diff [expression diff-variable] (call-method expression :diff diff-variable))


(def Constant:prototype
  {:evaluate       (fn [this _] (get-field this :value))
   :toString       (fn [this] (format "%.1f" (get-field this :value)))
   :toStringSuffix toString
   :toStringInfix  toString
   :diff           (fn [_ _] (Constant 0))})
(defn Constant [^Number value]
  {:prototype Constant:prototype
   :value     (double value)})

(def Variable:prototype
  {:evaluate       (fn [this mapping] (mapping (clojure.string/lower-case (subs (get-field this :name) 0 1))))
   :toString       (fn [this] (get-field this :name))
   :toStringSuffix toString
   :toStringInfix  toString
   :diff           (fn [this diff-variable] (Constant (if (= diff-variable (get-field this :name)) 1 0)))})
(defn Variable [^String name]
  {:prototype Variable:prototype
   :name      name})

(def Operation:prototype
  {:evaluate-inners       (fn [this mapping] (mapv #(evaluate % mapping) (get-field this :inners)))
   :evaluate              (fn [this mapping] (apply (get-field this :perform) (call-method this :evaluate-inners mapping)))
   :toString-inners       (fn [this] (clojure.string/join " " (mapv #(toString %) (get-field this :inners))))
   :toString              (fn [this] (str "(" (get-field this :represent) " " (call-method this :toString-inners) ")"))
   :toStringSuffix-inners (fn [this] (clojure.string/join " " (mapv #(toStringSuffix %) (get-field this :inners))))
   :toStringSuffix        (fn [this] (str "(" (call-method this :toStringSuffix-inners) " " (get-field this :represent) ")"))
   :toStringInfix         (fn [this] (str
                                       "("
                                       (toStringInfix (nth (get-field this :inners) 0))
                                       " "
                                       (get-field this :represent)
                                       " "
                                       (toStringInfix (nth (get-field this :inners) 1))
                                       ")"))
   :diff-inners           (fn [this diff-variable] (mapv #(diff % diff-variable) (get-field this :inners)))})
(defn Operation [custom]
  (fn [& inners]
    (merge custom
           {:prototype Operation:prototype
            :inners    inners})))

(def Add
  (Operation
    {:perform   +
     :represent "+"
     :diff      (fn [this diff-variable]
                  (apply Add (call-method this :diff-inners diff-variable)))}))
(def Subtract
  (Operation
    {:perform   -
     :represent "-"
     :diff      (fn [this diff-variable]
                  (apply Subtract (call-method this :diff-inners diff-variable)))}))
(def Multiply
  (Operation
    {:perform   *
     :represent "*"
     :diff      (fn [this diff-variable]
                  (let [inners (get-field this :inners)
                        diff-inners (call-method this :diff-inners diff-variable)]
                    (Add
                      (Multiply (nth inners 0) (nth diff-inners 1))
                      (Multiply (nth diff-inners 0) (nth inners 1)))))}))
(def Divide
  (Operation
    {:perform   (fn [a & xs] (/ (double a) (apply * xs)))
     :represent "/"
     :diff      (fn [this diff-variable]
                  (let [inners (get-field this :inners)
                        diff-inners (call-method this :diff-inners diff-variable)]
                    (Divide
                      (Subtract (Multiply (nth diff-inners 0) (nth inners 1)) (Multiply (nth inners 0) (nth diff-inners 1)))
                      (Multiply (nth inners 1) (nth inners 1)))))}))
(def Negate
  (Operation
    {:perform       -
     :represent     "negate"
     :diff          (fn [this diff-variable] (apply Negate (call-method this :diff-inners diff-variable)))
     :toStringInfix (fn [this] (str "negate(" (toStringInfix (nth (get-field this :inners) 0)) ")"))}))
(def Pow
  (Operation
    {:perform   #(Math/pow %1 %2)
     :represent "**"}))
(def Log
  (Operation
    {:perform   #(/ (Math/log (Math/abs %2)) (Math/log (Math/abs %1)))
     :represent "//"}))
(def And
  (Operation
    {:perform   #(Double/longBitsToDouble (bit-and (Double/doubleToLongBits %1) (Double/doubleToLongBits %2)))
     :represent "&"}))
(def Or
  (Operation
    {:perform   #(Double/longBitsToDouble (bit-or (Double/doubleToLongBits %1) (Double/doubleToLongBits %2)))
     :represent "|"}))
(def Xor
  (Operation
    {:perform   #(Double/longBitsToDouble (bit-xor (Double/doubleToLongBits %1) (Double/doubleToLongBits %2)))
     :represent "^"}))



(def operations {'+            Add,
                 '-            Subtract,
                 '*            Multiply,
                 '/            Divide,
                 'negate       Negate,
                 '**           Pow,
                 (symbol "//") Log,
                 '&            And,
                 '|            Or,
                 (symbol "^")  Xor})

(defn parseObject [unit] (cond
                           (string? unit) (parseObject (read-string unit))
                           (number? unit) (Constant unit)
                           (symbol? unit) (Variable (str unit))
                           (list? unit) (apply (operations (first unit)) (map parseObject (rest unit)))))


;-----


(declare *suffix-value)
(def *digit (+char "0123456789"))
(def *variable-letter (+char "XYZxyz"))
(def *const (+map #(Constant (read-string %)) (+seqf str (+opt (+char "+-")) (+str (+plus *digit)) (+opt (+seqf str (+char ".") (+str (+plus *digit)))))))
(def *variable (+map #(Variable %) (+str (+plus *variable-letter))))
(def *whitespace (+ignore (+star (+char " "))))
(def *operator (+map #(symbol (str %)) (+or (+string "**")
                                            (+string "//")
                                            (+char "+-*/&|^")
                                            (+string "negate"))))
(def *list (+map #(apply (operations (nth % 1)) (nth % 0))
                 (+seq (+ignore (+char "("))
                       (+plus (+seqn 0 (delay *suffix-value)))
                       *whitespace
                       *operator
                       *whitespace
                       (+ignore (+char ")")))))
(def *suffix-value (+seqn 0 *whitespace (+or *const *variable *list)))

(defn parseObjectSuffix [expression] ((*suffix-value expression) :value))


;-----


(declare *infix-value)
(defn *read-left [*op *next]
  (+map #(reduce (fn [a b] ((operations (nth b 0)) a (nth b 1))) (first %) (last %))
        (+seq *whitespace *next (+star (+seq *whitespace *op *whitespace *next)))))
(defn *read-right [*op *next]
  (+map #(reduce (fn [a b] ((operations (nth b 1)) (nth b 0) a)) (first %) (reverse (last %)))
        (+map #(reverse %) (+seq (+star (+seq *whitespace *next *whitespace *op)) *whitespace *next))))

(defn *op-level [*read ops *next]
  (let [*op (+map #(symbol (str %)) (apply +or (map #(+string %) ops)))] (*read *op *next)))


(def *wrapped (+seqn 0 (+ignore (+char "(")) *whitespace (delay *infix-value) *whitespace (+ignore (+char ")"))))
(def *primary (+or *wrapped *const *variable))

(def *negate (+or *primary (+map #(Negate %) (+seqn 1 (+string "negate") *whitespace (delay *negate)))))
(def *PowLog (*op-level *read-right ["**" "//"] *negate))
(def *MulDiv (*op-level *read-left ["*" "/"] *PowLog))
(def *AddSub (*op-level *read-left ["+" "-"] *MulDiv))
(def *And (*op-level *read-left ["&"] *AddSub))
(def *Or (*op-level *read-left ["|"] *And))
(def *Xor (*op-level *read-left ["^"] *Or))

(def *infix-value (+seqn 0 *whitespace *Xor))
(defn parseObjectInfix [str] ((*infix-value str) :value))
