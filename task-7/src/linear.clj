(defn v+ [& xs] (apply map + xs))
(defn v- [& xs] (apply map - xs))
(defn v* [& xs] (apply map * xs))
(defn scalar [a b] (reduce + (v* a b)))

(defn m+ [& xs] (apply map v+ xs))
(defn m- [& xs] (apply map v- xs))
(defn m* [& xs] (apply map v* xs))