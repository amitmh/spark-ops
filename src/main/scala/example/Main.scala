package example


import org.apache.spark.SparkConf
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.types.{IntegerType, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

object Main extends App {

  val conf = new SparkConf().setAppName(name)
  val spark: SparkSession = SparkSession.builder.config(conf)
    .config("spark.master", "local[*]")
    .getOrCreate()
  val refData = Seq(
    Row(1, null, 12, 13, 14),
    Row(2, 22, null, 23, 24),
    Row(3, 33, 32, null, 34),
    Row(4, 44, 42, 43, null),
  )

  val refSchema = new StructType()
    .add("PK", IntegerType)
    .add("T1", IntegerType)
    .add("T2", IntegerType)
    .add("T3", IntegerType)
    .add("T4", IntegerType)

  val variableCols = Seq("T1", "T2", "T3", "T4")
  val df: DataFrame = spark.createDataFrame(spark.sparkContext.parallelize(refData), refSchema)
  df.printSchema()
  df.show(false)

  val df2 = transpose(df, variableCols)
  df2.explain(true)
  df2.printSchema()
  df2.show(false)

  spark.stop()

  def name = this.getClass.getName


  def transpose(df: DataFrame, transposeCols: Seq[String], varName: String = "VAR", valName: String = "VAL"): DataFrame = {
    val standardCols = df.schema.fields.map(_.name).toSet -- variableCols.toSet
    val exprs = standardCols.toSeq :+ s"Stack(${transposeCols.size}, ${transposeCols.map(c => s"'$c', $c").mkString(", ")})"
    df.selectExpr(exprs: _*)
      .withColumnRenamed("col0", varName)
      .withColumnRenamed("col1", valName)
      .filter(col(valName).isNotNull)

  }
}


