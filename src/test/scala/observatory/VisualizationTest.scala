package observatory


import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class VisualizationTest extends FunSuite with Checkers {

  // Calculate distances between points: http://www.movable-type.co.uk/scripts/latlong.html
  test("'greatCircleDistance()' between two points") {
    val p1 = Location(37.358, -078.438)
    val p2 = Location(37.350, -078.433)

    val actual = Visualization.distance(p1, p2)
    val actualRounded = BigDecimal(actual).setScale(1, BigDecimal.RoundingMode.HALF_UP).toDouble
    val expected = 993.3

    assertResult(expected)(actualRounded)
  }

}
