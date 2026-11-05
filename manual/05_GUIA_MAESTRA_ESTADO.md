# Parte 5: Guía Maestra del Estado
### ¿Cuándo poner las cosas en el `ViewModel`, cuándo en el `UIState` y cuándo en el Composable?
### 2º DAM — Programación Multimedia y Dispositivos Móviles (PMDM)

Esta es la cuestión de arquitectura más importante de 2º DAM. Saber clasificar correctamente cada dato evita código enrevesado, bugs al girar la pantalla y recomposiciones innecesarias.

---

## 1. El Triángulo del Estado en Compose

En una aplicación moderna con Jetpack Compose y MVVM, el estado vive en tres niveles diferentes:

```
                      ┌─────────────────────────────────┐
                      │            VIEWMODEL            │
                      │  (Cerebro, algoritmos, flujos)  │
                      └────────────────┬────────────────┘
                                       │
                      ┌────────────────┴────────────────┐
                      │             UISTATE             │
                      │  (Fotografía inmutable de UI)   │
                      └────────────────┬────────────────┘
                                       │
                      ┌────────────────┴────────────────┐
                      │        COMPOSABLE LOCAL         │
                      │  (`remember { mutableStateOf }`)│
                      └─────────────────────────────────┘
```

#### Tabla Comparativa Exhaustiva de los 3 Niveles:

| Criterio | Composable Local (`remember`) | `UIState` (`data class`) | `ViewModel` (Clase lógica) |
| :--- | :--- | :--- | :--- |
| **Ubicación en Memoria** | Ligado al árbol de composición del Composable. | Instancia inmutable dentro del `StateFlow` del ViewModel. | Objeto gestionado por el framework de Android (`ViewModelStore`). |
| **¿Sobrevive a Recomposición?** | ✅ **SÍ** (gracias a `remember`). | ✅ **SÍ** (persiste en el StateFlow). | ✅ **SÍ**. |
| **¿Sobrevive a Rotación?** | ❌ **NO** (se destruye y resetea). | ✅ **SÍ** (retenido por el ViewModel). | ✅ **SÍ** (retiene datos y corrutinas activas). |
| **¿Contiene lógica?** | ❌ NO (solo valores visuales). | ❌ NO (solo datos pasivos `val`). | ✅ **SÍ** (cálculos, validaciones, algoritmos). |
| **Cómo se lee en la UI** | Directamente: `texto` o `textoState.value`. | `val uiState by vm.uiState.collectAsState()`. | Invocando métodos: `vm.hacerAccion()`. |
| **Cómo se actualiza** | Reasignación: `texto = nuevo`. | Solo el ViewModel con `_uiState.update { copy() }`. | Ejecutando funciones públicas de negocio. |

---

## 2. Diagrama de Decisión: ¿Dónde guardo mi variable?

Hazte las siguientes preguntas en orden:

```
[ ¿Necesito guardar una variable o dato? ]
                   │
                   ▼
¿Afecta a las reglas de negocio, lógica de la app, cálculo, o debe
sobrevivir intacta si el usuario gira el teléfono móvil?
                   │
         ┌─────────┴─────────┐
        SÍ                  NO
         │                   │
         │                   ▼
         │        ¿Es puramente visual o efímera de este widget?
         │        (ej: si una tarjeta está desplegada, color temporal...)
         │                   │
         │                   ▼
         │        ┌──────────────────────────────────────────────┐
         │        │ Guardar en el COMPOSABLE LOCAL con:          │
         │        │ `var estado by remember { mutableStateOf() }`│
         │        └──────────────────────────────────────────────┘
         ▼
¿Es un DATO que la pantalla debe MOSTRAR o es una ACCIÓN / CONTROL INTERNO?
         │
         ├─────────────────────────────────────────┐
         ▼                                         ▼
   [ Es un DATO VISIBLE ]                 [ Es LÓGICA / CONTROL INTERNO ]
         │                                         │
         ▼                                         ▼
┌─────────────────────────────────┐       ┌─────────────────────────────────┐
│ Guardar dentro de `UIState`     │       │ Guardar dentro de `ViewModel`   │
│ (`data class` inmutable con val)│       │ (Métodos o variables privadas   │
│                                 │       │  como Sets o índices internos)  │
└─────────────────────────────────┘       └─────────────────────────────────┘
```

