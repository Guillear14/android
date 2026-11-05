# Parte 1: Fundamentos del Lenguaje Kotlin
### 2º DAM — Programación Multimedia y Dispositivos Móviles (PMDM)

Este documento aborda en profundidad la arquitectura del sistema Android, la estructura de proyectos, la depuración con Logcat y la sintaxis completa del lenguaje Kotlin, cubriendo tanto `Android_Ud1_Introduccion.pdf` como `Android_ProgramacionKotlin.pdf` y los repositorios oficiales de examen.

---

## 0. Plataforma Android y Entorno de Desarrollo (`Android_Ud1_Introduccion.pdf`)

### 0.1 Capas de la Arquitectura de Android

El Framework de Android ofrece clases para interactuar con el sistema y acceder a funcionalidades del dispositivo (cámara, Wi-Fi, Bluetooth). El acceso al hardware se realiza mediante **abstracciones de capas**: las capas inferiores resuelven problemas pequeños y la combinación de todas provee la funcionalidad completa.

#### A) Resumen de 4 Capas (Diapositiva 4 del PDF oficial):
1. **Aplicaciones**: Apps de usuario y del sistema.
2. **Framework**: Servicios y gestores accesibles mediante API.
3. **Sistema Operativo Android**: Bibliotecas nativas C/C++, Runtime ART y Kernel.
4. **Hardware del dispositivo**: Pantalla, procesador, memoria y sensores.

#### B) Diagrama Detallado de 5 Capas de la Plataforma Android:

```
┌────────────────────────────────────────────────────────────────────────┐
│ 1. SYSTEM APPS (Aplicaciones del Sistema y de Usuario)                 │
│    Dialer, Email, Calendario, Navegador, Tus Apps en Compose...       │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
┌───────────────────────────────────▼────────────────────────────────────┐
│ 2. JAVA API FRAMEWORK (APIs disponibles para desarrolladores)           │
│    Activity Manager, Window Manager, Content Providers, View System,   │
│    Notification Manager, Package Manager, Resource Manager...          │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
┌───────────────────────────────────▼────────────────────────────────────┐
│ 3. NATIVE C/C++ LIBRARIES         │ ANDROID RUNTIME (ART)              │
│    OpenGL ES, WebKit, SQLite,     │ Ahead-Of-Time (AOT), JIT,          │
│    Media Framework, FreeType,     │ Recolector de Basura (GC),         │
│    Bionic (libc optimizada)       │ Core Runtime Libraries             │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
┌───────────────────────────────────▼────────────────────────────────────┐
│ 4. HARDWARE ABSTRACTION LAYER (HAL)                                    │
│    Interfaces estándar para interactuar con drivers específicos:       │
│    Cámara, Audio, Bluetooth, Sensores, GPS, Radio...                   │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
┌───────────────────────────────────▼────────────────────────────────────┐
│ 5. LINUX KERNEL                                                        │
│    Controladores de pantalla, cámara, memoria, gestión de energía,     │
│    seguridad y aislamiento de procesos (Sandboxing por UID).           │
└────────────────────────────────────────────────────────────────────────┘
```

---

### 0.2 Componentes de una Aplicación Android y `AndroidManifest.xml`

Una aplicación Android es una colección de componentes que trabajan coordinados mediante el Framework. Existen **4 tipos esenciales de componentes**:

| Componente | Rol y Responsabilidad |
| :--- | :--- |
| **Actividades (`Activity`)** | Responsables de la interacción con el usuario. Crea la ventana que dibuja la UI y recibe los eventos del sistema. |
| **Servicios (`Service`)** | Procesos en segundo plano (*background*) para tareas de larga duración que no requieren interfaz visual. |
| **Proveedores de Contenido (`Content Provider`)** | Gestionan y comparten datos estructurados entre aplicaciones (ej. agenda de contactos, multimedia). |
| **Receptores de Emisiones (`Broadcast Receiver`)** | Escuchan y responden a anuncios o eventos globales del sistema (batería baja, reinicio, cambio de conectividad). |

#### Registro Obligatorio en `AndroidManifest.xml`:
Android conoce la existencia de cada componente porque **están registrados obligatoriamente** en `AndroidManifest.xml`:

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/Theme.AppTest">

        <!-- Declaración del componente Actividad -->
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:windowSoftInputMode="adjustResize">
            <!-- El intent-filter indica que esta es la primera actividad en ejecutarse al lanzar la app -->
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

    </application>
</manifest>
```

> [!NOTE]
> **Nombre del paquete único**: El nombre del paquete está formado por el dominio web invertido seguido del nombre de la app (ej. `com.carlosc.appTest`). Debe ser estrictamente único para poder publicarse en Google Play.

---

### 0.3 Estructura de Ficheros y Carpetas del Proyecto

| Directorio / Archivo | Contenido y Propósito según el Temario Oficial |
| :--- | :--- |
| **`manifests/AndroidManifest.xml`** | Información esencial del proyecto: componentes registrados, permisos y librerías. |
| **`java/` o `kotlin/` (`MainActivity.kt`)** | Actividad principal. Define el comportamiento y la interfaz del usuario. |
| **`res/drawable`** | Contiene imágenes vectoriales y recursos gráficos utilizados en la aplicación. |
| **`res/drawable-nodpi`** | Contiene imágenes de mapa de bits (PNG/JPG) que **no deben ser escaladas** por densidad. |
| **`res/mipmap`** | Contiene los iconos de la aplicación (*Launcher icons*) adaptados a diferentes densidades. |
| **`res/values/colors.xml`** | Define los colores hexadecimales utilizados en la aplicación. |
| **`res/values/strings.xml`** | Cadenas de texto internacionalizadas (títulos, botones, textos de UI). |
| **`res/values/themes.xml`** | Temas y estilos tradicionales del sistema de vistas XML. |
| **`res/xml/backup_rules.xml`** | Reglas para realizar copias de seguridad de datos en la nube. |
| **`gradle/libs.versions.toml`** | Catálogo de versiones centralizado de dependencias y plugins de Gradle. |
| **`build.gradle.kts (Module :app)`** | Script de construcción del módulo: SDKs objetivo (`minSdk`, `targetSdk`) y librerías. |

---

### 0.4 Consola Logcat, Niveles de Log y Advertencia Oficial de Examen

Android Studio dispone de la consola **Logcat** para monitorizar mensajes en tiempo de ejecución. Existen 5 niveles oficiales de Log:

```kotlin
// Definición de la etiqueta (TAG) en el Companion Object o a nivel de fichero:
companion object {
    const val LOG_TAG: String = "MainActivity"
}

// 1. Error (e): Fallos graves o excepciones capturadas
Log.e(LOG_TAG, "Ha ocurrido un error crítico en el cálculo")

// 2. Warning (w): Advertencias sobre situaciones inesperadas no fatales
Log.w(LOG_TAG, "El usuario intentó introducir una letra repetida")

// 3. Information (i): Hitos significativos de la ejecución
Log.i(LOG_TAG, "Partida iniciada correctamente")

// 4. Debug (d): Información de depuración útil para el desarrollador
Log.d(LOG_TAG, "Valor actual de la ronda: 3")

// 5. Verbose (v): Mensajes muy detallados de trazabilidad fina
Log.v(LOG_TAG, "Bandera pulsada: España (ID: 2131230845)")
```

> [!WARNING]
> **Advertencia Oficial del Profesor sobre Logcat (`Android_Ud1_Introduccion.pdf`, Diapositiva 35):**
> *"Cuando hayamos descubierto el problema y veamos que todo funciona bien debemos quitar del código los Logs ya que la pila que los almacena tiene un tamaño limitado. Si ponemos muchos Logs puede que un mensaje importante desaparezca pronto."*

---

## 1. Introducción e Interoperabilidad

Kotlin es un lenguaje de programación moderno desarrollado por JetBrains. En 2017 Google lo nombró lenguaje oficial de Android y en 2019 pasó a ser prioritario (**Kotlin-First**).

### Características clave:
- **100% Interoperable con Java**: Kotlin compila a bytecode ejecutable por la JVM de Java. Puedes invocar librerías y clases Java desde Kotlin y viceversa sin necesidad de adaptadores.
- **Conciso y Seguro**: Reduce drásticamente el código repetitivo (*boilerplate*) respecto a Java y cuenta con un sistema de tipos diseñado para erradicar las excepciones `NullPointerException`.
- **Soporta Programación Funcional y POO**: Las funciones son ciudadanos de primer orden (pueden asignarse a variables y pasarse como argumentos).

---

## 2. Variables, Mutabilidad e Inferencia de Tipos

Kotlin exige definir la mutabilidad de cada variable mediante dos palabras reservadas:

- **`val` (*Value*)**: Referencia de solo lectura / inmutable. Una vez asignado su valor, **no puede reasignarse**. Equivale a `final` en Java. Se debe priorizar siempre el uso de `val`.
- **`var` (*Variable*)**: Referencia mutable. Puede reasignarse tantas veces como sea necesario a lo largo de su ciclo de vida, siempre que mantenga el mismo tipo de dato.

```kotlin
val nombre: String = "Guillermo" // Inmutable
var contador: Int = 0             // Mutable
contador = 1                      // Correcto
// nombre = "Carlos"             // ERROR: Val cannot be reassigned
```

### Inferencia de Tipos (Type Inference)
El compilador deduce automáticamente el tipo de dato basándose en el valor asignado:
```kotlin
val edad = 20        // Infiere Int
val pi = 3.14159     // Infiere Double
val activo = true    // Infiere Boolean
val inicial = 'A'    // Infiere Char
val ciudad = "Madrid" // Infiere String
```
> [!TIP]
> Solo es obligatorio indicar el tipo explícito `: Tipo` cuando declaras una variable sin inicializar de inmediato o en firmas de métodos y parámetros de funciones.

---

## 3. Strings y Plantillas (String Templates)

En Java la concatenación de cadenas requería el operador `+` o constructores `StringBuilder`. En Kotlin se utilizan las **plantillas de cadenas** con el símbolo `$`:

```kotlin
val producto = "Hamburguesa"
val precio = 7.50
val cantidad = 2

