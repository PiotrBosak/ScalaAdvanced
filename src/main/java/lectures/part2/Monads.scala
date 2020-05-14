package lectures.part2

object Monads extends App {

  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]

  }

  object Attempt {
    def apply[A](a: => A): Attempt[A] =
      try {
        Success(a)
      }
      catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Success[+A](value: A) extends Attempt[A] {
    override def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try {
        f(value)
      }
    catch {
      case e: Throwable => Fail(e)
    }
  }

  case class Fail(exc: Throwable) extends Attempt[Nothing] {
    override def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] =
      this
  }

  //left
  //unit.flatMap(f) = f(x)
  //Attempt(x).flatMap(f) = f(x)
  //success case
  // Success(x).flatMap(f) = f(value)
  //2.right
  //Attempt(v).flatMap(x => Attempt(x)) ==  Attempt(v)
  //Success(v).flatMap(x => Attempt(x) == Success(v)
  //associativity
  //Attempt.flatMap(f).flatMap(g) == Attempt.flatMap(x => f(x).flatMap(g))
  //Fail.flatMap.flatMap == Fail
  //Fail.flatMap(...) == Fail
  //Success.flatMap(f).flatMap(g) == f(value).flatMap(g) == g(f(value))
  //Success.flatMap(x => f(x).flatMap(g)) == Success.flatMap(x = > g(f(value)) = g(f(value))//in case of no fails
  //Success.flatMap(fail).flatMap(g) == fail.flatMap(g) == fail
  //Success.flatMap(x => fail(x).flatMap(g)) == Success.flatMap(fail) == fail
  //Success.flatMap(f).flatMap(fail) == f(x).flatMap(fail) = fail
  //Success.flatMap(x => f(x).flatMap(fail) == fail



//lazy[T] monad
  //Monads = unit + flatmap
  //Monad = unit + map + flatten

   class LazyMonad[+A](v: => A) {
     private lazy val internalV = v
     def value: A = internalV
    def flatMap[B](f: ( =>A ) => LazyMonad[B]):LazyMonad[B] =
      f(value)
  }
  object LazyMonad {
    def apply[A](a: => A): LazyMonad[A] = new LazyMonad(a)

  }
  val l =  LazyMonad {
    println("diamba dziamba")
    5
  }

  val flatMappedInstance = l.flatMap(x => LazyMonad {
    10* x
  })
  val flatMappedInstance2 = l.flatMap(x => LazyMonad {
    10* x
  })
  flatMappedInstance.value
  flatMappedInstance2.value
  // map and flatten in terms of flatMap
//  map = flatMap(x => unit(f(x)) : Monad[b]
  //flatten: m.flatMap(x : Monad[t] => x
  val mapUsingflatMap = List(1,2,3).flatMap(x => List(x*2))
  val fas = List(List(1,2),List(3,4),List(5,6)).flatMap( x => x)
  val sameAsAbove = List(List(1, 2), List(3, 4), List(5, 6)).flatten




}
