package lectures.part1Advanced

object AdvancedPatternMatching extends App{
  val numbers = List(1)
  val description = numbers match {
    case head:: Nil => println(s"the only element is $head")
    case _ =>
  }
  /*
  -constants
  -wildcards
  -case classes
  -tuples
  -some special magic
   */
  class Person(val name: String, val age: Int)

  object Person{
    def unapply(arg: Person): Option[(String, Int)] = Some((arg.name, arg.age))
  }

  val bob = new Person("Bob",33)
  val greeting = bob match {
    case Person(n,a) => s"I'm $n and I'm $a"
  }
  //it's not case class but pattern matching still works because of unapply
/*
Exercises.
 */
  val n: Int = 43
  val mathProperty = n match {
    case Even() => "I'm even"
    case BiggerThan5(_) => "Bigger than 5"
  }

  print(mathProperty)
      object Even {
        def unapply(n: Int): Boolean = n %2 == 0// that's better  than biggerThan5 implementation

      }
      object BiggerThan5 {
        def unapply(n: Int): Option[Boolean] =
          if(n > 5) Some(true)
          else None
      }




}
