package org.scalaml.thompsonsampling

/**
 * Generic Heuristic for a Bayesian decision rule. The heuristic can
 * be either parametric such as Thompson Sampling or not.
 */
private[thompsonsampling] abstract class BayesianHeuristic[T: Arm] {
  def selectArm(arms: Seq[T]): T

  def getAsymptoticBounds: Option[AsymptoticBounds[T]]
}