---

## 3. Definición Detallada de cada Nivel

### Nivel 1: En el `UIState` (`data class` inmutable)
**¿Qué es?** Es la representación fiel de la pantalla en un momento determinado.
- **Qué debe ir aquí**:
  - Todo lo que un Composable necesita **leer y pintar en pantalla**.
  - Datos acumulados (ej. `puntos: Int`, `ronda: Int`, `pedidos: List<Pedido>`).
  - Lista de elementos erróneos introducidos (ej. `letrasFallidas: List<Char>`).
  - Indicadores de error que alteran la interfaz (ej. `isLetraIncorrecta: Boolean`).
  - Banderas de control de navegación o modales (ej. `isFinJuego: Boolean`).
- **Regla de oro**:
  - Debe ser siempre una `data class` donde todos los atributos sean `val`.
  - Solo se actualiza emitiendo una copia nueva mediante `.copy()`.

```kotlin
// ==========================================================
// CÓDIGO EXACTO DEL REPOSITORIO OFICIAL (Fpcarlosc/Ud4_Ejemplo3)
// ==========================================================

data class JuegoUIState(
    val palabraActual: String = "",
    val isLetraIncorrecta: Boolean = false,
    val puntos: Int = 0,
    val ronda: Int = 1,
    val isFinJuego: Boolean = false
)
```

---

### Nivel 2: En el `ViewModel` (Clase lógica)
**¿Qué es?** Es el encargado de tomar decisiones, procesar eventos y coordinar los datos.
- **Qué debe ir aquí**:
  - El flujo reactivo: `private val _uiState = MutableStateFlow(...)` y `val uiState = _uiState.asStateFlow()`.
  - Las variables de entrada de texto controladas con el **patrón híbrido del profesor**: `var letraUsuario by mutableStateOf("") private set`.
  - Los **métodos de negocio** que responden a los eventos de usuario (`fun comprobarLetraUsuario()`, `fun reiniciarJuego()`).
  - **Variables de control interno que la UI NO necesita ver ni pintar**:
    - Banco de palabras o repositorio local.
    - Conjunto de palabras ya usadas (`private var palabrasUsadas: MutableSet<String>`).
    - La palabra real en memoria (`private lateinit var palabraActual: String`).
    - La palabra enmascarada con guiones bajos (`private lateinit var palabraOculta: String`).
    - Temporizadores o jobs de corrutinas (`viewModelScope`).

