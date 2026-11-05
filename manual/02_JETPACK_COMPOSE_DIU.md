# Parte 2: Diseño de Interfaces con Jetpack Compose (DIU)
### 2º DAM — Programación Multimedia y Dispositivos Móviles (PMDM)

Este documento cubre todos los fundamentos y técnicas de diseño de interfaces de usuario con Jetpack Compose, integrando los conceptos de `Android_Ud2_DIU.pdf` y las soluciones prácticas de los ejercicios (RadioButtons agrupados, Checkbox, Switch, formateo monetario, animaciones y diálogos).

---

## 1. Paradigma Declarativo vs Imperativo

#### Comparativa Conceptual:

| Característica | Paradigma Imperativo Tradicional (XML + Views) | Paradigma Declarativo Moderno (Jetpack Compose) |
| :--- | :--- | :--- |
| **Definición de UI** | Archivos XML estáticos separados del código Kotlin. | Funciones Kotlin puras anotadas con `@Composable`. |
| **Paso de Datos** | La Activity busca la vista con `findViewById` y muta sus propiedades manualmente. | La UI se describe en función del estado actual: $UI = f(Estado)$. |
| **Actualización** | Manual (`textView.setText(...)`, `layout.setVisibility(...)`). Propenso a inconsistencias. | **Automática (Recomposición)**: Solo se redibujan los componentes cuyo estado cambió. |
| **Acoplamiento** | Alto acoplamiento entre IDs de XML y código fuente. | Componentes modulares, desacoplados y fácilmente reutilizables. |

#### Comparativa de Código: Implementar un Contador Interactivo

```kotlin
// ==========================================================
// 1. ENFOQUE IMPERATIVO (Tradicional - XML + Activity)
// ==========================================================
// Requiere un layout XML (activity_main.xml) con <TextView android:id="@+id/tvContador"/>
// y <Button android:id="@+id/btnIncrementar"/>:
class MainActivityVieja : AppCompatActivity() {
    private var contador = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvContador = findViewById<TextView>(R.id.tvContador)
        val btnIncrementar = findViewById<Button>(R.id.btnIncrementar)

        // Mutación imperativa manual paso a paso:
        btnIncrementar.setOnClickListener {
            contador++
            tvContador.text = "Pulsaciones: $contador" // Si olvidas esta línea, la UI queda desincronizada
        }
    }
}

// ==========================================================
// 2. ENFOQUE DECLARATIVO (Jetpack Compose)
// ==========================================================
// Se describe qué pintar en base al estado 'contador'. La UI reacciona sola al cambio:
@Composable
fun ContadorDeclarativo() {
    var contador by remember { mutableStateOf(0) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Pulsaciones: $contador")
        Button(onClick = { contador++ }) { // Al cambiar contador, Compose recompone el Text automáticamente
            Text("Incrementar")
        }
    }
}
```

---

## 2. Funciones Composable y Reglas de Nomenclatura

Una función composable es el bloque de construcción elemental de Compose:
- Se declara con la anotación `@Composable`.
- No retorna ningún valor (emite nodos en el árbol de UI).
- **Convención de nomenclatura**: Su nombre debe comenzar por **mayúscula** y debe ser un **sustantivo** (ej. `FormularioPedido`, `TarjetaBandera`, `Calculadora`), nunca un verbo (`dibujarPantalla`), adjetivo o adverbio.
- Se lanzan dentro de la actividad llamando a `setContent { ... }` en `ComponentActivity`.

```kotlin
@Composable
fun SaludoUsuario(nombre: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hola, $nombre",
        modifier = modifier
    )
}
```

---

## 3. Modificadores (`Modifier`): El Impacto Crítico del Orden

Un `Modifier` decora o modifica el tamaño, aspecto, comportamiento y eventos de un elemento composable.

> [!CAUTION]
> **El orden de encadenamiento de los modificadores es secuencial y acumulativo.**
> Compose procesa cada llamada de izquierda a derecha. Alterar el orden cambia completamente el resultado visual y funcional:

