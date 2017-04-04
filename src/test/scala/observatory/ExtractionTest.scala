package observatory

import java.time.LocalDate

import observatory.model.{Stations, Temperatures}
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ExtractionTest extends FunSuite {

  test("'locateTemperatures' should return the temperature converted in degrees") {
    val rddStations = Extraction.spark.sparkContext
    .textFile("src/test/resources/stations.csv")
    .map(Stations.parse)
    val rddTemperatures = Extraction.spark.sparkContext
    .textFile("src/test/resources/2015.csv")
    .map(Temperatures.parse)
    val actual = Extraction.locateTemperatures(2015, rddStations, rddTemperatures).toSeq
    val expected = Seq(
      (LocalDate.of(2015, 8, 11), Location(37.35, -78.433), 27.3),
      (LocalDate.of(2015, 12, 6), Location(37.358, -78.438), 0.0),
      (LocalDate.of(2015, 1, 29), Location(37.358, -78.438), 2.0)
    )
    assertResult(expected)(actual)
  }
  
}