package exercises

trait MySet[A] extends (A => Boolean){
  /*
  implement functional set
   */

  def contains(elem: A): Boolean
  def+(elem: A): MySet[A]
  def ++ (anotherSet: MySet[A]):MySet[A]

  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate : A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit

  override def apply(v1: A): Boolean = contains(v1)


}
class EmptySet[A] extends MySet[A]{
  override def contains(elem: A): Boolean = false

  override def +(elem: A): MySet[A] = new NonEmptySet[A](elem,this)

  override def ++(anotherSet: MySet[A]): MySet[A] = anotherSet

  override def map[B](f: A => B): MySet[B] = new EmptySet[B]

  override def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]

  override def filter(predicate: A => Boolean): MySet[A] = this

  override def foreach(f: A => Unit): Unit = ()

}

class NonEmptySet[A](val head: A, val  tail: MySet[A]) extends MySet[A] {
  override def contains(elem: A): Boolean =
    if(head == elem) true
    else tail.contains(elem)

  override def +(elem: A): MySet[A] =
    if(contains(elem)) this
    else new NonEmptySet[A](elem,this)

  override def ++(anotherSet: MySet[A]):MySet[A] =
    tail ++ anotherSet + head


  override def map[B](f: A => B): MySet[B] =
    (tail map f) + f(head)

  override def flatMap[B](f: A => MySet[B]): MySet[B] =
    (tail flatMap f) ++ f(head)


  override def filter(predicate: A => Boolean): MySet[A] = {
    val filteredTail = tail.filter(predicate)
    if(predicate(head)) filteredTail + head
    else filteredTail
  }

  override def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }




}
object MySet{
  def apply[A](values: A*): MySet[A] = {
    @scala.annotation.tailrec
    def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A] =
      if(valSeq.isEmpty) acc
      else buildSet(valSeq.tail,acc+valSeq.head)
    buildSet(values.toSeq,new EmptySet[A])
  }

}
