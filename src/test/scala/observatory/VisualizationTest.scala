package observatory


import com.sksamuel.scrimage.Pixel
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class VisualizationTest extends FunSuite with Checkers {

  // Calculate distances between points: http://www.movable-type.co.uk/scripts/latlong.html
  test("'distance()' between two points") {
    val p1 = Location(37.358, -078.438)
    val p2 = Location(37.350, -078.433)

    val actual = Visualization.distance(p1, p2)
    val actualRounded = Visualization.roundTo(actual, 1)
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
    val actual = Visualization.roundTo(Visualization.estimateTempAt(p, range), 4)
    val expected = 11.2497

    assertResult(expected)(actual)
  }

  test("'closestTwoPointsToGiven()' should return (None, smallOfList) if the given is the smallest") {
    val value = 0.3
    val points = Seq(
      (2.0, Color(1,2,3)),
      (1.5, Color(4,5,6)),
      (1.0, Color(7,8,9))
    )
    val expected = (None, Some((1.0, Color(7,8,9))))
    val actual = Visualization.closestTwoPointsToGiven(points, value)

    assertResult(expected)(actual)
  }

  test("'closestTwoPointsToGiven()' should return (None, biggest) if the given is the biggest") {
    val value = 3
    val points = Seq(
      (2.0, Color(1,2,3)),
      (1.5, Color(4,5,6)),
      (1.0, Color(7,8,9))
    )
    val expected = (Some((2.0, Color(1,2,3))), None)
    val actual = Visualization.closestTwoPointsToGiven(points, value)

    assertResult(expected)(actual)
  }

  test("'closestTwoPointsToGiven()' should give (smallest, smallest) if the given is in the middle of two") {
    val value = 1.75
    val points = Seq(
      (2.0, Color(1,2,3)),
      (1.5, Color(4,5,6)),
      (1.0, Color(7,8,9))
    )
    val expected = (Some((1.5, Color(4,5,6))), Some((2.0, Color(1,2,3))))
    val actual = Visualization.closestTwoPointsToGiven(points, value)

    assertResult(expected)(actual)
  }

  test("'interpolateColor()' of a range of points if the point exists in the range") {
    val value =  3.0
    val points = Seq(
      (2.0, Color(1,0,0)),
      (3.0, Color(0,1,0)),
      (1.0, Color(1,1,1))
    )
    val expected = Color(0,1,0)
    val actual = Visualization.interpolateColor(points, value)

    assertResult(expected)(actual)
  }

  test("'interpolateColor() of a range of points if point between two") {
    val value = 1.75
    val points = Seq(
      (2.0, Color(1,2,3)),
      (1.5, Color(4,5,6)),
      (1.0, Color(7,8,9))
    )
//    val expected = Color(2,3,4)
    val expected = Color(3,4,5)
    val actual = Visualization.interpolateColor(points, value)

    assertResult(expected)(actual)
  }

  test("'interpolateColor() of a range of points if point is the biggest") {
    val value = 3.0
    val points = Seq(
      (2.0, Color(1,2,3)),
      (1.5, Color(4,5,6)),
      (1.0, Color(7,8,9))
    )
    val expected = Color(1,2,3)
    val actual = Visualization.interpolateColor(points, value)

    assertResult(expected)(actual)
  }

  test("'interpolateColor() of a range of points if point is the smallest") {
    val value = 0.3
    val points = Seq(
      (2.0, Color(1,2,3)),
      (1.5, Color(4,5,6)),
      (1.0, Color(7,8,9))
    )
    val expected = Color(7,8,9)
    val actual = Visualization.interpolateColor(points, value)

    assertResult(expected)(actual)
  }

  test("'interpolateColor() of a range of points if point is more close to one given") {
    val value = 1.3
    val points = Seq(
      (2.0, Color(1,2,3)),
      (1.5, Color(4,5,6)),
      (1.0, Color(7,8,9))
    )
    val expected = Color(5,6,7)
    val actual = Visualization.interpolateColor(points, value)

    assertResult(expected)(actual)
  }

  test("'failed test 1") {
    val value = 1.073741823E9
    val points = List(
      (-1.0,Color(255,0,0)),
      (2.147483647E9,Color(0,0,255))
    )
    val expected = Color(128,0,128)
    val actual = Visualization.interpolateColor(points, value)

    assertResult(expected)(actual)
  }

//  test("'locationsToPixelArray() should take a location and a color and return a pixel in the top-right position") {
//    val pixel = Pixel(0, 0, 0, 1)
//    val input = List(
//      (Location(90.0, -180.0), pixel)
//    )
//    val expected : Array[Pixel] = Array.ofDim(Visualization.dim)
//    expected.update(0, pixel)
//    val actual = Visualization.locationsToPixelArray(input)
//
//    assertResult(expected)(actual)
//  }
//
//  test("'coordinatesToArrayIdx() should take a location of 90, -180 and return a position of 0") {
//    val lon = 90.0
//    val lat = -180.0
//    val expected = 0
//    val actual = Visualization.coordinatesToArrayIdx(lon, lat)
//
//    assertResult(expected)(actual)
//  }
//
//
//  test("'coordinatesToArrayIdx() should take a location of 90, 180 and return a position of 360") {
//    val lon = 90.0
//    val lat = 180.0
//    val expected = 360
//    val actual = Visualization.coordinatesToArrayIdx(lon, lat)
//
//    assertResult(expected)(actual)
//  }
//
//  test("'coordinatesToArrayIdx()' should take a location of 0, 0 and return the idx of the center of the image") {
//    val lon = 0
//    val lat = 0
//    val expected = 360 * (90 / 2)
//    val actual  = Visualization.coordinatesToArrayIdx(lon, lat)
//
//    assertResult(expected)(actual)
//  }
}
