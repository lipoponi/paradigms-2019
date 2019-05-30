find((X, Y), [(X, Y) | _]).
find((X, Y), [(Key, Value) | Tail]) :- Key < X, find((X, Y), Tail).

remove([(X, Y) | Tail], Tail, (X, Y)).
remove([(Key, Value) | Tail1], [(Key, Value) | Tail2], (X, Y)) :- remove(Tail1, Tail2, (X, Y)).

merge(List, [], List).
merge([(Key1, Value1) | Tail1], [(Key2, Value2) | Tail2], [(Key1, Value1) | TailResult]) :- Key1 < Key2, !, merge(Tail1, [(Key2, Value2) | Tail2], TailResult).
merge(List1, [(Key2, Value2) | Tail2], [(Key2, Value2) | TailResult]) :- merge(List1, Tail2, TailResult).

eq(ListMap, Key, Value, Result) :-
    find((Key, _), ListMap) -> ( remove(ListMap, List, (Key, _)), merge([(Key, Value)], List, Result), ! ); merge([(Key, Value)], ListMap, Result).

map_get(ListMap, Key, Value) :-
	find((Key, Value), ListMap).

map_put(ListMap, Key, Value, Result) :-
	(length(ListMap, Length), Length > 0) -> ( eq(ListMap, Key, Value, Result), ! ); merge([(Key, Value)], [], Result).

map_remove(ListMap, Key, Result) :-
	find((Key, _), ListMap) -> ( remove(ListMap, Result, (Key, _)), ! ); Result = ListMap.

map_replace(ListMap, Key, Value, Result) :- find((Key, _), ListMap) -> ( remove(ListMap, List, (Key, _)), merge([(Key, Value)], List, Result), ! ); Result = ListMap.