package com.cai.sequence

/**
 * @author cai584770
 * @date 2023/10/29 22:17
 * @Version
 */
object DNASequenceTest {

  def main(args: Array[String]): Unit = {
    var filePath = "D:\\program\\Sequence\\Sequence\\src\\main\\scala\\data\\chr1.fa"
    val dnaSeq = new DNASequence(filePath)
    dnaSeq.getProcessResult(filePath)
    val information = dnaSeq.getInformation()
    println(information)
    val sequence = dnaSeq.getSequence()
    println(sequence)
    val len = dnaSeq.length()
    println(len)
    val format = dnaSeq.getFormat()
    println(format)


  }



}
