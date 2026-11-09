# 📱 CHEATSHEET TÉCNICA DE ANDROID (KOTLIN & JETPACK COMPOSE)
> **Chuleta rápida de consulta para exámenes.**  
> Formato indexado para búsqueda ultrarrápida mediante `Ctrl + F`.  
> *Palabras clave indexadas*: `Composable`, `Modifier`, `State`, `remember`, `mutableStateOf`, `LazyColumn`, `LazyVerticalGrid`, `ViewModel`, `StateFlow`, `collectAsState`, `Scaffold`, `Card`, `AlertDialog`, `RadioButton`, `Checkbox`, `Switch`, `OutlinedTextField`, `TextField`, `KeyboardOptions`, `KeyboardActions`, `selectableGroup`, `selectable`, `NumberFormat`, `update`, `copy`.

---

## 📑 ÍNDICE DE EJERCICIOS Y MÓDULOS

1. [Módulo 1: Ud2_Ejercicio1 — Calculadora de IVA Dinámica](#módulo-1-ud2_ejercicio1--calculadora-de-iva-dinámica)
   - *Keywords*: `remember`, `mutableStateOf`, `OutlinedTextField`, `TextField`, `RadioButton`, `selectableGroup`, `selectable`, `NumberFormat.getCurrencyInstance`, `KeyboardOptions`.
2. [Módulo 2: Ud3_Ejemplo1 — Lista Básica con LazyColumn](#módulo-2-ud3_ejemplo1--lista-básica-con-lazycolumn)
   - *Keywords*: `LazyColumn`, `items`, `Card`, `@StringRes`, `@DrawableRes`, `Bandera`, `Datos`, `Image`, `painterResource`, `stringResource`.
3. [Módulo 3: Ud3_Ejemplo2 — Lista con Detección de Clics y Callbacks](#módulo-3-ud3_ejemplo2--lista-con-detección-de-clics-y-callbacks)
   - *Keywords*: `LazyColumn`, `Modifier.clickable`, `onItemClick`, `Log.v`, `Card`, `ItemBandera`.
4. [Módulo 4: Ud3_Ejemplo3 — Tarjetas Expandibles y Visibilidad Condicional](#módulo-4-ud3_ejemplo3--tarjetas-expandibles-y-visibilidad-condicional)
   - *Keywords*: `Card`, `IconButton`, `Icon`, `remember`, `mutableStateOf`, `abierto`, `Modifier.weight`, `Spacer`, `expand_circle_down`, `expand_circle_up`.
5. [Módulo 5: Ud4_Ejemplo1 — Monitor del Ciclo de Vida de una Actividad](#módulo-5-ud4_ejemplo1--monitor-del-ciclo-de-vida-de-una-actividad)
   - *Keywords*: `ComponentActivity`, `Lifecycle`, `onCreate`, `onStart`, `onResume`, `onPause`, `onStop`, `onRestart`, `onDestroy`, `Log.d`, `TAG`.
6. [Módulo 6: Ud4_Ejemplo2 — Calculadora Reactiva MVVM (StateFlow)](#módulo-6-ud4_ejemplo2--calculadora-reactiva-mvvm-stateflow)
   - *Keywords*: `ViewModel`, `StateFlow`, `MutableStateFlow`, `asStateFlow`, `update`, `copy`, `collectAsState`, `onFocusChanged`, `toIntOrNull`.
7. [Módulo 7: Ud4_Ejemplo3 — Juego del Ahorcado MVVM con Diálogo Modal](#módulo-7-ud4_ejemplo3--juego-del-ahorcado-mvvm-con-diálogo-modal)
   - *Keywords*: `ViewModel`, `StateFlow`, `JuegoUIState`, `AlertDialog`, `ImeAction.Done`, `KeyboardActions`, `KeyboardOptions`, `clip`, `background`, `repeat`.
8. [Módulo 8: Ud4_Ejercicio4 — Restaurante DAM (POO, Herencia y StateFlow)](#módulo-8-ud4_ejercicio4--restaurante-dam-poo-herencia-y-stateflow)
   - *Keywords*: `open class Producto`, `Hamburguesa`, `Pizza`, `Pedido`, `RestauranteUIState`, `RestauranteViewModel`, `RadioButton`, `Checkbox`, `Switch`, `LocalDate`.
9. [Módulo 9: Ejercicio 2 — Gestor de Módulos DAM / Tareas Urgentes](#módulo-9-ejercicio-2--gestor-de-módulos-dam--tareas-urgentes)
   - *Keywords*: `modulo`, `ModuloUIState`, `ModuloViewModel`, `LazyColumn`, `Card`, `RadioButton`, `Checkbox`, `OutlinedTextField`, `Modifier.weight`, `HorizontalDivider`.
10. [Módulo 10: Referencia Rápida de Snippets y Patrones de Examen](#módulo-10-referencia-rápida-de-snippets-y-patrones-de-examen)

---

## Módulo 1: Ud2_Ejercicio1 — Calculadora de IVA Dinámica
*Keywords: Composable, Modifier, State, remember, mutableStateOf, TextField, RadioButton, selectableGroup, selectable, NumberFormat, KeyboardOptions, KeyboardType*

### Bloque 1.1: Actividad y Punto de Entrada Compose
- **Ejercicio**: Ud2_Ejercicio1 (Calculadora de IVA)
- **Archivo**: `Ud2_Ejercicio1-master/app/src/main/java/com/example/ud2_ejercicio1/MainActivity.kt`
- **Inicio**: Línea 38 (`class MainActivity : ComponentActivity()`)
- **Fin**: Línea 52 (`}`)
- **Código**:
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ud2_Ejercicio1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Ud2Ejercicio2App(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
```

### Bloque 1.2: Pantalla Principal con Estado Local y RadioButtons
- **Ejercicio**: Ud2_Ejercicio1 (Calculadora de IVA)
- **Archivo**: `Ud2_Ejercicio1-master/app/src/main/java/com/example/ud2_ejercicio1/MainActivity.kt`
- **Inicio**: Línea 54 (`fun Ud2Ejercicio2App(modifier: Modifier = Modifier)`)
- **Fin**: Línea 120 (`}`)
- **Código**:
```kotlin
@Composable
fun Ud2Ejercicio2App(modifier: Modifier = Modifier) {
    // Guardamos el estado del precio escrito por el usuario en el TextField.
    var precioInput by remember { mutableStateOf("") }

    val listaIVA = listOf(21, 10, 3)

    var (eleccion, onOpcionElegida) = remember { mutableStateOf(listaIVA[0])}

    var ivaRadio by remember { mutableStateOf(eleccion.toDouble()) }

    // Si el valor de precioInput es null se asignará el valor 0.0
    val precio = precioInput.toDoubleOrNull() ?: 0.0

    val iva = calcularIva(precio, ivaRadio)

    Column(
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.calcular_precio_con_iva) ,
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )
        CampoEdicionPrecio(
            value = precioInput,
            onValueChange = {precioInput = it},
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )

        Column (Modifier.selectableGroup()) {
            listaIVA.forEach { opcion ->
                Row(
                    modifier = Modifier.selectable(
                        selected = (opcion == eleccion),
                        onClick = {
                            eleccion = opcion
                            onOpcionElegida(opcion)
                            ivaRadio = eleccion.toDouble()}
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    RadioButton(selected = (opcion == eleccion),
                        onClick = {
                            eleccion = opcion
                            onOpcionElegida(opcion)
                            ivaRadio = eleccion.toDouble()})
                    Text(text = "${opcion}%")
                }
            }
        }

        Text(
            text = stringResource(R.string.precio, iva),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}
```

### Bloque 1.3: Campo de Entrada Reutilizable para Números
- **Ejercicio**: Ud2_Ejercicio1 (Calculadora de IVA)
- **Archivo**: `Ud2_Ejercicio1-master/app/src/main/java/com/example/ud2_ejercicio1/MainActivity.kt`
- **Inicio**: Línea 122 (`fun CampoEdicionPrecio(`)
- **Fin**: Línea 136 (`}`)
- **Código**:
```kotlin
@Composable
fun CampoEdicionPrecio(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        label = { Text(text = stringResource(R.string.precio_del_producto))},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
    )
}
```

### Bloque 1.4: Lógica de Negocio y Formateo Monetario
- **Ejercicio**: Ud2_Ejercicio1 (Calculadora de IVA)
- **Archivo**: `Ud2_Ejercicio1-master/app/src/main/java/com/example/ud2_ejercicio1/MainActivity.kt`
- **Inicio**: Línea 138 (`private fun calcularIva(precio: Double, ivaRadio: Double): String`)
- **Fin**: Línea 143 (`}`)
- **Código**:
```kotlin
private fun calcularIva(precio: Double, ivaRadio: Double): String {
    val iva = precio + (ivaRadio / 100 * precio)

    // Devolvemos el precio final en la moneda del país en el que está configurado el dispositivo.
    return NumberFormat.getCurrencyInstance().format(iva)
}
```

---

## Módulo 2: Ud3_Ejemplo1 — Lista Básica con LazyColumn
*Keywords: LazyColumn, items, Card, Image, painterResource, stringResource, @StringRes, @DrawableRes, Data Class, List*

### Bloque 2.1: Modelo de Datos con Anotaciones de Recursos
- **Ejercicio**: Ud3_Ejemplo1 (Lista Básica)
- **Archivo**: `Ud3_Ejemplo1-master/app/src/main/java/com/example/ud3_ejemplo1/modelo/Bandera.kt`
- **Inicio**: Línea 6 (`data class Bandera(`)
- **Fin**: Línea 10 (`)`)
- **Código**:
```kotlin
data class Bandera(
    // Necesitamos dos ids, uno para el texto de la bandera y otro para la imagen
    @StringRes val stringResourceId: Int,
    @DrawableRes val imagenResourceId: Int
)
```

### Bloque 2.2: Repositorio de Carga de Datos
- **Ejercicio**: Ud3_Ejemplo1 (Lista Básica)
- **Archivo**: `Ud3_Ejemplo1-master/app/src/main/java/com/example/ud3_ejemplo1/datos/Datos.kt`
- **Inicio**: Línea 6 (`class Datos {`)
- **Fin**: Línea 14 (`}`)
- **Código**:
```kotlin
class Datos {
    fun cargarBanderas(): List<Bandera> = listOf(
        Bandera(R.string.bandera1, R.drawable.bandera1),
        Bandera(R.string.bandera2, R.drawable.bandera2),
        Bandera(R.string.bandera3, R.drawable.bandera3),
        Bandera(R.string.bandera4, R.drawable.bandera4),
        Bandera(R.string.bandera5, R.drawable.bandera5)
    )
}
```

### Bloque 2.3: Implementación de LazyColumn
- **Ejercicio**: Ud3_Ejemplo1 (Lista Básica)
- **Archivo**: `Ud3_Ejemplo1-master/app/src/main/java/com/example/ud3_ejemplo1/MainActivity.kt`
- **Inicio**: Línea 44 (`fun Ud3Ejemplo1App(modifier: Modifier = Modifier)`)
- **Fin**: Línea 62 (`}`)
- **Código**:
```kotlin
@Composable
fun Ud3Ejemplo1App(modifier: Modifier = Modifier) {
    ListaBanderas(
        lista = Datos().cargarBanderas(),
        modifier = modifier
    )
}

@Composable
fun ListaBanderas(lista: List<Bandera>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(lista) { bandera ->
            TarjetaBandera(
                bandera = bandera,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
```

### Bloque 2.4: Diseño del Ítem con Card, Row e Image
- **Ejercicio**: Ud3_Ejemplo1 (Lista Básica)
- **Archivo**: `Ud3_Ejemplo1-master/app/src/main/java/com/example/ud3_ejemplo1/MainActivity.kt`
- **Inicio**: Línea 64 (`fun TarjetaBandera(bandera: Bandera, modifier: Modifier = Modifier)`)
- **Fin**: Línea 82 (`}`)
- **Código**:
```kotlin
@Composable
fun TarjetaBandera(bandera: Bandera, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(bandera.imagenResourceId),
                contentDescription = stringResource(bandera.stringResourceId)
            )
            Text(
                text = stringResource(bandera.stringResourceId),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}
```

---

## Módulo 3: Ud3_Ejemplo2 — Lista con Detección de Clics y Callbacks
*Keywords: LazyColumn, Modifier.clickable, onItemClick, Log.v, Callbacks, Card, Row*

### Bloque 3.1: LazyColumn con Evento de Selección y Callback Lambda
- **Ejercicio**: Ud3_Ejemplo2 (Lista con Clics)
- **Archivo**: `Ud3_Ejemplo2-master/app/src/main/java/com/example/ud3_ejemplo2/MainActivity.kt`
- **Inicio**: Línea 47 (`fun Ud3Ejemplo2App(modifier: Modifier = Modifier)`)
- **Fin**: Línea 69 (`}`)
- **Código**:
```kotlin
@Composable
fun Ud3Ejemplo2App(modifier: Modifier = Modifier) {
    ListaBanderas(
        lista = Datos().cargarBanderas(),
        modifier = modifier
    )
}

// Función que crea la lista desplegable y llama a ItemBandera
@Composable
fun ListaBanderas(lista: List<Bandera>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(lista) { bandera ->
            ItemBandera(
                bandera = bandera,
                onItemClick = { banderaElegida ->
                    // Acción al pulsar esa bandera
                    Log.v("Bandera pulsada: ", banderaElegida.stringResourceId.toString())
                }
            )
        }
    }
}
```

### Bloque 3.2: Envoltura Clicable para el Ítem de la Lista
- **Ejercicio**: Ud3_Ejemplo2 (Lista con Clics)
- **Archivo**: `Ud3_Ejemplo2-master/app/src/main/java/com/example/ud3_ejemplo2/MainActivity.kt`
- **Inicio**: Línea 71 (`fun ItemBandera(bandera: Bandera, onItemClick: (Bandera) -> Unit)`)
- **Fin**: Línea 80 (`}`)
- **Código**:
```kotlin
@Composable
fun ItemBandera(bandera: Bandera, onItemClick: (Bandera) -> Unit) {
    TarjetaBandera(
        bandera = bandera,
        modifier = Modifier
            .clickable { onItemClick(bandera) }
            .padding(8.dp)
    )
}
```

### Bloque 3.3: Tarjeta Visual del Ítem
- **Ejercicio**: Ud3_Ejemplo2 (Lista con Clics)
- **Archivo**: `Ud3_Ejemplo2-master/app/src/main/java/com/example/ud3_ejemplo2/MainActivity.kt`
- **Inicio**: Línea 83 (`fun TarjetaBandera(bandera: Bandera, modifier: Modifier = Modifier)`)
- **Fin**: Línea 101 (`}`)
- **Código**:
```kotlin
@Composable
fun TarjetaBandera(bandera: Bandera, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(bandera.imagenResourceId),
                contentDescription = stringResource(bandera.stringResourceId)
            )
            Text(
                text = stringResource(bandera.stringResourceId),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}
```

---

## Módulo 4: Ud3_Ejemplo3 — Tarjetas Expandibles y Visibilidad Condicional
*Keywords: Card, IconButton, Icon, remember, mutableStateOf, abierto, Spacer, Modifier.weight, expand_circle_up, expand_circle_down, Visibilidad Condicional*

### Bloque 4.1: Modelo con Recurso de Información Extra
- **Ejercicio**: Ud3_Ejemplo3 (Tarjetas Expandibles)
- **Archivo**: `Ud3_Ejemplo3-master/app/src/main/java/com/example/ud3_ejemplo3/modelo/Bandera.kt`
- **Inicio**: Línea 6 (`data class Bandera(`)
- **Fin**: Línea 11 (`)`)
- **Código**:
```kotlin
data class Bandera(
    // Necesitamos dos ids, uno para el texto de la bandera y otro para la imagen
    @StringRes val stringResourceId: Int,
    @DrawableRes val imagenResourceId: Int,
    @StringRes val capital: Int
)
```

### Bloque 4.2: Tarjeta con Estado Local de Expansión
- **Ejercicio**: Ud3_Ejemplo3 (Tarjetas Expandibles)
- **Archivo**: `Ud3_Ejemplo3-master/app/src/main/java/com/example/ud3_ejemplo3/MainActivity.kt`
- **Inicio**: Línea 73 (`fun TarjetaBandera(bandera: Bandera, modifier: Modifier = Modifier)`)
- **Fin**: Línea 112 (`}`)
- **Código**:
```kotlin
@Composable
fun TarjetaBandera(bandera: Bandera, modifier: Modifier = Modifier) {

    var abierto by remember { mutableStateOf(false) }

    Card(modifier = modifier) {
        Column{
            Row(modifier = Modifier
                .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(bandera.imagenResourceId),
                    contentDescription = stringResource(bandera.stringResourceId)
                )
                Text(
                    text = stringResource(bandera.stringResourceId),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.weight(1f))
                TarjetaBoton(
                    abierto = abierto,
                    onClick = { abierto = !abierto}
                )
            }
            if (abierto) {
                InformacionBandera(
                    bandera.capital,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 8.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
                )
            }
        }
    }
}
```

### Bloque 4.3: Botón de Toggle con Alternancia de Iconos
- **Ejercicio**: Ud3_Ejemplo3 (Tarjetas Expandibles)
- **Archivo**: `Ud3_Ejemplo3-master/app/src/main/java/com/example/ud3_ejemplo3/MainActivity.kt`
- **Inicio**: Línea 114 (`private fun TarjetaBoton(`)
- **Fin**: Línea 131 (`}`)
- **Código**:
```kotlin
@Composable
private fun TarjetaBoton(
    abierto: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = if (abierto) painterResource(R.drawable.expand_circle_down) else painterResource(R.drawable.expand_circle_up),
            contentDescription = if (abierto) stringResource(R.string.abrir) else stringResource(R.string.cerrar)
        )
    }
}
```

### Bloque 4.4: Contenido Revelado Condicionalmente
- **Ejercicio**: Ud3_Ejemplo3 (Tarjetas Expandibles)
- **Archivo**: `Ud3_Ejemplo3-master/app/src/main/java/com/example/ud3_ejemplo3/MainActivity.kt`
- **Inicio**: Línea 133 (`fun InformacionBandera(`)
- **Fin**: Línea 150 (`}`)
- **Código**:
```kotlin
@Composable
fun InformacionBandera(
    @StringRes info: Int,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
    )  {
        Text(
            text = stringResource(R.string.capital),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = stringResource(info),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
```

---

## Módulo 5: Ud4_Ejemplo1 — Monitor del Ciclo de Vida de una Actividad
*Keywords: ComponentActivity, Lifecycle, onCreate, onStart, onResume, onPause, onStop, onRestart, onDestroy, Log.d, TAG*

### Bloque 5.1: Sobrescritura de los 7 Callbacks del Ciclo de Vida
- **Ejercicio**: Ud4_Ejemplo1 (Ciclo de Vida)
- **Archivo**: `Ud4_Ejemplo1-master/app/src/main/java/com/example/ud4_ejemplo1/MainActivity.kt`
- **Inicio**: Línea 16 (`private const val TAG = "MainActivity"`)
- **Fin**: Línea 64 (`}`)
- **Código**:
```kotlin
// Etiqueta para el Log
private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ud4_Ejemplo1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}
```

---

## Módulo 6: Ud4_Ejemplo2 — Calculadora Reactiva MVVM (StateFlow)
*Keywords: ViewModel, StateFlow, MutableStateFlow, asStateFlow, update, copy, collectAsState, onFocusChanged, toIntOrNull, TextField, Row, Column*

### Bloque 6.1: Estado Inmutable de la Interfaz (UI State)
- **Ejercicio**: Ud4_Ejemplo2 (Calculadora MVVM)
- **Archivo**: `Ud4_Ejemplo2-master/app/src/main/java/com/example/ud4_ejemplo2/modelo/OperacionUIState.kt`
- **Inicio**: Línea 3 (`data class OperacionUIState(`)
- **Fin**: Línea 7 (`)`)
- **Código**:
```kotlin
data class OperacionUIState(
    val numero1: String = "",
    val numero2: String = "",
    val resultado: String = ""
)
```

### Bloque 6.2: ViewModel con StateFlow, Mutación Atómica y Cálculos
- **Ejercicio**: Ud4_Ejemplo2 (Calculadora MVVM)
- **Archivo**: `Ud4_Ejemplo2-master/app/src/main/java/com/example/ud4_ejemplo2/ui/OperacionViewModel.kt`
- **Inicio**: Línea 13 (`class OperacionViewModel : ViewModel() {`)
- **Fin**: Línea 59 (`}`)
- **Código**:
```kotlin
class OperacionViewModel : ViewModel() {
    private val _operacionUItate = MutableStateFlow(OperacionUIState())

    val operacionUIState: StateFlow<OperacionUIState> = _operacionUItate.asStateFlow()

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
            estadoActual.copy (
                numero2 = respuestaNumero2
            )
        }

        calcular()
    }

    private fun calcular() {
        _operacionUItate.update { estadoActual ->
            val num1 = estadoActual.numero1.toIntOrNull() ?: 0
            val num2 = estadoActual.numero2.toIntOrNull() ?: 0

            estadoActual.copy(
                resultado = (num1 + num2).toString()
            )
        }
    }
}
```

### Bloque 6.3: UI Composable con Observación StateFlow y Gestión de Foco
- **Ejercicio**: Ud4_Ejemplo2 (Calculadora MVVM)
- **Archivo**: `Ud4_Ejemplo2-master/app/src/main/java/com/example/ud4_ejemplo2/MainActivity.kt`
- **Inicio**: Línea 45 (`fun Calculadora(`)
- **Fin**: Línea 109 (`}`)
- **Código**:
```kotlin
@Composable
fun Calculadora(
    modifier: Modifier = Modifier,
    operacionViewModel: OperacionViewModel = viewModel()
) {

    // Cada vez que haya un cambio en el valor de operacionUIState se produce una
    // recomposición para los elementos componibles con el valor de uiState.
    val uIState by operacionViewModel.operacionUIState.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ){
        Text(
            text = stringResource(R.string.calculadora),
            style = MaterialTheme.typography.displayMedium
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            TextField(
                // El número a mostrar será el que está almacenado en el uIState
                value = uIState.numero1,
                label = {Text (text = stringResource(R.string.numero_1)) },
                onValueChange = {
                    // Al escribir el número lo actualizamos en nuestro ViewModel
                    operacionViewModel.actualizarNumero1(it)
                },
                modifier = Modifier.weight(1F)
                    .onFocusChanged { focus ->
                        if (focus.isFocused) {
                            operacionViewModel.actualizarNumero1("")
                        }
                    }
            )
            Text(
                text = "+",
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1F)
            )
            TextField(
                // El número a mostrar será el que está almacenado en el uIState
                value = uIState.numero2,
                label = {Text (text = stringResource(R.string.numero_2))},
                onValueChange = {
                    // Al escribir el número lo actualizamos en nuestro ViewModel
                    operacionViewModel.actualizarNumero2(it)
                },
                modifier = Modifier.weight(1F)
                    .onFocusChanged { focus ->
                        if (focus.isFocused) {
                            operacionViewModel.actualizarNumero2("")
                        }
                    }
            )
            Text(
                text = "= ${uIState.resultado}",
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1F)
            )
        }
    }
}
```

---

## Módulo 7: Ud4_Ejemplo3 — Juego del Ahorcado MVVM con Diálogo Modal
*Keywords: ViewModel, StateFlow, JuegoUIState, AlertDialog, OutlinedTextField, KeyboardOptions, KeyboardActions, ImeAction.Done, clip, background, repeat, contains, indices*

### Bloque 7.1: Modelo de Estado UI del Juego
- **Ejercicio**: Ud4_Ejemplo3 (Juego del Ahorcado)
- **Archivo**: `Ud4_Ejemplo3-master/app/src/main/java/com/example/ud4_ejemplo3/modelo/JuegoUIState.kt`
- **Inicio**: Línea 3 (`data class JuegoUIState (`)
- **Fin**: Línea 9 (`)`)
- **Código**:
```kotlin
data class JuegoUIState (
    val palabraActual: String = "",
    val isLetraIncorrecta: Boolean = false,
    val puntos: Int = 0,
    val ronda: Int = 1,
    val isFinJuego: Boolean = false
)
```

### Bloque 7.2: ViewModel con Lógica UDF y Reglas del Juego
- **Ejercicio**: Ud4_Ejemplo3 (Juego del Ahorcado)
- **Archivo**: `Ud4_Ejemplo3-master/app/src/main/java/com/example/ud4_ejemplo3/ui/JuegoViewModel.kt`
- **Inicio**: Línea 16 (`class JuegoViewModel : ViewModel(){`)
- **Fin**: Línea 116 (`}`)
- **Código**:
```kotlin
class JuegoViewModel : ViewModel(){