```kotlin
// Código Exacto del Repositorio Oficial (Fpcarlosc/Ud4_Ejemplo3):
class JuegoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(JuegoUIState())
    val uiState: StateFlow<JuegoUIState> = _uiState.asStateFlow()

    // Variables internas de control algorítmico (NO van en el UIState):
    private lateinit var palabraActual: String
    private lateinit var palabraOculta: String
    private var palabrasUsadas: MutableSet<String> = mutableSetOf()

    // Entrada de texto con el patrón híbrido del profesor:
    var letraUsuario by mutableStateOf("")
        private set

    init {
        reiniciarJuego()
    }

    fun reiniciarJuego() {
        palabrasUsadas.clear()
        _uiState.value = JuegoUIState(
            palabraActual = elegirPalabraAleatoria(),
            isLetraIncorrecta = false,
            puntos = 0,
            ronda = 1,
            isFinJuego = false
        )
    }

    private fun elegirPalabraAleatoria(): String {
        palabraActual = listaPalabras.random()

        return if (palabrasUsadas.contains(palabraActual)) {
            elegirPalabraAleatoria()
        } else {
            palabrasUsadas.add(palabraActual)
            palabraOculta = "_".repeat(palabraActual.length)
            palabraOculta
        }
    }

    fun actualizarLetraUsuario(letraRespuesta: String) {
        // En el repositorio oficial se asigna directamente la cadena:
        letraUsuario = letraRespuesta
    }

    fun comprobarLetraUsuario() {
        if (letraUsuario.isEmpty()) return

        val charBuscado = letraUsuario[0]

        if (palabraActual.contains(charBuscado, ignoreCase = true)) {
            // Descubrimos los guiones que coinciden con la letra acertada:
            val guiones = palabraOculta.toCharArray()
            for (i in palabraActual.indices) {
                if (palabraActual[i].equals(charBuscado, ignoreCase = true)) {
                    guiones[i] = charBuscado
                }
            }
            palabraOculta = String(guiones)
            palabraActual = palabraActual.replace(charBuscado, '_', ignoreCase = true)

            _uiState.update { estadoActual ->
                estadoActual.copy(
                    palabraActual = palabraOculta,
                    isLetraIncorrecta = false
                )
            }

            if (!palabraOculta.contains('_')) {
                val puntuacionActual = _uiState.value.puntos.plus(PUNTOS_POR_ACIERTO)
                actualizarEstadoJuego(puntuacionActual)
            }
        } else {
            // En el repositorio oficial solo se activa la bandera de error:
            _uiState.update { estadoActual ->
                estadoActual.copy(isLetraIncorrecta = true)
            }
        }

        actualizarLetraUsuario("")
    }

    fun actualizarEstadoJuego(puntuacionActual: Int) {
        if (palabrasUsadas.size == RONDAS) {
            _uiState.update { estadoActual ->
                estadoActual.copy(
                    isLetraIncorrecta = false,
                    puntos = puntuacionActual,
                    isFinJuego = true
                )
            }
        } else {
            _uiState.update { estadoActual ->
                estadoActual.copy(
                    palabraActual = elegirPalabraAleatoria(),
                    isLetraIncorrecta = false,
                    puntos = puntuacionActual,
                    ronda = estadoActual.ronda + 1
                )
            }
        }
    }
}
```

#### Ampliación frecuente de examen (Contador de errores y Letras falladas)

En los exámenes prácticos de DAM es muy habitual solicitar que la app cuente los errores y muestre en pantalla qué letras ha fallado el usuario. Para realizar esta ampliación sin romper el UDF se aplican los siguientes 3 ajustes:

1. **Ampliación en `JuegoUIState`**:
```kotlin
data class JuegoUIState(
    val palabraActual: String = "",
    val isLetraIncorrecta: Boolean = false,
    val puntos: Int = 0,
    val ronda: Int = 1,
    val isFinJuego: Boolean = false,
    // --- Campos añadidos en la ampliación de examen ---
    val letrasFallidas: List<Char> = emptyList(), // Lista inmutable de letras erróneas sin duplicados
    val contadorErrores: Int = 0,                 // Errores en la ronda actual
    val totalErrores: Int = 0                     // Errores acumulados en toda la partida
)
```

2. **Ampliación en la rama `else` de `comprobarLetraUsuario()`**:
En lugar de únicamente activar `isLetraIncorrecta = true`, se comprueba si la letra ya estaba registrada para no duplicarla y se incrementan los errores:
```kotlin
        } else {
            // Ampliación: Control de errores y letras fallidas sin duplicados:
            _uiState.update { estadoActual ->
                val yaFallada = estadoActual.letrasFallidas.contains(charBuscado.uppercaseChar())
                val nuevasLetras = if (yaFallada) {
                    estadoActual.letrasFallidas
                } else {
                    estadoActual.letrasFallidas + charBuscado.uppercaseChar()
                }
                estadoActual.copy(
                    isLetraIncorrecta = true,
                    letrasFallidas = nuevasLetras,
                    contadorErrores = estadoActual.contadorErrores + 1,
                    totalErrores = estadoActual.totalErrores + 1
                )
            }
        }
```

3. **Filtrado defensivo en `actualizarLetraUsuario`**:
```kotlin
fun actualizarLetraUsuario(letraRespuesta: String) {
    // Solo acepta caracteres alfabéticos, máximo 1 letra y en mayúscula:
    letraUsuario = letraRespuesta.filter { it.isLetter() }.take(1).uppercase()
}
```