```kotlin
// CASO 1: El padding se aplica primero; el fondo y el área de clic cubren TODA la tarjeta
Box(
    modifier = Modifier
        .background(Color.Yellow)
        .clickable { println("Clic en toda la caja") }
        .padding(24.dp)
)

// CASO 2: El padding se aplica antes del clic; la zona de padding NO reacciona al clic
Box(
    modifier = Modifier
        .padding(24.dp)
        .background(Color.Yellow)
        .clickable { println("Clic solo en el área interna") }
)
```

### Modificadores indispensables:
- `.fillMaxSize()` / `.fillMaxWidth()`: Ocupa todo el espacio de su contenedor padre.
- `.padding(...)`: Aplica separación externa o interna.
- `.size(width, height)`: Asigna dimensiones fijas en `dp`.
- `.weight(1f)`: Reparte proporcionalmente el espacio disponible en `Row` o `Column`.
- `.clip(shape)`: Recorta el contenido a una silueta geométrica (`CircleShape`, `RoundedCornerShape`).
- `.animateContentSize()`: Suaviza y anima automáticamente los cambios de altura/anchura al expandir contenido.
- `.verticalScroll(rememberScrollState())`: Habilita scroll táctil en columnas estándar.

---

## 4. Elementos de Texto (`Text`) y Tipografías

El composable `Text()` muestra cadenas formateadas según Material Design.

#### A) Las 4 Formas de Asignar Color a un `Text` (Según el PDF Oficial)

Según el temario oficial (`Android_Ud2_DIU.pdf`), existen 4 mecanismos para definir el color de un texto:

1. **Mediante la clase `Color` estándar de Compose**:
   ```kotlin
   Text(text = "Azul directo", color = Color.Blue)
   Text(text = "Color Hexadecimal", color = Color(0xFFD0BCFF))
   ```
2. **Desde el archivo de recursos `res/values/colors.xml`**:
   ```kotlin
   Text(text = "Desde XML", color = colorResource(id = R.color.purple_200))
   ```
3. **Desde el archivo de paleta `ui.theme/Color.kt`**:
   ```kotlin
   Text(text = "Color de paleta", color = Purple80)
   ```
4. **Desde el Tema dinámico `ui.theme/Theme.kt` (Enfoque Recomendable)**:
   Garantiza coherencia estética y adaptación automática a modo claro/oscuro:
   ```kotlin
   Text(text = "Color del Tema", color = MaterialTheme.colorScheme.primary)
   ```

#### B) Estilos Tipográficos y Altura de Línea (`lineHeight` con `.sp` y `.em`)

- **`fontWeight`**: Grosor del trazo (`FontWeight.Bold`, `FontWeight.Normal`, `FontWeight.SemiBold`).
- **`fontStyle`**: Estilo cursiva (`FontStyle.Italic`).
- **`lineHeight` (Altura de la línea de texto)**: Según el PDF oficial, se puede configurar tanto con unidades fijas **`.sp`** (`lineHeight = 24.sp`) como con la unidad de medida relativa **`.em`** (`lineHeight = 1.5.em`) para mejorar la precisión y proporcionalidad tipográfica.

#### C) Comparativa de Unidades: `sp` vs `dp`

| Unidad | Significado | Escalado con Accesibilidad | Uso Recomendado |
| :--- | :--- | :---: | :--- |
| **`sp`** | *Scale-independent Pixels* | **SÍ** (se agranda si el usuario activa fuente grande en ajustes) | **Exclusivamente para tamaños de texto (`fontSize`, `lineHeight`)**. |
| **`dp`** | *Density-independent Pixels* | **NO** (mantiene proporciones físicas de pantalla) | **Márgenes, padding, anchuras, alturas (`size`) e iconos**. |

#### D) Comparativa de Alineación: `Start`/`End` vs `Left`/`Right` (Soporte RTL)

En Android internacionalizado existen idiomas que se leen de izquierda a derecha (LTR, ej. español o inglés) y otros de derecha a izquierda (RTL, ej. árabe o hebreo):

