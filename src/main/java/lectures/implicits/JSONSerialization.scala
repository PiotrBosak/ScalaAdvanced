package lectures.implicits

import java.util.Date

object JSONSerialization extends App {

  /*
  users, posts, feeds
  serialize it to json
   */

  case class User(name: String, age: Int, email: String)

  case class Post(content: String, createdAt: Date)

  case class Feed(user: User, posts: List[Post])
  /*
  1create intermediate data type for int, string, list, date
  2 type classes for conversion
  3- serialize to json
   */
  sealed trait JSONValue {
    def stringify: String
  }
  final case class JSONString(value: String) extends JSONValue {
    override def stringify: String = "\"" + value + "\""
  }

  final case class JSONObject(values: Map[String,JSONValue]) extends JSONValue{
    override def stringify: String = values.map {
      case (key,value) => "\"" + key + "\": " + value.stringify
    }.mkString("{",",","}")
  }

  final case class JSONNumber(value: Int) extends JSONValue {
    override def stringify: String = value.toString
  }
   final case class JSONArray(values: List[JSONValue]) extends JSONValue {
     override def stringify: String =
       values.map(_.stringify).mkString("[",",","]")

   }
  implicit class JSONOps[T](value: T) {
    def toJSON(implicit converter: JSONConverter[T]): JSONValue = converter.convert(value)
  }

  val u = User("cos", 5, "fas")
  //type class
  trait JSONConverter[T] {
    def convert(a: T): JSONValue
  }

  //existing data types
  implicit object StringConverter extends JSONConverter[String] {
    override def convert(a: String): JSONValue = JSONString(a)
  }
  implicit  object NumberConverter extends JSONConverter[Int] {
    override def convert(a: Int): JSONValue = JSONNumber(a)
  }

  //our
  implicit  object UserConverter extends JSONConverter[User] {
    override def convert(user: User): JSONValue =  JSONObject(Map (
      "name" -> JSONString(user.name),
      "age" -> JSONNumber(user.age),
      "email" -> JSONString(user.email)
    ))
  }

  implicit object PostConverter extends JSONConverter[Post] {
    override def convert(post: Post): JSONValue = JSONObject(Map (
      "content" -> JSONString(post.content),
      "date" -> JSONString(post.createdAt.toString)
    ))
  }

  implicit object FeedConverter extends JSONConverter[Feed] {
    override def convert(feed: Feed): JSONValue = JSONObject(Map (
      "user" -> feed.user.toJSON,
      "posts" -> JSONArray(feed.posts.map(_.toJSON))
    ))
  }
  //2.3 conversion

  val now = new Date(System.currentTimeMillis())
  val mark = User("mark",32,"john@gmail.com")
  val feed = Feed(mark, List(
    Post("hello", now)
  ))
  println(feed.toJSON.stringify)




}
