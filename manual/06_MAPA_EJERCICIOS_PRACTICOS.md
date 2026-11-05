# Parte 6: Mapa de Ejercicios Prácticos del Proyecto
### 2º DAM — Programación Multimedia y Dispositivos Móviles (PMDM)

Este documento relaciona cada concepto teórico de los manuales anteriores con los archivos de código fuente exactos del proyecto integrado `EjerciciosAndroid`.

---

## 🔗 Tabla de Equivalencia: Repositorios Oficiales del Profesor (`Fpcarlosc`) vs Paquetes del Proyecto

El profesor organizó sus prácticas y exámenes en **8 repositorios oficiales de GitHub**. Para facilitar el estudio y la ejecución unificada, en este proyecto se han integrado todos ellos organizados por unidades temáticas:

| Repositorio Oficial GitHub (`Fpcarlosc`) | Paquete en `EjerciciosAndroid` | Tema Principal y Componentes Evaluados |
| :--- | :--- | :--- |
| **`Ud2_Ejercicio1`** | [`unidad2/ejercicio1`](../../app/src/main/java/com/example/ejerciciosandroid/unidad2/ejercicio1) | Calculadora de IVA con `OutlinedTextField`, `RadioButton` y formateo monetario `NumberFormat`. |
| **`Ud3_Ejemplo1`** | [`unidad3/ejemplo1`](../../app/src/main/java/com/example/ejerciciosandroid/unidad3/ejemplo1) | Lista básica con `LazyColumn`, reciclaje diferido, modelo `Bandera` y clase `class Datos`. |
| **`Ud3_Ejemplo2`** | [`unidad3/ejemplo2`](../../app/src/main/java/com/example/ejerciciosandroid/unidad3/ejemplo2) | Detección de clics en `LazyColumn` con `Modifier.clickable` y emisión a Logcat (`Log.v`). *(Ampliación en proyecto: Toast y visualización en pantalla)*. |
| **`Ud3_Ejemplo3`** | [`unidad3/ejemplo3`](../../app/src/main/java/com/example/ejerciciosandroid/unidad3/ejemplo3) | Tarjetas `Card` desplegables con animación suave mediante `Modifier.animateContentSize()` y capital del país en `@StringRes capitalResourceId`. |
| **`Ud4_Ejemplo1`** | [`unidad4/ejemplo1`](../../app/src/main/java/com/example/ejerciciosandroid/unidad4/ejemplo1) | Monitor de los 7 callbacks del Ciclo de Vida de una `ComponentActivity` en pantalla y Logcat (comenzando en `onStart`). |
| **`Ud4_Ejemplo2`** | [`unidad4/ejemplo2`](../../app/src/main/java/com/example/ejerciciosandroid/unidad4/ejemplo2) | Calculadora MVVM con `StateFlow`, patrón híbrido `mutableStateOf`, `toIntOrNull()` y vaciado en `onFocusChanged`. |
| **`Ud4_Ejemplo3`** | [`unidad4/ejemplo3`](../../app/src/main/java/com/example/ejerciciosandroid/unidad4/ejemplo3) | **Juego del Ahorcado oficial**: UDF, StateFlow, 5 propiedades de estado, `palabraOculta` y `AlertDialog`. *(Ampliación en proyecto: contador de errores y letras falladas)*. |
| **`Ud4_Ejercicio`** | [`unidad4/ejercicio4`](../../app/src/main/java/com/example/ejerciciosandroid/unidad4/ejercicio4) | **Restaurante DAM oficial**: POO con `Producto`, `Hamburguesa`, `Pizza`, `Pedido(producto, cantidad)`, `RadioButton`, `Checkbox` (queso), `Switch` y acumulación en `StateFlow`. |

*(Nota: Los paquetes adicionales `unidad1/ejemplo1`, `unidad1/ejemplo2`, `unidad2/ejemplo2` y `unidad3/ejemplo4` fueron añadidos para cubrir de forma exhaustiva los conceptos teóricos de introducción a Kotlin, catálogo de layouts y cuadrículas responsivas).*

