package org.scalaml.plots

import java.awt.{Color, GradientPaint, Paint}

/**
  * Generic trait for visual display of a plotting graph using jFreeChart library
  */
trait PlotTheme {
  /**
    * Select the color from an existing palette or list compatible
    * with the background of the plot.
    */
  def color(index: Int): Color

  /**
    * Define the background color of the plot
    */
  def paint(width: Int, height: Int): Paint
}

/**
  * Class that define the visual display of a plotting graph using jFreeChart library
  * with a black background. The color of the data points, graphs, labels.. are set accordingly.
  */
final class BlackPlotTheme extends PlotTheme {
  private[this] val colorList = Array[Color](Color.white, Color.cyan, Color.yellow)

  /**
    * Select the color from an existing palette or list compatible
    * with the background of the plot.
    */
  override def color(index: Int): Color = colorList(index % colorList.length)

  /**
    * Define the background color of the plot at black
    */
  override def paint(width: Int, height: Int): Paint = {
    Plot.validateDisplaySize(width, height, "")
    Color.black
  }
}

/**
  * Class that define the visual display of a plotting graph using jFreeChart library
  * with a light grey background with gradient. The color of the data points,
  * graphs, labels.. are set accordingly.
  */
final class LightPlotTheme extends PlotTheme {
  private[this] val colorList = Array[Color](Color.black, Color.red, Color.green)

  /**
    * Select the color from an existing palette or list compatible
    * with the background of the plot.
    */
  override def color(index: Int): Color = colorList(index % colorList.length)

  /**
    * Define the background color of the plot as a gradient of light gray
    */
  override def paint(width: Int, height: Int): Paint = {
    Plot.validateDisplaySize(width, height, "")
    new GradientPaint(0, 0, Color.white, width, height, Color.lightGray, false)
  }
}

// ----------------------------------  EOF --------------------------------------------------------------