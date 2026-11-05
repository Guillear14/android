# Parte 4: Arquitectura, Ciclo de Vida y Gestión de Estado
### 2º DAM — Programación Multimedia y Dispositivos Móviles (PMDM)

Este documento detalla el ciclo de vida de las actividades de Android, las transiciones de estado del sistema, los principios de arquitectura recomendados por Google y el patrón de diseño reactivo MVVM implementado con `ViewModel` y `StateFlow`.

---

## 1. El Ciclo de Vida de una Actividad (Activity Lifecycle)

El sistema Android gestiona la memoria de manera dinámica. Cuando los recursos del teléfono escasean o el usuario atiende otra tarea prioritaria (como recibir una llamada telefónica o cambiar de aplicación), Android puede suspender o destruir actividades en segundo plano.

### Diagrama de Estados y Callbacks:

```
                  ┌──────────────┐
                  │   Lanzamiento│
                  └──────┬───────┘
                         ▼
                   [ onCreate() ]  ──> Estado Creado (Created)
                         │
                         ▼
                   [ onStart() ]   ──> Estado Iniciado (Started)
                         │
                         ▼
                   [ onResume() ]  ──> Estado Reanudado (Resumed)
                         │
         ┌───────────────┴───────────────┐
         │                               │
         │  La Actividad está en primer  │
         │  plano e interactúa con el    │
         │  usuario.                     │
         │                               │
         └───────────────┬───────────────┘
                         │
                 Usuario sale o pierde foco
                         ▼
                   [ onPause() ]   ──> Estado Pausado (Paused)
                         │
                 Ya no es visible en pantalla
                         ▼
                   [ onStop() ]    ──> Estado Detenido (Stopped)
                         │
           ┌─────────────┴─────────────┐
           │                           │
  Usuario regresa             El sistema destruye
  a la actividad              la actividad por memoria
           │                           │
           ▼                           ▼
     [ onRestart() ]             [ onDestroy() ] ──> Destruida (Destroyed)
           │                           │
           ▼                           ▼
     [ onStart() ]                  [ Fin ]
```

### Descripción de los Métodos Callback:
- **`onCreate()`**: Se ejecuta al instanciar la actividad. Se preparan los recursos estáticos y se invoca `setContent { ... }` para cargar la interfaz de Compose.
- **`onStart()`**: La actividad se hace visible en pantalla, pero aún no tiene el foco para recibir pulsaciones del usuario.
- **`onResume()`**: La actividad se encuentra en primer plano y el usuario puede interactuar con ella. Aquí se inician animaciones o accesos a sensores.
- **`onPause()`**: La actividad pierde el foco parcialmente (ej. se abre un diálogo modal o se entra en modo multiventana). Se deben pausar operaciones costosas.
- **`onStop()`**: La actividad deja de ser visible por completo (se pulsó el botón Home o se abrió otra app).
- **`onRestart()`**: Se invoca cuando la actividad estaba detenida y el usuario vuelve a ella antes de que sea destruida.
- **`onDestroy()`**: Último callback antes de que la actividad desaparezca de la memoria (por cierre definitivo o destrucción del sistema).

> [!NOTE]
> **Detalle de examen sobre `Ud4_Ejemplo1` y Logcat:**
> En el código fuente oficial del profesor (`Ud4_Ejemplo1`), se define la constante privada:
> ```kotlin
> private const val TAG = "Ud4_Ejemplo1"
> ```
> El profesor colocó llamadas `Log.d(TAG, "...")` únicamente dentro de los 6 métodos de ciclo de vida posteriores (`onStart()`, `onResume()`, `onRestart()`, `onPause()`, `onStop()`, `onDestroy()`), omitiendo intencionadamente el `Log.d` dentro de `onCreate()`. Esta es la razón por la que en las capturas de pantalla de Logcat del PDF oficial (`Android_Ud4_Arquitectura.pdf`, Diapositiva 6) la traza de eventos de la consola comienza directamente en `onStart` en lugar de `onCreate`.

---

## 2. El Problema del Cambio de Configuración (Configuration Change)

Cuando el usuario **gira la pantalla del teléfono** (de vertical a horizontal o viceversa), el sistema Android:
1. **Destruye por completo la instancia actual de la `Activity`** ejecutando la secuencia: `onPause() -> onStop() -> onDestroy()`.
2. **Crea una nueva instancia de la `Activity` desde cero** ejecutando: `onCreate() -> onStart() -> onResume()`.