    private val _uiState = MutableStateFlow(JuegoUIState())

    val uiState: StateFlow<JuegoUIState> = _uiState.asStateFlow()

    private lateinit var palabraActual: String
    private lateinit var palabraOculta: String
    private var palabrasUsadas: MutableSet<String> = mutableSetOf()

    var letraUsuario by mutableStateOf("")
        private set

    init {
        reiniciarJuego()
    }

    fun reiniciarJuego() {
        palabrasUsadas.clear()
        _uiState.value = JuegoUIState(
            palabraActual = elegirPalabraAleatoria()
        )
    }

    private fun elegirPalabraAleatoria(): String {
        palabraActual = listaPalabras.random()

        if(palabrasUsadas.contains(palabraActual)) {
            return elegirPalabraAleatoria()
        }
        else {
            palabrasUsadas.add(palabraActual)

            palabraOculta = "_".repeat(palabraActual.length)

            return palabraOculta
        }
    }

    fun actualizarLetraUsuario(letraRespuesta: String) {
        letraUsuario = letraRespuesta.uppercase()
    }

    fun comprobarLetraUsuario() {
        if(palabraActual.contains(letraUsuario[0], ignoreCase = true)){

            val guiones = palabraOculta.toCharArray()

            // Reemplaza las ocurrencias de la letra en la posición correspondiente
            for (i in palabraActual.indices) {
                if (palabraActual[i] == letraUsuario[0]) {
                    guiones[i] = letraUsuario[0]
                }
            }
            palabraOculta = String(guiones)

            palabraActual = palabraActual.replace(letraUsuario[0], '_')

            _uiState.update { estadoActual ->
                estadoActual.copy(
                    palabraActual = palabraOculta,
                    isLetraIncorrecta = false)
            }

            if(!palabraOculta.contains('_')) {
                val puntuacionActual = _uiState.value.puntos.plus(PUNTOS_POR_ACIERTO)

                actualizarEstadoJuego(puntuacionActual)
            }
        }
        else {
            _uiState.update { estadoActual ->
                estadoActual.copy(isLetraIncorrecta = true)
            }
        }

        actualizarLetraUsuario("")
    }