---

## 🗺️ Mapa Completo de Implementación Práctica

### 🔹 Unidad 1: Introducción y Fundamentos de Kotlin

#### 1. `unidad1/ejemplo1/` — Monitor de Logcat y Estructura Base
- **Teoría aplicada**:
  - Uso de la clase `android.util.Log` con sus distintos niveles: `Log.v`, `Log.d`, `Log.i`, `Log.w`, `Log.e`.
  - Definición de constante `TAG` en cabecera.
  - Registro de eventos en el callback `onCreate()`.
- **Archivo principal**: [`unidad1/ejemplo1/MainActivity.kt`](../../app/src/main/java/com/example/ejerciciosandroid/unidad1/ejemplo1/MainActivity.kt)

#### 2. `unidad1/ejemplo2/` — Fundamentos del Lenguaje Kotlin
- **Teoría aplicada**:
  - Comprobación interactiva de mutabilidad (`val` inmutable vs `var` mutable).
  - Estructura `when` con evaluación de rangos numéricos (`in 1..10`).
  - Colecciones estándar: `List` (ordenada), `Set` (sin duplicados) y `Map` (clave-valor).
  - Null Safety: Operador de llamada segura `?.` y operador Elvis `?:`.
- **Archivo principal**: [`unidad1/ejemplo2/MainActivity.kt`](../../app/src/main/java/com/example/ejerciciosandroid/unidad1/ejemplo2/MainActivity.kt)

---

### 🔹 Unidad 2: Diseño de Interfaces de Usuario (DIU)

#### 3. `unidad2/ejercicio1/` — Calculadora de IVA Dinámica
- **Teoría aplicada**:
  - Campo de entrada numérica `OutlinedTextField` con `KeyboardOptions(keyboardType = KeyboardType.Number)`.
  - Grupo de botones de selección exclusiva `RadioButton` con `Modifier.selectableGroup()` y `Modifier.selectable()`.
  - Formateo de precios y moneda con la clase estándar `NumberFormat.getCurrencyInstance()`.
  - Estado local gestionado con `remember { mutableStateOf(...) }`.
- **Archivo principal**: [`unidad2/ejercicio1/MainActivity.kt`](../../app/src/main/java/com/example/ejerciciosandroid/unidad2/ejercicio1/MainActivity.kt)

#### 4. `unidad2/ejemplo2/` — Catálogo de Componentes y Layouts Compose
- **Teoría aplicada**:
  - Variantes tipográficas con `fontSize` en unidades `.sp` y alineación con `TextAlign.Start` y `TextAlign.End`.
  - Comparativa directa entre `TextField` (estilo con relleno) y `OutlinedTextField` (estilo contorneado).
  - Superposición de elementos en capas con el contenedor `Box`.
  - Demostración práctica del **impacto del orden en los modificadores** (`Modifier.clickable().padding()` vs `Modifier.padding().clickable()`).
  - Uso correcto del contenedor `Scaffold` con aplicación obligatoria de `innerPadding`.
- **Archivo principal**: [`unidad2/ejemplo2/MainActivity.kt`](../../app/src/main/java/com/example/ejerciciosandroid/unidad2/ejemplo2/MainActivity.kt)

---

### 🔹 Unidad 3: Listas Desplegables, Grids y Temas

#### 5. `unidad3/ejemplo1/` — Lista Básica con `LazyColumn`
- **Teoría aplicada**:
  - Despliegue eficiente de colecciones mediante reciclaje de memoria con `LazyColumn` y `items()`.
  - Separación limpia en capas:
    - Capa de modelo: `data class Bandera` con anotaciones `@StringRes` y `@DrawableRes`.
    - Capa de datos: Repositorio `Datos.kt` con la colección de datos.
