package com.cai.sequence
import java.io.File
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
 * @author cai584770
 * @date 2023/10/29 21:47
 * @Version
 */
class ReadSequence(filePath: String) {

  /*
  * input:FilePath
  * output:Result(ArrayBuffer[Map[Int, String]])
  * Map[linenumber，information/sequence]
  *
  *
  * */
  def process():ArrayBuffer[Map[Int, String]] = {
    val source = Source.fromFile(filePath)
    val arrayOfMaps = ArrayBuffer[Map[Int, String]]()
    var lineNumber = 0
    try {
      for (line <- source.getLines()) {
        val keyValueMap = Map(lineNumber -> line)
        arrayOfMaps += keyValueMap

        lineNumber += 1
      }
    } finally {
      source.close()
    }

    var concatenatedValue = arrayOfMaps(1)(1)

    if ((arrayOfMaps.length-1)>2){
      for (i <- 2 until arrayOfMaps.length) {
        concatenatedValue = concatenatedValue + arrayOfMaps(i)(i)
      }
      arrayOfMaps(1) = Map(1 -> concatenatedValue)
      arrayOfMaps.remove(2,arrayOfMaps.length-2)
    }

    arrayOfMaps
  }

  def getFileFormat():String = {
    val file = new File(filePath)
    if (file.exists() && file.isFile) {
      val fileName = file.getName
      val fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1)

      // 使用文件后缀名来判断文件类型
      fileExtension.toLowerCase() match {
        case "sam" => "SAM"
        case "fa" | "fas" | "fasta"  => "FASTA"
        case "fq" | "fastq" => "FASTQ"
        case "bam" => "BAM"
        // 可拓展

        case _ => "Other"
      }
    } else {
      "文件不存在或不是一个有效的文件"
    }

  }


}
