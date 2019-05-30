tree_build(L,R) :-
	rnd_permu(L,L1), inl(L1,nil,R).

map_get(T, K, V) :-
	lookup((K,V),T).

map_replace(T, K, V, R) :- tree2list(T, L), replace(L, K, V, LL), length(LL, Len), (Len > 0) -> tree_build(LL, R); R = nil.

map_put(T, K, V, R) :-
	tree2list(T, L), pput(L, K, V, X), rnd_permu(X, Y), tree_build(Y, R).

map_remove(T, K, R) :-
	tree2list(T, L), remove(L, K, X), rnd_permu(X, Y), tree_build(Y, R).


ins((IK, IV), nil, t(IK, IV, nil, nil)).
ins((IK, IV), t(XK, XV, L, R), t(YK, YV, P, Q)) :-
	( 	IK < XK ->  ins((IK, IV), L, U), P = U, (YK, YV) = (XK, XV), Q = R;
    IK > XK ->  ins((IK, IV), R, U), (P, (YK, YV), Q) = (L, (XK, XV), U);
    (P, (YK, YV), Q) = (L, (XK, XV), R) ).

inl([], T, T).
inl([(NK,NV) | Ns], T0, T) :-
	ins((NK, NV), T0, T1), inl(Ns, T1, T).

lookup((K, V), t(K, V,_,_)).
lookup((K, V), t(VK, VV,L,_)) :-
	K < VK, lookup((K, V), L).
lookup((K, V), t(VK, VV,_,R)) :-
	K > VK, lookup((K, V), R).


find((X, Y), [(X, Y) | _]).
find((X, Y), [(Key, Value) | Tail]) :-
	Key < X, find((X, Y), Tail).

delet([(X, Y) | Tail], Tail, (X, Y)).
delet([(Key, Value) | Tail1], [(Key, Value) | Tail2], (X, Y)) :-
	delet(Tail1, Tail2, (X, Y)).

concat(List, [], List).
concat([(Key1, Value1) | Tail1], [(Key2, Value2) | Tail2], [(Key1, Value1) | TailResult]) :-
	Key1 < Key2, !, concat(Tail1, [(Key2, Value2) | Tail2], TailResult).
concat(List1, [(Key2, Value2) | Tail2], [(Key2, Value2) | TailResult]) :-
	concat(List1, Tail2, TailResult).

pput(ListMap, Key, Value, Result) :-
	(length(ListMap, Length), Length > 0) -> ( equil(ListMap, Key, Value, Result), ! ); concat([(Key, Value)], [], Result).

	equil(ListMap, Key, Value, Result) :-
		find((Key, _), ListMap) -> ( delet(ListMap, List, (Key, _)), concat([(Key, Value)], List, Result), ! ); concat([(Key, Value)], ListMap, Result).

remove(ListMap, Key, Result) :-
	find((Key, _), ListMap) -> ( delet(ListMap, Result, (Key, _)), ! ); Result = ListMap.

replace(ListMap, Key, Value, Result) :- find((Key, _), ListMap) -> ( delet(ListMap, List, (Key, _)), concat([(Key, Value)], List, Result), ! ); Result = ListMap.


tree2list(nil, []).
tree2list(t(N1, N2, L, R), X) :-
	tree2list(L, LL), tree2list(R, RR), append(LL, [(N1, N2)], L1), append(L1, RR, X).


remove_at(X, [X | Xs], 1, Xs).
remove_at(X, [Y | Xs], K, [Y | Ys]) :-
	K > 1, K1 is K - 1, remove_at(X, Xs, K1, Ys).

rnd_select(_, 0, []).
rnd_select(Xs, N, [X | Zs]) :-
	N > 0, length(Xs, L), rand_int(L, Res), I is Res + 1, remove_at(X, Xs, I, Ys), N1 is N - 1, rnd_select(Ys, N1, Zs).

rnd_permu(L1, L2) :- length(L1, N), rnd_select(L1, N, L2).