// Concatenación anticuada e ineficiente:
val textoViejo = "Has pedido " + cantidad + " de " + producto + " por " + (precio * cantidad) + " euros."

// Plantillas modernas con $ y ${}:
val textoModerno = "Has pedido $cantidad de $producto por ${precio * cantidad} euros."
```
- `$variable`: Para imprimir el valor de una variable simple.
- `${expresion}`: Para evaluar cualquier expresión o llamada a método dentro de la cadena (ej. `${producto.uppercase()}`).

---

## 4. Estructuras de Control

### 4.1 `if` como Expresión
En Kotlin `if` es una **expresión**, no solo una sentencia. Esto significa que **devuelve un valor**:

```kotlin
val a = 15
val b = 20

// En Java se usaba el ternario: int mayor = (a > b) ? a : b;
// En Kotlin el if reemplaza al operador ternario:
val mayor = if (a > b) a else b
```
Si el bloque contiene varias instrucciones entre llaves, el valor de retorno es la última línea evaluada:
```kotlin
val mensaje = if (edad >= 18) {
    println("Acceso concedido")
    "Mayor de edad"
} else {
    println("Acceso denegado")
    "Menor de edad"
}
```

### 4.2 `when` (Sustituto avanzado de `switch`)
La estructura `when` reemplaza al clásico `switch` aportando mayor potencia:
- **Sin `break`**: No hay caída accidental al siguiente caso.
- **Agrupación de casos**: Se separan por comas.
- **Rangos y colecciones**: Evalúa pertenencia con `in 1..10` o `in lista`.
- **Comprobación de tipos**: Comprueba el tipo con `is String`.
- **Sin argumento**: Puede utilizarse como sustituto limpio de una cadena de `if - else if`.

```kotlin
val nota = 8

val calificacion = when (nota) {
    10 -> "Matrícula de Honor"
    9, 8 -> "Sobresaliente / Notable"
    7, 6 -> "Bien"
    5 -> "Suficiente"
    in 1..4 -> "Suspenso"
    else -> "Nota inválida"
}
```
> [!NOTE]
> Cuando `when` se usa como **expresión** (asignando su resultado a una variable), la rama `else` es **obligatoria**, salvo que se esté evaluando un `enum` o una `sealed class` donde todos los casos posibles estén completamente cubiertos.

### 4.3 Bucles y Rangos: Guía Completa de Formas de hacer `for` en Kotlin

A diferencia de Java, C o C++, **en Kotlin NO existe el bucle tradicional de estilo C con punto y coma** (`for (int i=0; i<10; i++)`). En su lugar, el bucle `for` en Kotlin siempre itera sobre **cualquier cosa que proporcione un iterador** (rangos numéricos, colecciones, arrays, cadenas o tipos personalizados).

A continuación se detallan **todas las variantes y formas de iteración existentes**, relacionándolas con las diapositivas de `Android_ProgramacionKotlin.pdf` y los ejercicios del proyecto:

---

#### 1. Rangos Numéricos (Crecientes, Decrecientes y Pasos)

| Sintaxis | Tipo de Rango | Valores Generados | Equivalente en Java |
| :--- | :--- | :--- | :--- |
| `for (i in 1..5)` | **Rango cerrado inclusivo** (incluye el 5) | `1 2 3 4 5` | `for (int i=1; i<=5; i++)` |
| `for (i in 1 until 5)` | **Rango semiabierto** (excluye el 5) | `1 2 3 4` | `for (int i=1; i<5; i++)` |
| `for (i in 3 downTo 1)` | **Cuenta regresiva** inclusiva *(Diapositiva 8)* | `3 2 1` | `for (int i=3; i>=1; i--)` |
| `for (i in 1..7 step 2)` | **Paso ascendente** *(Diapositiva 8)* | `1 3 5 7` | `for (int i=1; i<=7; i+=2)` |
| `for (i in 10 downTo 0 step 2)` | **Paso descendente** | `10 8 6 4 2 0` | `for (int i=10; i>=0; i-=2)` |

```kotlin
// 1. Inclusivo con '..'
for (i in 1..3) { print("$i ") } // 1 2 3

// 2. Semiabierto con 'until' (ideal para tamaños de arrays)
for (i in 0 until 3) { print("$i ") } // 0 1 2

// 3. Decreciente con 'downTo' (Diapositiva 8 del temario)
for (i in 3 downTo 1) { print("$i ") } // 3 2 1

// 4. Con salto personalizado con 'step' (Diapositiva 8 del temario)
for (i in 1..7 step 2) { print("$i ") } // 1 3 5 7
```

---

#### 2. Rangos de Caracteres (`CharRange`)

Los rangos en Kotlin funcionan de manera nativa con caracteres Unicode/ASCII:

```kotlin
// Rango alfabético creciente:
for (letra in 'a'..'e') {
    print("$letra ") // a b c d e
}

// Rango alfabético inverso con salto:
for (c in 'Z' downTo 'T' step 2) {
    print("$c ") // Z X V T
}
```

---

#### 3. Recorrido Directo de Colecciones y Cadenas

Equivale al bucle `for-each` de Java (`for (Tipo x : coleccion)`):

```kotlin
// Recorrido de una cadena carácter a carácter (Diapositiva 8 del temario):
for (c in "kotlin") {
    print("‘$c’ ") // ‘k’ ‘o’ ‘t’ ‘l’ ’i’ ‘n’
}

// Recorrido de una lista:
val frutas = listOf("Manzana", "Pera", "Plátano")
for (fruta in frutas) {
    println("Fruta: $fruta")
}
```

---

#### 4. Recorrido por Índices con `.indices` *(¡Utilizado en los Ejercicios!)*

En lugar de calcular manualmente `0 until lista.size`, Kotlin proporciona la propiedad extendida `.indices`, que genera el rango seguro de posiciones `0..size-1`.

> [!NOTE]
> Esta forma es exactamente la utilizada en el ejercicio del **Ahorcado** (`unidad4/ejemplo3/ui/JuegoViewModel.kt:74`):
> ```kotlin
> // Recorre las posiciones de la palabra para comprobar coincidencias:
> for (i in palabraActual.indices) {
>     if (palabraActual[i].equals(charBuscado, ignoreCase = true)) {
>         guiones[i] = charBuscado
>     }
> }
> ```

```kotlin
val nombres = listOf("Ana", "Bernardo", "Carlos")

for (i in nombres.indices) {
    println("Índice $i contiene a ${nombres[i]}")
}
```

---

#### 5. Recorrido con Índice y Valor Simultáneo: `.withIndex()`

Permite obtener tanto el índice entero como el elemento desempaquetados en una tupla de desestructuración `(indice, elemento)`:

```kotlin
val lenguajes = listOf("Kotlin", "Java", "Python")

for ((indice, lenguaje) in lenguajes.withIndex()) {
    println("Top #$indice: $lenguaje")
}
// Imprime:
// Top #0: Kotlin
// Top #1: Java
// Top #2: Python
```

---

#### 6. Recorrido de Mapas (Diccionarios Clave-Valor)

Existen cuatro formas distintas de recorrer un mapa según lo que se necesite:

```kotlin
val notas = mapOf("Guillermo" to 10, "Lucía" to 9, "Marcos" to 8)

// A) Recorrido con desestructuración de clave y valor (la más habitual):
for ((alumno, nota) in notas) {
    println("$alumno obtuvo $nota")
}

// B) Recorrido solo de las claves:
for (alumno in notas.keys) {
    println("Alumno registrado: $alumno")
}

// C) Recorrido solo de los valores:
for (nota in notas.values) {
    println("Calificación: $nota")
}

// D) Recorrido de las entradas (Map.Entry):
for (entrada in notas.entries) {
    println("${entrada.key} = ${entrada.value}")
}
```

---

#### 7. Bucles Anidados y Control de Flujo con Etiquetas (`@label`)

Cuando tenemos bucles dentro de bucles, un simple `break` o `continue` solo afecta al bucle más interno. Para romper o continuar el bucle **exterior**, Kotlin utiliza **etiquetas (`label@`)**:

```kotlin
bucleExterno@ for (i in 1..3) {
    for (j in 1..3) {
        if (i == 2 && j == 2) {
            println("Rompiendo directamente el bucle exterior en i=$i, j=$j")
            break@bucleExterno // Rompe bucleExterno, NO solo el bucle j
        }
        println("i = $i, j = $j")
    }
}
```

---

#### 8. La Función Estándar `repeat(n)`

Cuando solo necesitas repetir una operación $N$ veces y no necesitas la sintaxis completa de un bucle `for`, Kotlin ofrece la función de alto nivel `repeat`:

```kotlin
// Equivalente idiomático a: for (i in 0 until 5)
repeat(5) { numeroIteracion ->
    println("Ejecutando vuelta #$numeroIteracion") // 0, 1, 2, 3, 4
}
```

---

#### 9. Bucles Personalizados con el Operador `iterator()`

En Kotlin el bucle `for` no está atado a colecciones predefinidas del sistema. Puedes hacer que **absolutamente cualquier clase propia sea iterable** en un bucle `for (elemento in miObjeto)` gracias al **diseño basado en convenciones** (*Convention-based design*) del compilador.

---

##### ¿Cómo funciona el bucle `for` bajo el capó? (Traducción del compilador)

Cuando escribes en Kotlin:
```kotlin
for (elemento in miContenedor) {
    println(elemento)
}
```

El compilador de Kotlin lo traduce internamente en bytecode exactamente a este bucle tradicional `while`:
```kotlin
val iterador = miContenedor.iterator()
while (iterador.hasNext()) {
    val elemento = iterador.next()
    println(elemento)
}
```

##### Requisitos técnicos obligatorios:
Para que una clase pueda utilizarse en un bucle `for`, **NO es obligatorio heredar de ninguna interfaz `Iterable<T>` de Java**. Solo necesita cumplir dos condiciones sintácticas:
1. Declarar una función con la palabra clave **`operator`** llamada **`iterator()`** (puede ser un método dentro de la clase o una función de extensión externa).
2. Dicha función debe retornar un objeto que implemente:
   - `operator fun hasNext(): Boolean` (indica si quedan más elementos por recorrer).
   - `operator fun next(): T` (devuelve el siguiente elemento de tipo `T`).

---

##### Caso 1: Delegando en una colección interna (El enfoque más común)
Ideal cuando creamos una clase que encapsula una lista privada y queremos que se pueda iterar directamente sin exponer la lista mutable:

```kotlin
// Clase que encapsula una lista de alumnos:
class Aula(private val alumnos: List<String>) {
    // Sobrecargamos el operador iterator delegando en el iterador de la lista:
    operator fun iterator(): Iterator<String> = alumnos.iterator()
}

