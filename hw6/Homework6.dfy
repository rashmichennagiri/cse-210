
datatype Tree<T> = Leaf(T) | Node(Tree<T>, Tree<T>, T)

datatype List<T> = Nil | Cons(T, List<T>)


/*unction flatten<T>(tree:Tree<T>):List<T>
{
  match tree
  case Leaf(x) => x
  case Node(l, r, T) => append( flatten(l), flatten(r) )
}   */

function append<T>(xs:List<T>, ys:List<T>):List<T>
ensures xs == Nil ==> append(xs,ys) == ys
ensures ys == Nil ==> append(xs,ys) == xs
ensures length(append(xs,ys)) == length(xs)  +  length(ys)
  // decreases xs
  // ignore blue lines
{
    match xs
    case Nil => ys
    case Cons(x, xs') => Cons(x, append(xs', ys))
}

function length<T>(xs:List<T>) : int
{
    match xs
    case Nil => 0
    case Cons(x,xs') => 1 + length(xs')
}

function treeContains<T>(tree:Tree<T>, element:T):bool
{
	match tree
    case Leaf(x) => (x==element)
    case Node(l, r, T) => treeContains(l, element) || treeContains(r, element)
}

function listContains<T>(xs:List<T>, element:T):bool
{
    match xs
    case Nil => false
    case Cons(x, xs') => (x==element) || listContains(xs', element)
	
}

/*
lemma sameElements<T>(tree:Tree<T>, element:T)
ensures treeContains(tree, element) <==> listContains(flatten(tree), element)
{
	
}



method Main()
{ 
    var q := append({1,2}, {3,4});
    assert q == List(){1,2, 3,4};
    
}

*/