package exercises

import scala.annotation.tailrec
import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Random, Success, Try}
import scala.concurrent.duration._

object FutureExercises extends App {
  /*
  inSequence(fa,fb)
   */

  def inSequence(a: Future[Int], b: Future[Int]): Unit =
    a.flatMap(_ => b)

  inSequence(Future(5), Future(10))
  Thread.sleep(1000)


  //first(fa,fb) returns future that returned first
  @tailrec
  def first[A](a: Future[A], b: Future[A]): Future[A] = {
    if (a.isCompleted) a
    else if (b.isCompleted) b
    else first(a, b)
  }

  def differentFirst[A](a: Future[A],b: Future[A]): Future[A] = {
    val promise = Promise[A]
    def tryComplete(promise: Promise[A], result: Try[A]) = result match{
      case Success(v) => try {
        promise.success(v)
      }catch {
        case _ =>
      }
      case Failure(e) => try {
        promise.failure(e)
      }
      catch {
        case _ =>
      }
    }
    a.onComplete(tryComplete(promise,_))
    b.onComplete(promise.tryComplete)
    promise.future
  }


  val a = Future {
    Thread.sleep(1001)
    5
  }
  val b = Future {
    Thread.sleep(1000)
    21
  }
  val result: Unit = first(a, b).onComplete {
    case Success(value) => println(value)
  }

  //last
  def last[A](a: Future[A], b: Future[A]): Future[A] = {
    if (a.isCompleted) b
    else if (b.isCompleted) a
    else last(a, b)
  }

  //
  def retryUntil[T](action: () => Future[T], condition: T => Boolean): Future[T] = {
    action()
      .filter(condition)
      .recoverWith {
        case _ => retryUntil(action,condition)
      }



  }

  val random = new Random
  val cond: Int => Boolean = (x) => x % 10 == 0
  val action: () => Future[Int] = () => Future(random.nextInt)
  val res = retryUntil[Int](action, cond)
  res.onComplete {
    case Success(value) => println("success")
    case Failure(e) => println("got exception")
  }
  Thread.sleep(5000)
}
