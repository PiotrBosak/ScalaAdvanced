package lectures.concurrency

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Random, Success}

object FuturesAndPromises extends App {
  def calculateSthLong: Int = {
    Thread.sleep(2000)
    5
  }

  //  val aFuture = Future {
  //    calculateSthLong //calculates it on another thread
  //  }(global)
  //  aFuture.onComplete {
  //    case Success(value) => println(value)
  //    case Failure(exception) => println("something didn't work" + exception)
  //  }(global)
  //  Thread.sleep(4000)

  //mini social network
  case class Profile(id: Int, name: String) {
    def poke(another: Profile): Unit =
      println(s"${this.name} poking ${another.name}")
  }

  object SocialNetwork {
    val names = Map(
      1 -> "Mark",
      2 -> "John",
      3 -> "Mary"
    )

    val friends = Map(
      1 -> 2
    )
    val random = new Random(System.nanoTime())

    //api
    def fetchProfile(id: Int): Future[Profile] = Future {
      Thread.sleep(random.nextInt(300))
      Profile(id, names(id))
    }(global)

    def fetchBestFriend(profile: Profile): Future[Profile] = Future {
      Thread.sleep(400)
      val bfId = friends(profile.id)
      Profile(bfId, names(bfId))
    }(global)

    //client: mark to poke bill
  }

  val mark: Future[Profile] = SocialNetwork.fetchProfile(1)
  mark.onComplete {
    case Success(markProfile) => {
      val john = SocialNetwork.fetchBestFriend(markProfile)
      john.onComplete {
        case Success(johnProfile) => johnProfile.poke(markProfile)
        case Failure(e) => e.printStackTrace()
      }(global)
    }
    case Failure(e) => e.printStackTrace()

  }(global) //works but looks terrible and is inconvient
  Thread.sleep(3000)
  //functional composition of futures
  //map, flatMap, filter
  val marksName = mark.map(profile => profile.name)(global)
  val markksBestFriendName = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))(global)
  val friendIfNameStartsAtA = mark.filter(profile => profile.name.startsWith("A"))(global)
  for {
    mark <- SocialNetwork.fetchProfile(1)
    john <- SocialNetwork.fetchBestFriend(mark)

  }yield mark.poke(john)
  Thread.sleep(2000)
  //fallback
  val dummy = SocialNetwork.fetchProfile(-1).recover {
    case e: Throwable => Profile(0,"dummy")
  }
  val afetchedProfile = SocialNetwork.fetchProfile(-1).recoverWith {
    case e: Throwable => SocialNetwork.fetchProfile(1)//existing, if not will throw exception, but it will be the second one, instead of first one like in fallbackTo
  }
  val fallBackResult = SocialNetwork.fetchProfile(-1).fallbackTo(SocialNetwork.fetchProfile(1))// if that fails, we return the first one(failed one)


}
