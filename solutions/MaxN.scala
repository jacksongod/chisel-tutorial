package TutorialSolutions

import scala.collection.mutable.HashMap
import scala.util.Random

import Chisel._ 

class MaxN(val n: Int, val w: Int) extends Module {

  private def Max2(x: UInt, y: UInt) = Mux(x > y, x, y)

  val io = new Bundle {
    val in  = Vec.fill(n){ UInt(INPUT, w) }
    val out = UInt(OUTPUT, w)
  }
  io.out := io.in.reduceLeft(Max2)
}

class MaxNTests(c: MaxN) extends Tester(c, Array(c.io)) {
  defTests {
    var allGood = true
    val vars    = new HashMap[Node, Node]()
    val rnd     = new Random()
    for (i <- 0 until 10) {
      vars.clear()
      var m = 0
      for (i <- 0 until c.n) {
        val e = rnd.nextInt(1 << c.w)
        vars(c.io.in(i)) = UInt(e)
        if (e > m) m = e
      }
      vars(c.io.out) = UInt(m)
      allGood = step(vars) && allGood
    }
    allGood
  }
}