### Tabla Comparativa: `remember` vs `rememberSaveable` vs `ViewModel`

| Mecanismo | ¿Sobrevive a Recomposición normal? | ¿Sobrevive a Rotación de pantalla? | ¿Sobrevive al cierre de proceso por baja RAM? | Tipo de datos permitidos |
| :--- | :---: | :---: | :---: | :--- |
| **`remember { }`** | ✅ SÍ | ❌ **NO (se reinicia a 0 o vacío)** | ❌ NO | Cualquier objeto Kotlin en memoria. |
| **`rememberSaveable { }`** | ✅ SÍ | ✅ **SÍ (lo guarda en un Bundle)** | ✅ SÍ | Solo tipos primitivos o parcelables/serializables. |
| **`ViewModel`** | ✅ SÍ | ✅ **SÍ (el objeto persiste intacto)** | ❌ NO (salvo con `SavedStateHandle`) | Toda la lógica, flujos de datos, repositorios y clases complejas. |

#### Comparativa de Código: Qué ocurre al rotar el teléfono

```kotlin
@Composable
fun DemoRotacion(miViewModel: ContadorViewModel = viewModel()) {
    // CASO 1: remember simple -> ¡SE PIERDE AL GIRAR! (Vuelve a 0)
    var contadorVolatil by remember { mutableStateOf(0) }

    // CASO 2: rememberSaveable -> Sobrevive a la rotación pero está acoplado a la vista
    var contadorSaveable by rememberSaveable { mutableStateOf(0) }

    // CASO 3: ViewModel -> Arquitectura profesional recomendada por Google
    val uiState by miViewModel.uiState.collectAsState()

    Column {
        Button(onClick = { contadorVolatil++ }) { 
            Text("remember simple: $contadorVolatil (se resetea al girar)") 
        }
        Button(onClick = { contadorSaveable++ }) { 
            Text("rememberSaveable: $contadorSaveable (persiste)") 
        }
        Button(onClick = { miViewModel.sumar() }) { 
            Text("ViewModel: ${uiState.cuenta} (persiste + UDF)") 
        }
    }
}
```

---

## 3. Principios de Arquitectura recomendados por Google

La guía oficial de arquitectura de Android establece dos pilares fundamentales:

1. **Separación de problemas (*Separation of Concerns*)**:
   - Cada clase debe tener una única responsabilidad bien definida.
   - La capa de interfaz (Composables) solo debe encargarse de renderizar píxeles y capturar eventos del usuario; **nunca debe contener lógica de negocio, cálculos complejos ni acceso a bases de datos**.
2. **Controlar la UI a partir de Modelos de Datos (*UI Driven by Data Models*)**:
   - La interfaz de usuario debe reflejar fielmente un modelo de datos persistente e independiente del ciclo de vida.

### Las 3 Capas de la Arquitectura Android:
- **Capa de UI (`UI Layer`)**:
  - Elementos de UI (Composables de Jetpack Compose).
  - Contenedores de estado (*State Holders*): **`ViewModel`**.
- **Capa de Dominio (`Domain Layer`, opcional)**: Casos de uso reutilizables para simplificar lógica compleja entre UI y Datos.
- **Capa de Datos (`Data Layer`)**:
  - Repositorios que centralizan el acceso a fuentes de datos locales (Room, SQLite, DataStore) o remotas (Retrofit, APIs REST).

---

## 4. El Patrón UDF (Unidirectional Data Flow)

En Compose, el flujo de información es siempre **unidireccional y circular**:

```
           ┌──────────────────────────────────────────┐
           │                VIEWMODEL                 │
           │  (Mantiene el estado y ejecuta lógica)   │
           └──────┬────────────────────────────▲──────┘
                  │                            │
             Estado (UIState)            Eventos (Clics,
             fluye hacia abajo           texto) fluyen
                  │                      hacia arriba
                  ▼                            │
           ┌───────────────────────────────────┴──────┐
           │              ELEMENTOS DE UI             │
           │           (Funciones Composable)         │
           └──────────────────────────────────────────┘
```

1. **El Estado baja**: El `ViewModel` expone un flujo de estado inmutable (`StateFlow<UIState>`). Los composables se suscriben a él y se recomponen cada vez que emite un nuevo valor.
2. **Los Eventos suben**: Cuando el usuario interactúa (pulsa un botón o escribe), la UI no manipula los datos directamente; emite un evento llamando a una función del `ViewModel` (ej. `viewModel.insertarProducto(pedido)`).

