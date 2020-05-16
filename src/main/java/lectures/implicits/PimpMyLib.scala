package lectures.implicits

object PimpMyLib extends App {

  //2.isPrime

  implicit class RichInt(val value: Int) extends AnyVal {
    def isEven: Boolean = value % 2 == 0

    def sqrt: Double = Math.sqrt(value)

    def times(fun: () => Unit): Unit = {
      @scala.annotation.tailrec
      def aux(counter: Int): Unit =
        if(counter <= 0) ()
        else {
          fun()
          aux(counter-1)
        }
      aux(value)
    }

    def *[T](list: List[T]):List[T] = {
      @scala.annotation.tailrec
      def aux(counter: Int, acc: List[T]): List[T] =
        if(counter<=  0) acc
        else aux( counter-1,acc ++ list)
      aux(value,list)
    }
  }

  implicit class RicherInt(richInt: RichInt) {
    def isOdd: Boolean = !richInt.isEven
  }

  println(2.isEven) //pimping, really its RichInt(2).isEven
  // compiler doesn't do multiple implicit searches
  //  println(4.isOdd)  doesnt compile

  /*
  Enrich string class
  add as Int method
  encrypt method
   */

  implicit class RichString(val value: String) extends AnyVal {
    def asInt: Int = Integer.valueOf(value)

    def encrypt: String = value.map(x => (x + 1).toChar)
  }

  println("abcde".encrypt)
  println(3 * List(1,2,3))




  // part 3


}