---

### Nivel 2.1: Configuración de Dependencias Gradle para ViewModel y Patrón Híbrido Oficial

#### A) Configuración oficial de Gradle (`Android_Ud4_Arquitectura.pdf`, Diapositiva 18):
En `gradle/libs.versions.toml`:
```toml
[versions]
viewmodel = "2.8.6"

[libraries]
androidx-viewmodel = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "viewmodel" }
```

En `app/build.gradle.kts`:
```kotlin
dependencies {
    implementation(libs.androidx.viewmodel)
}
```

#### B) El Patrón Híbrido del Profesor (`MutableStateFlow` + `mutableStateOf` con `private set`):
En los repositorios de examen (`Ud4_Ejemplo2`, `Ud4_Ejemplo3`, `Ud4_Ejercicio`), el profesor utiliza un patrón específico:
1. El estado general inmutable que lee la interfaz se expone como `StateFlow<UIState>`.
2. Las entradas de los campos de texto (`TextField`) se controlan dentro del propio ViewModel con `var ... by mutableStateOf("") private set`.
3. En la función de cálculo, los valores se parsean de forma segura con `.toIntOrNull() ?: 0`.
4. En `MainActivity.kt`, los `TextField` utilizan `Modifier.onFocusChanged { focus -> if (focus.isFocused) operacionViewModel.actualizarNumero1("") }` para limpiar el campo automáticamente al hacer clic en él.

```kotlin
// OperacionViewModel.kt (Ud4_Ejemplo2 del profesor):
class OperacionViewModel : ViewModel() {
    private val _operacionUItate = MutableStateFlow(OperacionUIState())
    val operacionUIState: StateFlow<OperacionUIState> = _operacionUItate.asStateFlow()

    var respuestaNumero1 by mutableStateOf("")
        private set
    var respuestaNumero2 by mutableStateOf("")
        private set

    fun actualizarNumero1(num: String) {
        respuestaNumero1 = num
        _operacionUItate.update { it.copy(numero1 = respuestaNumero1) }
        calcular()
    }

    fun actualizarNumero2(num: String) {
        respuestaNumero2 = num
        _operacionUItate.update { it.copy(numero2 = respuestaNumero2) }
        calcular()
    }

    private fun calcular() {
        _operacionUItate.update { estadoActual ->
            val num1 = estadoActual.numero1.toIntOrNull() ?: 0
            val num2 = estadoActual.numero2.toIntOrNull() ?: 0
            estadoActual.copy(resultado = (num1 + num2).toString())
        }
    }
}
```

---

### Nivel 3: En el Composable con `remember { mutableStateOf(...) }`
**¿Qué es?** Estado local efímero de la vista. Solo le importa al propio elemento visual y no tiene trascendencia para el negocio de la app.
- **Qué debe ir aquí**:
  - Si una tarjeta individual está abierta o cerrada (`var abierto by remember { mutableStateOf(false) }`).
  - El texto borrador que el usuario está tecleando en un `OutlinedTextField` antes de pulsar "Añadir" o "Aceptar".
  - La opción marcada temporalmente en un grupo de RadioButtons antes de formalizar la compra.
  - La animación o visibilidad temporal de un menú contextual desplegable (*DropdownMenu*).

---

## 4. Matriz de Casos Reales (Ejercicios de DAM)

