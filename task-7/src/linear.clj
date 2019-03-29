(defn v+ [& xs] (apply mapv + xs))
(defn v- [& xs] (apply mapv - xs))
(defn v* [& xs] (apply mapv * xs))
(defn scalar [a b] (reduce + (v* a b)))

(defn m+ [& xs] (apply mapv v+ xs))
(defn m- [& xs] (apply mapv v- xs))
(defn m* [& xs] (apply mapv v* xs))