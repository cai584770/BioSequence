package com.cai.sequence

import java.util

/**
 * @author cai584770
 * @date 2023/10/29 21:47
 * @Version
 */
abstract class BioSequence(filePath:String) {

//  def genomeName:String
//
//  def institution:String
//
//  def project:String
//
//  def id:Int
//
//  def chromosome:String
//
//  def version:String

  def getInformation:String

  def getSequence:String

  def getFormat:String

  def length:Int

  def format: Unit

  def compress(reference_seq :String): util.List[String]

  def uncompress(compress_result :util.List[String]): String


  def comparison:Unit

}

