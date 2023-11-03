package com.cai.sequence

import com.cai.sequence.Impl.DNASequence

import java.util
import scala.collection.JavaConverters._

/**
 * @author cai584770
 * @date 2023/10/29 22:17
 * @Version
 */
object DNASequenceTest {

  def main(args: Array[String]): Unit = {
    val ref_file_path = "/home/caiorg/program/Seq/data/data/pseudo88_chr1.fa"
    val tar_file_path = "/home/caiorg/program/Seq/data/data/pseudo108_chr1.fa"

    val ref_seq = new DNASequence(ref_file_path)
    val tar_seq = new DNASequence(tar_file_path)

    tar_seq.getProcessResult(tar_file_path)

    // 基础信息
    val information = tar_seq.getInformation()
    println(information)
    val sequence = tar_seq.getSequence()
//    println(sequence)
    val temp_strs1 = sequence.take(100)
    println(temp_strs1)
    val len = tar_seq.length()
    println(len)
    val format = tar_seq.getFormat()
    println(format)

    // 获取ref序列
    ref_seq.getProcessResult(ref_file_path)
    val ref_sequence = ref_seq.getSequence()

    // 以ref为参考序列压缩target 结果保存在List[String]
    val compress_result:util.List[String] = tar_seq.compress(ref_sequence)
    val scalaList = compress_result.asScala.take(2).toList
    scalaList.foreach(println)

    // 解压
    val target_sequence:String = ref_seq.uncompress(compress_result)
    val temp_strs = target_sequence.take(100)
    println(temp_strs)

  }



}
