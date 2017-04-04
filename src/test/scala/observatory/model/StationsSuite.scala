package observatory.model

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class StationsSuite extends FunSuite {
  test("'parse()' should create a Stations class from a full string") {
    val input = "724017,03707,+37.358,-078.438"
    val expected = Stations("724017", Some("03707"), Some("+37.358"), Some("-078.438"))
    val actual = Stations.parse(input)

    assertResult(expected)(actual)
  }

  test("'parse()' should create a Station from a partial string") {
    val input = "724017,,,-078.438"
    val expected = Stations("724017", None, None, Some("-078.438"))
    val actual = Stations.parse(input)

    assertResult(expected)(actual)
  }
}
