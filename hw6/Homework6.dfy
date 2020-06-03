
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
  case Node(l, r, e) => append( flatten(l), append( Cons(e, Nil), flatten(r) ))
} 


function treeContains<T>(tree:Tree<T>, element:T):bool
{
	match tree
    case Leaf => false
    case Node(l, r, e) => treeContains(l, element) || treeContains(r, element) || (e==element)
}



ghost method listContainsAssociative<T>(xs:List<T>,ys:List<T>,element:T)
ensures listContains(append(xs,ys), element) == ( listContains(xs, element) || listContains(ys, element))
{
    match(xs)

    case Nil => listContains(ys,element)
    case Cons(x, xs') => {
        calc {
            //listContains(append(xs,ys), element);
            //== listContains( Cons(x, append(xs', ys)), element);
            //     case Cons(x, xs') => Cons(x, append(xs', ys))

            //== (x==element) || listContains(xs', element) || listContains(ys,element);
        }
    }

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
                // definition of treeContains: case Node(l, r, e) => treeContains(l, element) || treeContains(r, element) || (e==element)

            == listContains(flatten(l),element) || listContains(flatten(r), element)||  e==element;

            == listContains(flatten(l),element) || listContains(Cons( e, flatten(r)), element)  ;
                //   definition of list-contains:  case Cons(x, xs') => (x==element) || listContains(xs', element)

            == listContains(flatten(l),element) || listContains(Cons( e, flatten(r)), element) ;

                        //== listContains(Cons(e, flatten(l)),element)  || listContains(Cons( e, flatten(r)), element) ;
            
            == listContains(append(flatten(l), Nil),element) || listContains( append(Cons( e, flatten(r)), Nil), element) ;

            == listContains(append(flatten(l), Nil),element) || listContains( append(Cons(e, Nil), flatten(r)), element) ;
            
            == listContains(flatten(l),element) || listContains( append(Cons(e, Nil), flatten(r)), element) ;


            // append( flatten(l), append( flatten(r), Cons(e, Nil) ))

           // == listContains( append( flatten(l), append(flatten(r), Cons(e, Nil) ) ) , element);
            // == listContains ( flatten(l), element) || listContains( flatten(r), element) || listContains(Cons(e, Nil) , element) ;

            // == listContains(append(flatten(l), flatten(r)), element) || listContains(Cons(e, Nil) , element) ;

            == listContains ( append( flatten(l), append( Cons(e, Nil),flatten(r))), element);
            == listContains(flatten(tree), element);

        }


        // calc{
        //     listContains(flatten(tree), element);
        //     == listContains ( append( flatten(l), append( Cons(e, Nil),flatten(r))), element);
        // }
    }

    }
}
