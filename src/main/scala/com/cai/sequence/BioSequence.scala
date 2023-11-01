package com.cai.sequence

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

  def compress: Unit

  def comparison:Unit

}

