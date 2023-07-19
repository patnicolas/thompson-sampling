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

import AsymptoticBounds._

case class Bounds(lrb: Double, urb: Double)

/**
 * Asymptotic bounds for regret for each arm used to validate expected
 * regret as computed using Thompson Sampling.
 */
final class AsymptoticBounds[T: Arm](ucb: (Double, Int) => Double = auerUCB) {

  /**
   * Compute the asymptotic bounds for the current distribution of mean and mean*
   * @param input sequence of pairs (mean*, mean)
   * @return (asymptotic lower, upper) regret bounds pair
   */
  def bounds(input: Iterable[(Double, Double)]): Bounds = {
    val (deltaSum, klSum) = input.map {
      case (delta, kl) => (delta, kl)
    }./:((0.0, 0.0)) {
      case (s, (delta, kl)) => (s._1 + delta, s._2 + kl)
    }

    println(s"deltaSum $deltaSum $klSum")
    val urb = ucb(deltaSum, input.size)
    val lrb = klSum * 0.5 * Math.sqrt(Math.log(input.size))
    Bounds(lrb, urb)
  }

  def bounds(input: Iterable[(Double, Double)], period: Int): Bounds = {
    val actualSize = if (input.size > period) period else input.size
    bounds(input.take(actualSize))
  }
}

object AsymptoticBounds {
  final val URB_DELTA_COEF = Math.PI * Math.PI / 3.0
  val auerUCB = (deltaSum: Double, n: Int) => 1.0 / deltaSum + URB_DELTA_COEF * deltaSum
}