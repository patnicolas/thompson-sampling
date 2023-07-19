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