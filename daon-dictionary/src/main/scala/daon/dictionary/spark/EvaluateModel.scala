package daon.dictionary.spark

import java.util
import java.util.{ArrayList, List}

import ch.qos.logback.classic.{Level, Logger}
import daon.analysis.ko.DaonAnalyzer
import daon.analysis.ko.model.{EojeolInfo, ModelInfo}
import daon.analysis.ko.reader.ModelReader
import org.apache.commons.lang3.time.StopWatch
import org.apache.spark.sql._
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.mutable.ArrayBuffer

/**
  * 재현율 측정용
  * 특수문자는 측정 제외 필요
  * (학습 데이터에서 특수문자 오매칭이 많음)
  */
object EvaluateModel {

  val model: ModelInfo = ModelReader.create.load
  val daonAnalyzer = new DaonAnalyzer(model)
  var ratioArr: ArrayBuffer[Float] = ArrayBuffer[Float]()

  case class Keyword(word:String, tag:String)

//  val SENTENCES_INDEX_TYPE = "train_sentences_v3/sentence"
  val SENTENCES_INDEX_TYPE = "test_sentences_v3/sentence"

  def main(args: Array[String]) {

    val spark = SparkSession
      .builder()
      .appName("daon dictionary")
      .master("local[*]")
      .config("es.nodes", "localhost")
      .config("es.port", "9200")
      .config("es.index.auto.create", "true")
      .getOrCreate()

    readEs(spark)

    //기분석 사전 재현율 측정 필요. 사전 구분 및 위치 구분 확인
    // 앞부분(어절 시작 부분)에 forwardFst 사전 데이터가 나오는지
    // 뒷부분(시작 형태소 이후 형태소)에 backwardFst 데이터가 나오는지

  }

  private def readEs(spark: SparkSession) = {

    val df = spark.read.format("es").load(SENTENCES_INDEX_TYPE)
      .limit(10000) // 1만건 대상

    val evaluateSet = df

    val totalMorphCnt = spark.sparkContext.longAccumulator("totalMorphCnt")
    val totalMorphErrorCnt = spark.sparkContext.longAccumulator("totalMorphErrorCnt")

    val totalEojeolCnt = spark.sparkContext.longAccumulator("totalEojeolCnt")
    val totalEojeolErrorCnt = spark.sparkContext.longAccumulator("totalEojeolErrorCnt")

    evaluateSet.foreach(row =>{
      val sentence = row.getAs[String]("sentence")

      val results = analyze(sentence)

      val eojeols = row.getAs[Seq[Row]]("eojeols")

      totalEojeolCnt.add(eojeols.size)

      eojeols.indices.foreach(e=> {
        val eojeol = eojeols(e)
        val surface = eojeol.getAs[String]("surface")
        val morphemes = eojeol.getAs[Seq[Row]]("morphemes")

        val analyzeEojeol = results.get(e)
        val nodes = analyzeEojeol.getNodes

        val analyzeWords = ArrayBuffer[Keyword]()

        for ( node <- nodes ) {
          for( keyword <- node.getKeywords ){
            analyzeWords += Keyword(keyword.getWord, keyword.getTag.name)
          }
        }

        val correctWords = ArrayBuffer[Keyword]()

        morphemes.indices.foreach(m=>{
          val morpheme = morphemes(m)
          val seq = morpheme.getAs[Long]("seq")
          val word = morpheme.getAs[String]("word")
          val tag = morpheme.getAs[String]("tag")

          correctWords += Keyword(word, tag)
        })

        val errorCnt = check(correctWords, analyzeWords)

        val totalCnt = correctWords.size

        totalMorphCnt.add(totalCnt)
        totalMorphErrorCnt.add(errorCnt)

        if(errorCnt > 0){
          val correctKeywords = correctWords.map(k=>k.word + "/" + k.tag).mkString("+")
          val analyzedKeywords = analyzeWords.map(k=>k.word + "/" + k.tag).mkString("+")

          // 에러 결과 별도 리포팅 필요
          println(s"$errorCnt : $surface => $correctKeywords || $analyzedKeywords << $sentence")

          totalEojeolErrorCnt.add(1)
        }

      })
    })

    val eojeolAccuracyRatio = (totalEojeolCnt.value - totalEojeolErrorCnt.value).toFloat / totalEojeolCnt.value.toFloat * 100
    val morphAccuracyRatio = (totalMorphCnt.value - totalMorphErrorCnt.value).toFloat / totalMorphCnt.value.toFloat * 100

    println("eojeol accuracyRatio : " + eojeolAccuracyRatio + ", error : " + totalEojeolErrorCnt.value + ", total : " + totalEojeolCnt.value)
    println("morph accuracyRatio : " + morphAccuracyRatio + ", error : " + totalMorphErrorCnt.value + ", total : " + totalMorphCnt.value)
  }

  private def analyze(sentence: String): util.List[EojeolInfo] = {
    val result = new util.ArrayList[EojeolInfo]()

    try{
      result.addAll(daonAnalyzer.analyzeText(sentence))
    }catch {
      case e: NullPointerException => println(e, sentence)
    }

    result
  }

  private def check(correct: ArrayBuffer[Keyword], analyzed: ArrayBuffer[Keyword]) = {
    var errorCnt = 0

    correct.indices.foreach(i=>{
      val a = correct(i)
      var b = Keyword("","")

      //존재하는 경우 정답으로, 위치가 틀어지는 경우 전체가 에러로 처리됨을 방지
      var isExist = false
      analyzed.foreach(m => {
        b = m
        if(a.word == b.word && a.tag == b.tag){
          isExist = true
        }
      })

      if(!isExist){
        errorCnt += 1
      }

    })

    errorCnt
  }


  private def checkBefore(correct: ArrayBuffer[Keyword], analyzed: ArrayBuffer[Keyword]) = {
    var errorCnt = 0

    correct.indices.foreach(i=>{
      val a = correct(i)
      var b = Keyword("","")

      if(i < analyzed.size){
        b = analyzed(i)
      }

      if(a.word != b.word || a.tag != b.tag){
        errorCnt += 1
      }

    })

    errorCnt
  }
}
