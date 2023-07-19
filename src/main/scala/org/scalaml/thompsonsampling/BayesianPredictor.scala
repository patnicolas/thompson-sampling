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

private[thompsonsampling] trait BayesianPredictor {

}

private[thompsonsampling] class LinearBayesianPredictor[T: Arm](dim: Int) extends BayesianPredictor {
  import LinearBayesianPredictor._

  private[this] var means: Vector[Double] = Vector.fill(dim)(0.0)
  private[this] val expectedMean: Double = _
  private[this] val f: Vector[Double] = Vector.fill(dim)(0.0)
  private[this] var epsilon: Double = 0.5

  private def LiParameter(epsilon: Double): Double = {
    require(epsilon >= MinEpsilon && epsilon <= 1.0, s"LinearBayesianPredictor Epsilon $epsilon is out of range ]0,1]")
    RGaussian*Math.sqrt(24.0*dim*Math.log(LiInvDelta)/epsilon)
  }

  /*
  def play(bandit: Bandit): T = {
    if(bandit.isContextual)
      bandit.arms.map(arm => (new GaussianPdf(mu, LiParameter(epsilon))).draw)

  }
  */

  def update: Unit = {

  }
}

private[thompsonsampling] object LinearBayesianPredictor {
  final val MinEpsilon = 0.05
  final val LiDelta = 0.5
  final val LiInvDelta: Double = 1.0/LiDelta
  final val RGaussian: Double = 0.5
}

private[thompsonsampling] class RegularizedLogBayesianPredictor(
  lambda: Double,
  dim: Int) extends BayesianPredictor {
  import Math._

  private[this] var liFactor: Double = lambda
  // private[this] val f: Vector[Double] = Vector.fill(dim)(liFactor)
  private[this] var q: Vector[Double] = Vector.fill(dim)(liFactor)
  private[this] var mean: Vector[Double] = Vector.fill(dim)(0.0)
  private[this] var weights: Vector[Double] =

  private def predict(marginRewards: Seq[(Double, Double)]): Double =
    marginRewards./:(0.0) { case (s, (margin, reward)) => s + logExp(reward * margin) }

  private def logistic(x: Double): Double = 1.0 / (1.0 + exp(-x))
  private def logExp(x: Double): Double = log(1.0 + exp(-x))

  private def normDistribution(reward: Double, context: Vector[Double]): (Double, Vector[Double]) = {
    liFactor += context.map(x => x * x).sum
    (0 until q.size).foreach { n => q(n) + context(n) * reward }
    val stdDev = 1.0 / liFactor
    (stdDev, q.map(_ * stdDev))
  }

  private def train(history: History): Unit = {
    q = history.getSelection.map(selectedArm => {
      val p: Double = logistic(mean.zip(selectedArm.context).map { case (m, x) => m * 1.0 }.sum)
      1.0 * p * (1.0 - p)
    }).toVector
    // TODO update mean
  }

  private def loss(marginRewards: Seq[(Double, Double)], weightedModel: WeightedModel): Double = {
    predict(marginRewards) + (0 until dim)./:(0.0)((s, n) =>  s + q(n)*(weightedModel(n) - mean(n)))
  }
}