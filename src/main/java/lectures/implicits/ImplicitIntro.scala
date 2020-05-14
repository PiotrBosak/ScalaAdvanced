package lectures.implicits

object ImplicitIntro extends App{
var pair = "Daniel" -> "555"
  val intPair = 1 -> 2
  case class Person(name: String) {
    def greet = s"Hi I'm $name"
  }
  implicit  def fromStringtoPerson(str: String): Person = Person(str)
  println("Peter".greet)

  def increment(x: Int)(implicit  y: Int) = x+y
  implicit val amount: Int = 10
  println(increment(5))//not the same as default parameter value
}
