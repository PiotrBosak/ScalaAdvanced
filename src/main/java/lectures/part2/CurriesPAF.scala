package lectures.part2

object CurriesPAF extends App{
    //curried function
  val supperAdder: Int => Int => Int =
    x => y => x+y
  val adder = supperAdder(5)
  val value = adder(7)
  print(value)
  val sth = supperAdder(5)(5)
  def curriedAdder(x: Int)(y: Int): Int = x+y
  val add4: Int => Int = curriedAdder(4)// lifting (eta-expansion)
  //functions != methods
  // partial function applications
  val add5 = curriedAdder(5) _ //_ enforces eta expansion(convert it to Int => Int function
  //Exercise
  val simpleAddFunction = (x: Int, y: Int) => x+y
  def simpleAddMethod(x: Int, y: Int) = x+y
  def curriedAddMethod(x:Int)(y:Int) = x+y
  //add 7: Int => Int = y => 7+y
  val add71 = curriedAddMethod(7) _
  val add72: Int => Int = simpleAddMethod(7,_: Int)
  val add73 = simpleAddFunction.curried(7)
  val add74 = curriedAddMethod(7)(_)
  val add76 = simpleAddFunction(7,_:Int) // y => simpleAddMethod(7,y)
  //underscores are powerful


  def concatenation(a:String, b: String,c:String) = a+b+c
  val insertName = concatenation("Hello", _:String,"how are you")
  // exercises
  //1. process a list of numbers, and return their string representation with different formats
  def curriedFormatter(format:String)(number:Double) = format.format(number)
  @scala.annotation.tailrec
  def printNumbers(numbers: List[Double], formatter: Double =>  String,acc:String):String = {
    if(numbers.isEmpty) acc
    else printNumbers(numbers.tail,formatter, acc + formatter(numbers.head) + " ")
  }

  val firstFormatter = curriedFormatter("%8.6g") _
  val b = printNumbers(List(6,8,9),curriedFormatter("%4.2f"),"")
  val formattedNumbers = printNumbers(List(6,77,5),firstFormatter,"")
  print(formattedNumbers)
  //2
  /*
  2. difference between
  -functions vs methods
  -parameters by name vs 0-lambda
   */
  def byName(n: => Int) = n+1
  def byFunction(f: () => Int) = f()+1

  def method: Int = 42
  def parenthesis(): Int = 42
  /*
  calling byname and byfuntion
  /int
  method
  parenmethod
  lambda
  partially applied functions
   */
  val az = byName(5)
  val bz = byName(method)
  val cz = byName(parenthesis())
  //val dz = byName(curriedAddMethod(5))
  val av = byFunction(() => 5)



}
