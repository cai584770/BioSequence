package com.cai.sequence

import scala.collection.mutable.{ArrayBuffer}

/**
 * @author cai584770
 * @date 2023/10/29 21:47
 * @Version
 */
class DNASequence(filePath: String) extends BioSequence(filePath){

  private var information: String = _
  private var DNAsequence: String = _
  private var fileformat: String = _


  def getProcessResult(filePath: String): Unit = {
    val readSequence = new ReadSequence(filePath)
    var data = ArrayBuffer[Map[Int, String]]()
    data = readSequence.process()
    fileformat = readSequence.getFileFormat()
    information = data(0)(0)
    DNAsequence = data(1)(1)
  }

  override def getInformation(): String = {
    information
  }

  override def getSequence(): String = {
    DNAsequence
  }

  override def getFormat(): String = {
    fileformat
  }


  override def length(): Int = {
    DNAsequence.length
  }

  override def comparison: Unit = {

  }

  override def format: Unit = {
    fileformat
  }

  override def compress: Unit = {
    println("cc")
  }

}



//object Main extends App {
//  val dna = new DNASequence("aaaa")
//  dna.format
//  dna.compress
//}