fun main() {
    val miAula = Aula(listOf("Guillermo", "Lucía", "Carlos"))

    // ¡Gracias a 'operator fun iterator()', podemos recorrer 'miAula' directamente!
    for (alumno in miAula) {
        println("Estudiante presente: $alumno")
    }
}
```

---

##### Caso 2: Implementando un `Iterator` manual desde cero (Generador dinámico en memoria)
Ideal cuando queremos iterar sobre secuencias matemáticas o rangos personalizados **sin almacenar listas gigantescas en memoria RAM**:

```kotlin
// Generador que recorre únicamente números pares en un rango dado:
class RangoPares(val inicio: Int, val fin: Int) {

    // 1. La función iterator() marcada con 'operator'
    operator fun iterator(): Iterator<Int> = object : Iterator<Int> {
        // Variable interna para controlar el número actual:
        private var actual = if (inicio % 2 == 0) inicio else inicio + 1

        // 2. Comprueba si el siguiente número par no supera el límite fin:
        override fun hasNext(): Boolean = actual <= fin

        // 3. Devuelve el número par actual y avanza 2 posiciones:
        override fun next(): Int {
            if (!hasNext()) throw NoSuchElementException("No quedan más números pares")
            val valor = actual
            actual += 2
            return valor
        }
    }
}

fun main() {
    val pares = RangoPares(inicio = 1, fin = 10)

    // Recorre los pares calculándolos sobre la marcha:
    for (n in pares) {
        print("$n ") // Imprime: 2 4 6 8 10
    }
}
```

---

##### Caso 3: Haciendo iterable una clase de terceros con Funciones de Extensión
Si una clase de una librería externa o de Android no es iterable, puedes habilitar el bucle `for` agregándole una función de extensión `operator fun iterator()`:

```kotlin
// Ejemplo: permitir iterar sobre una tupla de coordenadas
data class Coordenada3D(val x: Double, val y: Double, val z: Double)

// Añadimos el operador iterator como función de extensión:
operator fun Coordenada3D.iterator(): Iterator<Double> = listOf(x, y, z).iterator()

fun main() {
    val punto = Coordenada3D(10.5, 20.0, 5.2)

    for (eje in punto) {
        println("Valor de eje: $eje")
    }
}
```

---

#### 10. Tabla Resumen: Comparativa de Bucles Java vs Kotlin

| Enfoque en Java | Equivalente Idiomático en Kotlin |
| :--- | :--- |
| `for (int i = 0; i < n; i++)` | `for (i in 0 until n)` o `repeat(n) { i -> }` |
| `for (int i = 0; i <= n; i++)` | `for (i in 0..n)` |
| `for (int i = n; i >= 0; i--)` | `for (i in n downTo 0)` |
| `for (int i = 0; i < n; i += 2)` | `for (i in 0 until n step 2)` |
| `for (int i = 0; i < list.size(); i++)` | `for (i in list.indices)` *(como en el Ahorcado)* |
| `for (String s : list)` | `for (s in list)` |
| `for (int i = 0; i < list.size(); i++)` *(con índice y valor)* | `for ((i, s) in list.withIndex())` |
| `for (Map.Entry<K,V> e : map.entrySet())` | `for ((k, v) in map)` |

---

## 5. Funciones y Expresiones Lambda

### 5.1 Declaración y Funciones de Expresión Única
Las funciones se declaran con la palabra clave `fun`. Si no devuelven nada, su tipo de retorno es `Unit`:

```kotlin
fun saludar(nombre: String): Unit {
    println("Hola, $nombre")
}

// Función de expresión única:
fun doble(n: Int): Int = n * 2
```

### 5.2 Parámetros Nombrados y Valores por Defecto
Evitan la proliferación de constructores y sobrecargas de métodos:
```kotlin
fun configurarDispositivo(
    brillo: Int = 100,
    modoOscuro: Boolean = false,
    volumen: Int = 50
) {
    println("Brillo: $brillo, Oscuro: $modoOscuro, Vol: $volumen")
}

// Llamadas válidas:
configurarDispositivo()                                        // Valores por defecto
configurarDispositivo(modoOscuro = true)                       // Solo cambiamos uno
configurarDispositivo(volumen = 80, brillo = 50)               // Alterando el orden
```

---

### 5.3 Lambdas, Referencias a Funciones e Identificador `it`

En Kotlin, las funciones son **ciudadanos de primer orden** (*First-Class Citizens*): pueden almacenarse en variables, pasarse como argumentos a otras funciones y retornarse como resultado.

---

#### A) Almacenar funciones en variables: Operador de Referencia `::` vs Función Lambda

Existen dos mecanismos para asignar una función a una variable o pasarla como callback:

| Criterio | Operador de Referencia `::` | Función Lambda `{ ... }` |
| :--- | :--- | :--- |
| **Definición** | Apunta directamente a una función o método que **ya existe y tiene nombre**. | Crea una **función anónima in-situ** (en el acto) entre llaves `{}`. |
| **Lógica adicional** | No puede añadir lógica nueva; ejecuta exactamente la función apuntada. | Puede contener múltiples líneas, cálculos, transformaciones y llamadas. |
| **Captura de variables** | No captura variables externas. | Puede capturar y modificar variables del ámbito exterior (*Closure*). |
| **Legibilidad** | Extremadamente conciso cuando la función ya existe (`lista.forEach(::println)`). | Más flexible cuando necesitamos transformar o combinar parámetros. |

##### Las 4 Variantes del Operador de Referencia `::`:
1. **Referencia a función libre o de nivel superior (`::funcion`)**: Apunta a una función independiente.
2. **Referencia ligada a objeto (`objeto::metodo`)**: Apunta al método de una instancia concreta fija.
3. **Referencia no ligada a clase (`Clase::metodo`)**: El primer parámetro del tipo de función debe ser la instancia de la clase receptora.
4. **Referencia a constructor (`::NombreClase`)**: Apunta al constructor de una clase para instanciar objetos.

##### Comparativa Exhaustiva de Código:

```kotlin
// Funciones normales ya existentes:
fun convertirAMayusculas(texto: String): String = texto.uppercase()
fun esNumeroPar(n: Int): Boolean = (n % 2 == 0)

class ImpresoraTicket(val prefijo: String) {
    fun formatear(importe: Double): String = "$prefijo ${importe} €"
}

data class Alumno(val nombre: String, val edad: Int)

fun main() {
    // ==========================================================
    // 1. COMPARATIVA: Función Top-Level (:: vs Lambda)
    // ==========================================================
    // Con operador de referencia :: (apunta a la función existente sin duplicar código)
    val refMayusculas: (String) -> String = ::convertirAMayusculas

    // Con lambda equivalente (crea una función anónima que delega):
    val lambdaMayusculas: (String) -> String = { texto -> convertirAMayusculas(texto) }

    println(refMayusculas("kotlin"))    // "KOTLIN"
    println(lambdaMayusculas("kotlin")) // "KOTLIN"


    // ==========================================================
    // 2. REFERENCIA LIGADA A INSTANCIA (objeto::metodo)
    // ==========================================================
    val impresoraTienda = ImpresoraTicket("TICKET #01:")
    // Ligamos la referencia al objeto 'impresoraTienda':
    val formatearPrecio: (Double) -> String = impresoraTienda::formatear
    println(formatearPrecio(49.99)) // "TICKET #01: 49.99 €"


    // ==========================================================
    // 3. REFERENCIA AL CONSTRUCTOR DE UNA CLASE (::NombreClase)
    // ==========================================================
    // Referencia al constructor de Alumno (String, Int) -> Alumno
    val creadorAlumnos: (String, Int) -> Alumno = ::Alumno
    val alumnoNuevo = creadorAlumnos("Guillermo", 25)
    println(alumnoNuevo) // Alumno(nombre=Guillermo, edad=25)


    // ==========================================================
    // 4. USO IDIOMÁTICO EN COLECCIONES
    // ==========================================================
    val numeros = listOf(1, 2, 3, 4, 5, 6)

    // Con referencia :: limpia y legible:
    val paresRef = numeros.filter(::esNumeroPar)

    // Con lambda:
    val paresLambda = numeros.filter { it % 2 == 0 }

    // Imprimir por consola:
    numeros.forEach(::println) // Equivale a: numeros.forEach { println(it) }
}
```

---

#### B) Tipos de Función Explícitos: Guía y Catálogo Completo

El compilador de Kotlin deduce los tipos por inferencia, pero **en interfaces, callbacks, firmas de ViewModel y Compose es imprescindible declarar el tipo de función explícito**.

##### Sintaxis General:
` (TipoParam1, TipoParam2, ...) -> TipoRetorno `

> [!IMPORTANT]
> **Reglas de sintaxis de los tipos de función:**
> 1. Los paréntesis para los parámetros son **siempre obligatorios**, incluso si no recibe parámetros: `() -> Unit` (escribir `-> Unit` es un error de compilación).
> 2. Si la función no devuelve ningún valor, el tipo de retorno debe ser explícitamente **`Unit`** (no puede omitirse).
> 3. Para funciones con un solo parámetro en su lambda, Kotlin provee automáticamente la palabra clave implícita **`it`**.

##### Catálogo de Tipos de Función y su Aplicación en Android:

```kotlin
// 1. SIN PARÁMETROS Y SIN RETORNO (Acciones puras / Eventos de clic):
// Usado en Compose: Button(onClick: () -> Unit)
val accionSimple: () -> Unit = { 
    println("Botón pulsado") 
}

