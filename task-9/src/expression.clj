(defn get-field [object key]
  (cond
    (contains? object key) (object key)
    (contains? object :prototype) (get-field (object :prototype) key)))
(defn call-method [object key & args] (apply (get-field object key) (cons object args)))



(declare Constant Variable Add Subtract Multiply Divide Negate)

(defn evaluate [expression mapping] (call-method expression :evaluate mapping))
(defn toString [expression] (call-method expression :toString))
(defn diff [expression diff-variable] (call-method expression :diff diff-variable))


(def Constant:prototype
  {:evaluate (fn [this _] (get-field this :value))
   :toString (fn [this] (format "%.1f" (get-field this :value)))
   :diff     (fn [_ _] (Constant 0))})
(defn Constant [^Number value]
  {:prototype Constant:prototype
   :value     (double value)})

(def Variable:prototype
  {:evaluate (fn [this mapping] (mapping (get-field this :name)))
   :toString (fn [this] (get-field this :name))
   :diff     (fn [this diff-variable] (Constant (if (= diff-variable (get-field this :name)) 1 0)))})
(defn Variable [^String name]
  {:prototype Variable:prototype
   :name      name})

(def Operation:prototype
  {:evaluate-inners (fn [this mapping] (mapv #(evaluate % mapping) (get-field this :inners)))
   :evaluate        (fn [this mapping] (apply (get-field this :perform) (call-method this :evaluate-inners mapping)))
   :toString-inners (fn [this] (clojure.string/join " " (mapv #(toString %) (get-field this :inners))))
   :toString        (fn [this] (str "(" (get-field this :represent) " " (call-method this :toString-inners) ")"))
   :diff-inners     (fn [this diff-variable] (mapv #(diff % diff-variable) (get-field this :inners)))})
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
    {:perform   -
     :represent "negate"
     :diff      (fn [this diff-variable] (apply Negate (call-method this :diff-inners diff-variable)))}))



(def operations {'+ Add '- Subtract '* Multiply '/ Divide 'negate Negate})

(defn parseObject [unit] (cond
                           (string? unit) (parseObject (read-string unit))
                           (number? unit) (Constant unit)
                           (symbol? unit) (Variable (str unit))
                           (list? unit) (apply (operations (first unit)) (map parseObject (rest unit)))))