package exercises

import scala.annotation.tailrec

abstract class MyStream[+A]{
  def isEmpty: Boolean
  def head: A
  def tail: MyStream[A]
  def #::[B >: A](element: B) : MyStream[B] // prepend
  def ++[B >:A](anotherStream: => MyStream[B]): MyStream[B]
  def foreach(f: A => Unit): Unit
  def map[B](f: A => B): MyStream[B]
  def flatMap[B](f: A => MyStream[B]): MyStream[B]
  def filter(predicate:  A => Boolean): MyStream[A]
  def take(n:Int): MyStream[A] // take first n elements
  def takeAsList(n: Int):List[A]
  @tailrec
  final def toList[B >:A](acc: List[B] = Nil): List[B] = {
    if(isEmpty) acc.reverse// we prepend it so we change the order
    else tail.toList(head :: acc)
  }
}
object EmptyStream extends MyStream[Nothing]{
  def isEmpty: Boolean = true
  def head: Nothing = throw new IllegalArgumentException
  def tail: MyStream[Nothing] = throw new IllegalArgumentException

  override def #::[B >: Nothing](element: B): MyStream[B] = new NonEmptyStream[B](element,EmptyStream)

  override def ++[B >: Nothing](anotherStream:  => MyStream[B]): MyStream[B] = anotherStream

  override def foreach(f: Nothing => Unit): Unit = ()

  override def map[B](f: Nothing => B): MyStream[B] = this

  override def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] = this

  override def filter(predicate: Nothing => Boolean): MyStream[Nothing] = this

  override def take(n: Int): MyStream[Nothing] = this

  override def takeAsList(n: Int): List[Nothing] = Nil
}
class NonEmptyStream[+A](h: A,  t: => MyStream[A]) extends MyStream[A]{
  override def isEmpty: Boolean = false

  override val head: A = h

  override lazy val tail: MyStream[A] = t// call by need

  override def #::[B >: A](element: B): MyStream[B] = new NonEmptyStream[B](element,this)

  override def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] = new NonEmptyStream[B](head,tail ++ anotherStream)

  override def foreach(f: A => Unit): Unit = {
    f(h)
    tail.foreach(f)
  }

  override def map[B](f: A => B): MyStream[B] = new NonEmptyStream[B](f(head),tail.map(f))


  override def flatMap[B](f: A => MyStream[B]): MyStream[B] = f(head) ++ tail.flatMap(f)

  override def filter(predicate: A => Boolean): MyStream[A] =
    if(predicate(head)) new NonEmptyStream(head,tail.filter(predicate))
    else tail.filter(predicate)

  override def take(n: Int): MyStream[A] = {
  if(n <= 0) EmptyStream
  else if(n == 1) new NonEmptyStream[A](head,EmptyStream)
  else new NonEmptyStream[A](head,tail.take(n-1))
  }


  override def takeAsList(n: Int): List[A] =
//    if(n <= 0) List()
//    else if (n == 1) List(head)
//    else List(head) ++ tail.takeAsList(n-1) should work though
  take(n).toList()
}


object MyStream {
  def from[A](start: A)(generator: A => A): MyStream[A] =
    new NonEmptyStream[A](start,MyStream.from(generator(start))(generator))
}
object MyApp2 extends App {
  val odd = MyStream.from[Int](1)(a => a+2).takeAsList(10)
  odd.foreach(println)
  val fa = (MyStream.from(1)(_+1)).flatMap(x => new NonEmptyStream(x,new NonEmptyStream(x+1,EmptyStream))).takeAsList(10)
  fa.foreach(println)
  val filtered = MyStream.from(1)(_+1).flatMap(x => new NonEmptyStream(x,new NonEmptyStream(x+1,EmptyStream))).filter(_ < 10).take(10)
  filtered.foreach(println)
}