| Propiedad | Comportamiento en LTR (Español) | Comportamiento en RTL (Árabe) | Veredicto |
| :--- | :--- | :--- | :--- |
| **`TextAlign.Start`** | Alinea al borde **izquierdo** | Alinea al borde **derecho** | ✅ **Recomendado por Google** |
| **`TextAlign.End`** | Alinea al borde **derecho** | Alinea al borde **izquierdo** | ✅ **Recomendado por Google** |
| **`TextAlign.Left`** | Alinea siempre a la izquierda | Alinea a la izquierda (rompe la lectura natural) | ❌ Desaconsejado |
| **`TextAlign.Right`** | Alinea siempre a la derecha | Alinea a la derecha | ❌ Desaconsejado |

---

## 5. Entrada de Datos: `TextField` vs `OutlinedTextField`

Compose ofrece dos estilos oficiales de campos de texto según Material 3:

| Criterio | `TextField` (Filled) | `OutlinedTextField` (Contorneado) |
| :--- | :--- | :--- |
| **Estilo Visual** | Fondo con relleno tonal sutil y línea de acento inferior. | Borde perimetral nítido con etiqueta que flota hacia la línea. |
| **Énfasis Visual** | Mayor peso visual; ideal para formularios breves o búsquedas aisladas. | Aspecto más limpio; ideal para formularios densos con muchos campos contiguos. |
| **Uso en el temario** | Usado en ejemplos exploratorios de DIU. | Usado en la Calculadora de IVA y Restaurante DAM. |

#### Comparativa de Implementación:

```kotlin
var entrada1 by remember { mutableStateOf("") }
var entrada2 by remember { mutableStateOf("") }

Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
    // 1. TextField con fondo relleno:
    TextField(
        value = entrada1,
        onValueChange = { entrada1 = it },
        label = { Text("Nombre (Filled TextField)") },
        modifier = Modifier.fillMaxWidth()
    )

    // 2. OutlinedTextField con borde contorneado y validación de error:
    OutlinedTextField(
        value = entrada2,
        onValueChange = { entrada2 = it },
        label = { Text("Introduce una cantidad (Outlined)") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        isError = entrada2.isEmpty(),
        supportingText = {
            if (entrada2.isEmpty()) {
                Text("Campo obligatorio", color = MaterialTheme.colorScheme.error)
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}
```

---

## 6. Manejo de Imágenes y Densidades de Pantalla

El composable `Image()` renderiza mapas de bits o gráficos vectoriales.

#### A) Parámetros Esenciales de `Image()`

```kotlin
Image(
    painter = painterResource(id = R.drawable.hamburguesa),
    contentDescription = "Hamburguesa gourmet", // Texto de accesibilidad para invidentes
    contentScale = ContentScale.Crop,          // Modo de escalado
    alpha = 0.5F,                              // Opacidad de la imagen (de 0.0F transparente a 1.0F opaca)
    modifier = Modifier.size(100.dp)
)
```

> [!TIP]
> **Imágenes Decorativas y Accesibilidad (`contentDescription = null`):**
> Según el temario oficial, cuando una imagen cumple un rol **puramente estético o decorativo** (como un fondo de pantalla, un divisor gráfico o un icono secundario), se debe pasar explícitamente **`contentDescription = null`**. De este modo, los servicios de accesibilidad (como *TalkBack*) ignoran la imagen y no saturan al usuario con lecturas innecesarias.

#### B) ¿Por qué importar en `res/drawable-nodpi/`?
Android cuenta con múltiples densidades de píxeles (`mdpi`, `hdpi`, `xhdpi`, `xxhdpi`, `xxxhdpi`).
- Si colocas una imagen en `drawable` por defecto, Android intentará redimensionarla para pantallas de alta densidad, lo que puede provocar que la imagen se vea borrosa o consuma excesiva memoria RAM.
- Al colocar recursos gráficos de alta resolución en `res/drawable-nodpi/` (*No Density*), se le indica al sistema que **no aplique escalado automático**, garantizando nitidez y eficiencia.

