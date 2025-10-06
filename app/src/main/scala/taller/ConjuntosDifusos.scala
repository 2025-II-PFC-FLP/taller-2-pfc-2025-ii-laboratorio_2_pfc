package taller

import scala.annotation.tailrec

class ConjuntosDifusos {

  type ConjDifuso = Int => Double
  def pertenece(elem: Int, s: ConjDifuso): Double = {
    s(elem)
  }
  // Define un conjunto difuso de números grandes
  // usando la fórmula (n / (n + d))^e
  def grande(d: Int, e: Int): ConjDifuso = {
    (x: Int) => {
      if (x <= 0) {
        0.0
      } else {
        val dconj = if (d < 1) 1 else d
        val econj = if (e < 1) 1 else e
        val fraccion = x.toDouble / (x + dconj).toDouble //(n / (n + d))^e
        math.pow(fraccion, econj.toDouble) //(n / (n + d))^e}
      }
    }
    }

  //se calcula como 1-grado de pertenencia
  def complemento(c: ConjDifuso): ConjDifuso = {
    (x: Int) => {
      //decimos que:
      val valor = c(x)
      1.0 - valor
    }
  }

  //se toma el valor máximo entre los dos conjuntos
  //f1 U f2 = max(f1,f2)
  def union(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
    (x: Int) => {
      val v1 = cd1(x)
      val v2 = cd2(x)
      if (v1 > v2) v1 else v2
    }
  }

  def interseccion(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
    (x: Int) => math.min(cd1(x), cd2(x))
  }

  def inclusion(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
    @tailrec
    def aux(v: Int): Boolean = {
      if (v > 1000) true
      else if (cd1(v) > cd2(v)) false
      else aux(v + 1)
    }
    aux(0)
  }

  def igualdad(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
    inclusion(cd1, cd2) && inclusion(cd2, cd1)
  }

}

