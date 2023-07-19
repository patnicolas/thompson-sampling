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

import java.awt.{BasicStroke, Stroke}

import org.jfree.chart.ChartFactory
import org.jfree.chart.axis.NumberAxis
import org.jfree.chart.plot.{PlotOrientation, XYPlot}
import org.jfree.chart.renderer.xy.{XYDotRenderer, XYShapeRenderer}
import org.jfree.data.xy.{XYSeries, XYSeriesCollection}
import org.jfree.util.ShapeUtilities

import scala.collection._
import Plot._

/**
  * Class to create a Scatter plot using the JFreeChart library.
  */
final class ScatterPlot(legend: Legend, theme: PlotTheme) extends Plot(legend, theme) {
  import java.awt.{Shape, Stroke}

  val strokeList: immutable.List[Stroke] = ScatterPlot.strokesList

  /**
    * DisplayUtils array of tuple (x,y) in a Scatter plot for a given width and height
    */
  override def display(xy: Vector[(Double, Double)], width: Int, height: Int): Boolean = {
    val validDisplay = validateDisplay[Vector[(Double, Double)]](xy, width, height, "ScatterPlot.display")
    if (validDisplay) {
      val series = xy./:(new XYSeries(legend.title))((s, xy) => { s.add(xy._1, xy._2); s })

      val seriesCollection = new XYSeriesCollection
      seriesCollection.addSeries(series)
      draw(seriesCollection, width, height)
    }
    validDisplay
  }

  /**
    * DisplayUtils a vector of Double value in a Scatter plot with counts [0, n] on X-Axis and
    * vector value on Y-Axis with a given width and height
    */
  override def display(y: Array[Double], width: Int, height: Int): Boolean = {
    val validDisplay = validateDisplay[Array[Double]](y, width, height, "ScatterPlot.display")

    if (validDisplay) {
      val series = y.zipWithIndex./:(new XYSeries(legend.title)) { case (s, (x, n)) => s.add(x, n); s }
      val seriesCollection = new XYSeriesCollection
      seriesCollection.addSeries(series)
      draw(seriesCollection, width, height)
    }
    validDisplay
  }

  /**
    * DisplayUtils two two-dimension datasets (x, y) in this scatter plot
    */
  @throws(classOf[IllegalArgumentException])
  def display(xy1: Vector[(Double, Double)], xy2: Vector[(Double, Double)], width: Int, height: Int): Boolean = {
    require(
      xy1.nonEmpty,
      "ScatterPlot.DisplayUtils Cannot display with first series undefined"
    )
    require(
      xy2.nonEmpty,
      "ScatterPlot.DisplayUtils Cannot display with second series undefined "
    )

    val validDisplay = validateDisplaySize(width, height, "ScatterPlot.DisplayUtils")
    if (validDisplay) {

      val seriesCollection1 = new XYSeriesCollection
      val seriesCollection2 = new XYSeriesCollection
      val series1 = xy1./:(new XYSeries(legend.title))((s, xy) => { s.add(xy._1, xy._2); s })
      val series2 = xy2./:(new XYSeries(legend.title))((s, xy) => { s.add(xy._1, xy._2); s })
      seriesCollection1.addSeries(series1)
      seriesCollection2.addSeries(series2)

      val chart = ChartFactory.createScatterPlot(legend.title, legend.xLabel, legend.yLabel,
        seriesCollection1, PlotOrientation.VERTICAL, true, false, false)
      setTitle(legend.title, chart)

      val plot = chart.getPlot.asInstanceOf[XYPlot]
      val renderer1 = new XYDotRenderer
      plot.setRenderer(0, renderer1)
      plot.setDomainAxis(0, new NumberAxis("x"))
      renderer1.setSeriesPaint(0, theme.color(0))
      renderer1.setDotHeight(4)
      renderer1.setDotWidth(4)

      plot.setDataset(1, seriesCollection2)
      val renderer2 = new XYShapeRenderer
      plot.setRenderer(1, renderer2)
      renderer2.setSeriesShape(0, ShapeUtilities.createDiamond(3.0F))
      renderer2.setSeriesPaint(0, theme.color(1))

      plot.setBackgroundPaint(theme.paint(width, height))
      createFrame(legend.title, chart)
    }
    validDisplay
  }

