
datatype Tree<T> = Leaf | Node(Tree<T>, Tree<T>, T)

datatype List<T> = Nil | Cons(T, List<T>)


function append<T>(xs:List<T>, ys:List<T>):List<T>
ensures xs == Nil ==> append(xs,ys) == ys
ensures ys == Nil ==> append(xs,ys) == xs
ensures length(append(xs,ys)) == length(xs)  +  length(ys)
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



function listContains<T>(xs:List<T>, element:T):bool
// requires element ==  Nil
{
    match xs
    case Nil => false
    case Cons(x, xs') => (x==element) || listContains(xs', element)
	
}


function flatten<T>(tree:Tree<T>):List<T>
{
  match tree
  case Leaf => Nil
  case Node(l, r, e) => append( flatten(l), append( flatten(r), Cons(e, Nil) ))
} 


function treeContains<T>(tree:Tree<T>, element:T):bool
{
	match tree
    case Leaf => false
    case Node(l, r, e) => treeContains(l, element) || treeContains(r, element) || (e==element)
}


lemma sameElements<T>(tree:Tree<T>, element:T)
ensures treeContains(tree, element) <==> listContains(flatten(tree), element)
{
	match (tree){
    case Leaf => {}
    case Node(l, r, e) => {
        calc{
            treeContains(tree, element); 
            == treeContains( Node(l,r,e), element); 
            == treeContains(l, element) || treeContains(r, element) || e==element;
            == listContains(flatten(l),element) || listContains(flatten(r), element) ||  e==element ;

            // listContains(Cons(e, Nil) , element) ||
           // == listContains( append( flatten(l), append(flatten(r), Cons(e, Nil) ) ) , element);
            == listContains ( flatten(l), element) || listContains( flatten(r), element) || listContains(Cons(e, Nil) , element) ;

            == listContains ( flatten(l), element) || listContains( append( flatten(r), Cons(e, Nil) ), element);

            == listContains ( append( flatten(l), append( flatten(r), Cons(e, Nil) )), element);
            == listContains(flatten(tree), element);

        }
    }

    }
}
