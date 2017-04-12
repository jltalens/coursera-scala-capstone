package observatory

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

import scala.collection.concurrent.TrieMap

@RunWith(classOf[JUnitRunner])
class InteractionTest extends FunSuite with Checkers {

  test("'tileLocation()' basic to location") {
    val expected = Location(51.512161249555156,0.02197265625)
    val actual = Tile(65544,43582,17).toLocation

    assertResult(expected)(actual)
  }

  test("'tileLocation()' basic to uri") {
    val expected = "http://tile.openstreetmap.org/17/65544/43582.png"
    val actual = Tile(65544,43582,17).toURI.toString
    assertResult(expected)(actual)
  }

}