  import scala.collection.immutable.List
  type OBS = List[Vector[(Double, Double)]]
  def display(xs: OBS, lbls: List[String], w: Int, h: Int): Boolean = {
    val validDisplay = validateDisplay[OBS](xs, w, h, "ScatterPlot.display")
    if (validDisplay) {

      val seriesCollectionsList = xs.zipWithIndex
        ./:(scala.List[XYSeriesCollection]())((xs, xy) => {
          val seriesCollection = new XYSeriesCollection
          val series = xy._1./:(new XYSeries(lbls(xy._2)))((s, t) => {
            s.add(t._1, t._2)
            s
          })
          seriesCollection.addSeries(series)
          seriesCollection :: xs
        })

      val chart = ChartFactory.createScatterPlot(legend.title, legend.xLabel, legend.yLabel,
        seriesCollectionsList.last, PlotOrientation.VERTICAL, true, false, false)
      setTitle(legend.title, chart)
      val plot = chart.getPlot.asInstanceOf[XYPlot]

      val renderer1 = new XYDotRenderer
      plot.setRenderer(0, renderer1)
      plot.setDomainAxis(0, new NumberAxis("Trading sessions"))
      renderer1.setSeriesPaint(0, theme.color(0))
      renderer1.setDotHeight(4)
      renderer1.setDotWidth(4)

      val shapes = Array[Shape](
        ShapeUtilities.createDiamond(3.0F),
        ShapeUtilities.createRegularCross(2.0F, 2.0F)
      )

      seriesCollectionsList.dropRight(1).zipWithIndex.foreach(sColi => {
        plot.setDataset(sColi._2 + 1, sColi._1)
        val renderer = new XYShapeRenderer
        plot.setRenderer(sColi._2 + 1, renderer)
        renderer.setSeriesShape(0, shapes(sColi._2 % shapes.length))
        renderer.setSeriesPaint(0, theme.color(sColi._2 + 1))
      })

      plot.setBackgroundPaint(theme.paint(w, h))
      createFrame(legend.title, chart)
    }
    validDisplay
  }

  private def draw(series: XYSeriesCollection, w: Int, h: Int) {
    validateDisplaySize(w, h, "ScatterPlot.draw")

    val chart = ChartFactory.createScatterPlot(legend.title, legend.xLabel, legend.yLabel, series,
      PlotOrientation.VERTICAL, true, false, false)
    setTitle(legend.title, chart)

    val plot = chart.getPlot.asInstanceOf[XYPlot]

    val renderer = new XYDotRenderer
    plot.setRenderer(renderer)
    renderer.setSeriesShape(0, ShapeUtilities.createDiamond(4.0F))
    renderer.setSeriesPaint(0, theme.color(3))
    renderer.setDotHeight(3)
    renderer.setDotWidth(3)

    plot.setBackgroundPaint(theme.paint(w, h))
    createFrame(legend.title, chart)
  }
}

object ScatterPlot {

  val strokesList: immutable.List[Stroke] = immutable.List[Stroke](
    new BasicStroke(1.0f),
    new BasicStroke(2.0f),
    new BasicStroke(3.0f)
  )

  private val DEFAULT_WIDTH = 320
  private val DEFAULT_HEIGHT = 260

  def display(
    y: Array[Double],
    legend: Legend,
    theme: PlotTheme
  ): Boolean = {
    require(
      y.nonEmpty,
      s"${legend.key}.display Cannot plot an undefined time series"
    )

    val plotter = new ScatterPlot(legend, theme)
    plotter.display(y, DEFAULT_WIDTH, DEFAULT_WIDTH)
  }

  def display(
    xySeries: Vector[(Double, Double)],
    legend: Legend,
    theme: PlotTheme
  ): Boolean = display(xySeries, Vector.empty[(Double, Double)], legend, theme)

  def display(
    xySeries1: Vector[(Double, Double)],
    xySeries2: Vector[(Double, Double)],
    legend: Legend,
    theme: PlotTheme
  ): Boolean = {

    require(xySeries1.nonEmpty, s"${legend.key} display Cannot plot an undefined time series")

    val plotter = new ScatterPlot(legend, theme)

    if (xySeries2.nonEmpty)
      plotter.display(xySeries1, xySeries2, DEFAULT_WIDTH, DEFAULT_WIDTH)
    else
      plotter.display(xySeries1, DEFAULT_WIDTH, DEFAULT_WIDTH)
  }
}

// ------------------------  EOF ----------------------------------------------