package org.scalaml.thompsonsampling

import scala.collection.mutable


case class SelectedArm(context: Seq[String], reward: Boolean)

/**
  * History is defined as the new batch of training set
  */
final class History(batchSize: Int) {
  private[this] val selectedArms = new mutable.ArrayBuffer[SelectedArm]

  def += (context: Seq[String], reward: Boolean): Boolean = {
    selectedArms.append(SelectedArm(context, reward))
    selectedArms.size >= batchSize
  }

  final def getSelection: Array[SelectedArm] = selectedArms.toArray
}

final class ThompsonSampling[T: Arm, U <: BayesianPredictor] extends BayesianHeuristic[T] {
  /**
   * Extract the wining arm using a draw from a specific bandit
   */
  override def selectArm(newArms: Seq[T], context: U): T = {
    val c: T = implicitly[BanditContext].apply(newArms)

    val (mean, stdDev) = context.

  }

  def update(recentHistory: History): Unit = {
    // Step 1. Apply
  }

  override def getAsymptoticBounds: Option[AsymptoticBounds[T, U]] = {
    None
  }

  /**
   * Compute the expected total regret as
   * {{{
   *   SUM_i {mu(*) - mu(i)).E[count_i] = numArms * SUM_i {mu(*) - mu(i))/count_i
   *   }}}
   */
  //  final def expectedTotalRegret: Double = bandit.expectedTotalRegret

  /**
   * Called when a selected arm has been rewarded. It is the bestArmId
   * if it has been rewarded, or another arm
   */
  //  def update(armId: String, bestArmId: String): Unit = bandit.update(armId, bestArmId)
}

