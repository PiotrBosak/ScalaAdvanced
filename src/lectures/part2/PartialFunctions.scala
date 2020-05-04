package lectures.part2

import java.util.Scanner

object PartialFunctions extends App{
  val aFunction = (x: Int) => x+1
  val aFussyFunction = (x:Int) => x match {
    case 1 => 4
  }

  val aPartialFunction: PartialFunction[Int,Int] = {
    case 5 => 5
  }
  println(aPartialFunction.isDefinedAt(5))
  //lift
  val liftedFunction = aPartialFunction.lift
  val chain = aPartialFunction.orElse[Int,Int] {
    case 45 => 5
    case d: Int if d  > 5 => 65
  }
  print(chain(9999))
  //partial functions extend normal function

  /*
  Note: partial functons can only have  1 paramter type

   */
  /**
  exercises
   1.construct a pf instance yourself
   2. chatbot as a PF
   **/
val apartialFunction = new PartialFunction[Int,Int] {
  override def isDefinedAt(x: Int): Boolean =  x ==5 && x == 6

  override def apply(v1: Int): Int =
    if(isDefinedAt(v1)) v1
    else throw new RuntimeException
}
  val dumbChatBot :  PartialFunction[String,String] = {
    case "How are you" => "I'm fine thanks"
    case _=> "I didn't understand the command, try again"
  }
  val scanner = new Scanner(System.in)

  scala.io.Source.stdin.getLines().foreach(line => println(dumbChatBot(line)))
  scala.io.Source.stdin.getLines().map(dumbChatBot).foreach(println)





}
