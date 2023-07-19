package org.scalaml.thompsonsampling

import org.scalaml.plots.{BlackPlotTheme, Legend, LightPlotTheme, LinePlot}
import org.scalatest.{FunSuite, Matchers}

final class ThompsonSamplingTest extends FunSuite with Matchers {

  test("Thompson sampling") {
    /*
    import scala.collection.mutable
    class MyArm(myId: String) extends Arm {
      val id: String = myId
    }
    final val numArms = 128
    final val selectedArmMean = 38
    val boundedBandit: BoundedBandit[MyArm] = (0 until numArms)./:(new BoundedBandit[MyArm])(
      (bandit, n) => bandit += new MyArm(n.toString)
    )
    val bernouilliThompsonSampling = new ThompsonSampling[MyArm, Bandit[MyArm]](boundedBandit)
    val gaussian = new GaussianPdf(selectedArmMean, 14.0)
    // Initialize - back fill the selection of best arm
    val output = (0 until 8000)./:(new mutable.ArrayBuffer[TSOutput])((s, n) => {
      val selectedArmId = scala.util.Random.nextInt(numArms)
      val bestArmId = gaussian.draw.floor.toInt
      bernouilliThompsonSampling.update(selectedArmId.toString, bestArmId.toString)
      val ucb = (deltaSum: Double, period: Int) => deltaSum * Math.sqrt(period * Math.log(period))
      val asymptoticRegret = new AsymptoticRegretBounds[MyArm, BoundedBandit[MyArm]]()
      val asymptoticBounds = asymptoticRegret.banditBounds(boundedBandit, n)
      val selectedArm: Arm = bernouilliThompsonSampling.winningArm(boundedBandit.getArmsSeq)
      s += TSOutput(bestArmId, asymptoticBounds.lrb, asymptoticBounds.urb, bernouilliThompsonSampling.expectedTotalRegret(selectedArm.id, bestArmId.toString))
    })
    val cumulRegret = output.map(_.regret)
    val dataset: List[(Vector[Double], String)] = List[(Vector[Double], String)](
      (cumulRegret.toVector, "cumulative regret"),
      (output.map(_.upperBound).toVector, "Upper bound"),
      (output.map(_.lowerBound).toVector, "Lower bound")
    )
    val plot = new LinePlot(Legend("Best arm distribution", "Distribution id best arm", "trials", "Best arm id"), new LightPlotTheme)
    plot.display(output.map(_.bestArm.toDouble).toArray, 400, 300)
    val plot5 = new LinePlot(Legend("Best arm distribution", "Average cumulative regret with asymptotic bounds", "Trials", "Cumulative regret/trials"), new BlackPlotTheme)
    plot5.display(dataset, 600, 500)
    */

    /*
    val plot2 = new LinePlot(Legend("Regret", "Thompson sampling expected regret", "trials", "Cumul. regret"), new BlackPlotTheme)
    plot2.display(cumulRegret.toArray, 400, 300)
    val plot3 = new LinePlot(Legend("Upper bound", "Upper bound", "trials", "Cumul. regret"), new BlackPlotTheme)
    plot3.display(output.map(_.upperBound).toArray, 400, 300)
    val plot4 = new LinePlot(Legend("Lower bound", "Lower bound", "trials", "Cumul. regret"), new BlackPlotTheme)
    plot4.display(output.map(_.lowerBound).toArray, 400, 300)
    */
  }
}