- **Archivos**:
  - Modelo: [`Bandera.kt`](../../app/src/main/java/com/example/ejerciciosandroid/unidad3/ejemplo1/modelo/Bandera.kt)
  - Repositorio: [`Datos.kt`](../../app/src/main/java/com/example/ejerciciosandroid/unidad3/ejemplo1/datos/Datos.kt)
  - Pantalla: [`MainActivity.kt`](../../app/src/main/java/com/example/ejerciciosandroid/unidad3/ejemplo1/MainActivity.kt)

#### 6. `unidad3/ejemplo2/` — Lista con Detección de Clics
- **Repositorio Oficial (`Fpcarlosc/Ud3_Ejemplo2`)**:
  - Detección de eventos de pulsación individual en cada tarjeta de la lista mediante `Modifier.clickable { ... }`.
  - Emisión de la traza a la consola Logcat en nivel Verbose (`Log.v(TAG, "Bandera pulsada: $nombre ...")`).
- **Ampliación en proyecto `EjerciciosAndroid`**:
  - Notificación emergente visual mediante `Toast.makeText(...)` y retención de la última bandera pulsada en pantalla (`ultimaBanderaPulsada`).
- **Archivos**: Paquete [`unidad3/ejemplo2/`](../../app/src/main/java/com/example/ejerciciosandroid/unidad3/ejemplo2)

#### 7. `unidad3/ejemplo3/` — Tarjetas Expandibles con Animación
- **Teoría aplicada**:
  - Modelo `Bandera` ampliado con su tercera propiedad: `@StringRes val capitalResourceId: Int`.
  - Tarjetas `Card` con animación automática de tamaño mediante `Modifier.animateContentSize()`.
  - Control de iconos vectoriales interactivos (`expand_circle_up` y `expand_circle_down`).
  - Estado local de apertura/cierre encapsulado con `var abierto by remember { mutableStateOf(false) }`.
  - Visualización de la capital resolviendo el recurso oficial: `stringResource(bandera.capitalResourceId)`.
- **Archivos**: Paquete [`unidad3/ejemplo3/`](../../app/src/main/java/com/example/ejerciciosandroid/unidad3/ejemplo3)

#### 8. `unidad3/ejemplo4/` — Cuadrículas con `LazyVerticalGrid`
- **Teoría aplicada**:
  - Organización en rejilla con `LazyVerticalGrid`.
  - Comparativa en tiempo real entre columnas estáticas con `GridCells.Fixed(count = 2)` y columnas responsivas con `GridCells.Adaptive(minSize = 128.dp)` *(según el PDF de la Unidad 3)*.
- **Archivos**: Paquete [`unidad3/ejemplo4/`](../../app/src/main/java/com/example/ejerciciosandroid/unidad3/ejemplo4)

---

### 🔹 Unidad 4: Arquitectura, Ciclo de Vida y Estado

#### 9. `unidad4/ejemplo1/` — Monitor del Ciclo de Vida de una Actividad
- **Teoría aplicada**:
  - Sobrescritura e inspección en tiempo real de los 7 callbacks del ciclo de vida:
    `onCreate()`, `onStart()`, `onResume()`, `onPause()`, `onStop()`, `onRestart()`, `onDestroy()`.
  - Emisión sincronizada a consola Logcat y a la pantalla del dispositivo móvil (nota de examen: el profesor no colocó `Log.d` en `onCreate()`, por lo que la traza del PDF empieza en `onStart`).
- **Archivo principal**: [`unidad4/ejemplo1/MainActivity.kt`](../../app/src/main/java/com/example/ejerciciosandroid/unidad4/ejemplo1/MainActivity.kt)

#### 10. `unidad4/ejemplo2/` — Calculadora Reactiva MVVM
- **Teoría aplicada**:
  - Separación de capas estricta: Capa de UI (`MainActivity.kt`), Capa de ViewModel (`OperacionViewModel.kt`) y Estado inmutable (`OperacionUIState.kt`).
  - Flujo reactivo con `StateFlow` y `MutableStateFlow`.
  - Patrón híbrido del profesor: entradas de texto controladas con `mutableStateOf` y reseteadas al ganar foco con `.onFocusChanged { if (it.isFocused) operacionViewModel.actualizarNumero1("") }`.
  - Conversión segura con `.toIntOrNull() ?: 0`.
  - Actualización atómica de estado con `.copy()`.
  - Observación reactiva en Compose con `.collectAsState()`.
