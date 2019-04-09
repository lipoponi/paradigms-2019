(declare v+ v- v* scalar vect v*s)
(declare m+ m- m* m*s m*v m*m transpose)
(declare s+ s- s*)
(declare t+ t- t*)
(declare depth same-depth? same-count?)
(declare vect? matrix? tensor?)


(defn depth [v] {:pre [(or (number? v) (and (sequential? v) (apply same-depth? v)))]}
  (if (number? v) 0 (+ 1 (depth (nth v 0)))))
(defn same-depth? [& v] (apply = (mapv depth v)))
(defn same-count? [& v]
  (cond
    (every? number? v) true
    (every? sequential? v) (apply = (mapv count v))
    :else false))


(defn tensor? [t]
  (cond
    (or (number? t) (every? number? t)) true
    (every? sequential? t) (and (apply same-count? t) (tensor? (into [] (apply concat t))))
    :else false))
(defn vect? [v] (and (tensor? v) (= 1 (depth v))))
(defn matrix? [m] (and (tensor? m) (= 2 (depth m))))


(defn vv [op] (fn [& xs] {:pre [(every? vect? xs) (apply same-count? xs)]} (apply mapv op xs)))
(def v+ (vv +))
(def v- (vv -))
(def v* (vv *))
(defn scalar [a b] {:pre [(vect? a) (vect? b) (same-count? a b)]} (reduce + (v* a b)))
(defn vect
  ([a] (vec a))
  ([a b] {:pre [(vect? a) (vect? b) (= 3 (count a) (count b))]} [(- (* (nth a 1) (nth b 2)) (* (nth a 2) (nth b 1)))
                                                                 (- (* (nth a 2) (nth b 0)) (* (nth a 0) (nth b 2)))
                                                                 (- (* (nth a 0) (nth b 1)) (* (nth a 1) (nth b 0)))])
  ([a b & xs] (reduce vect (vect a b) xs)))
(defn v*s [v & xs] {:pre [(vect? v) (every? number? xs)]} (mapv #(* % (reduce * xs)) v))


(defn mm [op] (fn [& xs] {:pre [(every? matrix? xs) (apply same-count? xs)]} (apply mapv (vv op) xs)))
(def m+ (mm +))
(def m- (mm -))
(def m* (mm *))
(defn m*s [m & xs] {:pre [(matrix? m) (every? number? xs)]} (mapv #(apply v*s % xs) m))
(defn m*v [m v] {:pre [(matrix? m) (vect? v) (= (count (nth m 0)) (count v))]} (mapv #(scalar v %) m))
(defn m*m
  ([a] a)
  ([a b] {:pre [(matrix? a) (matrix? b) (same-count? (nth a 0) b)]} (mapv #(m*v (transpose b) %) a))
  ([a b & xs] (reduce m*m (m*m a b) xs)))
(defn transpose [m] {:pre [(matrix? m)]} (apply mapv vector m))


(defn shapeless [op] (fn f [& xs] (if (every? number? xs) (apply op xs) (apply mapv f xs))))
(def s+ (shapeless +))
(def s* (shapeless *))
(def s- (shapeless -))


(defn tensor [op] (fn f [& xs] {:pre [(tensor? xs)]} (if (every? number? xs) (apply op xs) (apply mapv f xs))))
(def t+ (tensor +))
(def t* (tensor *))
(def t- (tensor -))