| Variable o Caso | ¿Dónde va? | Justificación técnica |
|---|:---:|---|
| **Lista de pedidos realizados (`pedidos: List<Pedido>`)** | `UIState` | Es el dato central que la lista en pantalla dibuja; debe sobrevivir si el usuario rota la pantalla. |
| **Función `insertarProducto(pedido)`** | `ViewModel` | Es la acción que procesa y valida la lógica de negocio. |
| **Opción seleccionada temporalmente en los RadioButtons (Pollo / Ternera)** | Composable (`remember`) | Es el borrador del formulario. Solo se convierte en pedido definitivo cuando se pulsa "Añadir". |
| **Puntuación y Rondas en el Ahorcado** | `UIState` | Datos del juego que la pantalla muestra y que no deben reiniciarse a 0 al girar el dispositivo. |
| **Conjunto de palabras ya jugadas para no repetirlas** | `ViewModel` (privado) | Control interno del algoritmo. La UI no dibuja este set, solo necesita la palabra actual. |
| **Letras falladas por el usuario** | `UIState` (`letrasFallidas: List<Char>`) | La pantalla dibuja las insignias rojas de cada letra fallada. |
| **Estado de apertura de una tarjeta desplegable en LazyColumn** | Composable (`remember`) | Es un efecto puramente estético y local de esa tarjeta individual. |
| **Texto numérico en la Calculadora de IVA** | Composable (`remember`) o `UIState` | Si el cálculo es instantáneo mientras teclea, puede coordinarse con el ViewModel; si es un formulario con botón calcular, se mantiene local hasta pulsar. |

### 4.1 Caso Integral de Examen: Restaurante DAM (`Ud4_Ejercicio`)

Este ejercicio integra toda la arquitectura MVVM con Programación Orientada a Objetos, formularios interactivos y listas reactivas.

#### 1. Jerarquía de Modelos Polimórficos (`modelo/`)
```kotlin
// modelo/Producto.kt
open class Producto(
    @DrawableRes val imagen: Int = 0,
    val nombre: String = ""
)

// modelo/Hamburguesa.kt
data class Hamburguesa(
    val tipoCarne: String = "",
    val isQueso: Boolean = false
) : Producto(imagen = R.drawable.hamburguesa, nombre = "Hamburguesa")

// modelo/Pizza.kt
data class Pizza(
    val tipoPizza: String = "",
    val tamanyo: String = ""
) : Producto(imagen = R.drawable.pizza, nombre = "Pizza")

// modelo/Pedido.kt (Código exacto del repositorio oficial Fpcarlosc/Ud4_Ejercicio):
data class Pedido(
    val producto: Producto = Producto(),
    val cantidad: String = ""
)
```

#### 2. Estado Inmutable (`modelo/RestauranteUIState.kt`)
```kotlin
data class RestauranteUIState(
    val pedidos: List<Pedido> = listOf() // List de solo lectura para recomposición garantizada en Compose
)
```

#### 3. Lógica de Negocio en ViewModel (`ui/RestauranteViewModel.kt`)
```kotlin
class RestauranteViewModel : ViewModel() {
    private val _restauranteUIState = MutableStateFlow(RestauranteUIState())
    val restauranteUIState: StateFlow<RestauranteUIState> = _restauranteUIState.asStateFlow()

    fun insertarProducto(pedido: Pedido) {
        _restauranteUIState.update { estadoActual ->
            estadoActual.copy(
                pedidos = estadoActual.pedidos + pedido // El operador '+' genera una NUEVA instancia inmutable de List
            )
        }
    }
}
```

#### 4. Justificación de Diseño: ¿Qué va en el Composable vs ViewModel/UIState?
- **En el Composable Local (`remember { mutableStateOf(...) }`)**:
  - `var tipoComida by remember { mutableStateOf("Hamburguesa") }`: Selección del producto principal mediante **`RadioButton`**.
  - Si es **Hamburguesa**: el tipo de carne (`"Pollo"` o `"Ternera"`) se selecciona con **`RadioButton`**, y el queso extra (`isQueso`) con **`Checkbox`**.
  - Si es **Pizza**: tanto el tipo de pizza (`"Margarita"`, `"Barbacoa"`, `"Cuatro Quesos"`) como el tamaño (`tamanyo`: `"Individual"`, `"Mediana"`, `"Familiar"`) se seleccionan mediante grupos de **`RadioButton`**.
  - `var cantidadText by remember { mutableStateOf("1") }`: Entrada numérica temporal mediante **`OutlinedTextField`**.
  - *Justificación técnica*: Son controles temporales de formulario. Mientras el usuario cambia de opción en un radio button o activa un checkbox, el pedido **aún no se ha realizado**. Guardar estos valores en el `ViewModel` provocaría recomposiciones globales innecesarias y mezclaría el borrador efímero de la pantalla con los datos reales del negocio.