- **Archivos**:
  - Estado: [`OperacionUIState.kt`](../../app/src/main/java/com/example/ejerciciosandroid/unidad4/ejemplo2/modelo/OperacionUIState.kt)
  - ViewModel: [`OperacionViewModel.kt`](../../app/src/main/java/com/example/ejerciciosandroid/unidad4/ejemplo2/ui/OperacionViewModel.kt)
  - Pantalla: [`MainActivity.kt`](../../app/src/main/java/com/example/ejerciciosandroid/unidad4/ejemplo2/MainActivity.kt)

#### 11. `unidad4/ejemplo3/` — Juego del Ahorcado (Hangman)
- **Repositorio Oficial (`Fpcarlosc/Ud4_Ejemplo3`)**:
  - Arquitectura UDF (*Unidirectional Data Flow*) con `StateFlow<JuegoUIState>`.
  - `JuegoUIState` con 5 propiedades esenciales: `palabraActual`, `isLetraIncorrecta`, `puntos`, `ronda`, `isFinJuego`.
  - Enmascaramiento de caracteres mediante `palabraOculta` y comprobación con `.indices`.
  - Diálogo modal de fin de juego con `AlertDialog`.
- **Ampliación en proyecto `EjerciciosAndroid` (Práctica de examen)**:
  - **Contador de errores**: registro de fallos en la ronda (`contadorErrores`) y fallos globales acumulados (`totalErrores`).
  - **Visualizador de letras falladas**: colección inmutable `letrasFallidas: List<Char>` que dibuja distintivos e insignias rojas en la interfaz.
- **Archivos**:
  - Estado: [`JuegoUIState.kt`](../../app/src/main/java/com/example/ejerciciosandroid/unidad4/ejemplo3/modelo/JuegoUIState.kt)
  - ViewModel: [`JuegoViewModel.kt`](../../app/src/main/java/com/example/ejerciciosandroid/unidad4/ejemplo3/ui/JuegoViewModel.kt)
  - Pantalla: [`PantallaJuego.kt`](../../app/src/main/java/com/example/ejerciciosandroid/unidad4/ejemplo3/PantallaJuego.kt)
  - Actividad: [`MainActivity.kt`](../../app/src/main/java/com/example/ejerciciosandroid/unidad4/ejemplo3/MainActivity.kt)

#### 12. `unidad4/ejercicio4/` — Restaurante DAM (Gestión de Pedidos)
- **Repositorio Oficial (`Fpcarlosc/Ud4_Ejercicio`)**:
  - Jerarquía de herencia orientada a objetos: clase abierta `open class Producto` extendida por `Hamburguesa` y `Pizza`.
  - Modelo `Pedido(val producto: Producto = Producto(), val cantidad: String = "")`.
  - Formulario interactivo:
    - Selección de producto principal (`Hamburguesa` o `Pizza`) con **`RadioButton`**.
    - Opciones de hamburguesa: tipo de carne con **`RadioButton`** y queso extra (`isQueso`) con **`Checkbox`**.
    - Opciones de pizza: tipo de pizza y tamaño (`tamanyo`) con **`RadioButton`**.
    - Tipo de masa con **`Switch`**.
  - Acumulación reactiva en `StateFlow`: la función `insertarProducto(pedido: Pedido)` recibe el pedido instanciado desde la interfaz y actualiza la lista inmutable con `pedidos = estadoActual.pedidos + pedido`.