---

## 7. Contenedor `Scaffold` y el `innerPadding`

`Scaffold` (andamio) estructura la pantalla implementando el patrón visual de Material Design:
- `topBar`: Barra superior de la aplicación (`TopAppBar`, `CenterAlignedTopAppBar`).
- `bottomBar`: Barra de navegación inferior (`BottomAppBar`).
- `floatingActionButton`: Botón de acción flotante (FAB) situado en la esquina inferior.
- **`innerPadding`**: Espacio que reserva el sistema para las barras. **Es obligatorio pasarlo como padding al contenido principal** para evitar que la UI quede oculta debajo de la TopBar o de la barra de navegación del teléfono.

#### Ejemplo Completo Oficial (TopBar, BottomBar, FAB y Cierre Seguro de Activity):

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipalScaffold() {
    // Para cerrar la Activity desde un Composable se obtiene la Activity actual mediante LocalContext:
    val activity = (LocalContext.current as? Activity)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Aplicación DAM") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        // Cierre seguro de la Activity:
                        activity?.finish()
                    }) {
                        Text("←", fontSize = 24.sp)
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Barra inferior"
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { println("FAB pulsado") }) {
                Text("+", fontSize = 24.sp)
            }
        }
    ) { innerPadding ->
        // innerPadding DEBE aplicarse obligatoriamente aquí:
        ContenidoPrincipal(modifier = Modifier.padding(innerPadding))
    }
}
```

---

## 8. Layouts Fundamentales: `Column`, `Row` y `Box`

Compose no usa coordenadas absolutas en píxeles, sino contenedores de distribución relativa:

```
    ┌──────────┐        ┌───────────────────────┐        ┌──────────┐
    │  Texto 1 │        │ Texto 1  │  Texto 2   │        │  Capa 1  │
    ├──────────┤        └───────────────────────┘        │  Capa 2  │
    │  Texto 2 │                 Row                     └──────────┘
    └──────────┘                                             Box
       Column
```

#### A) Comparativa: `Arrangement` (Eje Principal) vs `Alignment` (Eje Transversal)

Una fuente común de confusión en Compose es la diferencia entre ordenamiento (*Arrangement*) y alineación (*Alignment*):

| Concepto | En `Column` (Eje Vertical) | En `Row` (Eje Horizontal) |
| :--- | :--- | :--- |
| **`Arrangement`** *(Eje Principal)* | Controla cómo se reparten los elementos **de arriba a abajo** (`verticalArrangement`). | Controla cómo se reparten los elementos **de izquierda a derecha** (`horizontalArrangement`). |
| **`Alignment`** *(Eje Transversal)* | Controla cómo se alinean los elementos **horizontalmente** (`horizontalAlignment`: `Start`, `CenterHorizontally`, `End`). | Controla cómo se alinean los elementos **verticalmente** (`verticalAlignment`: `Top`, `CenterVertically`, `Bottom`). |

#### B) Comparativa de Opciones de `Arrangement`

| Opción de Arrangement | Comportamiento Visual |
| :--- | :--- |
| **`Arrangement.Top` / `.Start`** | Apila todos los elementos al inicio del contenedor (por defecto). |
| **`Arrangement.Center`** | Centra todos los elementos juntos en el medio del contenedor. |
| **`Arrangement.SpaceBetween`** | Coloca el primer elemento al inicio, el último al final, y reparte el resto uniformemente (sin margen exterior). |
| **`Arrangement.SpaceAround`** | Reparte el espacio por igual alrededor de cada elemento (los bordes exteriores tienen la mitad de espacio que los huecos intermedios). |
| **`Arrangement.SpaceEvenly`** | El espacio entre elementos y los bordes exteriores es exactamente idéntico. |
| **`Arrangement.spacedBy(8.dp)`** | Aplica una separación fija exacta de `8.dp` entre cada elemento sucesivo. |

---

## 9. Componentes Interactivos Avanzados (Vistos en los Ejercicios)

### 9.1 RadioButtons y Grupos Seleccionables
Para crear selectores mutuamente excluyentes (ej. porcentaje de IVA o tipo de carne):
```kotlin
val opciones = listOf("Pollo", "Ternera")
var seleccionado by remember { mutableStateOf(opciones[0]) }

