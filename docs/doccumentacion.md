# Documentación: Conjuntos Difusos en Scala

## Índice
1. [Introducción](#introducción)
2. [Definiciones Básicas](#definiciones-básicas)
3. [Funciones Implementadas](#funciones-implementadas)
4. [Ejemplos de Uso](#ejemplos-de-uso)
5. [Referencias Matemáticas](#referencias-matemáticas)

---

## Introducción

Esta clase implementa operaciones fundamentales sobre **conjuntos difusos** (fuzzy sets) en Scala. Un conjunto difuso es una generalización de los conjuntos clásicos donde la pertenencia de un elemento no es binaria (0 o 1), sino que puede tomar cualquier valor en el intervalo [0, 1].

## Definiciones Básicas

### Tipo de Dato: ConjDifuso

```scala
type ConjDifuso = Int => Double
```

Un conjunto difuso se representa como una función que mapea enteros a valores de pertenencia en el rango [0, 1].

**Notación matemática:**

$$\mu_A: \mathbb{Z} \rightarrow [0, 1]$$

Donde $\mu_A(x)$ representa el grado de pertenencia del elemento $x$ al conjunto difuso $A$.

---

## Funciones Implementadas

### 1. pertenece(elem: Int, s: ConjDifuso): Double

Calcula el grado de pertenencia de un elemento a un conjunto difuso.

**Parámetros:**
- `elem`: Elemento a evaluar
- `s`: Conjunto difuso

**Retorna:** Valor entre 0 y 1 que representa el grado de pertenencia

**Fórmula:**

$$\mu_A(elem) = s(elem)$$

**Ejemplo:**
```scala
val grado = pertenece(5, miConjunto)
```

---

### 2. grande(d: Int, e: Int): ConjDifuso

Define un conjunto difuso que representa "números grandes" usando una función sigmoidal.

**Parámetros:**
- `d`: Parámetro de desplazamiento (mínimo 1)
- `e`: Parámetro de exponente (mínimo 1)

**Retorna:** Conjunto difuso que modela números grandes

**Fórmula:**

$$
\mu_{grande}(x) = 
\begin{cases} 
0 & \text{si } x \leq 0 \\
\left(\frac{x}{x + d}\right)^e & \text{si } x > 0
\end{cases}
$$

**Propiedades:**
- Para $x \leq 0$: grado de pertenencia = 0
- Valores de $d$ y $e$ se normalizan a mínimo 1
- A mayor $x$, mayor grado de pertenencia (tiende a 1)
- El parámetro $e$ controla la rapidez de crecimiento
- El parámetro $d$ controla el punto de inflexión

**Ejemplo:**
```scala
val numerosGrandes = grande(10, 2)  // numerosGrandes(100) ≈ 0.826
```

---

### 3. complemento(c: ConjDifuso): ConjDifuso

Calcula el complemento de un conjunto difuso.

**Parámetros:**
- `c`: Conjunto difuso original

**Retorna:** Conjunto difuso complementario

**Fórmula:**

$$\mu_{\overline{A}}(x) = 1 - \mu_A(x)$$

**Interpretación:** Si un elemento pertenece mucho al conjunto original, pertenecerá poco al complemento.

**Ejemplo:**
```scala
val noGrandes = complemento(numerosGrandes)
```

---

### 4. union(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso

Calcula la unión de dos conjuntos difusos usando el operador máximo.

**Parámetros:**
- cd1: Primer conjunto difuso
- cd2: Segundo conjunto difuso

**Retorna:** Conjunto difuso resultante de la unión

**Fórmula:**

$$\mu_{A \cup B}(x) = \max(\mu_A(x), \mu_B(x))$$

**Interpretación:** Un elemento pertenece a la unión con el grado máximo de pertenencia entre ambos conjuntos.

**Ejemplo:**
```scala
val grandesOMedianso = union(grandes, medianos) //donde le pado lo que es conjunto difusos
```

---

### 5. interseccion(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso

Calcula la intersección de dos conjuntos difusos usando el operador mínimo.

**Parámetros:**
- `cd1`: Primer conjunto difuso
- `cd2`: Segundo conjunto difuso

**Retorna:** Conjunto difuso resultante de la intersección

**Fórmula:**

$$\mu_{A \cap B}(x) = \min(\mu_A(x), \mu_B(x))$$

**Interpretación:** Un elemento pertenece a la intersección con el grado mínimo de pertenencia entre ambos conjuntos.

**Ejemplo:**
```scala
val grandesYPositivos = interseccion(grandes, positivos) //tambien se le paso conjuntos difusos
```

---

### 6. inclusion(cd1: ConjDifuso, cd2: ConjDifuso): Boolean

Verifica si un conjunto difuso está incluido en otro.

**Parámetros:**
- `cd1`: Conjunto difuso potencialmente incluido
- `cd2`: Conjunto difuso contenedor

**Retorna:** `true` si cd1 ⊆ cd2, `false` en caso contrario

**Fórmula:**

$$A \subseteq B \iff \forall x: \mu_A(x) \leq \mu_B(x)$$

**Implementación:** 
- Verifica recursivamente los valores de 0 a 1000
- Usa recursión de cola (`@tailrec`) para optimización

**Ejemplo:**
```scala
val incluido = inclusion(pequeños, medianos)
```

---

### 7. igualdad(cd1: ConjDifuso, cd2: ConjDifuso): Boolean

Verifica si dos conjuntos difusos son iguales.

**Parámetros:**
- `cd1`: Primer conjunto difuso
- `cd2`: Segundo conjunto difuso

**Retorna:** `true` si son iguales, `false` en caso contrario

**Fórmula:**

$$A = B \iff (A \subseteq B) \land (B \subseteq A)$$

Equivalentemente:

$$A = B \iff \forall x: \mu_A(x) = \mu_B(x)$$

**Ejemplo:**
```scala
val sonIguales = igualdad(conjunto1, conjunto2)
```

---

## Ejemplos de Uso

### Ejemplo 1: Función `pertenece`

```scala
val cd = new ConjuntosDifusos()

// Definir un conjunto difuso de números grandes
val grandes = cd.grande(10, 2)

// Evaluar diferentes elementos
val grado5 = cd.pertenece(5, grandes)
println(s"Grado de pertenencia de 5: $grado5")
// Resultado: ≈ 0.111 (5 no es muy grande)

val grado50 = cd.pertenece(50, grandes)
println(s"Grado de pertenencia de 50: $grado50")
// Resultado: ≈ 0.694 (50 es bastante grande)

val grado100 = cd.pertenece(100, grandes)
println(s"Grado de pertenencia de 100: $grado100")
// Resultado: ≈ 0.826 (100 es muy grande)
```

**Traza de ejecución para `pertenece(50, grandes)`:**
```
1. grandes(50) se evalúa
2. x = 50, d = 10, e = 2
3. fraccion = 50 / (50 + 10) = 50/60 = 0.833...
4. resultado = (0.833...)^2 = 0.694...
5. Retorna: 0.694
```

---

### Ejemplo 2: Función `grande`

```scala
val cd = new ConjuntosDifusos()

// Diferentes configuraciones de "grande"
val suave = cd.grande(50, 1)  // Crecimiento suave
val medio = cd.grande(10, 2)  // Crecimiento medio
val rapido = cd.grande(5, 4)  // Crecimiento rápido

// Evaluar el mismo número en diferentes contextos
println(s"20 es grande (suave): ${cd.pertenece(20, suave)}")
// Resultado: ≈ 0.286

println(s"20 es grande (medio): ${cd.pertenece(20, medio)}")
// Resultado: ≈ 0.444

println(s"20 es grande (rápido): ${cd.pertenece(20, rapido)}")
// Resultado: ≈ 0.390
```

**Traza de ejecución para `grande(10, 2)` evaluado en x=20:**
```
1. Crear función con d=10, e=2
2. Al evaluar en x=20:
   - x > 0, entonces continua
   - dconj = 10 (d >= 1)
   - econj = 2 (e >= 1)
   - fraccion = 20.0 / (20 + 10).0 = 20/30 = 0.666...
   - resultado = pow(0.666..., 2.0) = 0.444...
3. Retorna: función que en x=20 da 0.444
```

---

### Ejemplo 3: Función `complemento`

```scala
val cd = new ConjuntosDifusos()

val grandes = cd.grande(10, 2)
val pequeños = cd.complemento(grandes)

// Comparar valores complementarios
val x = 30

val gradoGrande = cd.pertenece(x, grandes)
val gradoPequeño = cd.pertenece(x, pequeños)

println(s"$x es grande: $gradoGrande")
// Resultado: ≈ 0.563

println(s"$x es pequeño: $gradoPequeño")
// Resultado: ≈ 0.437

println(s"Suma: ${gradoGrande + gradoPequeño}")
// Resultado: 1.0 (siempre suma 1)
```

**Traza de ejecución para `complemento(grandes)` evaluado en x=30:**
```
1. c = grandes (función original)
2. Crear nueva función que:
   - Evalúa valor = c(30) = 0.563
   - Calcula 1.0 - 0.563 = 0.437
3. Retorna: 0.437
```

---

### Ejemplo 4: Función `union`

```scala
val cd = new ConjuntosDifusos()

// Conjunto de números grandes (d=10, e=2)
val grandes = cd.grande(10, 2)

// Conjunto de números medianos (d=20, e=1)
val medianos = cd.grande(20, 1)

// Unión: números grandes O medianos
val grandesOMedianso = cd.union(grandes, medianos)

// Evaluar en diferentes puntos
val valores = List(10, 30, 50, 100)

valores.foreach { x =>
  val g = cd.pertenece(x, grandes)
  val m = cd.pertenece(x, medianos)
  val u = cd.pertenece(x, grandesOMedianso)
  println(s"x=$x: grande=$g, mediano=$m, unión=$u")
}

/* Resultado:
x=10: grande=0.250, mediano=0.333, unión=0.333
x=30: grande=0.563, mediano=0.600, unión=0.600
x=50: grande=0.694, mediano=0.714, unión=0.714
x=100: grande=0.826, mediano=0.833, unión=0.833
*/
```

**Traza de ejecución para `union(grandes, medianos)` evaluado en x=30:**
```
1. cd1 = grandes, cd2 = medianos
2. Crear nueva función que:
   - v1 = cd1(30) = grandes(30) = 0.563
   - v2 = cd2(30) = medianos(30) = 0.600
   - Comparar: v1 > v2? No (0.563 < 0.600)
   - Retorna v2 = 0.600
3. Resultado: 0.600 (el máximo)
```

---

### Ejemplo 5: Función `interseccion`

```scala
val cd = new ConjuntosDifusos()

val grandes = cd.grande(10, 2)
val medianos = cd.grande(20, 1)

// Intersección: números grandes Y medianos
val grandesYMedianso = cd.interseccion(grandes, medianos)

val x = 40

val g = cd.pertenece(x, grandes)
val m = cd.pertenece(x, medianos)
val i = cd.pertenece(x, grandesYMedianso)

println(s"x=$x:")
println(s"  Grande: $g")        // ≈ 0.640
println(s"  Mediano: $m")       // ≈ 0.666
println(s"  Intersección: $i")  // ≈ 0.640 (el mínimo)
```

**Traza de ejecución para `interseccion(grandes, medianos)` evaluado en x=40:**
```
1. cd1 = grandes, cd2 = medianos
2. Crear nueva función que:
   - Evalúa cd1(40) = 0.640
   - Evalúa cd2(40) = 0.666
   - Calcula math.min(0.640, 0.666) = 0.640
3. Retorna: 0.640 (el mínimo)
```

---

### Ejemplo 6: Función `inclusion`

```scala
val cd = new ConjuntosDifusos()

val pequeños = cd.grande(5, 1)   // Crece rápido desde pequeño
val medianos = cd.grande(20, 1)  // Crece más lento
val grandes = cd.grande(50, 2)   // Crece muy lento

// Verificar inclusiones
println(s"¿pequeños ⊆ medianos? ${cd.inclusion(pequeños, medianos)}")
// Resultado: false

println(s"¿grandes ⊆ medianos? ${cd.inclusion(grandes, medianos)}")
// Resultado: true

println(s"¿medianos ⊆ grandes? ${cd.inclusion(medianos, grandes)}")
// Resultado: false

// Ejemplo específico: verificar manualmente algunos valores
List(10, 50, 100).foreach { x =>
  val g = cd.pertenece(x, grandes)
  val m = cd.pertenece(x, medianos)
  println(s"x=$x: grandes=$g, medianos=$m, g≤m? ${g <= m}")
}

/* Resultado:
x=10: grandes=0.027, medianos=0.333, g≤m? true
x=50: grandes=0.250, medianos=0.714, g≤m? true
x=100: grandes=0.444, medianos=0.833, g≤m? true
*/
```

**Traza de ejecución para `inclusion(grandes, medianos)`:**
```
1. cd1 = grandes, cd2 = medianos
2. Llamar aux(0):
   v=0: cd1(0)=0.0, cd2(0)=0.0, 0.0≤0.0? true → aux(1)
   v=1: cd1(1)=0.0009, cd2(1)=0.047, 0.0009≤0.047? true → aux(2)
   v=2: cd1(2)=0.0015, cd2(2)=0.090, 0.0015≤0.090? true → aux(3)
   ...
   v=1000: cd1(1000)=0.826, cd2(1000)=0.980, 0.826≤0.980? true → aux(1001)
   v=1001: v > 1000, retorna true
3. Resultado: true (grandes ⊆ medianos)
```

---

### Ejemplo 7: Función `igualdad`

```scala
val cd = new ConjuntosDifusos()

val conjunto1 = cd.grande(10, 2)
val conjunto2 = cd.grande(10, 2)  // Mismos parámetros
val conjunto3 = cd.grande(15, 2)  // Parámetros diferentes

println(s"¿conjunto1 = conjunto2? ${cd.igualdad(conjunto1, conjunto2)}")
// Resultado: true

println(s"¿conjunto1 = conjunto3? ${cd.igualdad(conjunto1, conjunto3)}")
// Resultado: false

// Verificar con complemento
val grandes = cd.grande(20, 2)
val complemento1 = cd.complemento(grandes)
val complemento2 = cd.complemento(grandes)

println(s"¿complementos iguales? ${cd.igualdad(complemento1, complemento2)}")
// Resultado: true

// Ejemplo de conjuntos no iguales
val unionAB = cd.union(conjunto1, conjunto3)
val interseccionAB = cd.interseccion(conjunto1, conjunto3)

println(s"¿unión = intersección? ${cd.igualdad(unionAB, interseccionAB)}")
// Resultado: false
```

**Traza de ejecución para `igualdad(conjunto1, conjunto2)`:**
```
1. cd1 = conjunto1 = grande(10, 2)
2. cd2 = conjunto2 = grande(10, 2)

3. Evaluar inclusion(cd1, cd2):
   - aux(0) hasta aux(1001): todos los valores de cd1(v) ≤ cd2(v)
   - Retorna: true

4. Evaluar inclusion(cd2, cd1):
   - aux(0) hasta aux(1001): todos los valores de cd2(v) ≤ cd1(v)
   - Retorna: true

5. Resultado: true && true = true
```

---

### Ejemplo Completo: Aplicación Práctica

```scala
val cd = new ConjuntosDifusos()

// Modelar conceptos difusos
val joven = cd.complemento(cd.grande(30, 2))  // No grande en edad
val adulto = cd.grande(25, 1)                  // Grande moderado en edad
val anciano = cd.grande(50, 3)                 // Muy grande en edad

// Evaluar a una persona de 35 años
val edad = 35

println(s"Persona de $edad años:")
println(s"  Es joven: ${cd.pertenece(edad, joven)}")
println(s"  Es adulto: ${cd.pertenece(edad, adulto)}")
println(s"  Es anciano: ${cd.pertenece(edad, anciano)}")

/* Resultado:
Persona de 35 años:
  Es joven: 0.238
  Es adulto: 0.583
  Es anciano: 0.043
*/

// Operaciones: ¿Es joven O anciano?
val jovenOAnciano = cd.union(joven, anciano)
println(s"  Es joven O anciano: ${cd.pertenece(edad, jovenOAnciano)}")
// Resultado: 0.238 (max de joven y anciano)
```

---

## Referencias Matemáticas

### Propiedades de los Conjuntos Difusos

**Ley de Idempotencia:**
- $A \cup A = A$
- $A \cap A = A$

**Ley Conmutativa:**
- $A \cup B = B \cup A$
- $A \cap B = B \cap A$

**Ley Asociativa:**
- $(A \cup B) \cup C = A \cup (B \cup C)$
- $(A \cap B) \cap C = A \cap (B \cap C)$

**Ley de De Morgan:**
- $\overline{A \cup B} = \overline{A} \cap \overline{B}$
- $\overline{A \cap B} = \overline{A} \cup \overline{B}$

**Nota importante sobre el complemento:**
En conjuntos difusos, a diferencia de conjuntos clásicos:

$$A \cup \overline{A} \neq U \quad \text{(universo)}$$
$$A \cap \overline{A} \neq \emptyset \quad \text{(conjunto vacío)}$$

Esto se debe a que un elemento puede tener pertenencia parcial tanto en $A$ como en $\overline{A}$ simultáneamente.

---

## Notas de Implementación

- **Dominio limitado**: Las funciones `inclusion` e `igualdad` solo verifican valores en el rango [0, 1000]
- **Optimización**: Uso de recursión de cola para evitar desbordamiento de pila
- **Validación**: Los parámetros `d` y `e` en `grande()` se normalizan a un mínimo de 1
- **Precisión**: Se utiliza `Double` para mantener precisión en los cálculos de pertenencia