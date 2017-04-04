package observatory

import java.time.LocalDate

import observatory.model.{Stations, Temperatures}
import org.apache.spark.rdd.RDD
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ExtractionTest extends FunSuite {

  val rddStations: RDD[Stations] = Extraction.spark.sparkContext
    .textFile("src/test/resources/stations.csv")
    .map(Stations.parse)

  val rddTemperatures: RDD[Temperatures] = Extraction.spark.sparkContext
    .textFile("src/test/resources/2015.csv")
    .map(Temperatures.parse)

  test("'locateTemperatures' should return the temperature converted in degrees") {

    val actual = Extraction.locateTemperatures(2015, rddStations, rddTemperatures).toSeq
    val expected = Seq(
      (LocalDate.of(2015, 8, 11), Location(37.35, -78.433), 27.3),
      (LocalDate.of(2015, 12, 6), Location(37.358, -78.438), 0.0),
      (LocalDate.of(2015, 1, 29), Location(37.358, -78.438), 2.0)
    )
    assertResult(expected)(actual)
  }


  test("'localtionYearlyAverageRecords()' should return the average on each location over the year") {
    val records = Extraction.locateTemperatures(2015, rddStations, rddTemperatures)
    val actual = Extraction.locationYearlyAverageRecords(records)
    val expected = Seq(
      (Location(37.35, -78.433), 27.3),
      (Location(37.358, -78.438), 1.0)
    )
  }
}