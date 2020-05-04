package lectures.part1Advanced

object AdvancedPatternMatching extends App {
  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => println(s"the only element is $head")
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

  object Person {
    def unapply(arg: Person): Option[(String, Int)] = Some((arg.name, arg.age))
  }

  val bob = new Person("Bob", 33)
  val greeting = bob match {
    case Person(n, a) => s"I'm $n and I'm $a"
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
    def unapply(n: Int): Boolean = n % 2 == 0 // that's better  than biggerThan5 implementation

  }

  object BiggerThan5 {
    def unapply(n: Int): Option[Boolean] =
      if (n > 5) Some(true)
      else None
  }

  case class Or[A, B](a: A, b: B) //either
  val either = Or(2, "two")
  val humanDesc = either match {
    case number Or string => s"$number as $string" // instead of Or(number,string)


  }
  //decomposing sequences

  val vararg = numbers match {
    case List(1, _*) => "list with 1 at the beginning"

  }

  abstract class MyList[+A] {
    def head: A = ???

    def tail: MyList[A] = ???
  }

  case object Empty extends MyList[Nothing]

  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }

  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
  val decomposed = myList match {
    case MyList(1, 2, _*) => "starting with 1 and 2"
    case _ => "something else"
  }

  //custom return types for unapply
  //isEmpty: Boolean, get:something

  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }
  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] =
      new Wrapper[String] {
        override def get: String = s"${person.name} is ${person.age}"

        override def isEmpty: Boolean = false
      }
  }

    val newPerson = new Person("Bob",43)
    val personMatch: String = newPerson match {
      case PersonWrapper(n) => s"$n"
      case _=> "something else"
    }
    println(personMatch)





}