    fun actualizarEstadoJuego(puntuacionActual: Int) {
        if(palabrasUsadas.size == RONDAS){
            _uiState.update { estadoActual->
                estadoActual.copy(
                    isLetraIncorrecta = false,
                    puntos = puntuacionActual,
                    isFinJuego = true
                )
            }
        }
        else {
            _uiState.update { estadoactual ->
                estadoactual.copy(
                    palabraActual = elegirPalabraAleatoria(),
                    isLetraIncorrecta = false,
                    puntos = puntuacionActual,
                    ronda = estadoactual.ronda.inc()
                )
            }
        }
    }
}
```

### Bloque 7.3: Composable Principal de Pantalla del Juego
- **Ejercicio**: Ud4_Ejemplo3 (Juego del Ahorcado)
- **Archivo**: `Ud4_Ejemplo3-master/app/src/main/java/com/example/ud4_ejemplo3/PantallaJuego.kt`
- **Inicio**: Línea 38 (`fun PantallaJuego(`)
- **Fin**: Línea 101 (`}`)
- **Código**:
```kotlin
@Composable
fun PantallaJuego(
    modifier: Modifier = Modifier,
    juegoViewModel: JuegoViewModel = viewModel()
){
    val juegoUIState by juegoViewModel.uiState.collectAsState()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.ahorcado),
            style = MaterialTheme.typography.headlineLarge
        )

        Row {
            NumeroRondasJuego(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1F)
            )
            PuntuacionJuego(
                puntos = juegoUIState.puntos,
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1F)
            )
        }

        LayoutJuego(
            onLetraUsuarioChanged = {juegoViewModel.actualizarLetraUsuario(it)},
            onKeyboardDone = { juegoViewModel.comprobarLetraUsuario() },
            palabraActual = juegoUIState.palabraActual,
            letraUsuario = juegoViewModel.letraUsuario,
            isLetraIncorrecta = juegoUIState.isLetraIncorrecta,
            ronda = juegoUIState.ronda,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
        )

        Button(
            onClick = {
                if(!juegoViewModel.letraUsuario.isEmpty())
                    juegoViewModel.comprobarLetraUsuario() // Al pulsar el botón llamamos al ViewModel y comprobamos la respuesta del usuario.
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Aceptar",
                fontSize = 16.sp
            )
        }

        if(juegoUIState.isFinJuego){
            DialogoFinJuego(
                puntos = juegoUIState.puntos,
                onJugarOtraVez = { juegoViewModel.reiniciarJuego() })
        }
    }
}
```

### Bloque 7.4: Layout del Juego con KeyboardActions y Trigger IME
- **Ejercicio**: Ud4_Ejemplo3 (Juego del Ahorcado)
- **Archivo**: `Ud4_Ejemplo3-master/app/src/main/java/com/example/ud4_ejemplo3/PantallaJuego.kt`
- **Inicio**: Línea 132 (`fun LayoutJuego(`)
- **Fin**: Línea 187 (`}`)
- **Código**:
```kotlin
@Composable
fun LayoutJuego(
    onLetraUsuarioChanged: (String) -> Unit,
    onKeyboardDone: () -> Unit,
    palabraActual: String,
    letraUsuario: String,
    isLetraIncorrecta: Boolean,
    ronda: Int,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp) // Le damos un poco de relieve.
    ){
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium) // Aplicamos un clip al texto
                    .background(MaterialTheme.colorScheme.surfaceTint)
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                text = stringResource(R.string.ronda, ronda), // En el clip mostramos el número de ronda que toca.
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = palabraActual,
                letterSpacing = 5.sp,
                style = MaterialTheme.typography.displayMedium,

                )
            OutlinedTextField(
                value = letraUsuario.uppercase(), // Asignamos como valor al TextField la respuesta pasada desde el ViewModel
                singleLine = true,
                shape = MaterialTheme.shapes.large,
                onValueChange = onLetraUsuarioChanged,
                label = {
                    Text(text = stringResource(R.string.introduce_una_letra))
                },
                isError = isLetraIncorrecta,
                keyboardOptions = KeyboardOptions.Default.copy( // El usuario ha introducido un valor y se lanza un trigger
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions ( // Acción al lanzarse el trigger del TextField
                    onDone = { onKeyboardDone() }
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}
```

### Bloque 7.5: Diálogo Modal de Fin de Juego con AlertDialog
- **Ejercicio**: Ud4_Ejemplo3 (Juego del Ahorcado)
- **Archivo**: `Ud4_Ejemplo3-master/app/src/main/java/com/example/ud4_ejemplo3/PantallaJuego.kt`
- **Inicio**: Línea 189 (`fun DialogoFinJuego (`)
- **Fin**: Línea 217 (`}`)
- **Código**:
```kotlin
@Composable
fun DialogoFinJuego (
    puntos: Int,
    onJugarOtraVez: () -> Unit,
    modifier: Modifier = Modifier
) {
    val actividad = LocalActivity.current

    AlertDialog(
        onDismissRequest = {
            // Controla la acción si se pulsa fuera del dialogo.
        },
        title = { Text(text = stringResource(R.string.enhorabuena))},
        text = { Text(text = stringResource(R.string.puntos, puntos))},
        dismissButton = {
            TextButton(
                onClick = {
                    actividad?.finish()
                }) {
                Text(text = stringResource(R.string.salir))
            }
        },
        confirmButton = {
            TextButton(onClick = onJugarOtraVez ) {
                Text(text = stringResource(R.string.jugar_otra_vez))
            }
        },
        modifier = modifier)
}
```

---

## Módulo 8: Ud4_Ejercicio4 — Restaurante DAM (POO, Herencia y StateFlow)
*Keywords: open class Producto, Hamburguesa, Pizza, Pedido, RestauranteUIState, RestauranteViewModel, RadioButton, Checkbox, Switch, OutlinedTextField, LocalDate, StateFlow*

### Bloque 8.1: Jerarquía POO con Herencia y Polimorfismo
- **Ejercicio**: Ud4_Ejercicio4 (Restaurante DAM)
- **Archivo**: `Ud4_Ejercicio4-master/app/src/main/java/com/example/ud4_ejercicio/modelo/Producto.kt` (y clases derivadas)
- **Inicio**: `open class Producto(`
- **Fin**: Cierre de las clases de modelo
- **Código**:
```kotlin
// Archivo: modelo/Producto.kt
open class Producto(
    @DrawableRes val imagen: Int = 0,
    val nombre: String = ""
)

// Archivo: modelo/Hamburguesa.kt
data class Hamburguesa(
    val tipoCarne: String = "",
    val isQueso: Boolean = false
): Producto(imagen = R.drawable.hamburguesa, nombre = "Hamburguesa")

// Archivo: modelo/Pizza.kt
data class Pizza(
    val tipoPizza: String = "",
    val tamanyo: String = ""
): Producto(imagen = R.drawable.pizza, nombre = "Pizza")

// Archivo: modelo/Pedido.kt
data class Pedido(
    val producto: Producto,
    val cantidad: Int,
    val fecha: String
)
```

### Bloque 8.2: UIState y ViewModel de Acumulación Reactiva
- **Ejercicio**: Ud4_Ejercicio4 (Restaurante DAM)
- **Archivo**: `Ud4_Ejercicio4-master/app/src/main/java/com/example/ud4_ejercicio/ui/RestauranteViewModel.kt`
- **Inicio**: Línea 11 (`class RestauranteViewModel: ViewModel() {`)
- **Fin**: Línea 24 (`}`)
- **Código**:
```kotlin
// Archivo: modelo/RestauranteUIState.kt
data class RestauranteUIState(
    val pedidos: List<Pedido> = listOf()
)

// Archivo: ui/RestauranteViewModel.kt
class RestauranteViewModel: ViewModel() {
    private val _restauranteUIState = MutableStateFlow(RestauranteUIState())
    val restauranteUIState: StateFlow<RestauranteUIState> = _restauranteUIState.asStateFlow()

    fun insertarProducto(pedido: Pedido) {
        _restauranteUIState.update { estadoActual ->
            estadoActual.copy(
                pedidos = estadoActual.pedidos + pedido
            )
        }
    }
}
```

### Bloque 8.3: Formulario Interactivo con RadioButtons, Checkbox, Switch y Campos Numéricos
- **Ejercicio**: Ud4_Ejercicio4 (Restaurante DAM)
- **Archivo**: `Ud4_Ejercicio4-master/app/src/main/java/com/example/ud4_ejercicio/MainActivity.kt`
- **Inicio**: Línea 91 (`fun Formulario(`)
- **Fin**: Línea 350 (`}`)
- **Código**:
```kotlin
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Formulario(
    modifier: Modifier = Modifier,
    restauranteViewModel: RestauranteViewModel = viewModel()
){
    val uIState by restauranteViewModel.restauranteUIState.collectAsState()

    // Variables Producto
    var fecha by remember { mutableStateOf(LocalDate.now().toString()) }
    var cantidad by remember { mutableStateOf("") }
    val listaProductos = listOf("Hamburguesa", "Pizza")
    var (eleccProducto, onOpcionProducto) = remember { mutableStateOf(listaProductos[0])}

    // Variables Hamburguesa
    var queso by remember { mutableStateOf(false) }
    val listaCarne = listOf("Pollo", "Ternera")
    var (eleccCarne, onOpcionCarne) = remember { mutableStateOf(listaCarne[0]) }

    // Variables Pizza
    var tipo_pizza by remember { mutableStateOf(false) }
    val listaTam = listOf("Pequeña", "Mediana", "Grande")
    var (elecTam, onOpcionTam) = remember { mutableStateOf(listaTam[0]) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Elige el producto",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Card(
            modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0x5C3F51B5),
            )
        ) {
            Row(
                Modifier
                    .selectableGroup()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                listaProductos.forEach { opcion ->
                    val tipo = when (opcion) {
                        "Hamburguesa" -> R.drawable.hamburguesa
                        "Pizza" -> R.drawable.pizza
                        else -> R.drawable.pizza
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(tipo),
                            contentDescription = opcion,
                            modifier = Modifier.size(100.dp)
                                .selectable(
                                    selected = (opcion == eleccProducto),
                                    onClick = {
                                        eleccProducto = opcion
                                        onOpcionProducto(opcion)
                                    }
                                )
                        )
                        Row(
                            modifier = Modifier.selectable(
                                selected = (opcion == eleccProducto),
                                onClick = {
                                    eleccProducto = opcion
                                    onOpcionProducto(opcion)
                                }
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (opcion == eleccProducto),
                                onClick = {
                                    eleccProducto = opcion
                                    onOpcionProducto(opcion)
                                })
                            Text(text = opcion)
                        }
                    }
                }
            }
        }

        when (eleccProducto) {
            "Hamburguesa" -> {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Queso",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Checkbox(
                            checked = queso,
                            onCheckedChange = { queso = it }
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Carne",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        Column(Modifier.selectableGroup()) {
                            listaCarne.forEach { opcion ->
                                Row(
                                    modifier = Modifier.selectable(
                                        selected = (opcion == eleccCarne),
                                        onClick = {
                                            eleccCarne = opcion
                                            onOpcionCarne(opcion)
                                        }
                                    ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = (opcion == eleccCarne),
                                        onClick = {
                                            eleccCarne = opcion
                                            onOpcionCarne(opcion)
                                        }
                                    )
                                    Text(text = opcion)
                                }
                            }
                        }
                    }
                }
            }

            "Pizza" -> {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Margarita/Romana",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Switch(
                            checked = tipo_pizza,
                            onCheckedChange = {
                                tipo_pizza = it
                            }
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Tamaño",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        Column(Modifier.selectableGroup()) {
                            listaTam.forEach { opcion ->
                                Row(
                                    modifier = Modifier.selectable(
                                        selected = (opcion == elecTam),
                                        onClick = {
                                            elecTam = opcion
                                            onOpcionTam(opcion)
                                        }
                                    ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = (opcion == elecTam),
                                        onClick = {
                                            elecTam = opcion
                                            onOpcionTam(opcion)
                                        })
                                    Text(text = opcion)
                                }
                            }
                        }
                    }
                }
            }
            else -> {}
        }

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp)
        ) {
            OutlinedTextField(
                value = cantidad,
                onValueChange = { cantidad = it },
                label = { Text("Cantidad") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(0.3f)
            )

            Spacer(modifier = Modifier.weight(0.1f))

            OutlinedTextField(
                value = fecha,
                onValueChange = { },
                label = { Text("Fecha") },
                enabled = false,
                modifier = Modifier.weight(0.6f)
            )
        }
        Row (modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
            Button(
                onClick = {
                    val producto =
                    if (eleccProducto == "Hamburguesa") {
                        Hamburguesa(eleccCarne, queso)
                    } else {
                        Pizza(elecTam, if(tipo_pizza) "Romana" else "Margarita")
                    }
                    restauranteViewModel.insertarProducto(
                        Pedido(
                            producto,
                            cantidad.toInt(),
                            fecha
                        )
                    )
                },
                modifier = Modifier.padding(end = 10.dp)
             ) {
                Text(
                    text = "Añadir",
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = { },
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Text(
                    text = "Cancelar",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(5.dp))
        Text(
            text = "${uIState.pedidos}"
        )
    }
}
```

---

## Módulo 9: Ejercicio 2 — Gestor de Módulos DAM / Tareas Urgentes
*Keywords: modulo, ModuloUIState, ModuloViewModel, LazyColumn, Card, RadioButton, Checkbox, OutlinedTextField, Modifier.weight, HorizontalDivider*

### Bloque 9.1: Modelo de Datos y Estado UI Inmutable
- **Ejercicio**: Ejercicio 2 (Módulos DAM)
- **Archivo**: `ejercicio2/app/src/main/java/com/example/ud4_ejercicio/modelo/modulo.kt` & `ModuloUIState.kt`
- **Inicio**: `data class modulo(...)`
- **Fin**: Cierre de las clases de modelo
- **Código**:
```kotlin
// Archivo: modelo/modulo.kt
package com.example.ud4_ejercicio.modelo;

data class modulo(val nombre: String, val urgente: Boolean, val descripcion: String, val titulo: String) {}

// Archivo: modelo/ModuloUIState.kt
package com.example.ud4_ejercicio.modelo

data class ModuloUIState(
    val listaModulo: List<modulo> = listOf(),
    val modulo: modulo = modulo("", false, "", "")
)
```

### Bloque 9.2: ViewModel de Registro y Acumulación
- **Ejercicio**: Ejercicio 2 (Módulos DAM)
- **Archivo**: `ejercicio2/app/src/main/java/com/example/ud4_ejercicio/ui/ModuloViewModel.kt`
- **Inicio**: Línea 11 (`class ModuloViewModel: ViewModel() {`)
- **Fin**: Línea 32 (`}`)
- **Código**:
```kotlin
class ModuloViewModel: ViewModel() {
    private val _modulosUIState = MutableStateFlow(ModuloUIState())
    val modulosUIState: StateFlow<ModuloUIState> = _modulosUIState.asStateFlow()

    fun updatelistaModulo(modulo: modulo){
        _modulosUIState.update {EstadoActual ->
            EstadoActual.copy(
                listaModulo = EstadoActual.listaModulo + modulo
            )
        }
    }

    fun updateModulo(nombre: String, urgente: Boolean,  descripcion: String, titulo: String) {
        _modulosUIState.update { EstadoActual ->
            EstadoActual.copy(
                modulo = modulo(nombre, urgente, descripcion, titulo)
            )
        }
        updatelistaModulo(_modulosUIState.value.modulo)
    }
}
```

### Bloque 9.3: Formulario, Selección de Asignaturas y LazyColumn Reactiva
- **Ejercicio**: Ejercicio 2 (Módulos DAM)
- **Archivo**: `ejercicio2/app/src/main/java/com/example/ud4_ejercicio/MainActivity.kt`
- **Inicio**: Línea 80 (`fun Formulario(`)
- **Fin**: Línea 220 (`}`)
- **Código**:
```kotlin
@Composable
fun Formulario(
    modifier: Modifier = Modifier,
    moduloViewModel: ModuloViewModel = viewModel()
){
    val uIState by moduloViewModel.modulosUIState.collectAsState()
    var descripcion by remember { mutableStateOf("") }
    var titulo by remember { mutableStateOf("") }
    val listaAsignaturas = listOf("AD", "DI", "PMDM")
    var (eleccAsignatura, onOpcionAsignatura) = remember { mutableStateOf(listaAsignaturas[0])}
    var urgencia by remember { mutableStateOf(false) }
    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Elige el producto",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 10.dp)
        )
            Card(
                modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp).fillMaxWidth(),
            ) {
                Row(
                    Modifier
                        .selectableGroup()
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    listaAsignaturas.forEach { opcion ->

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.selectable(
                                    selected = (opcion == eleccAsignatura),
                                    onClick = {
                                        eleccAsignatura = opcion
                                        onOpcionAsignatura(opcion)
                                    }
                                ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (opcion == eleccAsignatura),
                                    onClick = {
                                        eleccAsignatura = opcion
                                        onOpcionAsignatura(opcion)
                                    })
                                Text(text = opcion)
                            }
                        }
                    }
                }
            }
                Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Es urgente",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Checkbox(
                        checked = urgencia,
                        onCheckedChange = { urgencia = it }
                    )
                }
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(10.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(0.6f),
                    value = titulo,
                    singleLine = true,
                    onValueChange = { titulo = it },
                    label = { Text("titulo") }
                )
                Spacer(modifier = Modifier.weight(0.1f),)
                OutlinedTextField(
                    modifier = Modifier.weight(0.3f),
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("descripcion") }

                )
            }
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                        moduloViewModel.updateModulo(eleccAsignatura, urgencia, descripcion, titulo)
                },
                modifier = Modifier.padding(end = 10.dp)
            ) {
                Text(
                    text = "Añadir",
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = { },
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Text(
                    text = "Cancelar",
                    fontWeight = FontWeight.Bold
                )
            }
        }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Lista de Módulos",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        HorizontalDivider(
            thickness = 2.dp,
            color = Color.Gray
        )
        LazyColumn(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
        items(uIState.listaModulo) { modulo ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                if (!modulo.urgente) {
                    Text(text = "${modulo.nombre} - ${modulo.titulo} - ${modulo.descripcion}")
                }else{
                    Text(text = "${modulo.nombre} - ${modulo.titulo} - ${modulo.descripcion}", color = Color.Red)
                }
            }
        }
        }
        }
    }
