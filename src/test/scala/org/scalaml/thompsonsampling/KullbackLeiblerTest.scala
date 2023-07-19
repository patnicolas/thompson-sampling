package org.scalaml.thompsonsampling

import org.scalatest.{FunSuite, Matchers}

class KullbackLeiblerTest extends FunSuite with Matchers {
  test("Test KL divergence with two uniform distributions") {

    val dataGenerator1 = new RandomDatasetGenerator[UniformPdf](new UniformPdf)
    val dataset1 = dataGenerator1.draw(1000)

    val dataGenerator2 = new RandomDatasetGenerator[UniformPdf](new UniformPdf)
    val dataset2 = dataGenerator2.draw(1000)

    val div = KullbackLeibler.divergence(dataset1.zip(dataset2))
    Math.abs(div) > 100
  }

  test("Test KL divergence with two Normal distributions") {

    val dataGenerator1 = new RandomDatasetGenerator[GaussianPdf](new GaussianPdf)
    val dataset1 = dataGenerator1.draw(1000)

    val dataGenerator2 = new RandomDatasetGenerator[GaussianPdf](new GaussianPdf)
    val dataset2 = dataGenerator2.draw(1000)

    val div = KullbackLeibler.divergence(dataset1.zip(dataset2))
    Math.abs(div) < 2.0
  }

  test("Test KL divergence with on Normal distribution and one uniform distribution") {

    val dataGenerator1 = new RandomDatasetGenerator[GaussianPdf](new GaussianPdf)
    val dataset1 = dataGenerator1.draw(1000)

    val dataGenerator2 = new RandomDatasetGenerator[UniformPdf](new UniformPdf)
    val dataset2 = dataGenerator2.draw(1000)

    val div = KullbackLeibler.divergence(dataset1.zip(dataset2))
    Math.abs(div) > 200
  }

  test("Test KL divergence corner case ") {
    val kl = new KullbackLeibler
    val div1 = kl.divergence(2.5, 3.6)
    println(s"div1 $div1")

    val div2 = kl.divergence(2.5, 3.0)
    println(s"div2 $div2")

    val div3 = kl.divergence(0.0, 3.0)
    println(s"div3 $div3")
    val div4 = kl.divergence(3.0, 3.0)
    println(s"div4 $div4")

    val div5 = kl.divergence(3.0, 0.0)
    println(s"div5 $div5")

    val div6 = kl.divergence(1.2, 3.0)
    println(s"div6 $div6")
  }

}