Column(modifier = Modifier.selectableGroup()) {
    opciones.forEach { opcion ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = (opcion == seleccionado),
                    onClick = { seleccionado = opcion }
                )
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = (opcion == seleccionado),
                onClick = { seleccionado = opcion }
            )
            Text(text = opcion, modifier = Modifier.padding(start = 8.dp))
        }
    }
}
```

### 9.2 Controles de Selección: `RadioButton` vs `Checkbox` vs `Switch`

> [!IMPORTANT]
> **Presencia de Controles en los Repositorios Oficiales del Profesor:**
> - En **`Ud2_Ejercicio1` (Calculadora de IVA)** se utilizan estrictamente:
>   1. **`OutlinedTextField`**: para la entrada numérica del importe base del servicio o producto.
>   2. **`RadioButton`**: para la selección excluyente del tipo de IVA (Superreducido 4%, Reducido 10%, General 21%).
>   3. **`Switch`**: para activar o desactivar la opción de redondeo del importe total.
> - El control **`Checkbox`** se reserva y evalúa en **`Ud4_Ejercicio` (Restaurante DAM)** para seleccionar ingredientes o complementos opcionales e independientes (como marcar si la hamburguesa lleva o no queso extra: `isQueso = true/false`).

#### Tabla Comparativa de Controles:

| Control | Tipo de Selección | Cuándo usarlo | Ejercicio Oficial de Referencia |
| :--- | :--- | :--- | :--- |
| **`RadioButton`** | **Excluyente única** (1 de N) | Cuando el usuario DEBE elegir exactamente una opción entre varias. | **`Ud2_Ejercicio1`** (IVA 4%, 10% o 21%) y **`Ud4_Ejercicio`** (Pizza o Hamburguesa; tamaño y tipo de pizza). |
| **`Switch`** | **Binario On/Off inmediato** (2 estados) | Para preferencias globales de activación instantánea. | **`Ud2_Ejercicio1`** (Redondear importe total) y **`Ud4_Ejercicio`** (tipo de masa). |
| **`Checkbox`** | **Múltiple independiente** (0 a N) | Cuando el usuario puede activar/desactivar opciones booleanas independientes. | **`Ud4_Ejercicio`** (Añadir queso a la hamburguesa: `isQueso`). |

#### Código Comparativo Integrado:

```kotlin
@Composable
fun EjemploControlesSeleccion() {
    var conQueso by remember { mutableStateOf(false) }
    var conBacon by remember { mutableStateOf(false) }
    var entregaRapida by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Personaliza tu hamburguesa:", style = MaterialTheme.typography.titleMedium)

        // 1. CHECKBOXES (Selección múltiple de ingredientes extra):
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = conQueso, onCheckedChange = { conQueso = it })
            Text("Añadir Queso (+1,00 €)")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = conBacon, onCheckedChange = { conBacon = it })
            Text("Añadir Bacon (+1,50 €)")
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // 2. SWITCH (Preferencia binaria directa):
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Servicio de Entrega Exprés")
            Switch(checked = entregaRapida, onCheckedChange = { entregaRapida = it })
        }
    }
}
```

### 9.3 Formateo Monetario con `NumberFormat`
Para evitar concatenar símbolos de moneda a mano (`precio + " €"`):
```kotlin
val precioConIva = 24.50
val formateador = NumberFormat.getCurrencyInstance() // Adapta al locale del dispositivo (ej. 24,50 € en España)
val textoPrecio = formateador.format(precioConIva)
```

### 9.4 Cuadros de Diálogo (`AlertDialog`)

Para cerrar la actividad desde el diálogo se usa `val activity = (LocalContext.current as? Activity)` y `activity?.finish()`:

```kotlin
val activity = (LocalContext.current as? Activity)

