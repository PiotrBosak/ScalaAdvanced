package lectures.concurrency

object ThreadCommunication extends App{
  /*
  the producer-consumer problem

   */
  class SimpleContainer {
    private var value: Int = 0
    def isEmpty: Boolean =  value == 0
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
      while(container.isEmpty)//busy waiting
        println("still waiting")
      println("I've consumed " + container.getValue)
    })

    val  producer = new Thread(() => {
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
  smartProdCons()



}
