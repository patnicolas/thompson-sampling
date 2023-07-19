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

package org.scalaml.thompsonsampling

import breeze.stats.distributions.{Uniform, Gaussian, LogNormal}

private[scalaml] sealed trait Pdf {
  def draw: Double
  def apply(p: Double): Double
}

private[scalaml] class GaussianPdf(mean: Double = 1.0, stdDev: Double = 0.0) extends Pdf {
  private[this] val gaussian = new Gaussian(mean, stdDev)

  override def apply(x: Double): Double = gaussian(x)
  override def draw: Double = gaussian.draw
}

private[scalaml] final object NormalPdf extends GaussianPdf(0.0, 1.0)


private[scalaml] final class LogNormalPdf(mean: Double, stdDev: Double) extends Pdf {
  private[this] val logNormal = new LogNormal(mean, stdDev)

  override def apply(x: Double): Double = logNormal(x)
  override def draw: Double = logNormal.draw
}

private[scalaml] final class UniformPdf(a: Int = 0, b: Int = 1) extends Pdf {
  private[this] val uniform = new Uniform(a, b)
  override def draw: Double = uniform.draw
  override def apply(x: Double): Double = if( x >= a && x <= b) x else (a + b)*0.5
}


class RandomDatasetGenerator[T <: Pdf](pdf: T) {
  def draw(numDataPoints: Int): Seq[Double] = Seq.tabulate(numDataPoints)( _ => pdf.draw)
}