// 2. UN PARÁMETRO CON 'it' Y SIN RETORNO (Consumidores / Notificaciones):
// Usado en Compose: OutlinedTextField(onValueChange: (String) -> Unit)
val onCambioTexto: (String) -> Unit = { nuevoTexto ->
    println("El usuario escribió: $nuevoTexto")
}
// Con identificador implícito 'it':
val logMensaje: (String) -> Unit = { println("[LOG] $it") }

// 3. PREDICADO BOOLEANO (Condiciones / Filtros):
// Usado en: lista.filter(predicate: (T) -> Boolean)
val esMayorDeEdad: (Int) -> Boolean = { it >= 18 }

// 4. TRANSFORMACIÓN / MAPEO (Entrada -> Salida):
// Usado en: lista.map(transform: (T) -> R)
val calcularIva: (Double) -> Double = { base -> base * 0.21 }
val formatearMoneda: (Double, String) -> String = { cantidad, simbolo -> "$cantidad $simbolo" }

// 5. FUNCIÓN QUE DEVUELVE UN TIPO ANULABLE (Nullable Return):
// El signo '?' va en el tipo de retorno: devuelve un Int o null
val parsearSeguro: (String) -> Int? = { texto -> texto.toIntOrNull() }

// 6. VARIABLE DE FUNCIÓN ANULABLE (Callback Opcional):
// Los paréntesis encierran todo el tipo de función seguido de '?':
// La variable puede contener una función o ser null
var callbackOpcional: ((String) -> Unit)? = null

fun invocarCallbackOpcional() {
    // Invocación segura mediante ?.invoke(...)
    callbackOpcional?.invoke("Datos listos") // Si es null, no hace nada; si existe, se ejecuta
}

// 7. FUNCIÓN DE ORDEN SUPERIOR COMO PARÁMETRO (Patrón Callback en Arquitectura):
fun procesarPedido(
    precioBase: Double,
    onCalculoCompletado: (total: Double, mensaje: String) -> Unit
) {
    val totalConIva = precioBase * 1.21
    // Se ejecuta el callback pasando los resultados calculados:
    onCalculoCompletado(totalConIva, "Pedido facturado con éxito")
}

fun probarCallbacks() {
    procesarPedido(100.0) { total, mensaje ->
        println("$mensaje. Total: $total €")
    }
}
```

---

#### C) Trailing Lambda (Sintaxis de Lambda Final): El Corazón de Compose

La sintaxis de **Trailing Lambda** (o lambda final) es la regla de diseño sintáctico más importante de Kotlin y la responsable directa de cómo se escribe **Jetpack Compose**.

##### La Regla Oficial:
> Si el **último parámetro** de una función es otra función o expresión lambda, las llaves `{}` que definen el cuerpo de la lambda pueden colocarse **fuera de los paréntesis `()` de la función**.
> Además, **si la lambda es el ÚNICO parámetro de la función, los paréntesis `()` pueden omitirse por completo**.

##### Progresión Sintáctica (De Java a Kotlin Idiomático):

```kotlin
// Definición de una función receptora:
fun ejecutarTransaccion(codigo: Int, onCompletado: (String) -> Unit) {
    println("Ejecutando transacción #$codigo...")
    onCompletado("OK")
}

// 1. SINTAXIS TRADICIONAL (Lambda DENTRO de los paréntesis, estilo Java):
ejecutarTransaccion(101, { resultado ->
    println("Transacción finalizada con estado: $resultado")
})

// 2. SINTAXIS IDIOMÁTICA CON TRAILING LAMBDA (Llaves FUERA de los paréntesis):
ejecutarTransaccion(101) { resultado ->
    println("Transacción finalizada con estado: $resultado")
}

// 3. CUANDO LA LAMBDA ES EL ÚNICO PARÁMETRO (Sin paréntesis):
fun registrarEvento(accion: () -> Unit) { accion() }

// En lugar de registrarEvento({ println("Evento") }) escribimos:
registrarEvento {
    println("¡Evento registrado limpiamente sin paréntesis!")
}
```

##### ¿Por qué es la Piedra Angular de Jetpack Compose?
Jetpack Compose estructura toda la jerarquía de interfaces declarativas utilizando trailing lambdas. Gracias a esta regla, el código Kotlin se comporta como un lenguaje de marcado jerárquico (DSL):

```kotlin
// En Compose, el contenido hijo de los contenedores es una trailing lambda:
// fun Column(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit)

// Gracias a Trailing Lambda se escribe de forma natural y anidada:
Column(
    modifier = Modifier.fillMaxSize().padding(16.dp)
) { // <- Esta llave es la Trailing Lambda del parámetro 'content'
    Text(text = "Título Principal")

    // Button recibe 'onClick' en paréntesis y su texto hijo como Trailing Lambda:
    Button(
        onClick = { println("Botón pulsado") } // Parámetro regular
    ) { // <- Trailing Lambda para el contenido visual del botón
        Text(text = "Guardar Cambios")
    }
}
```

> [!CAUTION]
> **¿Qué pasa si la lambda NO es el último parámetro?**
> Si una función define: `fun operar(accion: () -> Unit, numero: Int)`, la lambda **NO se puede sacar fuera de los paréntesis**. Tendrías que escribir obligatoriamente: `operar({ ... }, 5)`.
> Por esta razón, en Kotlin y en Android **siempre se sitúa el parámetro de tipo lambda en la última posición de la firma de la función**.

---

## 6. Programación Orientada a Objetos (POO)

### 6.1 Constructores: Primario y Secundario

En Kotlin una clase puede tener un **constructor primario** y uno o más **constructores secundarios**.

> [!IMPORTANT]
> **Regla oficial del temario (`Android_ProgramacionKotlin.pdf`, Diapositivas 15 y 16):**
> Según el temario oficial del profesor:
> - El **constructor principal** se define como parte del encabezado de la clase y **NO puede contener código**. Puede ser predeterminado (sin parámetros) o con parámetros.
> - Los **constructores secundarios** se definen dentro del cuerpo de la clase con la palabra reservada `constructor`, pueden ser con o sin parámetros y **tienen un cuerpo con código**.
> - **Obligación**: Si la clase tiene un constructor principal, **cada constructor secundario debe inicializarlo obligatoriamente** utilizando la sintaxis `: this(...)`.

#### Comparativa: Constructor Primario vs Secundario

```kotlin
// 1. CONSTRUCTOR PRIMARIO:
// Se define directamente en la cabecera. No puede contener código ejecutable directo en su firma.
// Si lleva 'val' o 'var', crea y asigna las propiedades automáticamente.
class Alumno(val nombre: String, var edad: Int) {
    
    // Bloque de inicialización init: se ejecuta inmediatamente tras el constructor primario
    // (es el único lugar donde se puede ejecutar código asociado a la construcción principal)
    init {
        require(edad >= 0) { "La edad no puede ser negativa" }
        println("Alumno $nombre creado correctamente")
    }

    // 2. CONSTRUCTOR SECUNDARIO:
    // Se define dentro del cuerpo con la palabra clave 'constructor'. Tiene cuerpo entre llaves.
    // REGLA OBLIGATORIA: Debe delegar siempre en el constructor primario usando ': this(...)'
    constructor(nombre: String) : this(nombre, edad = 18) {
        println("Constructor secundario ejecutado (asignada edad por defecto: 18)")
    }

    // Otro constructor secundario con diferente firma:
    constructor(codigoMatricula: Int) : this("Estudiante-$codigoMatricula", edad = 20)
}

