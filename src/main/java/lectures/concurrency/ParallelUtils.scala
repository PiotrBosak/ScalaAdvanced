package lectures.concurrency
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicReference

import scala.collection.parallel.CollectionConverters._
import scala.collection.parallel.ForkJoinTaskSupport
import scala.collection.parallel.immutable.ParVector
object ParallelUtils extends App{
  //1 parallel collections
  val parList = (List(1,2,3))
  val aParVector = ParVector[Int](1,2)
  def measure[T](operation: => T): Long = {
    val time = System.currentTimeMillis()
    operation
    System.currentTimeMillis() - time
  }
  val list = (1 to 10000000).toList
  val serialTime = measure {
    list.map(_+1)
  }
  val parTime = measure {
    (list).par.map(_+1)
  }
  //it's only worth to use parallel if collection is pretty big
  println(serialTime)
  println(parTime)
  /**
   * map reduce model
   * split elements into chunks
   * do the operation
   * combine the chunks
   */
  //map, flatMap filter foreach are safe
  //reduce fold are not(if operation is not associative)
  //watch out race condition

  aParVector.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool(2))
  //2 atomic ops and references
  val atomic = new AtomicReference[Int](2)
  val current = atomic.get()// thread safe

}