```

---

## Módulo 10: Referencia Rápida de Snippets y Patrones de Examen
*Keywords: Patrones Comunes, Ctrl+F, Boilerplate, ViewModel, LazyColumn, Dialog, RadioButton, TextField*

### 10.1: Patrón UDF (Unidirectional Data Flow) en Jetpack Compose
```kotlin
// 1. Estado inmutable
data class MiUIState(val valor: String = "", val cargando: Boolean = false)

// 2. ViewModel con StateFlow
class MiViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MiUIState())
    val uiState: StateFlow<MiUIState> = _uiState.asStateFlow()

    fun actualizar(nuevoValor: String) {
        _uiState.update { it.copy(valor = nuevoValor) }
    }
}

// 3. Composable consumiendo con collectAsState
@Composable
fun MiPantalla(viewModel: MiViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Text(text = uiState.valor)
}
```

### 10.2: Patrón Grupo de RadioButtons Excluyentes
```kotlin
val opciones = listOf("Opción A", "Opción B", "Opción C")
var seleccion by remember { mutableStateOf(opciones[0]) }

Column(modifier = Modifier.selectableGroup()) {
    opciones.forEach { opcion ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = (opcion == seleccion),
                    onClick = { seleccion = opcion }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = (opcion == seleccion),
                onClick = { seleccion = opcion }
            )
            Text(text = opcion)
        }
    }
}
```

### 10.3: Patrón LazyColumn con Lista Inmutable y Card
```kotlin
LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    items(listaElementos) { elemento ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* acción al pulsar */ }
                .padding(8.dp)
        ) {
            Text(text = elemento.nombre, modifier = Modifier.padding(16.dp))
        }
    }
}
```

### 10.4: Patrón Cuadrícula Responsiva con LazyVerticalGrid
```kotlin
LazyVerticalGrid(
    columns = GridCells.Adaptive(minSize = 128.dp), // o GridCells.Fixed(2)
    modifier = Modifier.fillMaxSize()
) {
    items(listaElementos) { item ->
        Card(modifier = Modifier.padding(4.dp)) {
            Text(text = item.titulo)
        }
    }
}
```

### 10.5: Patrón Diálogo Modal con AlertDialog
```kotlin
var mostrarDialogo by remember { mutableStateOf(false) }

if (mostrarDialogo) {
    AlertDialog(
        onDismissRequest = { mostrarDialogo = false },
        title = { Text("Confirmación") },
        text = { Text("¿Deseas realizar esta acción?") },
        confirmButton = {
            Button(onClick = { 
                mostrarDialogo = false 
                /* acción confirmar */ 
            }) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            Button(onClick = { mostrarDialogo = false }) {
                Text("Cancelar")
            }
        }
    )
}
```

### 10.6: Patrón Campo Numérico con Auto-Limpieza de Foco
```kotlin
var texto by remember { mutableStateOf("") }

OutlinedTextField(
    value = texto,
    onValueChange = { texto = it },
    label = { Text("Introduce cantidad") },
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    modifier = Modifier
        .fillMaxWidth()
        .onFocusChanged { focusState ->
            if (focusState.isFocused) {
                texto = "" // Limpia al ganar foco
            }
        }
)
```

---
*Fin de la Cheatsheet.*
