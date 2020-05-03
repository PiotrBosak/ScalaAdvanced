package lectures

object DarkSugars extends App{
  //syntax sugar #1: methods with single param
  def singleArgMethod(arg: Int): String = "faj;"

  val description = singleArgMethod{
    5+4
  }
  val desc = singleArgMethod(5+5)
  List(1,2,3).map{
    x => x+5
  }
  //syntax sugar #2: single abstract method pattern
  trait Action  {
    def act(x: Int): Int
  }

  val anAction: Action = x => 5//like functional interface
  //example : Runnable
  val aThread:Thread = new Thread(() => print("something"))
  //also works for abstract classes with just 1 abstract method
  abstract class AnAbstractClass {
    def implemented: Int = 3;
    def unImplemented(x: Int): Int
  }
  val a: AnAbstractClass = x => x

  // syntax sugar #3: the :: and #:: methods

  val prependedList = 2 :: List(3,4)
  println(prependedList)
  //List(3,4).::(2)

  //scala specification: last character decides associativity of method
  1 :: 2:: 3:: 4 :: List(5)
  //===
  List(5).::(4).::(3).::(2).::(1)
  class MyStream[T]{
    def -->:(value : T) : MyStream[T] = this
  }
  val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int]
  // if operator ends with a colon, it means its right associative

  // syntax sugar #4: multiword method naming

  class TeenGirl(name:String){
    def `and then said`(x:String) = s"$name said $x"
  }

  val teen = new TeenGirl("Ann")
  print(teen.`and then said`("something"))
  //syntax sugar #5: infix types
  class Composite[A,B]
//  val composite: Composite[Int,String] = ???
//  val composite1: Int Composite String = ???
  class -->[A,B]
//  val towards: Int --> String = ???

  //syntax sugar #6: update method
  //special method, like apply
  val anArray = Array(1,2,3)
  anArray(2) = 7// rewritten to anArray.update(2,7)
  //used in mutible collections
  //syntax sugar: 7 setters for mutable containers

  class Mutable {
    private var internalMember: Int = 0
    def member = internalMember//getter
    def member_=(value: Int): Unit = internalMember = value//setter

  }
  val aMutable = new Mutable
  aMutable.member = 42// rewritten as aMutable.member_=(42)

}
