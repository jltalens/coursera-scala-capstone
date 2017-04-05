package observatory


import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class VisualizationTest extends FunSuite with Checkers {

  def roundTo(number: Double, decimals: Int) : Double =
    BigDecimal(number).setScale(decimals, BigDecimal.RoundingMode.HALF_UP).toDouble

  // Calculate distances between points: http://www.movable-type.co.uk/scripts/latlong.html
  test("'distance()' between two points") {
    val p1 = Location(37.358, -078.438)
    val p2 = Location(37.350, -078.433)

    val actual = Visualization.distance(p1, p2)
    val actualRounded = roundTo(actual, 1)
    val expected = 993.3

    assertResult(expected)(actualRounded)
  }


  test("'estimateTempAt()' in a range of temperatures if the point exists in the range") {
    val p = Location(37.35, -78.433)
    val range = Seq(
      (p, 27.3),
      (Location(37.358, -78.438), 1.0)
    )
    val actual = Visualization.estimateTempAt(p, range)
    val expected = 27.3

    assertResult(expected)(actual)
  }

  test("'estimateTempAt()' in a range if the point doesn't exist in the range") {
    val p = Location(30.04, 45.433)
    val range = Seq(
      (Location(37.35, -78.433), 27.3), //d = 10520000  => 0.000000095
      (Location(37.358, 70.438), 1.0), //d = 2443000 => 0.000000409
      (Location(35.888, 33.438), 14.7) //d = 1293000 => 0.000000773
    )
    // (0.000000095 * 27.3)+ (0.000000409 * 1.0) + (0.000000773 * 14.7)  | 0.000002593 + 0.000000409 + 0.000011363 | 0.000014365
    //        /
    // 0.000000095 + 0.000000409 + 0.000000773 // 0.000001277
    // => 11.249021143
    val actual = roundTo(Visualization.estimateTempAt(p, range), 4)
    val expected = 11.2497

    assertResult(expected)(actual)
  }
}
