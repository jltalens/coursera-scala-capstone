package observatory.model

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TemperaturesSuite extends FunSuite {

  test("'parse()' should be able to parse a full string") {
    val input = "724017,03707,01,29,35.6"
    val expected = Temperatures("724017", Some("03707"), Some(1), Some(29), Some(2.0))
    val actual = Temperatures.parse(input)

    assertResult(expected)(actual)

  }

  test("'parse()' should be able to parse a partial string") {
    val input = "010013,,11,25,39.2"
    val expected = Temperatures("010013", None, Some(11), Some(25), Some(4.0))
    val actual = Temperatures.parse(input)

    assertResult(expected)(actual)

  }

  test("'parse()' a temperature of 9999.9 means is missing") {
    val input = "010013,,11,25,9999.9"
    val expected = Temperatures("010013", None, Some(11), Some(25), None)
    val actual = Temperatures.parse(input)

    assertResult(expected)(actual)
  }

}
