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

import breeze.stats.distributions.Beta

import scala.collection.mutable

private[thompsonsampling] class Bandit[T: Arm, V <: BayesianHeuristic[T]](
    bayesianHeuristic: V
) {
  protected[this] val arms = new mutable.HashMap[String, T]

  def +=(arm: T): Unit = {
    val id = implicitly[Arm[T]].apply(arm).id
    if (!arms.contains(id))
      arms.put(id, arm)
  }

  /**
   * Add a new batch of arms to this bandit along with a new context
   *
   * @param newArms a new set of arms to be processed by the Bandit
   * @return self-reference to this bandit
   */
  def +=(newArms: Seq[T]): this.type = {
    newArms.foreach { += _ }
    bayesianHeuristic.selectArm(newArms)
    this
  }

  /**
   * Update the bandit and its arm with the id of the arm which has
   * been selected and the one which generate the highest reward.
   */
  def update(selectedArmId: String, winningArmId: String): Double = {
    arms.get(selectedArmId).map(arm => {
      val thisArm: Arm[T] = implicitly[Arm[T]].apply(arm)
    }).getOrElse(0.0)

  }
}

private[thompsonsampling] class ContextualBandit[T: Arm, V <: BayesianHeuristic[T]](
    bayesianHeuristic: V, batchSize: Int = 1
) extends Bandit[T, V](bayesianHeuristic) {

  private[this] val batch = new mutable.HashMap[String, T]
  private[this] val history: Option[History] = if (batchSize > 1) Some(new History(batchSize)) else None

  override def +=(arm: T): Unit = {
    val id = implicitly[Arm[T]].apply(arm).id
    if (!arms.contains(id))
      arms.put(id, arm)
  }

  /**
   * Update the bandit and its arm with the id of the arm which has
   * been selected and the one which generate the highest reward.
   */
  def update(selectedArmId: String, winningArmId: String): Double = {
    arms.get(selectedArmId).map(arm => {
      val thisArm: Arm[T] = implicitly[Arm[T]].apply(arm)
      // history +=
      // arms.get(winningArmId).map(winArm => thisArm.update(implicitly[Arm[T]].apply(winArm))).getOrElse(0.0)
    }).getOrElse(0.0)

  }
}
/*
private[thompsonsampling] trait Bandit[T <: Arm] {
  protected[this] val arms = new mutable.HashMap[String, T]
  def winningArm: T
  def +=(arm: T): this.type = {
    if (!arms.contains(arm.id))
      arms.put(arm.id, arm)
    this
  }
  def expectedTotalRegret: Double = {
    val (regret, count) = arms./:((0.0, 0)) { case (s, (id, arm)) => (s._1 + arm.getRegret._1, s._2 + arm.getRegret._2) }
    arms.size * regret / count
  }
  def update(armId: String, winningArmId: String): Double = arms.get(armId).map(arm => {
    arms.get(winningArmId).map(arm.update(_, 1)).getOrElse(0.0)
  }).getOrElse(0.0)
  override def toString: String = {
    val sortedArms: Iterable[(String, Double)] = arms.map { case (id, arm) => (id, arm.getRegret._1) }.toSeq.sortBy(_._2)
    sortedArms.map { case (id, arm) => s"$id: ${arm.toString}" }.mkString("\n")
  }
}
private[thompsonsampling] class BernoulliBandit[T <: Arm] extends Bandit[T] {
  override def winningArm: T = arms.map { case (key, arm) => (arm, drawBeta(arm)) }.maxBy(_._2)._1
  private def drawBeta(arm: T): Double = (new Beta(arm.successes + 1, arm.failures + 1)).draw
}
private[thompsonsampling] class StochasticBandit[T <: Arm] extends Bandit[T] {
  override def winningArm: T = arms.map { case (key, arm) => (arm, drawBeta(arm)) }.maxBy(_._2)._1
  override def update(armId: String, winningArmId: String): Double = arms.get(armId).map(arm => {
    arms.get(winningArmId).map(winArm => arm.update(winArm, if(winArm.mean > Random.nextDouble) 1 else 0)).getOrElse(0.0))
  }).getOrElse(0.0)
  private def drawBeta(arm: T): Double = (new Beta(arm.successes + 1, arm.failures + 1)).draw
}
*/
/*
private[thompsonsampling] class BernoulliBandit[T <: Arm] extends Bandit[T] {
  final def getArmsSeq: Iterable[T] = arms.values
  /**
   * Compute the expected total regret as
   * {{{
   *   SUM_i {mu(*) - mu(i)).E[count_i] = numArms * SUM_i {mu(*) - mu(i))/count_i
   *   }}}
   */
  override def expectedTotalRegret: Double = {
    val (regret, count) = arms./:((0.0, 0)) { case (s, (id, arm)) => (s._1 + arm.getRegret._1, s._2 + arm.getRegret._2) }
    arms.size * regret / count
  }
  def getBounds: Bounds = Bounds(0.0, 0.0)
  /**
   * Feedback loop
   */
  /**
   * Used to initialize the arm pool
   */
  def backfill(history: Seq[(String, String)]): Unit = history.foreach { case (id, bestId) => update(id, bestId) }
  override def toString: String = {
    val sortedArms: Iterable[(String, Double)] = arms.map { case (id, arm) => (id, arm.getRegret._1) }.toSeq.sortBy(_._2)
    sortedArms.map { case (id, arm) => s"$id: ${arm.toString}" }.mkString("\n")
  }
}
*/

/*
private[thompsonsampling] class BoundedBandit[T <: Arm] extends Bandit[T] {
  private[this] val delta_KLMap = new mutable.HashMap[String, (Double, Double)]
  private[this] val kullbackLeibler = new KullbackLeibler
  /**
   * Update the distribution of the delta and KL values
   */
  override def update(armId: String, winningArmId: String): Double = {
    val deltaArmRegret = super.update(winningArmId, armId)
    val armRegret = arms.get(armId).map(_.getRegret._1).getOrElse(0.0)
    val winingArmRegret = arms.get(winningArmId).map(_.getRegret._1).getOrElse(0.0)
    if (winingArmRegret > 0.0) {
      println(s"regretWiningArms $winningArmId  $winingArmRegret $armId ${armRegret}")
      val klDivergence = kullbackLeibler.divergence(winingArmRegret, armRegret)
      println(s"klDivergence $klDivergence")
    }
    val klDivergence = kullbackLeibler.divergence(armRegret, winingArmRegret)
    val delta_Kl = if (klDivergence == 0.0) deltaArmRegret else deltaArmRegret / klDivergence
    delta_KLMap.put(winningArmId, (deltaArmRegret, delta_Kl))
    delta_Kl
  }
  override def getBounds: Bounds = Bounds(0.0, 0.0)
  final def getDelta_KL: Iterable[(Double, Double)] = delta_KLMap.values
}
*/
