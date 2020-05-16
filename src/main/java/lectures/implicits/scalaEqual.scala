package lectures.implicits

object scalaEqual extends App {

  trait Equal[T] {
    def apply(a: T, b: T) : Boolean
  }
   implicit object NameEquality extends Equal[Int] {
    override def apply(a: Int, b: Int): Boolean = a == b
  }

  object Equal {
    def apply[T](a: T, b: T)(implicit equal: Equal[T]): Boolean  =
      equal(a,b)
  }

  println(Equal(5,3))
  // ad hoc polymorphism
}