if (mostrarDialogo) {
    AlertDialog(
        onDismissRequest = { mostrarDialogo = false },
        title = { Text("¡Fin de la partida!") },
        text = { Text("Puntuación final: 50 puntos.") },
        confirmButton = {
            TextButton(onClick = { reiniciarPartida() }) {
                Text("Jugar de nuevo")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                // Cierre seguro de la Activity:
                activity?.finish()
            }) {
                Text("Salir")
            }
        }
    )
}
```

---

## 10. Localización e Internacionalización (i18n)

### A) Organización de Archivos de Cadenas según el Temario Oficial

> [!IMPORTANT]
> **Estructura oficial del PDF (`Android_Ud2_DIU.pdf`, Diapositivas 48-52):**
> En Android Studio, el directorio de recursos por defecto:
> - **`res/values/strings.xml`**: Contiene las cadenas del **idioma por defecto (inglés)**.
> - **`res/values-es/strings.xml`**: Contiene las cadenas traducidas al **español** (utilizando el código de idioma estándar **ISO 639-1** `es`).
> 
> Android detecta automáticamente el idioma configurado en el sistema operativo del teléfono. Si el teléfono está en español, cargará `values-es/`. Si está en francés, alemán o cualquier idioma no configurado, recurrirá al directorio base `values/` (*fallback* en inglés).

### B) Herramienta Translations Editor y Casilla `Untranslatable`

Android Studio incluye la herramienta visual **Translations Editor**:
- **Cómo abrirlo**: Clic derecho sobre el archivo `res/values/strings.xml` y seleccionar **Open Translations Editor**.
- **Agregar idiomas**: Pulsando el icono del globo terráqueo (*Add Locale*) se selecciona el idioma deseado (ej. Spanish: es) y Android Studio crea automáticamente la carpeta `values-es/strings.xml`.
- **Casilla `Untranslatable`**: Si se marca esta casilla en una cadena (o se añade `translatable="false"` en el XML), se le indica a Android Studio que dicho elemento **no debe traducirse a otros idiomas** (útil para nombres de marcas, identificadores técnicos o constantes numéricas), evitando advertencias y errores del compilador.

### C) Comparativa: Cadenas Hardcoded (Anti-patrón ❌) vs `stringResource` (Buenas Prácticas ✅)

```kotlin
// ==========================================================
// INCORRECTO ❌ (Hardcoded Strings):
// ==========================================================
// El texto está fijado en el código. Si un usuario tiene el móvil en inglés o francés,
// seguirá viendo el texto en español. Además complica las traducciones y los tests:
Text(text = "Total a pagar: 25 euros")


// ==========================================================
// CORRECTO ✅ (stringResource con soporte multi-idioma):
// ==========================================================
// 1. En res/values/strings.xml (Inglés - por defecto):
//    <string name="total_pagar">Total to pay: €%1$.2f</string>
//
// 2. En res/values-es/strings.xml (Español):
//    <string name="total_pagar">Total a pagar: %1$.2f €</string>
//
// 3. En código Compose:
val total = 25.0
Text(text = stringResource(R.string.total_pagar, total))
```

#### Manejo de Plurales en Android (`<plurals>`):
Evita mensajes anti-estéticos como *"Tienes 1 pedidos"* cuando solo hay uno:

```xml
<!-- En res/values-es/strings.xml -->
<plurals name="contador_elementos">
    <item quantity="one">Queda %1$d intento disponible</item>
    <item quantity="other">Quedan %1$d intentos disponibles</item>
</plurals>
```

En Compose se utiliza la función `pluralStringResource`:
```kotlin
val intentosRestantes = 1
Text(
    text = pluralStringResource(
        id = R.plurals.contador_elementos,
        count = intentosRestantes,
        intentosRestantes // Argumento formateador %1$d
    )
) // Imprime: "Queda 1 intento disponible"
```
