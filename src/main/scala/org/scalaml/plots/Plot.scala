/**
 * Copyright 2020 Patrick R. Nicolas. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 */
package org.scalaml.plots

import org.jfree.chart.{ChartFrame, JFreeChart}
import org.jfree.chart.title.TextTitle

import scala.collection._
import Plot._

object ChartType extends Enumeration {
  type ChartType = Value
  val LINE, TIME_SERIES, SCATTER, BAR = Value
}

case class Legend(key: String, title: String, xLabel: String, yLabel: String) {
  def toList: List[String] = List[String](key, title, xLabel, yLabel)
}

import java.awt.Stroke

/**
  * Generic plotting class that uses the JFreeChart library.
  */
abstract class Plot protected (legend: Legend, theme: PlotTheme) {
  val strokeList: immutable.List[Stroke]
  def stroke(index: Int) = strokeList(index % strokeList.size)

  /**
    * DisplayUtils array of tuple (x,y) in a 2D plot for a given width and height
    */
  def display(xy: Vector[(Double, Double)], width: Int, height: Int): Boolean

  /**
    * DisplayUtils a vector of Double value in a 2D plot with counts [0, n] on X-Axis and
    * vector value on Y-Axis with a given width and height
    */

  def display(y: Array[Double], width: Int, height: Int): Boolean

  protected def createFrame(id: String, chart: JFreeChart): Unit = {
    val frame = new ChartFrame(s"Chart ${count + 1}: $id", chart)
    val anchor = getLocation
    frame.setLocation(anchor._1, anchor._2)
    frame.pack()
    frame.setVisible(true)
  }

  protected def setTitle(title: String, chart: JFreeChart): Unit = {
    val textTitle = new TextTitle(
      title,
      new java.awt.Font("Calibri", java.awt.Font.PLAIN, 12)
    )
    chart.setTitle(textTitle)
  }
}

/**
  * Companion object for the Plot class. This singleton define the method that validate the
  * display of any type of plots used in Scala for Machine Learning.
  */
object Plot {
  import scala.collection._

  // type PlotInfo = (String, String, String)
  final val DISPLAY_OFFSET = 25

  var count = 0
  final def getLocation: (Int, Int) = {
    count += 1
    val offset = count * DISPLAY_OFFSET
    (offset, offset % 420)
  }

  private val DEFAULT_WIDTH = 320
  private val DEFAULT_HEIGHT = 240

  private val MIN_DISPLAY_SIZE = 60
  private val MAX_DISPLAY_SIZE = 1280

  /**
    * Validate the input values for the display for a particular plot
    */
  def validateDisplay[T](y: T, width: Int, height: Int, comment: String): Boolean = {
    validateDisplaySize(width, height, comment)
  }

  /**
    * Validate the input values for the display for a particular plot
    */
  @throws(classOf[IllegalArgumentException])
  def validateDisplayUtils(y: Array[Double], width: Int, height: Int, comment: String): Boolean = {
    require(y.nonEmpty, s"$comment Cannot display an undefined series")
    validateDisplaySize(width, height, comment)
  }

  /**
    * Validate the input values for the display for a particular plot
    */
  @throws(classOf[IllegalArgumentException])
  def validateDisplayUtils(
    y: immutable.List[Double],
    width: Int,
    height: Int,
    comment: String
  ): Boolean = {
    validateDisplayUtils(y.toArray, width, height, comment)
  }

  /**
    * Validate the input values for the display for a particular plot
    */
  @throws(classOf[IllegalArgumentException])
  def validateDisplayUtils(
    y: immutable.Vector[Double],
    width: Int,
    height: Int,
    comment: String
  ): Boolean = {
    validateDisplayUtils(y.toArray, width, height, comment)
  }

  /**
    * Validate the display dimension for a particular plot
    * @param height  Height of the display
    * @param width Width of the display
    * @param comment Comments to be added to the chart or plot
    * @throws IllegalArgumentException if the display height or width is out or range
    */
  @throws(classOf[IllegalArgumentException])
  def validateDisplaySize(width: Int, height: Int, comment: String): Boolean = {
    require(
      width > MIN_DISPLAY_SIZE && width < MAX_DISPLAY_SIZE,
      s"$comment Width $width is out of range"
    )
    require(
      height > MIN_DISPLAY_SIZE && height < MAX_DISPLAY_SIZE,
      s"$comment  height $height is out of range"
    )
    true
  }
}

// ------------------------  EOF ----------------------------------------------