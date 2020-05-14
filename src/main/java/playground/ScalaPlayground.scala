package playground

import java.util.Date

object ScalaPlayground extends App{
  def printTimeByName( date: => Date) = {
    println(date)
    Thread.sleep(1000)
    println(date)
  }
  printTimeByName(new Date())


}
