// See LICENSE for license details.

package dsptools

import chisel3._
import org.scalatest.{Matchers, FreeSpec}

//scalastyle:off magic.number

class BaseNCircuit extends Module {
  val thing = Reg(BaseN(Seq.fill(5)(UInt(4)), rad = 3))
  val io = new Bundle {
    val inc = UInt(INPUT, 3)
    val out = thing.cloneType
  }
  val increment = BaseN(Seq.fill(5)(UInt(4)), rad = 3)

  increment := io.inc


  thing := thing + increment
  io.out := thing
}

class BaseNCircuitTester(c: BaseNCircuit) extends DspTester(c) {
  for(_ <- 0 to 7) {
    poke(c.io.inc, 1)
    step(1)
    println(f"current value ${peek(c.io.out.underlying.asUInt())}%04x") //scalastyle:off regex
  }
}

class BaseNSpec extends FreeSpec with Matchers {
  "baseN tester increments" in {
    chisel3.iotesters.Driver(() => new BaseNCircuit) { c =>
      new BaseNCircuitTester(c)
    } should be(true)

  }
}