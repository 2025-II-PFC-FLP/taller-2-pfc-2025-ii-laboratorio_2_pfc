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

### 1. `pertenece(elem: Int, s: ConjDifuso): Double`

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

### 2. `grande(d: Int, e: Int): ConjDifuso`

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
val numerosGrandes = grande(10, 2)
// numerosGrandes(100) ≈ 0.826
```

---

### 3. `complemento(c: ConjDifuso): ConjDifuso`

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

### 4. `union(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso`

Calcula la unión de dos conjuntos difusos usando el operador máximo.

**Parámetros:**
- `cd1`: Primer conjunto difuso
- `cd2`: Segundo conjunto difuso

**Retorna:** Conjunto difuso resultante de la unión

**Fórmula:**

$$\mu_{A \cup B}(x) = \max(\mu_A(x), \mu_B(x))$$

**Interpretación:** Un elemento pertenece a la unión con el grado máximo de pertenencia entre ambos conjuntos.

**Ejemplo:**
```scala
val grandesOMedianso = union(grandes, medianos)
```

---

### 5. `interseccion(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso`

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
val grandesYPositivos = interseccion(grandes, positivos)
```

---

### 6. `inclusion(cd1: ConjDifuso, cd2: ConjDifuso): Boolean`

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

### 7. `igualdad(cd1: ConjDifuso, cd2: ConjDifuso): Boolean`

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

### Ejemplo Completo

```scala
val cd = new ConjuntosDifusos()

// Definir conjuntos difusos
val numerosGrandes = cd.grande(10, 2)
val numerosPequeños = cd.complemento(numerosGrandes)

// Evaluar pertenencia
val grado50 = cd.pertenece(50, numerosGrandes)
println(s"50 es grande con grado: $grado50")

// Operaciones entre conjuntos
val unionConjuntos = cd.union(numerosGrandes, numerosPequeños)
val interseccionConjuntos = cd.interseccion(numerosGrandes, numerosPequeños)

// Verificar relaciones
val estaIncluido = cd.inclusion(numerosPequeños, numerosGrandes)
val sonIguales = cd.igualdad(numerosGrandes, numerosPequeños)
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