- **Archivos**:
  - Modelos: [`modelo/Producto.kt`](../../app/src/main/java/com/example/ejerciciosandroid/unidad4/ejercicio4/modelo/Producto.kt), [`Hamburguesa.kt`](../../app/src/main/java/com/example/ejerciciosandroid/unidad4/ejercicio4/modelo/Hamburguesa.kt), [`Pizza.kt`](../../app/src/main/java/com/example/ejerciciosandroid/unidad4/ejercicio4/modelo/Pizza.kt), [`Pedido.kt`](../../app/src/main/java/com/example/ejerciciosandroid/unidad4/ejercicio4/modelo/Pedido.kt)
  - ViewModel: [`ui/RestauranteViewModel.kt`](../../app/src/main/java/com/example/ejerciciosandroid/unidad4/ejercicio4/ui/RestauranteViewModel.kt)
  - Pantalla: [`MainActivity.kt`](../../app/src/main/java/com/example/ejerciciosandroid/unidad4/ejercicio4/MainActivity.kt)

---

## 📊 Matriz Comparativa de Ejercicios Prácticos

Esta tabla sintetiza las diferencias esenciales entre cada ejercicio para preparar exámenes prácticos:

| Ejercicio | Unidad | Arquitectura | Gestión del Estado | Componentes UI Clave | ¿Qué diferencia a este ejercicio? |
| :--- | :---: | :---: | :--- | :--- | :--- |
| **`unidad1/ejemplo1`** | Ud 1 | Básica | N/A | Logcat | Jerarquía de niveles de log (`Log.v` a `Log.e`). |
| **`unidad1/ejemplo2`** | Ud 1 | Básica | `remember` simple | `Text`, `Button`, `Column` | Sintaxis Kotlin: `val`/`var`, `when`, listas y operador Elvis `?:`. |
| **`unidad2/ejercicio1`** | Ud 2 | Compose UI | `remember` local | `OutlinedTextField`, `RadioButton`, `selectableGroup` | Formateo monetario automático con `NumberFormat.getCurrencyInstance()`. |
| **`unidad2/ejemplo2`** | Ud 2 | Compose UI | `remember` local | `TextField` vs `OutlinedTextField`, `Box`, `Scaffold` | Impacto crítico del orden en `Modifier` (`clickable` antes o después de `padding`). |
| **`unidad3/ejemplo1`** | Ud 3 | Separación Capas | Lista inmutable | `LazyColumn`, `items()`, `Card` | Reciclaje de memoria diferido y anotaciones `@StringRes` / `@DrawableRes`. |
| **`unidad3/ejemplo2`** | Ud 3 | Separación Capas | Lista inmutable | `LazyColumn` + `clickable` | Detección de clics individuales en elementos de lista reciclados. |
| **`unidad3/ejemplo3`** | Ud 3 | Compose UI | `remember` por ítem | `Card`, `.animateContentSize()`, `IconButton` | Animación automática de apertura y estado local booleano por tarjeta. |
| **`unidad3/ejemplo4`** | Ud 3 | Responsive UI | Lista inmutable | `LazyVerticalGrid` | Comparativa en tiempo real de `GridCells.Fixed(2)` vs `GridCells.Adaptive(128.dp)`. |
| **`unidad4/ejemplo1`** | Ud 4 | Ciclo de Vida | En memoria Activity | `Text` sincronizado con Logcat | Los 7 callbacks del ciclo de vida y la reconstrucción al rotar el teléfono. |
| **`unidad4/ejemplo2`** | Ud 4 | MVVM Reactivo | `StateFlow` + `UIState` | `TextField`, `Text`, `Button` | UDF estricto con actualización atómica `_uiState.update { copy() }`. |
| **`unidad4/ejemplo3`** | Ud 4 | MVVM Reactivo | `StateFlow` + `UIState` + VM | `AlertDialog`, Insignias de error, `TextField` | **Ahorcado**: Contador de errores, visualizador de letras falladas y `MutableSet` interno. |
| **`unidad4/ejercicio4`** | Ud 4 | MVVM + POO | `StateFlow` + `List` | `RadioButton`, `Checkbox`, `Switch`, `LazyColumn` | **Restaurante DAM**: Herencia (`open class Producto`), polimorfismo y listas inmutables acumulativas. |
