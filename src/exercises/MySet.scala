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
  def -(elem:A): MySet[A]
  def &(set:MySet[A]): MySet[A]
  def --(set:MySet[A]): MySet[A]
  def unary_! : MySet[A]
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

  override def -(elem: A): MySet[A] = this

  override def &(set: MySet[A]): MySet[A] = this

  override def --(set: MySet[A]): MySet[A] = this

  override def unary_! : MySet[A] = new PropertyBasedSet[A](_ => true)


}

// all elements of type A which satisfy a property
class PropertyBasedSet[A](property: A => Boolean) extends MySet[A] {
  override def contains(elem: A): Boolean = property(elem)

  override def +(elem: A): MySet[A] =
     new PropertyBasedSet[A](x => property(x) || x == elem)

  override def ++(anotherSet: MySet[A]): MySet[A] =
    new PropertyBasedSet[A](x => property(x) || anotherSet.contains(x))

  override def map[B](f: A => B): MySet[B] = throwExc


  def throwExc = throw new IllegalArgumentException

  override def flatMap[B](f: A => MySet[B]): MySet[B] = throwExc

  override def filter(predicate: A => Boolean): MySet[A] = new PropertyBasedSet[A](x => property(x) && predicate(x))

  override def foreach(f: A => Unit): Unit = throwExc

  override def -(elem: A): MySet[A] = filter(x => x != elem)

  override def &(set: MySet[A]): MySet[A] = filter(set)

  override def --(set: MySet[A]): MySet[A] = filter(!set)

  override def unary_! : MySet[A] = new PropertyBasedSet[A](x => !property(x))
}
/*
- remove element
-intersection with another set
-difference with another set
 */

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

  override def -(elem: A): MySet[A] =
    if(head == elem) tail
    else tail - elem + head

  override def --(set: MySet[A]): MySet[A] = {
//   if(set contains head) tail
//   else tail -- set + head
    filter(x => !set(x))
  }

  override def unary_! : MySet[A] = new PropertyBasedSet[A](x => !this.contains(x))



  override def &(set: MySet[A]): MySet[A] = {
//    if(set contains head) tail & set + head
//    else tail & set doesn't work
    filter(set)// filter( x => set.contains(x))
  }

  override def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }




}
object MySet{
  def apply[A](values: A*): MySet[A] = {
    def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A] =
      null

    buildSet(values.toSeq,new EmptySet[A])
  }

}
object myApp extends App {
  val set = new NonEmptySet[Int](1, new NonEmptySet[Int](2,new NonEmptySet[Int](3,new EmptySet[Int])))
  val set2 = new NonEmptySet[Int](2, new NonEmptySet[Int](4,new NonEmptySet[Int](5,new EmptySet[Int])))
  val intersect = set & set2
  val difference = set -- set2
  println(intersect.contains(2))
  println(intersect.contains(3))
  println("-----------")
  //println(difference.contains(2))
  //println(difference.contains(3))

}