fun main() {
    // Uso del constructor primario:
    val a1 = Alumno("Carlos", 22) // Imprime: Alumno Carlos creado correctamente

    // Uso del constructor secundario (delega en el primario y luego ejecuta su cuerpo):
    val a2 = Alumno("Lucía")      // Imprime: Alumno Lucía creado correctamente -> Constructor secundario...
}
```

> [!TIP]
> En Kotlin moderno casi nunca necesitarás constructores secundarios gracias a los **valores por defecto en el constructor primario**:
> `class Alumno(val nombre: String, var edad: Int = 18)` cubre ambos casos en una sola línea.

---

### 6.2 Propiedades, Getters, Setters y `field`

En Kotlin, el acceso a propiedades (`objeto.campo = valor`) invoca automáticamente el *getter* o *setter*.

> [!NOTE]
> **Generación interna de funciones `get()` y `set()` (`Android_ProgramacionKotlin.pdf`, Diapositiva 17):**
> Cuando no defines las funciones `get` y `set` para un atributo, el compilador de Kotlin crea estas funciones a nivel interno automáticamente (no las veremos en nuestro código).
> - **Las variables declaradas con `val` son variables de solo lectura**, por lo que el compilador **solo genera la función `get()` y NUNCA genera la función `set(value)`**.
> - Las variables declaradas con `var` generan tanto la función `get()` como la función `set(value)`.

Para acceder al valor real en memoria dentro de un accesor personalizado sin provocar llamadas recursivas infinitas, se usa la palabra clave `field` (campo de respaldo o *backing field*):

```kotlin
// Ejemplo exacto del temario oficial (Android_ProgramacionKotlin.pdf, Diapositiva 19):
class Alumno {
    var nota: Int = 0
        get() = field
        set(value) {
            // El parámetro 'value' recoge el valor que se intenta asignar:
            if (value in 1..10) {
                field = value
            }
        }
}

// Ejemplo bancario con validación de saldo:
class CuentaBancaria {
    var saldo: Double = 0.0
        get() = field
        set(nuevoSaldo) {
            if (nuevoSaldo >= 0) {
                field = nuevoSaldo
            } else {
                println("Error: saldo negativo no permitido")
            }
        }
}
```

---

### 6.3 Herencia: `open` y `override`
En Kotlin **todas las clases y métodos son finales (`final`) por defecto**. Para permitir que una clase sea heredada o que un método sea sobrescrito, deben marcarse explícitamente con `open`:

```kotlin
open class Producto(open val nombre: String, val precioBase: Double) {
    open fun calcularPrecioFinal(): Double = precioBase * 1.21
}

class Hamburguesa(
    override val nombre: String,
    precioBase: Double,
    val conQueso: Boolean
) : Producto(nombre, precioBase) {
    override fun calcularPrecioFinal(): Double {
        val extra = if (conQueso) 1.0 else 0.0
        return super.calcularPrecioFinal() + extra
    }
}
```
*(Este patrón de herencia se utiliza en el ejercicio `unidad4/ejercicio1` de Restaurante DAM / `Ud4_Ejercicio`).*

---

### 6.4 Interfaces (`interface`)

> [!IMPORTANT]
> **Regla oficial del temario (`Android_ProgramacionKotlin.pdf`, Diapositiva 21):**
> La implementación de una interfaz también se realiza mediante los dos puntos `:`.
> Si hay más de una clase a heredar / implementar, **se separa entre comas**.
> Al igual que en Java, **Kotlin no permite la herencia múltiple de clases pero sí implementar varias interfaces**.

```kotlin
// Definición de clase base abierta:
open class Persona(val nombre: String)

// Definición de interfaces:
interface Evaluacion {
    fun calificar(): Double
}

interface Notificable {
    fun enviarNotificacion(mensaje: String)
}

// Subclase que hereda de Persona e implementa DOS interfaces separadas por comas:
class Profesor(
    nombre: String,
    val asignatura: String
) : Persona(nombre), Evaluacion, Notificable {

    override fun calificar(): Double {
        println("Evaluando exámenes de $asignatura...")
        return 9.5
    }

    override fun enviarNotificacion(mensaje: String) {
        println("Notificación al profesor $nombre: $mensaje")
    }
}
```

---

### 6.5 Modificadores de Visibilidad: Diferencias y Ámbitos

A diferencia de Java (donde el valor por defecto es *package-private*), en Kotlin el modificador por defecto es **`public`**.

#### Matriz Oficial de Visibilidad (`Android_ProgramacionKotlin.pdf`, Diapositiva 22):

| Modificador | Accesible en la misma clase | Accesible en subclase | Accesible en el mismo módulo | Accesible fuera del módulo |
| :--- | :---: | :---: | :---: | :---: |
| **`private`** | **SÍ** | NO | NO | NO |
| **`protected`** | **SÍ** | **SÍ** | NO | NO |
| **`internal`** | **SÍ** | **SÍ** | **SÍ** | NO |
| **`public`** | **SÍ** | **SÍ** | **SÍ** | **SÍ** |

#### Ejemplo Práctico de Diferencias de Visibilidad:

```kotlin
open class ServidorBD {
    public val host: String = "192.168.1.100"      // Accesible desde cualquier clase o módulo
    internal val puerto: Int = 5432                // Accesible en cualquier archivo de este módulo Gradle (:app)
    protected val tokenSesion: String = "XYZ-123"  // Accesible solo aquí y en clases que hereden de ServidorBD
    private val passwordRoot: String = "admin123"  // Accesible EXCLUSIVAMENTE dentro de esta clase ServidorBD

    fun probarAccesoInterno() {
        println(passwordRoot) // CORRECTO: está dentro de la misma clase
    }
}

class ServidorSeguro : ServidorBD() {
    fun verificar() {
        println(host)        // CORRECTO: public
        println(puerto)      // CORRECTO: internal (mismo módulo)
        println(tokenSesion) // CORRECTO: protected es visible en subclases
        // println(passwordRoot) // ERROR DE COMPILACIÓN: passwordRoot es private
    }
}

fun main() {
    val servidor = ServidorBD()
    println(servidor.host)        // CORRECTO: public
    println(servidor.puerto)      // CORRECTO: internal
    // println(servidor.tokenSesion) // ERROR DE COMPILACIÓN: protected no es visible fuera de la jerarquía
    // println(servidor.passwordRoot) // ERROR DE COMPILACIÓN: private es invisible
}
```

---

### 6.6 Delegación de Propiedades (`by`): Comparativa `=` vs `by`

La delegación traslada la lógica de lectura y escritura (`getValue` y `setValue`) a un objeto delegado externo mediante la palabra reservada `by`.

#### Diferencia Práctica: Asignación Tradicional (`=`) vs Delegación (`by`)

```kotlin
// ==========================================================
// CASO 1: ASIGNACIÓN TRADICIONAL (=)
// ==========================================================
// La variable almacena directamente el contenedor 'MutableState<String>'
val textoState = remember { mutableStateOf("Hola") }

// LECTURA: Requiere acceder explícitamente a la propiedad .value
Text(text = textoState.value)

// ESCRITURA: Requiere asignar a .value
Button(onClick = { textoState.value = "Mundo" }) { ... }


// ==========================================================
// CASO 2: DELEGACIÓN CON 'by' (Idiomático en Jetpack Compose)
// ==========================================================
// La propiedad 'texto' delega sus accesos getter y setter al objeto MutableState
var texto by remember { mutableStateOf("Hola") }

// LECTURA: Se usa la variable directamente como un String transparente
Text(text = texto) // ¡Sin .value!

// ESCRITURA: Se reasigna la variable directamente como si fuera un String normal
Button(onClick = { texto = "Mundo" }) { ... }
```

> [!NOTE]
> Para usar la sintaxis `by` en Jetpack Compose, es imprescindible importar los delegados de extensión:
> ```kotlin
> import androidx.compose.runtime.getValue
> import androidx.compose.runtime.setValue
> ```

---

### 6.7 Clases Especiales en Kotlin

#### A) `class` Tradicional vs `data class`

> [!NOTE]
> **Definición oficial del temario (`Android_ProgramacionKotlin.pdf`, Diapositiva 27):**
> *"Una clase `data` es aquella que solo tiene datos y no tiene funciones que realicen acciones."*

Las `data class` están optimizadas por el compilador para almacenar datos, implementando automáticamente métodos fundamentales que en una `class` normal requerirían decenas de líneas:

```kotlin
// Clase normal:
class UsuarioNormal(val id: Int, val nombre: String)

// Data class:
data class UsuarioData(val id: Int, val nombre: String)

fun main() {
    val uNorm1 = UsuarioNormal(1, "Ana")
    val uNorm2 = UsuarioNormal(1, "Ana")

    val uData1 = UsuarioData(1, "Ana")
    val uData2 = UsuarioData(1, "Ana")

    // 1. COMPARACIÓN ESTRUCTURAL (equals / ==):
    println(uNorm1 == uNorm2) // false (compara la referencia de memoria en heap)
    println(uData1 == uData2) // true  (compara los valores campo por campo)

    // 2. REPRESENTACIÓN EN TEXTO (toString()):
    println(uNorm1) // UsuarioNormal@7b23ec81 (código hash ininteligible)
    println(uData1) // UsuarioData(id=1, nombre=Ana) (legible y formateado)

    // 3. COPIA CON MODIFICACIÓN (copy()):
    // uNorm1.copy(...) // ERROR: copy() no existe en class normal
    val uDataModificado = uData1.copy(nombre = "Ana María") // Clona id=1 y actualiza nombre

    // 4. DESESTRUCTURACIÓN AUTOMÁTICA:
    val (id, nombre) = uData1 // Genera automáticamente component1() y component2()
    println("ID: $id, Nombre: $nombre")
}
```

#### B) `enum class` (Clases de Enumeración)

> [!NOTE]
> **Definición oficial del temario (`Android_ProgramacionKotlin.pdf`, Diapositivas 26 y 27):**
> *"Una clase enum se usa para crear tipos con un conjunto limitado de valores posibles."*

En los cuestionarios y aplicaciones de examen, las `enum class` se combinan frecuentemente con `data class` y genéricos `<T>`:

```kotlin
// 1. Declaración de la clase enum según el PDF oficial:
enum class Dificultad {
    FACIL,
    MEDIO,
    DIFICIL
}

// 2. data class que integra la clase enum y un tipo genérico T para la respuesta:
data class Pregunta<T>(
    val enunciado: String,
    val respuesta: T,
    val dificultad: Dificultad
)

