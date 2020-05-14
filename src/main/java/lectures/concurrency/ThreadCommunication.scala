package lectures.concurrency

import scala.collection.mutable
import scala.util.Random

object ThreadCommunication extends App {

  /*
  the producer-consumer problem

   */
  class SimpleContainer {
    private var value: Int = 0

    def isEmpty: Boolean = value == 0

    def getValue: Int = {
      val result = value
      value = 0
      result
    }

    def setValue(v: Int): Unit = value = v
  }

  def naiveProducers(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("consumer is waiting")
      while (container.isEmpty) //busy waiting
        println("still waiting")
      println("I've consumed " + container.getValue)
    })

    val producer = new Thread(() => {
      println("I haven't produced yet")
      Thread.sleep(500)
      val value = 5
      println("I have produced" + value)
      container.setValue(value)
    })
    consumer.start()
    producer.start()

  }

  //  naiveProducers()

  //wait and notify
  def smartProdCons() = {
    val container = new SimpleContainer
    val consumer = new Thread(() => {
      println("cons waiting")
      container.synchronized {
        container.wait()
        println(s"I'm consumer 1 and I've got ${container.getValue} ")

      }
    })
    val consumer2 = new Thread(() => {
      println("cons waiting")
      container.synchronized {
        container.wait()
        println(s"I'm consumer 2 and I've got ${container.getValue} ")

      }
    })

    val producer = new Thread(() => {
      println("startd computing")
      Thread.sleep(2000)
      val value = 5
      container.synchronized {
        container.setValue(value)
        container.notifyAll()
      }
    })
    consumer.start()
    consumer2.start()
    producer.start()

  }

  //  smartProdCons()

  /*
  producer produces in 1 of 3 values
  consumer consumer new value

   */
  def bufferProdCons(): Unit = {
    val buffer = new mutable.Queue[Int]
    val capacity = 3
    val consumer = new Thread(() => {
      val random = new Random(System.nanoTime())
      while (true) {
        buffer.synchronized {
          if (buffer.isEmpty) {
            buffer.wait()
            println("consumer waiting")
          }

          val value = buffer.dequeue()
          println(s"I've got $value ")
          //just in case
          buffer.notify()
        }
        Thread.sleep(random.nextInt(250))
      }
    })

    val producer = new Thread(() => {
      val random = new Random(System.nanoTime())
      var i: Int = 0
      while (true) {
        buffer.synchronized {
          if (buffer.size == capacity) {
            buffer.wait()
            println("producer waiting")
          }
          buffer.enqueue(i)
          println("produced " + i)
          buffer.notify()
          i += 1
        }
        Thread.sleep(random.nextInt(500))
      }
    })
    producer.start()
    consumer.start()


  }

  //bufferProdCons()

  /*
  multiple consumers, producers
  acting on the same buffer

   */
  class ConsumerRunnable(id: Int, val buffer: mutable.Queue[Int]) extends Runnable {
    override def run(): Unit = {
      while (true) {
        buffer.synchronized {
          if (buffer.isEmpty) {
            println(s"consumer$id is waiting")
            buffer.wait()
          }
          val x = buffer.dequeue()
          println(s"I've got $x")
          //buffer.notifyAll()

        }
        Thread.sleep(ConsumerRunnable.random.nextInt(1000))
      }

    }
  }

  object ConsumerRunnable {
    val random = new Random()

  }

  class ProducerRunnable(id: Int, val buffer: mutable.Queue[Int]) extends Runnable {
    override def run(): Unit = {
      var i = 0
      while (true) {
        buffer.synchronized {
          if (buffer.size >= ProducerRunnable.capacity) {
            println(s"producer$id is waiting")
            buffer.wait()
          }

          buffer.enqueue(i)
          //buffer.notifyAll()
          i += 1

        }
        Thread.sleep(ProducerRunnable.random.nextInt(1000))
      }
    }
  }

  object ProducerRunnable {
    val capacity = 5
    val random = new Random()
  }

  def multipleProdCons(): Unit = {
    val buffer = new mutable.Queue[Int]
    for (i <- 1 to 3)
      new Thread(new ConsumerRunnable(i, buffer)).start()
    for (i <- 1 to 3)
      new Thread(new ProducerRunnable(i, buffer)).start()
  }

  //multipleProdCons()

  case class Friend(name: String) {
    val lock = new Object
    var side = "right"

    def bow(other: Friend): Unit = {
      lock.synchronized {
        println(s"$this is bowing to $other")
        other.rise(this)
      }
    }

    def switchSide(): Unit =
      if (side == "right") side = "left"
      else side = "right"

    def pass(other:Friend): Unit = {
      while(this.side == other.side) {
        println("lemme change lane")
        Thread.sleep(500)
        switchSide()
        Thread.sleep(1000)
      }
    }

    def rise(other: Friend): Unit = {
      lock.synchronized {
        println(s"$this has risen to $other")
      }
    }
  }

  def deadLock(): Unit = {
    val john = Friend("john")
    val mark = Friend("mark")
    new Thread(() => john.pass(mark)).start()
    new Thread(() => mark.pass(john)).start()
  }

  deadLock()
}
