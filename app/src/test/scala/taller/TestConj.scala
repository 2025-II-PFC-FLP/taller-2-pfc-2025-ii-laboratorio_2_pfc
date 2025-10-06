package taller


  import org.scalatest.funsuite.AnyFunSuite
  import org.junit.runner.RunWith
  import org.scalatestplus.junit.JUnitRunner

  @RunWith(classOf[JUnitRunner])
  class TestConj extends AnyFunSuite {

    val c = new ConjuntosDifusos()

    //PRUEBAS PARA grande
    val g1 = c.grande(1, 2)
    val g2 = c.grande(5, 3)

    test("grande: x = 0 debe ser 0.0") {
      assert(math.abs(c.pertenece(0, g1) - 0.0) < 0.001)
    }

    test("grande: x = 1 con d=1, e=2 debe ser aproximadamente 0.25") {
      assert(math.abs(c.pertenece(1, g1) - 0.25) < 0.01)
    }

    test("grande: x = 5 con d=1, e=2 debe ser aproximadamente 0.69") {
      assert(math.abs(c.pertenece(5, g1) - 0.694) < 0.01)
    }

    test("grande: x = 50 con d=1, e=2 debe ser aproximadamente 0.96") {
      assert(math.abs(c.pertenece(50, g1) - 0.961) < 0.01)
    }

    test("grande: x = 10 con d=5, e=3 debe ser menor que 0.4") {
      assert(c.pertenece(10, g2) < 0.4)
    }
    // pruebas para complemento ---
    val comp = c.complemento(g1)
    test("complemento: x = 0 debe ser 1.0") {
      assert(math.abs(c.pertenece(0, comp) - 1.0) < 0.001)
    }
    test("complemento: x = 1 debe ser aproximadamente 0.75") {
      assert(math.abs(c.pertenece(1, comp) - 0.75) < 0.01)
    }
    test("complemento: x = 5 debe ser aproximadamente 0.30") {
      assert(math.abs(c.pertenece(5, comp) - 0.30) < 0.02)
    }
    test("complemento: x = 20 debe ser aproximadamente 0.09") {
      assert(math.abs(c.pertenece(20, comp) - 0.09) < 0.02)
    }
    test("complemento: x = 100 debe ser cercano a 0.02") {
      assert(math.abs(c.pertenece(100, comp) - 0.02) < 0.01)
    }
    //pruebas para union
    val un = c.union(g1, g2)
    test("union: x = 0 debe ser 0.0") {
      assert(math.abs(c.pertenece(0, un) - 0.0) < 0.001)
    }
    test("union: x = 1 debe tomar el valor más alto entre g1 y g2") {
      val esperado = math.max(c.pertenece(1, g1), c.pertenece(1, g2))
      assert(math.abs(c.pertenece(1, un) - esperado) < 0.001)
    }
    test("union: x = 3 debe ser el máximo de ambos conjuntos") {
      val esperado = math.max(c.pertenece(3, g1), c.pertenece(3, g2))
      assert(math.abs(c.pertenece(3, un) - esperado) < 0.001)
    }

    test("union: x = 10 debe coincidir con el mayor valor entre g1 y g2") {
      val esperado = math.max(c.pertenece(10, g1), c.pertenece(10, g2))
      assert(math.abs(c.pertenece(10, un) - esperado) < 0.001)
    }
    test("union: x = 100 debe ser aproximadamente igual a ambos (0.98)") {
      assert(math.abs(c.pertenece(100, un) - 0.98) < 0.02)
    }
}
