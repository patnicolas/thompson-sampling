package org.scalaml.thompsonsampling

import org.scalatest.{FunSuite, Matchers}

final class AsymptoticRegretBoundsTest extends FunSuite with Matchers {
  /*
    // Simulate the linear increase of reward (mean = success/num trials) for a given arm
  def armReward(period: Int): Seq[Double] = Seq.tabulate(period)(n => n/(period-2) + 0.01)
  def bestArm2ArmDelta(otherArm: Seq[Double]): Seq[Double] =
    otherArm.zipWithIndex.map{ case (r, n) => r + 0.01*scala.util.Random.nextDouble/(n+1) }
  val ucb = (deltaSum: Double, period: Int) => deltaSum*Math.sqrt(period*Math.log(period))
  test("Regret bounds test manual validation") {
    val input: Iterable[(Double, Double)] = Seq[(Double, Double)](
      (0.5, 0.4), (0.6, 0.6), (0.7, 0.6), (0.7, 0.7)
    )
    val asymptoticRegret = new AsymptoticRegretBounds[Arm, BoundedBandit[Arm]](ucb)
    val bounds = asymptoticRegret.bounds(input)
    println(s"bounds $bounds")
  }
  test("Test lower asymptotic regret bounds") {
    val thisArmMean = armReward(10000)
    val bestArmMean = bestArm2ArmDelta(thisArmMean)
    val asymptoticRegret = new AsymptoticRegretBounds[Arm, BoundedBandit[Arm]](ucb)
    val meansDelta: Iterable[(Double, Double)] = bestArmMean.zip(thisArmMean)
    val bounds = asymptoticRegret.bounds(meansDelta)
    println(s"URB = ${bounds.urb}")
    println(s"LRB = ${bounds.lrb}")
  }
  test("Test Variable asymptotic regret bounds custom ucb function") {
    val thisArmMean = armReward(10000)
    val bestArmMean = bestArm2ArmDelta(thisArmMean)
    val asymptoticRegret = new AsymptoticRegretBounds[Arm, BoundedBandit[Arm]](ucb)
    val meansDelta: Iterable[(Double, Double)] = bestArmMean.zip(thisArmMean)
    (5000 until 10000 by 2000) foreach{ sz =>
      val bounds = asymptoticRegret.bounds(meansDelta.take(sz))
      println(s"bounds ${bounds.toString}")
    }
  }
  test("Test Variable asymptotic regret bounds Auer formula") {
    val thisArmMean = armReward(10000)
    val bestArmMean = bestArm2ArmDelta(thisArmMean)
    val asymptoticRegret = new AsymptoticRegretBounds[Arm, BoundedBandit[Arm]]()
    val meansDelta: Iterable[(Double, Double)] = bestArmMean.zip(thisArmMean)
    (5000 until 10000 by 2000) foreach{ sz =>
      val bounds = asymptoticRegret.bounds(meansDelta.take(sz))
      println(s"bounds ${bounds.toString}")
    }
  }
  */
}
