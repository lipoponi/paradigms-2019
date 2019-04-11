(defn constant [c] (constantly c))
(defn variable [name] (fn [values] (values name)))
(defn operation [op] (fn [& xs] (fn [vars] (apply op (map #(% vars) xs)))))

(def add (operation +))
(def subtract (operation -))
(def multiply (operation *))
(def divide (operation (fn [a b] (/ (double a) b))))
(def negate (operation -))

(def operations {'+ add '- subtract '* multiply '/ divide 'negate negate})


(defn parse [unit] (cond
                     (number? unit) (constant unit)
                     (symbol? unit) (variable (str unit))
                     (list? unit) (apply (operations (first unit)) (map parse (rest unit)))))
(defn parseFunction [expression] (parse (read-string expression)))