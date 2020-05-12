package lectures.part2

object LazyEvaluation extends App{
  //lazy delays evaluation of values
   lazy val x: Int = throw new RuntimeException// doesnt crash, becasue it is not used
  val y = {
    println("y")
    43
  }
  //examples
  def sideEffectCondition: Boolean = {
    println("foo")
    true
  }

  def simpleDivision: Boolean = false

  lazy val lazyCon = sideEffectCondition
  println(if(simpleDivision && lazyCon) "yes" else "no")

  // in conjunction with call byName
  def byNameMethod(n: => Int): Int ={
    lazy val t = n
    t+t+t+1
  }
  def fun = {
    Thread.sleep(1000)
    43
  }

  println(byNameMethod(fun))
  //better idea to use lazy vals
  //CALL BY NEED > call by name(we still sometimes use call by name

  //filtering with lazy vals
  def lessThan30(n: Int): Boolean = {
    println(s"$n smaller")
    n < 30
  }
  def greaterThan20(n: Int): Boolean = {
    println(s"$n greater")
    n > 20
  }
  val numbers = List(1,25,40,5)
  val lt30 = numbers.filter(lessThan30)
  val gt20 = lt30.filter(greaterThan20)

  val lt30lazy = numbers.withFilter(lessThan30)
  val gt20Lazy = lt30lazy.withFilter(greaterThan20)
  println(gt20Lazy)// it still doesnt evaluate what's inside the collection
  // for comprehensions use withFilter
  for {
    a <- List(1,2,3) if a%2 == 0// lazy vals used
  }yield a+1

  //===
  List(1,2,3).withFilter(_ %2 == 0).map(_+1)

  /*
  exercises
  implement a lazily evaluated, singly linked stream of elements
   */




}