fun main() {
    // Instanciación con respuesta booleana y dificultad FACIL:
    val p1 = Pregunta<Boolean>(
        enunciado = "¿Kotlin soporta herencia múltiple de clases?",
        respuesta = false,
        dificultad = Dificultad.FACIL
    )

    // Instanciación con respuesta de texto y dificultad DIFICIL:
    val p2 = Pregunta<String>(
        enunciado = "¿Qué palabra clave sustituye a static en Kotlin?",
        respuesta = "companion object",
        dificultad = Dificultad.DIFICIL
    )

    println("${p1.enunciado} [Nivel: ${p1.dificultad}] -> Solución: ${p1.respuesta}")
    println("${p2.enunciado} [Nivel: ${p2.dificultad}] -> Solución: ${p2.respuesta}")
}
```

#### C) Genéricos (`Generics` con `<T>`)

Permiten definir clases, interfaces y métodos que trabajan con cualquier tipo de dato de forma segura sin conversiones forzadas de tipo (*type casting*):

*(Ejemplo tomado del temario `Android_ProgramacionKotlin.pdf` — Cuestionario / Preguntas)*:

```kotlin
// Clase genérica que admite cualquier tipo de respuesta T (Boolean, String, Int, etc.):
class PreguntaGenerica<T>(
    val enunciado: String,
    val respuesta: T,
    val dificultad: String
)

fun main() {
    // Pregunta cuya respuesta es de tipo Boolean:
    val p1 = PreguntaGenerica<Boolean>("¿Kotlin es interoperable con Java?", true, "Fácil")
    println("${p1.enunciado} -> Respuesta correcta: ${p1.respuesta}")

    // Pregunta cuya respuesta es de tipo String:
    val p2 = PreguntaGenerica<String>("¿Capital de España?", "Madrid", "Media")
    println("${p2.enunciado} -> Respuesta correcta: ${p2.respuesta}")

    // Pregunta cuya respuesta es de tipo Int:
    val p3 = PreguntaGenerica<Int>("¿Cuántos bits tiene un Byte?", 8, "Fácil")
    println("${p3.enunciado} -> Respuesta: ${p3.respuesta}")
}
```

#### D) `object` (Singleton) vs `companion object` vs `class` Tradicional

Una de las dudas conceptuales más recurrentes al aprender Kotlin desde Java o C++ es la desaparición de la palabra clave `static`. Kotlin es un lenguaje puramente orientado a objetos donde **todo es un objeto**, y reemplaza `static` mediante `object` y `companion object`.

---

##### 1. `class` Tradicional: El Molde para Múltiples Instancias Dinámicas
- **Qué es**: Es únicamente un plano arquitectónico o plantilla de diseño (*blueprint*). No existe en memoria hasta que se llama a su constructor (`Alumno("Ana", 20)`).
- **Memoria y Ciclo de Vida**: Puedes crear tantas instancias en el *Heap* como necesites. Cada una tiene su propio estado independiente. El recolector de basura (*Garbage Collector*) destruye cada objeto cuando ya no hay variables apuntando hacia él.
- **Cuándo usarla**: Para modelar cualquier entidad que tenga múltiples copias o registros en la aplicación (ej. `Bandera`, `Pedido`, `Usuario`, `Producto`).

---

##### 2. `object` (Declaración de Objeto): El Patrón Singleton Nativo
- **Qué es**: En Java o C++, implementar el patrón **Singleton** (asegurar que solo existe una única instancia en toda la aplicación) requería código farragoso: constructores privados, variables estáticas `volatile` y bloques sincronizados complejos para evitar colisiones entre hilos. En Kotlin, **el Singleton es un ciudadano de primer orden nativo**: se declara directamente con la palabra `object`.
- **Inicialización Perezosa y Segura (*Lazy & Thread-Safe*)**: La JVM no instancia el `object` al arrancar la app, sino **en el milisegundo exacto en que alguna línea de código accede a él por primera vez**. La máquina virtual garantiza internamente que esta instanciación es 100% segura entre hilos sin necesidad de bloqueos manuales.
- **Prohibición de Constructores**: Un `object` **NO puede tener constructores** (escribir `object MiSingleton(val x: Int)` es un error de compilación), ya que tú nunca lo instanciarás con `MiSingleton()`. Sin embargo, **sí puede tener un bloque `init { ... }`** para inicializar datos.
- **Nombre explícito**: Tanto los `object` como los `companion object` pueden llevar nombre propio explícito.
- **Cuándo usarlo**: Para configuraciones globales, clientes de red únicos, bases de datos locales o utilidades compartidas.

```kotlin
// Ejemplo exacto del temario oficial (Android_ProgramacionKotlin.pdf, Diapositiva 28):
object Progreso {
    var falladas: Int = 0
    var acertadas: Int = 0

    fun mostrarProgreso() {
        println("Aciertos: $acertadas, Fallos: $falladas")
    }
}
```

> [!NOTE]
> **Aclaración sobre `Datos.kt` en el código oficial (`Ud3_Ejemplo1`):**
> En el repositorio oficial del profesor (`Ud3_Ejemplo1`), el archivo `Datos.kt` se declara como una **clase normal**:
> ```kotlin
> class Datos {
>     fun cargarBanderas(): List<Bandera> = listOf(
>         Bandera(R.string.bandera1, R.drawable.bandera1),
>         ...
>     )
> }
> ```
> y se instancia en `MainActivity` mediante:
> ```kotlin
> val listaBanderas = Datos().cargarBanderas()
> ```
> Aunque en una arquitectura real de producción también podría implementarse como `object Datos` (Singleton) para reutilizar la lista sin instanciar objetos nuevos en memoria, en el código oficial de examen se declara y utiliza explícitamente como `class Datos`.

---

##### 3. `companion object` (Objeto Compañero): El Reemplazo de los Miembros `static`
- **Qué es**: Es un objeto singleton que vive **anidado dentro de una clase y está íntimamente ligado a ella**.
- **Acceso Privilegiado**: A diferencia de una clase externa, el `companion object` **tiene acceso completo a todos los miembros, constructores y métodos privados** de la clase que lo hospeda.
- **Nombre Explícito**: Por defecto se llama `Companion`, pero el temario oficial demuestra que se le puede dar un nombre explícito (ej. `companion object Progreso`).
- **Sintaxis de Llamada**: Se invoca usando directamente el nombre de la clase contenedora: `MiClase.miMetodo()` o `MiClase.CONSTANTE`, exactamente igual que un método estático en Java (o `MiClase.Progreso.miMetodo()` si tiene nombre).
- **Cuándo usarlo**:
  1. **Constantes ligadas a la pantalla**: Por ejemplo `const val TAG = "MainActivity"` para monitorizar el ciclo de vida en Logcat.
  2. **Métodos Factoría (*Factory Methods*)**: Funciones creadoras como `Usuario.crearInvitado()` o `Fragment.newInstance()` que validan reglas antes de llamar al constructor privado.
  3. **Interoperabilidad Java**: Con las anotaciones `@JvmStatic` o `const val`, el compilador genera un método o campo estático real en el bytecode de Java.

```kotlin
// Ejemplo exacto del temario oficial con nombre explícito (Android_ProgramacionKotlin.pdf, Diapositiva 28):
class ExamenPreguntas {
    companion object Progreso {
        var falladas: Int = 0
        var acertadas: Int = 0

        fun reiniciar() {
            falladas = 0
            acertadas = 0
        }
    }
}
```

---

##### Tabla Comparativa de Arquitectura:

| Característica | `class` Tradicional | `object` (Singleton) | `companion object` (Compañero) |
| :--- | :--- | :--- | :--- |
| **¿Cuántas instancias existen?** | Infinitas (creadas con `Clase()`). | **Exactamente 1** en toda la aplicación. | **Exactamente 1**, ligada a su clase contenedora. |
| **¿Tiene constructores?** | ✅ Sí (primario y secundarios). | ❌ **No permitido** (solo bloque `init`). | ❌ **No permitido** (solo bloque `init`). |
| **¿Cuándo se crea en memoria?** | Al invocar explícitamente `Clase(...)`. | De forma perezosa (*Lazy*) al primer acceso. | Al cargar la clase que lo hospeda. |
| **¿Puede heredar de clases/interfaces?**| ✅ Sí (hereda clases `open` e interfaces).| ✅ Sí (puede implementar interfaces y extender clases). | ✅ Sí (puede implementar interfaces). |
| **Forma de Invocar** | `val obj = MiClase(); obj.hacer()` | `MiSingleton.hacer()` | `MiClase.hacer()` |
| **Equivalente en Java** | Clase regular instanciable con `new`. | Patrón Singleton tradicional. | Miembros y métodos **`static`**. |

---

##### Ejemplo de Código Exhaustivo (Dentro del Código):

```kotlin
// ==========================================================
// 1. CLASS TRADICIONAL: Molde para instanciar muchos alumnos
// ==========================================================
class Alumno(val id: Int, val nombre: String) {
    fun presentarse() {
        println("Hola, soy $nombre (ID #$id)")
    }
}


// ==========================================================
// 2. OBJECT: Singleton global único para la configuración de red
// ==========================================================
object ServidorApi {
    // Propiedades compartidas por toda la app:
    val urlBase: String = "https://dam.iesraza.es/api/v1"
    var tokenSesion: String? = null

    // Bloque init: se ejecuta SOLO una vez en toda la vida de la app (al primer acceso)
    init {
        println("[ServidorApi] Inicializado por primera vez de forma perezosa (Lazy)")
    }

    fun conectar() {
        println("[ServidorApi] Conectando a $urlBase con token: $tokenSesion")
    }
}


