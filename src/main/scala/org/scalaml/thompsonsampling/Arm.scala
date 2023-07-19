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

private[thompsonsampling] case class WeightedModel(w: Map[String, Double]) {
  final def lookup(key: String): Double = w.getOrElse(key, 0.0)

  final def margin(context: Seq[String], link: Double => Double): Double =
    link(context./:(0.0)((s, attr) => s + lookup(attr)))
}

/**
 * @todo Are params weights? Is context associated to each arm
 */
private[thompsonsampling] trait Arm[T] {
  // The pay-out function can be either linear (link = identity) or generalized
  // linear model (link = logistic, ....)
  val link: Double => Double
  def apply(t: T): Arm[T]

  val id: String
  var successes: Int = 1
  var failures: Int = 1
  protected[this] val params: Option[Vector[Double]] = _
  protected[this] val context: Option[Vector[Double]] = _
  private[this] var regret: Double = _

  final def isContextual: Boolean = context.isDefined

  def mean: Double = successes.toDouble / (successes + failures)

  def payout(contextAttributes: Seq[String], weights: WeightedModel): Double = weights.margin(contextAttributes, link)

  /**
   * Update the Bernoulli distribution (successes/failures) after this arm
   * been played and received a reward
   */
  def update(bestArm: Arm[T]): Double = {
    if (id == bestArm.id) {
      successes += 1
      bestArm.successes -= 1
    } else {
      failures += 1
      bestArm.failures -= 1
    }
    val deltaMean = (bestArm.mean - mean)
    regret += deltaMean
    deltaMean
  }

  def getRegret: (Double, Int) = (regret, successes + failures)

  override def toString: String = s"id=$id, count${successes + failures}, mean=$mean regret=$regret"
}

