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

/**
 * Generic Kullback-Leibler divergence.
 * The divergence is used to estimate the lower asymptotic bound for the
 * cumulative regret
 */
private[thompsonsampling] class KullbackLeibler {
  import scala.collection.mutable
  private[this] val dataset = new mutable.ArrayBuffer[(Double, Double)]

  /**
   * Generic divergence for a parameterized pdf, f with one parameter
   */
  final def divergence(px: Double, py: Double): Double = {
    dataset.append((px, py))
    KullbackLeibler.divergence(dataset)
  }
}

/**
 * Companion object for the generic computation of the divergence
 */
private[thompsonsampling] object KullbackLeibler {
  final val Eps = 1e-12
  type Dataset = Iterable[(Double, Double)]

  /**
   * Generic divergence for a parameterized pdf, f with one parameter
   */
  final def divergence(xy: Dataset, param: Int, f: (Double, Int) => Double): Double =
    -xy.filter { case (x, y) => Math.abs(y) > Eps }
      ./:(0.0) {
        case (s, (x, y)) => {
          val px = f(x, param)
          s + px * Math.log(px / y)
        }
      }

  /**
   * Generic divergence for a parameterized pdf, f with one parameter
   */
  final def divergence(xy: Dataset): Double =
    -xy.filter { case (x, y) => Math.abs(y) > Eps }
      ./:(0.0) {
        case (s, (x, y)) => if (x < Eps) s else s + x * Math.log(x / y)
      }
}

