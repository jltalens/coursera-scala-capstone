package observatory

import java.time.{LocalDate, LocalTime}

import observatory.model.{Stations, Temperatures}
import org.apache.spark.rdd.RDD



/**
  * 1st milestone: data extraction
  */
object Extraction {

  import org.apache.spark.sql.SparkSession

  val spark: SparkSession =
    SparkSession
      .builder()
      .appName("Observatory")
      .config("spark.master", "local")
      .getOrCreate()
//  val rdd = spark.sparkContext.textFile()


  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    */
  def locateTemperatures(year: Int, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Double)] = {
    ???
  }

  def locateTemperatures(year: Int, rddStations: RDD[Stations], rddTemperatures: RDD[Temperatures]): Iterable[(LocalDate, Location, Double)] = {
    def extractLocateTemps(stationsList: Iterable[Stations], tempList: Iterable[Temperatures]) : Iterable[(LocalDate, Location, Double)] =
      tempList.map(temp => (
        LocalDate.of(year, temp.month.get, temp.day.get),
        Location(stationsList.head.latitude.map(_.toDouble).get, stationsList.head.longitude.map(_.toDouble).get),
        temp.temperature.get))
    val groupedStation = rddStations
      .filter {
        case Stations(_, _, None, _) => false
        case Stations(_, _, _, None) => false
        case _ => true
      }
      .groupBy(stations => (stations.stn, stations.wban.getOrElse("")))
    val groupedTemperatures = rddTemperatures
      .groupBy(temperatures => (temperatures.stn, temperatures.wban.getOrElse("")))
    groupedStation
      .join(groupedTemperatures)
      .flatMapValues(group => extractLocateTemps(group._1, group._2))
      .values
      .collect()
  }

  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Double)]): Iterable[(Location, Double)] = {
    ???
  }

}
