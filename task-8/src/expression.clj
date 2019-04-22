(defn constant [c] (constantly c))
(defn variable [name] (fn [values] (values name)))
(defn operation [op] (fn [& xs] (fn [vars] (apply op (map #(% vars) xs)))))

(defn div [a b] (/ (double a) b))
(defn minimum [& xs] (reduce #(min %1 %2) (nth xs 0) xs))
(defn maximum [& xs] (reduce #(max %1 %2) (nth xs 0) xs))

(def add (operation +))
(def subtract (operation -))
(def multiply (operation *))
(def divide (operation div))
(def negate (operation -))
(def min (operation minimum))
(def max (operation maximum))

(def operations {'+ add '- subtract '* multiply '/ divide 'negate negate 'min min 'max max})


(defn parse [unit] (cond
                     (number? unit) (constant unit)
                     (symbol? unit) (variable (str unit))
                     (list? unit) (apply (operations (first unit)) (map parse (rest unit)))))
(defn parseFunction [expression] (parse (read-string expression)))