// ==========================================================
// 3. COMPANION OBJECT: Constantes y Factoría estática dentro de una clase
// ==========================================================
class ConexionBD private constructor(val cadenaConexion: String) {

    // companion object: sustituto de 'static' en Java
    companion object {
        // Constante accesible a nivel de clase:
        const val PUERTO_DEFAULT = 5432
        private const val TAG = "ConexionBD"

        // Método factoría estático: valida antes de construir el objeto privado
        fun crearConexionSegura(host: String): ConexionBD {
            println("[$TAG] Creando conexión segura para el host: $host en puerto $PUERTO_DEFAULT")
            return ConexionBD("jdbc:postgresql://$host:$PUERTO_DEFAULT/dam_db")
        }
    }
}


fun main() {
    // ----------------------------------------------------------
    // USO 1: class (instancias independientes en Heap)
    // ----------------------------------------------------------
    val alumno1 = Alumno(1, "Guillermo")
    val alumno2 = Alumno(2, "Lucía")
    alumno1.presentarse() // Hola, soy Guillermo (ID #1)
    alumno2.presentarse() // Hola, soy Lucía (ID #2)
    // alumno1 y alumno2 tienen referencias de memoria distintas


    // ----------------------------------------------------------
    // USO 2: object (acceso directo al Singleton global)
    // ----------------------------------------------------------
    // ServidorApi no se instancia con (); se accede directamente por su nombre:
    ServidorApi.tokenSesion = "TOKEN_JWT_SECRETO_987"
    ServidorApi.conectar()
    // Si otra parte de la app accede a ServidorApi, compartirá exactamente el mismo token


    // ----------------------------------------------------------
    // USO 3: companion object (llamada idéntica a 'static' en Java)
    // ----------------------------------------------------------
    // Accedemos a la constante sin crear ninguna instancia de ConexionBD:
    println("Puerto estándar de la base de datos: ${ConexionBD.PUERTO_DEFAULT}")

    // Usamos el método factoría del companion object para obtener una instancia:
    val miBd = ConexionBD.crearConexionSegura("192.168.1.50")
    println("Cadena generada: ${miBd.cadenaConexion}")
}
```

---

## 7. Colecciones y Manejo Exhaustivo de Listas

Las colecciones son estructuras de datos que agrupan elementos. En Kotlin, la jerarquía de colecciones está estrictamente dividida entre interfaces de **solo lectura** (*read-only*) e interfaces **mutables**.

---

### 7.1 Arrays (`Array`)
Los arrays tienen un tamaño fijo definido en su creación y representan arreglos contiguos en memoria:
- Creación: `arrayOf("Lunes", "Martes", "Miércoles")` o primitivos especializados como `intArrayOf(1, 2, 3)`.
- Acceso: Se accede y asigna mediante corchetes `dias[0]` o `.get(0)` y `.set(0, "Domingo")`.
- Propiedad de tamaño: `dias.size`.

```kotlin
val dias = arrayOf("Lunes", "Martes", "Miércoles")
println(dias[0])        // "Lunes"
dias[0] = "Festivo"     // Válido: modifica el contenido, aunque el tamaño sigue siendo 3
```

---

### 7.2 Listas: `List` (Solo Lectura) vs `MutableList` (Modificable)

Una lista es una colección ordenada de elementos que permite duplicados y acceso posicional por índice (base 0).

#### A) Creación de Listas
- **`List<T>` (Solo lectura / Inmutable)**: Creada con `listOf(...)` o `emptyList()`. No expone métodos para añadir, borrar o reemplazar elementos.
  ```kotlin
  val frutas: List<String> = listOf("Manzana", "Plátano", "Naranja")
  // frutas.add("Pera") // ERROR: No existe el método add() en List
  ```
- **`MutableList<T>` (Modificable)**: Creada con `mutableListOf(...)`. Hereda de `List<T>` y añade métodos de modificación.
  ```kotlin
  val tareas: MutableList<String> = mutableListOf("Comprar pan", "Estudiar DAM")
  tareas.add("Hacer ejercicio") // Válido
  ```

#### B) Métodos y Propiedades de Acceso y Consulta
Tanto `List` como `MutableList` comparten los siguientes métodos esenciales:
- **`lista.size`**: Retorna el número de elementos.
- **`lista[i]` o `lista.get(i)`**: Obtiene el elemento en la posición `i` (lanza `IndexOutOfBoundsException` si el índice no existe).
- **`lista.first()` / `lista.last()`**: Devuelve el primer o último elemento.
- **`lista.firstOrNull()` / `lista.lastOrNull()`**: Devuelve el elemento o `null` si la lista está vacía (evita excepciones).
- **`lista.indexOf(elemento)`**: Devuelve el índice de la primera coincidencia, o `-1` si no existe.
- **`lista.contains(elemento)` o `elemento in lista`**: Evalúa si el elemento pertenece a la lista (`true`/`false`).
- **`lista.isEmpty()` / `lista.isNotEmpty()`**: Comprueba si la lista tiene o no elementos.

#### C) Métodos de Modificación en `MutableList`
- **`lista.add(elemento)`**: Añade al final de la lista.
- **`lista.add(indice, elemento)`**: Inserta el elemento desplazando los existentes a la derecha.
- **`lista.remove(elemento)`**: Elimina la primera aparición del elemento especificado (`true` si existía).
- **`lista.removeAt(indice)`**: Elimina el elemento en esa posición y devuelve el elemento borrado.
- **`lista[indice] = nuevoElemento`**: Sobrescribe el valor en dicha posición.
- **`lista.clear()`**: Vacía la lista por completo (`size = 0`).

```kotlin
val numeros = mutableListOf(10, 20, 30)
numeros.add(40)          // [10, 20, 30, 40]
numeros.add(0, 5)        // [5, 10, 20, 30, 40]
numeros.remove(20)       // [5, 10, 30, 40]
numeros.removeAt(1)      // Elimina el 10 -> [5, 30, 40]
numeros[0] = 99          // [99, 30, 40]
```

#### D) Iteración y Recorrido de Listas
Existen diversas formas idiomáticas de recorrer una lista:

```kotlin
val nombres = listOf("Ana", "Bernardo", "Carlos")

// 1. Bucle for tradicional:
for (nombre in nombres) {
    println("Hola, $nombre")
}

// 2. Bucle for con índice mediante withIndex():
for ((indice, nombre) in nombres.withIndex()) {
    println("Posición $indice: $nombre")
}

// 3. Método forEach con lambda e 'it':
nombres.forEach { println("Estudiante: $it") }

// 4. Método forEachIndexed:
nombres.forEachIndexed { i, valor ->
    println("Ítem #$i = $valor")
}
```

#### E) Operaciones Funcionales y Transformaciones
Kotlin proporciona funciones de orden superior sobre listas extremadamente potentes:
- **`filter { condicion }`**: Devuelve una nueva lista con los elementos que cumplen la condición.
  ```kotlin
  val notas = listOf(4, 7, 3, 9, 10, 5)
  val aprobados = notas.filter { it >= 5 } // [7, 9, 10, 5]
  ```
- **`map { transformacion }`**: Transforma cada elemento aplicando una lambda.
  ```kotlin
  val preciosSinIva = listOf(10.0, 20.0, 50.0)
  val preciosConIva = preciosSinIva.map { it * 1.21 } // [12.1, 24.2, 60.5]
  ```
- **`sortedBy { ... }` / `sortedByDescending { ... }`**: Ordena la lista por una propiedad.
  ```kotlin
  val alumnosOrdenados = listaAlumnos.sortedBy { it.nombre }
  ```
- **`find { ... }`**: Devuelve el primer elemento que cumpla el criterio o `null`.
- **`take(n)` / `drop(n)`**: Toma o descarta los primeros `n` elementos.

---

### 7.3 ¡Regla de Oro en Android!: Listas en Compose, UIState y StateFlow

> [!IMPORTANT]
> **¿Por qué en Android NUNCA debes poner `MutableList` dentro de un `UIState`?**
> Si tu estado tiene `val pedidos: MutableList<Pedido>` y haces `pedidos.add(nuevoPedido)`, la referencia del objeto en memoria **sigue siendo la misma**.
> En consecuencia, **Jetpack Compose NO se entera del cambio y NO redibuja la pantalla**.
> 
> **La solución estándar en MVVM**:
> 1. En el `UIState`, declara la lista siempre como de solo lectura:
>    ```kotlin
>    data class RestauranteUIState(
>        val pedidos: List<Pedido> = emptyList() // List inmutable
>    )
>    ```
> 2. En el `ViewModel`, para añadir un elemento se usa el operador `+` creando una **nueva lista**:
>    ```kotlin
>    fun insertarProducto(pedido: Pedido) {
>        _restauranteUIState.update { estadoActual ->
>            estadoActual.copy(
>                pedidos = estadoActual.pedidos + pedido // Genera una nueva instancia List
>            )
>        }
>    }
>    ```
> Al emitirse una referencia de lista distinta, Compose detecta el cambio instantáneamente y recompone la pantalla.

---

### 7.4 Conjuntos (`Set` vs `MutableSet`)

Un conjunto es una colección no ordenada de elementos únicos (no admite duplicados).

#### A) Comparativa: `List` vs `Set`

```kotlin
fun main() {
    // 1. LISTA (Admite duplicados y preserva el orden de inserción):
    val lista = listOf("Manzana", "Pera", "Manzana", "Naranja")
    println(lista)      // [Manzana, Pera, Manzana, Naranja] -> size: 4

    // 2. CONJUNTO (Elimina automáticamente los elementos repetidos):
    val conjunto = setOf("Manzana", "Pera", "Manzana", "Naranja")
    println(conjunto)   // [Manzana, Pera, Naranja] -> size: 3
}
```

#### B) Comparativa: `Set` (Solo Lectura) vs `MutableSet` (Modificable)

```kotlin
// Inmutable: solo consulta
val vocales: Set<Char> = setOf('a', 'e', 'i', 'o', 'u')
// vocales.add('y') // ERROR DE COMPILACIÓN: Set es de solo lectura

