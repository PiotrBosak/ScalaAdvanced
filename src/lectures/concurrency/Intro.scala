package lectures.concurrency

import java.util.concurrent.Executors

object Intro extends App {
  //JVM threads

  val aThread: Thread = new Thread(() => println("cos"))
  aThread.start() //gives the signal to start the thread
  val pool = Executors.newFixedThreadPool(5)
  pool.execute(() => println("ff"))
  pool.shutdown()
  //  pool.execute(() => println("fjal;s"))//this would cause exception in the main thread(the one that called that method)
  // if we use pool.shutdownNow then threads that sleep will throw an exception(Interrupted)
  //  def runInParallel = {
  //    var x = 0
  //    val thread1 = new Thread(() => x = 1)
  //    val thread2 = new Thread(() => x = 2)
  //    thread1.start()
  //    thread2.start()
  //    println(x)
  //  }
  //  for(_ <- 1 to 100)
  //    runInParallel
  //  //race condition
  //option #1 use synchronized()
  /**
   * construct 50 inception threads
   * thread1 => thread2 => thread3
   * in reverse order
   * 2) var x = 0
   * val threads (1 to 100).map(_=> new Thread(() => x + //smallest = 100/number of cores
   */

def threads(max: Int, i: Int): Thread = new Thread(() => {
  if(i<max) {
    val thread = threads(max,i+1)
    thread.start()
    thread.join()
  }
  println(s"I'm$i")
})
  threads(50,1).start()
}