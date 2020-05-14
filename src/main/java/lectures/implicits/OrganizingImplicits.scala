package lectures.implicits

object OrganizingImplicits extends  App {
  implicit val reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  println(List(1,2,3,4).sorted)
  /*
  implicit values
  val/var
  object
  accessor methods = defs with no parameters

   */
  implicit val alphaOrder: Ordering[Person] = Ordering.fromLessThan((p1,p2) => p1.name < p2.name)

  //Exercise
  case class Person(name: String ,age: Int)
  val persons = List (
    Person("Jim",11),
    Person("Mark",21),
    Person("Mary",31),
    Person("T",41)
  ).sorted
  persons.foreach(println)
  /*
  implicit scope
  -normal scope = Local scope
  -imported scope
  -companions object of all types involved in the methods signature
   */
  /*
  add 3 ordering
  -total price = 50
  -unitcount = 25
  -unitprice = 25
   */
  case class Purchase(nUnits: Int, unitPrice: Double)

  object Purchase {
    implicit val totalOrdering: Ordering[Purchase] = Ordering.fromLessThan((p1,p2) => p1.nUnits * p1.unitPrice < p2.unitPrice * p2.nUnits)
  }

  object unitCount {
    implicit val unitCountOrdering: Ordering[Purchase] = Ordering.fromLessThan((p1, p2) => p1.nUnits < p2.nUnits)
  }
  object unitPrice {
    implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((p1, p2) => p1.unitPrice < p2.unitPrice)
  }

  import OrganizingImplicits.unitCount.unitCountOrdering
  val list = List (
    Purchase(1,5),
    Purchase(2,7),
    Purchase(3,6),
    Purchase(1,2),
  ).sorted

  list.foreach(println)
}
