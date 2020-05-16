package lectures.implicits

object TypeClasses extends  App{
  trait HTMLWritable {
    def toHtml: String
  }

  case class User(name: String, age: Int, email:String) extends HTMLWritable{
    override def toHtml: String = s"<div>$name $age yo <a href = $email/> </div>"
  }

  User("John",32,"john@gmail.com")
/*
it works only for the types we have written
one implementation

option 2
patternMatching
 */
  object HTMLSerializePM {
  def serializetoHTML(value: Any) = value match {
    case User(name,age,email) =>
      //violates open-closed principle
  }

}
  /*
  lost type safety
  need for modification
  still 1 implementation
   */
//type class
  trait HTMLSerializer[T] {
    def serialize(value:T): String
  }
// type class instance
  implicit object USerSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<div>${user.name} ${user.age} yo <a href = ${user.email}/> </div>"
  def fun() = "fun"
  }

  val user = User("john",1,"t")
  val user2 = User("john",1,"ti")

  //part 2
  object HTMLSerializer  {
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String =
      serializer.serialize(value)

    def apply[T](implicit serializer: HTMLSerializer[T]) = serializer
  }
  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div> something  $value"

  }
  println(HTMLSerializer.serialize(User("John",32,"Doe@gmail")))
  println(HTMLSerializer[User].serialize(User("John",32,"Doe@gmail")))// using this we jave access to all methods not only serialize
  trait MyTypeClassTemplate[T] {
    def action1(value: T): String
    def action2(value: T): Int
  }
  object MyTypeClassTemplate{
    def apply[T](implicit  instance: MyTypeClassTemplate[T]): MyTypeClassTemplate[T] = instance
  }
  implicit object NameAndEmailEquality extends Equal[User] {
    override def isEqual(a: User, b: User): Boolean =
      a.name.equals(b.name) &&
        a.email.equals(b.email)
  }
//  implicit object NameEquality extends Equal[User] {
//    def isEqual(a: User, b: User): Boolean = a.name.equals(b.name)
//  }

  trait Equal[T] {
    def isEqual(a: T, b: T): Boolean
  }

  object Equal {
    def apply[T](implicit equal: Equal[T]): Equal[T] = equal

    def isEqual[T](a: T,b: T)(implicit equal:Equal[T]): Boolean = equal.isEqual(a,b)

  }
  implicit class EqualEnrichment[T](val value: T) {

    def !==(other: T)(implicit equal: Equal[T]): Boolean = !equal.isEqual(value,other)
    def ===(other: T)(implicit equal: Equal[T]): Boolean = equal.isEqual(value,other)
  }

  val user1 = User("a",5,"f")
  val user5 = User("a",5,"g")
  println(user1.===(user5))
  println(user1.!==(user5))

  println(Equal[User].isEqual(User("a",1,"5"),User("a",5,"5")))
  implicit class HTMLEnrichment[T](val value: T) {
    def toHtml(implicit serializer:HTMLSerializer[T]): String = serializer.serialize(value)
  }

  println(2.toHtml)

  case class Person(name: String)
  implicit val b = Person("name")
val a = implicitly[Person]
  println()

}
