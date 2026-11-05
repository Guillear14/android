# Manual y Guía de Estudio de Android, Kotlin y Compose
### 2º DAM — Programación Multimedia y Dispositivos Móviles (PMDM)

Este manual ha sido estructurado en documentos temáticos independientes para facilitar su estudio, consulta rápida y preparación de exámenes prácticos y teóricos.

---

## 📚 Índice de Documentos

1. [**Parte 1: Fundamentos del Lenguaje Kotlin y Manejo Exhaustivo de Listas**](./01_KOTLIN_FUNDAMENTOS.md)
   - **Plataforma Android (`Android_Ud1_Introduccion.pdf`)**: Capas de arquitectura (4 y 5 capas: HAL, ART, Kernel), los 4 componentes (`Activity`, `Service`, `Content Provider`, `Broadcast Receiver`), `AndroidManifest.xml` (`MAIN`/`LAUNCHER`), estructura de carpetas y los 5 niveles de `Log` con la advertencia de límite de pila en Logcat.
   - Historia e interoperabilidad con Java.
   - Variables (`val` vs `var`), inferencia de tipos y plantillas de cadenas (`$`).
   - Estructuras de control (`if` como expresión, `when` avanzado, bucles `for`, `while` y rangos).
   - Funciones, parámetros nombrados, valores por defecto, lambdas e identificador `it`.
   - Programación Orientada a Objetos (constructores, backing field `field`, `open`/`override`, interfaces, visibilidad, delegación con `by`, `data class`, `enum`, `object` y `companion object`).
   - **Manejo completo de Listas y Colecciones**: `Array`, `List` (solo lectura) vs `MutableList`, métodos (`add`, `remove`, `size`, `first`, `indexOf`), iteración, transformaciones funcionales (`filter`, `map`, `sortedBy`), y **por qué en Compose el `UIState` debe usar `List` inmutable y actualizarse con `+`**.
   - Conjuntos (`Set`/`MutableSet`) y Mapas (`Map`/`MutableMap`).
   - Null Safety (`?`, `?.`, `?:`, `!!`, smart casts).

2. [**Parte 2: Diseño de Interfaces con Jetpack Compose (DIU)**](./02_JETPACK_COMPOSE_DIU.md)
   - Paradigma declarativo vs imperativo.
   - Funciones `@Composable` y reglas de nombrado.
   - Modificadores (`Modifier`): **el impacto crítico del orden de encadenamiento**.
   - Textos, estilos, unidades `.sp`, alineación `Start`/`End` vs `Left`/`Right` (soporte RTL).
   - Campos de texto (`TextField` vs `OutlinedTextField`, teclados y validación).
   - Manejo de imágenes, densidades de pantalla (`drawable-nodpi`) y accesibilidad.
   - Contenedor `Scaffold` y uso obligatorio de `innerPadding`.
   - Layouts (`Column`, `Row`, `Box`) y componentes interactivos (`RadioButton` agrupados con `selectableGroup`, `Checkbox`, `Switch`, `AlertDialog`).
   - Localización e Internacionalización (`strings.xml`, `values-es`, `values-en`, ISO 639-1).

3. [**Parte 3: Listas Diferidas, Cuadrículas y Temas Material 3**](./03_LISTAS_GRIDS_TEMAS.md)
   - Por qué `LazyColumn` recicla en memoria frente a `Column` con scroll (prevención de `OutOfMemoryError`).
   - Sintaxis DSL: `item` e `items`.
   - Tarjetas con animación de tamaño `.animateContentSize()`.
   - Cuadrículas con `LazyVerticalGrid` (`GridCells.Fixed` vs `GridCells.Adaptive`).
   - Sistema de temas Material Design 3 (`Color.kt`, `Shape.kt`, `Type.kt` con fuentes personalizadas en `res/font/`).

4. [**Parte 4: Arquitectura, Ciclo de Vida y Gestión de Estado**](./04_ARQUITECTURA_CICLO_DE_VIDA.md)
   - Los 7 callbacks del ciclo de vida de una Activity (`onCreate`, `onStart`, `onResume`, `onPause`, `onStop`, `onRestart`, `onDestroy`).
   - El problema de la rotación de pantalla (*Configuration Change*).
   - Principios de Arquitectura recomendados por Google (Separación de problemas y UDF - *Unidirectional Data Flow*).
   - Rol del `ViewModel` y su ciclo de vida persistente.
   - Flujo reactivo con `StateFlow` y `MutableStateFlow` (`.asStateFlow()`, `.update { copy(...) }`, `.collectAsState()`).

5. [**Parte 5: Guía Maestra del Estado — ¿Dónde poner cada dato?**](./05_GUIA_MAESTRA_ESTADO.md)
   - Comparativa de los 3 niveles: Composable local (`remember`), `UIState` (`data class`) y `ViewModel`.
   - Diagrama de flujo de decisión.
   - Configuración oficial de Gradle (`libs.versions.toml`) y el patrón híbrido del profesor (`StateFlow` + `mutableStateOf` con `private set`).
   - Matriz de ejemplos prácticos reales de DAM (Restaurante DAM, Ahorcado, Calculadoras).
   - Errores comunes y anti-patrones habituales en exámenes (con código INCORRECTO ❌ vs CORRECTO ✅).

6. [**Parte 6: Mapa de Ejercicios Prácticos del Proyecto**](./06_MAPA_EJERCICIOS_PRACTICOS.md)
   - Tabla de equivalencia directa con los 8 repositorios oficiales de GitHub del profesor (`Ud2_Ejercicio1`, `Ud3_Ejemplo1`, `Ud3_Ejemplo2`, `Ud3_Ejemplo3`, `Ud4_Ejemplo1`, `Ud4_Ejemplo2`, `Ud4_Ejemplo3`, `Ud4_Ejercicio`).
   - Relación cruzada entre cada concepto teórico y los archivos de código `.kt` del proyecto unificado `EjerciciosAndroid`.
   - Matriz comparativa sintética de preparación de exámenes.