// Mutable: permite agregar y quitar elementos
val letrasAdivinadas: MutableSet<Char> = mutableSetOf('a', 'e')
letrasAdivinadas.add('i')      // [a, e, i]
letrasAdivinadas.add('a')      // No hace nada porque 'a' ya existe
letrasAdivinadas.remove('e')   // [a, i]

// Búsqueda ultra-rápida en O(1) con el operador 'in':
if ('i' in letrasAdivinadas) {
    println("¡La letra ya fue descubierta!")
}
```

#### C) Operaciones Algebraicas entre Conjuntos

Kotlin permite realizar operaciones matemáticas de conjuntos de manera muy intuitiva:

```kotlin
val grupoA = setOf("Java", "Kotlin", "Python")
val grupoB = setOf("Kotlin", "Dart", "Swift")

// Unión (+ o .union): elementos presentes en cualquiera de los dos
val union = grupoA + grupoB 
println(union) // [Java, Kotlin, Python, Dart, Swift]

// Intersección (.intersect): elementos comunes en ambos
val comunes = grupoA.intersect(grupoB)
println(comunes) // [Kotlin]

// Diferencia (- o .subtract): elementos del primero que NO están en el segundo
val soloA = grupoA - grupoB
println(soloA) // [Java, Python]
```
*(En el ejercicio del Ahorcado en `unidad4/ejemplo3`, se utiliza `private var palabrasUsadas = mutableSetOf<String>()` y `private var letrasIntentadas = mutableSetOf<Char>()` para garantizar que ni las palabras ni las letras jugadas se repitan).*

---

### 7.5 Mapas (`Map` vs `MutableMap`)

Un mapa (o diccionario / array asociativo) es una colección de pares clave-valor donde **las claves son estrictamente únicas**, pero los valores pueden repetirse.

#### A) Comparativa: `Map` (Solo Lectura) vs `MutableMap` (Modificable)

```kotlin
// ==========================================================
// 1. MAP DE SOLO LECTURA (mapOf)
// ==========================================================
// El operador 'to' es una función infija que crea un objeto Pair(clave, valor)
val capitales: Map<String, String> = mapOf(
    "ES" to "Madrid",
    "FR" to "París",
    "IT" to "Roma"
)

// Lectura segura por clave (devuelve String? porque la clave podría no existir):
val cap1: String? = capitales["ES"]        // "Madrid"
val cap2: String? = capitales["DE"]        // null (no provoca crash)

// capitales["PT"] = "Lisboa" // ERROR DE COMPILACIÓN: Map es inmutable


// ==========================================================
// 2. MUTABLE MAP (mutableMapOf)
// ==========================================================
val inventario: MutableMap<String, Int> = mutableMapOf(
    "Hamburguesa Clásica" to 15,
    "Hamburguesa Doble" to 8
)

// Inserción y actualización de valores:
inventario["Hamburguesa Vegana"] = 10      // Añade nuevo par clave-valor
inventario["Hamburguesa Clásica"] = 14     // Actualiza el valor existente
inventario.put("Patatas Fritas", 25)       // Método alternativo put()

// Eliminación de un par por su clave:
inventario.remove("Hamburguesa Doble")     // Elimina la clave y su valor
```

#### B) Métodos Clave de Consulta y Seguridad

```kotlin
val precios = mapOf("Café" to 1.50, "Té" to 1.20)

// 1. getOrDefault: Proporciona un fallback si la clave no existe
val precioZumo = precios.getOrDefault("Zumo", 2.00) // 2.00

// 2. Comprobación de existencia de claves o valores:
val tieneCafe = "Café" in precios            // true (equivale a .containsKey("Café"))
val existePrecio = precios.containsValue(3.0) // false

// 3. Obtener listas o conjuntos derivados:
val todasLasClaves: Set<String> = precios.keys
val todosLosValores: Collection<Double> = precios.values
```

#### C) Recorrido e Iteración con Desestructuración

Kotlin permite desestructurar fácilmente cada tupla `(clave, valor)` directamente en el bucle:

```kotlin
val puntuaciones = mapOf("Carlos" to 95, "Lucía" to 98, "Marcos" to 87)

// Recorrido directo con desestructuración:
for ((nombre, puntos) in puntuaciones) {
    println("Estudiante $nombre ha obtenido $puntos puntos")
}

// Recorrido con forEach funcional:
puntuaciones.forEach { (nombre, puntos) ->
    println("$nombre -> $puntos")
}
```

---

## 8. Null Safety (Seguridad frente a Nulos)

El sistema de tipos de Kotlin está diseñado para erradicar las temidas excepciones `NullPointerException` (conocidas como "el error del billón de dólares"). Kotlin distingue en tiempo de compilación entre:
- **Tipos no anulables (`T`)**: Nunca pueden valer `null`.
- **Tipos anulables (`T?`)**: Pueden contener una referencia válida o `null`.

```kotlin
var noNulo: String = "Texto asegurado"
// noNulo = null // ERROR DE COMPILACIÓN en el editor

var anulable: String? = "Texto opcional"
anulable = null // TOTALMENTE VÁLIDO gracias al sufijo '?'
```

---

### 8.1 Tabla Comparativa de Operadores de Nulabilidad

| Operador | Nombre | Comportamiento si la variable es `null` | Comportamiento si NO es `null` | Nivel de Riesgo |
| :--- | :--- | :--- | :--- | :--- |
| **`?.`** | **Safe Call (Llamada Segura)** | Devuelve `null` directamente sin ejecutar el método. | Ejecuta la propiedad o método normalmente. | 🟢 Seguro |
| **`?:`** | **Elvis Operator** | Devuelve el valor por defecto indicado a la derecha. | Devuelve el valor de la izquierda sin evaluar la derecha. | 🟢 Seguro |
| **`?.let { }`** | **Safe Call + Scope Function** | No entra al bloque de llaves `{ }`. | Ejecuta el bloque con la variable no nula tipada como `it`. | 🟢 Seguro |
| **`!!`** | **Not-Null Assertion** | **Lanza de inmediato una excepción `NullPointerException` (Crash)**. | Desempaqueta el tipo forzosamente a no nulo (`T`). | 🔴 Peligroso |

---

### 8.2 Comparativa Práctica en Código: `?.` vs `?:` vs `?.let` vs `!!`

Veamos cómo reacciona cada operador ante la misma variable nullable:

```kotlin
fun procesarEntradaUsuario(input: String?) {

    println("--- Analizando input: '$input' ---")

    // ==========================================================
    // 1. LLAMADA SEGURA (?.)
    // ==========================================================
    // Devuelve un Int? (la longitud o null)
    val longitud: Int? = input?.length
    println("1. Safe Call longitud: $longitud")


    // ==========================================================
    // 2. OPERADOR ELVIS (?:)
    // ==========================================================
    // Si la expresión de la izquierda es null, adopta el valor de la derecha
    val longitudSegura: Int = input?.length ?: 0
    val textoMostrar: String = input ?: "Texto por defecto (vacío)"
    println("2. Elvis texto: $textoMostrar (longitud: $longitudSegura)")


    // ==========================================================
    // 3. LLAMADA SEGURA CON LET (?.let { ... })
    // ==========================================================
    // Solo se ejecuta si input NO es null; dentro del bloque, 'it' ya NO es nullable
    input?.let { textoNoNulo ->
        println("3. Let ejecutado: '${textoNoNulo.uppercase()}' (longitud: ${textoNoNulo.length})")
    } ?: run {
        println("3. Let no se ejecutó porque el valor era null")
    }


    // ==========================================================
    // 4. ASERCIÓN NO NULA (!!)
    // ==========================================================
    // Le decimos al compilador: "Juro que esto no es null, créeme".
    // Si input es null -> CRASH INMEDIATO de la aplicación (NullPointerException)
    try {
        val forzoso: Int = input!!.length
        println("4. Aserción forzosa exitosa: $forzoso")
    } catch (e: NullPointerException) {
        println("4. ¡CRASH EVITADO!: input!!.length provocó NullPointerException")
    }
}

fun main() {
    // Caso A: Con valor válido
    procesarEntradaUsuario("Hola Android")

    // Caso B: Con valor null
    procesarEntradaUsuario(null)
}
```

#### Salida del Caso B (`input = null`):
```text
--- Analizando input: 'null' ---
1. Safe Call longitud: null
2. Elvis texto: Texto por defecto (vacío) (longitud: 0)
3. Let no se ejecutó porque el valor era null
4. ¡CRASH EVITADO!: input!!.length provocó NullPointerException
```

---

### 8.3 Smart Casts (Casteos Inteligentes)

El compilador de Kotlin rastrea las comprobaciones condicionales. Si ya comprobaste que una variable no es nula con un `if`, Kotlin la convierte automáticamente a no nula dentro del bloque:

```kotlin
fun imprimirLongitud(mensaje: String?) {
    // Fuera del if, mensaje es String? (podría ser nulo)
    // println(mensaje.length) // ERROR: Only safe (?.) or non-null asserted (!!.) calls are allowed

    if (mensaje != null) {
        // SMART CAST: El compilador sabe con 100% de certeza que aquí mensaje no es null
        // Por lo tanto, no requiere el operador '?'
        println("Longitud garantizada: ${mensaje.length}")
    } else {
        println("El mensaje era nulo")
    }
}
```