---

## 5. El `ViewModel` y su Persistencia

El `ViewModel` de Android Jetpack es un componente con un ciclo de vida especial:
- **Sobrevive a las destrucciones por rotación de pantalla**: Cuando la Activity se destruye por un cambio de configuración, el framework retiene la instancia del `ViewModel` en memoria y la reengancha a la nueva Activity.
- Solo se destruye definitivamente cuando la actividad termina deliberadamente (por pulsar el botón Atrás o llamar a `finish()`).

> [!CAUTION]
> **Prohibido referenciar Vistas o Contextos en el ViewModel**:
> Un `ViewModel` **NUNCA debe almacenar referencias a un `Context`, `Activity`, `View` ni elemento Composable**. Guardar una referencia a una Activity impedirá que el recolector de basura la libere cuando sea destruida, provocando graves **fugas de memoria (*Memory Leaks*)**.

---

### 5.1 Configuración de Dependencias Gradle para ViewModel (`Android_Ud4_Arquitectura.pdf`)

Para poder utilizar la clase `ViewModel` e integrarla con Jetpack Compose mediante la función `viewModel()`, se debe añadir la biblioteca oficial de Android Jetpack Lifecycle:

#### 1. En `gradle/libs.versions.toml`:
```toml
[versions]
viewmodel = "2.8.6"

[libraries]
androidx-viewmodel = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "viewmodel" }
```

#### 2. En `app/build.gradle.kts` (Módulo :app):
```kotlin
dependencies {
    implementation(libs.androidx.viewmodel)
}
```

---

## 6. Implementación Reactiva con `StateFlow` y el Patrón Híbrido del Profesor

`StateFlow` es una estructura de corrutinas de Kotlin que retiene y emite estados de forma observable.

#### A) Comparativa: `LiveData` vs `StateFlow` vs `SharedFlow`

| Característica | `LiveData` (Antiguo) | `StateFlow` (Estándar Compose) | `SharedFlow` (Eventos) |
| :--- | :--- | :--- | :--- |
| **Plataforma** | Ligado al SDK de Android (`LifecycleOwner`). | **Kotlin puro (Corrutinas)**; multi-plataforma. | Kotlin puro (Corrutinas). |
| **Valor Inicial** | Opcional (puede ser `null`). | **Obligatorio** en el constructor. | Opcional / Configurable mediante buffer. |
| **Retención de Estado** | Retiene el último valor emitido. | **Siempre retiene el último estado emitido** (`.value`). | No retiene estado por defecto (flujo de eventos efímeros). |
| **Caso de Uso en DAM** | Proyectos legados con XML. | **`UIState` en MVVM con Jetpack Compose**. | Notificaciones de un solo uso (Snackbars, navegación). |

#### B) Comparativa de Modificación de Estado: Mutación Directa vs Actualización Atómica

```kotlin
// ==========================================================
// CASO 1: INCORRECTO ❌ (Riesgo de condición de carrera)
// ==========================================================
// Si dos corrutinas o eventos del usuario se ejecutan simultáneamente,
// una sobreescribirá los cambios de la otra porque leen el mismo .value desactualizado:
_uiState.value = _uiState.value.copy(numero1 = "25")


// ==========================================================
// CASO 2: CORRECTO ✅ (Actualización atómica con .update)
// ==========================================================
// El bloque .update es una operación atómica (Thread-Safe). Garantiza que 'estadoActual'
// sea siempre el más reciente en memoria sin pérdida de datos concurrentes:
_uiState.update { estadoActual ->
    estadoActual.copy(numero1 = "25")
}
```

---

#### C) El Patrón Híbrido del Profesor (`StateFlow` + `mutableStateOf` con `private set`)

> [!IMPORTANT]
> **Arquitectura oficial en los repositorios de examen (`Ud4_Ejemplo2`, `Ud4_Ejemplo3`, `Ud4_Ejercicio`):**
> En el temario y repositorios del profesor, el `ViewModel` implementa un **patrón híbrido muy característico**:
> 1. Expone un `StateFlow<UIState>` inmutable para que la UI observe el estado global consolidado.
> 2. Controla internamente las variables de entrada de texto mediante **`var ... by mutableStateOf("") private set`**. Con `private set`, la vista puede leer la variable si fuera necesario pero solo el ViewModel puede modificarla.
> 3. En la función de cálculo, los valores se convierten a número usando de forma segura **`.toIntOrNull() ?: 0`** para evitar excepciones si el usuario borra el campo o introduce caracteres no válidos.