- **En el `RestauranteUIState`**:
  - `val pedidos: List<Pedido>`: Es el dato final consolidado que el `LazyColumn` inferior dibuja en pantalla y que debe sobrevivir a cambios de configuración como la rotación de pantalla.
- **En el `RestauranteViewModel`**:
  - `fun insertarProducto(pedido: Pedido)`: Recibe el objeto `Pedido` ya instanciado desde la UI (tras validar la cantidad introducida) y actualiza la lista inmutable en el StateFlow mediante `pedidos = estadoActual.pedidos + pedido`.

---

## 5. Los 4 Errores y Anti-Patrones más frecuentes en Exámenes

### ❌ Anti-Patrón 1: Poner lógica de negocio en el Composable

```kotlin
// INCORRECTO ❌: El Composable realiza cálculos y lógica de negocio
@Composable
fun CalculadoraMal() {
    var total by remember { mutableStateOf(0.0) }
    Button(onClick = {
        // ERROR: Lógica compleja, validación y porcentajes metidos en la interfaz
        val iva = 0.21
        total = 100.0 * (1 + iva)
    }) { Text("Calcular") }
}

// CORRECTO ✅: UDF estricto: El Composable solo notifica el evento al ViewModel
@Composable
fun CalculadoraBien(viewModel: IvaViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    
    Button(onClick = { viewModel.calcularIva(precioBase = 100.0, tipoIva = 0.21) }) {
        Text("Calcular")
    }
}
```

---

### ❌ Anti-Patrón 2: Guardar referencias a `Context` o Vistas en el `ViewModel`

```kotlin
// INCORRECTO ❌: Genera fugas graves de memoria (Memory Leaks)
class MiViewModelMal(private val context: Context) : ViewModel() {
    fun obtenerTexto(): String {
        return context.getString(R.string.bienvenida) // ERROR: Retiene la Activity destruida
    }
}

// CORRECTO ✅: El ViewModel solo expone identificadores de recursos (@StringRes Int)
data class MiUIState(
    @StringRes val mensajeResId: Int = R.string.bienvenida
)

// Y el Composable resuelve el recurso usando su propio contexto del árbol:
@Composable
fun MiPantallaBien(viewModel: MiViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Text(text = stringResource(id = uiState.mensajeResId))
}
```

---

### ❌ Anti-Patrón 3: Mutación directa del `UIState` sin usar `copy()`

```kotlin
// INCORRECTO ❌: Mutación directa
fun sumarPuntoMal() {
    // Si UIState tuviera variables var:
    // _uiState.value.puntos++ // ERROR: La referencia es idéntica; Compose NO se recompone
}

// CORRECTO ✅: Generación de una nueva instancia inmutable con .copy()
fun sumarPuntoBien() {
    _uiState.update { estadoActual ->
        estadoActual.copy(puntos = estadoActual.puntos + 1)
    }
}
```

---

### ❌ Anti-Patrón 4: Usar `MutableList` dentro del `UIState`

```kotlin
// INCORRECTO ❌: MutableList dentro del estado
data class PedidoUIStateMal(
    val pedidos: MutableList<Pedido> = mutableListOf()
)

fun agregarPedidoMal(pedido: Pedido) {
    _uiState.value.pedidos.add(pedido) // ERROR: Compose no detecta adiciones internas a MutableList
}

// CORRECTO ✅: List inmutable y adición con operador '+'
data class PedidoUIStateBien(
    val pedidos: List<Pedido> = emptyList() // List de solo lectura
)

fun agregarPedidoBien(pedido: Pedido) {
    _uiState.update { estadoActual ->
        estadoActual.copy(
            pedidos = estadoActual.pedidos + pedido // Crea una NUEVA referencia List
        )
    }
}
```
