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
    // pruebas para complemento
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
    //pruebas para interseccion
    // Tests para intersección
    test("interseccion de dos conjuntos grandes") {
      val grande1 = c.grande(5, 2)
      val grande2 = c.grande(10, 3)
      val inter = c.interseccion(grande1, grande2)

      // La intersección debe tomar el valor mínimo
      assert(inter(10) === math.min(grande1(10), grande2(10)))
      assert(inter(20) === math.min(grande1(20), grande2(20)))
      assert(inter(50) === math.min(grande1(50), grande2(50)))
    }

    test("interseccion de un conjunto con su complemento debe dar valores <= 0.5") {
      val grande1 = c.grande(5, 2)
      val comp = c.complemento(grande1)
      val inter = c.interseccion(grande1, comp)

      // La intersección de un conjunto con su complemento debe ser <= 0.5
      assert(inter(10) <= 0.5)
      assert(inter(50) <= 0.5)
      assert(inter(100) <= 0.5)
    }

    test("interseccion con conjunto vacío (todo ceros)") {
      val grande1 = c.grande(5, 2)
      val vacio: c.ConjDifuso = (x: Int) => 0.0
      val inter = c.interseccion(grande1, vacio)

      assert(inter(10) === 0.0)
      assert(inter(50) === 0.0)
      assert(inter(100) === 0.0)
    }

    test("interseccion de un conjunto consigo mismo") {
      val grande1 = c.grande(5, 2)
      val inter = c.interseccion(grande1, grande1)

      assert(c.igualdad(grande1, inter) === true)
    }

    // Tests para inclusión
    test("inclusion: un conjunto está incluido en sí mismo") {
      val grande1 = c.grande(5, 2)
      assert(c.inclusion(grande1, grande1) === true)
    }

    test("inclusion: conjunto vacío incluido en cualquier conjunto") {
      val vacio: c.ConjDifuso = (x: Int) => 0.0
      val grande1 = c.grande(5, 2)

      assert(c.inclusion(vacio, grande1) === true)
    }

    test("inclusion: un conjunto no está incluido en su complemento") {
      val grande1 = c.grande(5, 2)
      val comp = c.complemento(grande1)

      // Un conjunto generalmente no está incluido en su complemento
      assert(c.inclusion(grande1, comp) === false)
    }

    test("inclusion: conjunto más restrictivo incluido en menos restrictivo") {
      val grande1 = c.grande(5, 3) // más restrictivo (exponente mayor)
      val grande2 = c.grande(5, 2) // menos restrictivo

      // grande1 debería estar incluido en grande2 para valores positivos
      assert(c.inclusion(grande1, grande2) === true)
    }

    test("inclusion: conjunto universal incluye a todos") {
      val universal: c.ConjDifuso = (x: Int) => 1.0
      val grande1 = c.grande(5, 2)

      assert(c.inclusion(grande1, universal) === true)
      assert(c.inclusion(universal, grande1) === false)
    }

    // Tests para igualdad
    test("igualdad: un conjunto es igual a sí mismo") {
      val grande1 = c.grande(5, 2)
      assert(c.igualdad(grande1, grande1) === true)
    }

    test("igualdad: dos conjuntos con los mismos parámetros son iguales") {
      val grande1 = c.grande(5, 2)
      val grande2 = c.grande(5, 2)

      assert(c.igualdad(grande1, grande2) === true)
    }

    test("igualdad: dos conjuntos con diferentes parámetros no son iguales") {
      val grande1 = c.grande(5, 2)
      val grande2 = c.grande(10, 3)

      assert(c.igualdad(grande1, grande2) === false)
    }

    test("igualdad: conjunto y su complemento no son iguales") {
      val grande1 = c.grande(5, 2)
      val comp = c.complemento(grande1)

      assert(c.igualdad(grande1, comp) === false)
    }

    test("igualdad: propiedad simétrica") {
      val grande1 = c.grande(5, 2)
      val grande2 = c.grande(8, 3)

      // Si A = B, entonces B = A
      assert(c.igualdad(grande1, grande2) === c.igualdad(grande2, grande1))
    }

    test("igualdad: unión de un conjunto consigo mismo es igual al conjunto") {
      val grande1 = c.grande(5, 2)
      val unionMisma = c.union(grande1, grande1)

      assert(c.igualdad(grande1, unionMisma) === true)
    }

    test("igualdad: intersección de un conjunto consigo mismo es igual al conjunto") {
      val grande1 = c.grande(5, 2)
      val interMisma = c.interseccion(grande1, grande1)

      assert(c.igualdad(grande1, interMisma) === true)
    }
}