#### Código Exacto Oficial de `OperacionViewModel.kt` (`Ud4_Ejemplo2`):

```kotlin
package com.example.ud4_ejemplo2.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ud4_ejemplo2.modelo.OperacionUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class OperacionViewModel : ViewModel() {
    // Flujo mutable privado interno y StateFlow público inmutable para la UI:
    private val _operacionUItate = MutableStateFlow(OperacionUIState())
    val operacionUIState: StateFlow<OperacionUIState> = _operacionUItate.asStateFlow()

    // Propiedades de estado local en el ViewModel con private set (patrón del profesor):
    var respuestaNumero1 by mutableStateOf("")
        private set

    var respuestaNumero2 by mutableStateOf("")
        private set

    fun actualizarNumero1(num: String) {
        respuestaNumero1 = num

        _operacionUItate.update { estadoActual ->
            estadoActual.copy(
                numero1 = respuestaNumero1
            )
        }

        calcular()
    }

    fun actualizarNumero2(num: String) {
        respuestaNumero2 = num

        _operacionUItate.update { estadoActual ->
            estadoActual.copy(
                numero2 = respuestaNumero2
            )
        }

        calcular()
    }

    private fun calcular() {
        _operacionUItate.update { estadoActual ->
            // Conversión segura de cadenas a enteros con toIntOrNull():
            val num1 = estadoActual.numero1.toIntOrNull() ?: 0
            val num2 = estadoActual.numero2.toIntOrNull() ?: 0

            estadoActual.copy(
                resultado = (num1 + num2).toString()
            )
        }
    }
}
```

#### Consumo en Compose con Limpieza Automática de Foco (`onFocusChanged`):

En `MainActivity.kt` (`Ud4_Ejemplo2`), el profesor utiliza el modificador **`.onFocusChanged`** en cada `TextField`. Cuando el usuario pulsa sobre el campo de texto y gana el foco (`focus.isFocused == true`), el campo se vacía automáticamente llamando a `actualizarNumero1("")`:

```kotlin
@Composable
fun Calculadora(
    modifier: Modifier = Modifier,
    operacionViewModel: OperacionViewModel = viewModel()
) {
    // Observa el StateFlow del ViewModel y recompone la pantalla ante cada emisión:
    val uIState by operacionViewModel.operacionUIState.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.calculadora),
            style = MaterialTheme.typography.displayMedium
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Campo Número 1:
            TextField(
                value = uIState.numero1,
                label = { Text(text = stringResource(R.string.numero_1)) },
                onValueChange = { operacionViewModel.actualizarNumero1(it) },
                modifier = Modifier
                    .weight(1F)
                    .onFocusChanged { focus ->
                        // Al hacer clic sobre el TextField (gana el foco), se vacía el campo:
                        if (focus.isFocused) {
                            operacionViewModel.actualizarNumero1("")
                        }
                    }
            )

            Text(
                text = "+",
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(0.5F)
            )

            // Campo Número 2:
            TextField(
                value = uIState.numero2,
                label = { Text(text = stringResource(R.string.numero_2)) },
                onValueChange = { operacionViewModel.actualizarNumero2(it) },
                modifier = Modifier
                    .weight(1F)
                    .onFocusChanged { focus ->
                        if (focus.isFocused) {
                            operacionViewModel.actualizarNumero2("")
                        }
                    }
            )

            // Resultado dentro del propio Row según el diseño oficial del PDF (Diapositivas 15 y 22):
            Text(
                text = "= ${uIState.resultado}",
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1F)
            )
        }
    }
}
```

> [!NOTE]
> **Detalle de sintaxis en Diapositiva 22 (`Android_Ud4_Arquitectura.pdf`):**
> En la diapositiva 22 del PDF oficial, el profesor escribió `text = stringResource("Calculadora")`. En Kotlin real (y en el código fuente del repositorio `Ud4_Ejemplo2`), `stringResource()` exige obligatoriamente un identificador entero de recurso (`@StringRes Int`) generado en la clase `R` (ej. `stringResource(R.string.calculadora)`), ya que pasar una cadena literal de texto `String` directamente a `stringResource(...)` provoca un error de compilación.
