package lectures.part1Advanced

object Recap extends App {
   val aCondition: Boolean = false
   val aConditionedVal = if(aCondition) 5 else 4
   //instructions and expressions

   val aCodeBlock = {
      if(aCondition) "Scala"
       5
   }
   //Unit = void
   //functions

   def aFunction(x : String) : Unit = println(x)
   //recursion: stack and tail

   def factorial(n: Int) = {
   @scala.annotation.tailrec
   def factorialAux(n:Int, acc: Int): Int =
      if(n <=1) acc
      else factorialAux(n-1,acc*n)
   factorialAux(n,1)
   }
   //object orientation

   class Animal{

   }

   val a = new Animal
   val b =




}
