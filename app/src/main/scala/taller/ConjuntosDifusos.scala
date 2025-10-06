package taller

class ConjuntosDifusos {

  type ConjDifuso = Int => Double

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
  }

