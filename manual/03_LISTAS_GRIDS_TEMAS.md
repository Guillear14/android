# Parte 3: Listas Diferidas, Cuadrículas y Temas Material 3
### 2º DAM — Programación Multimedia y Dispositivos Móviles (PMDM)

Este documento aborda la gestión eficiente de colecciones de datos en pantalla mediante listas diferidas (`LazyColumn` y `LazyRow`), cuadrículas adaptativas (`LazyVerticalGrid`) y la personalización completa del sistema de diseño Material Design 3.

---

## 1. Listas Diferidas: `LazyColumn` frente a `Column` con Scroll

En aplicaciones móviles el consumo de memoria RAM y la tasa de refresco (60 a 120 FPS) son factores críticos.

#### Tabla Comparativa de Rendimiento:

| Criterio de Comparación | `Column` con `Modifier.verticalScroll` | `LazyColumn` (Reciclaje Diferido) |
| :--- | :--- | :--- |
| **Instanciación de Nodos** | Crea **todos los elementos a la vez** en memoria, aunque no sean visibles. | **Solo instancia los elementos visibles** en pantalla (+ buffer mínimo). |
| **Consumo de Memoria (RAM)** | Crece linealmente con el tamaño de la lista: $O(N)$. | Constante y acotado: $O(\text{elementos en pantalla})$. |
| **Rendimiento con 1.000 ítems** | Provoca congelación de UI y cierre forzado por **`OutOfMemoryError`**. | **Scroll completamente fluido a 60/120 FPS** sin degradación. |
| **Analogía en Android Clásico** | `ScrollView` con `LinearLayout`. | `RecyclerView` con `ViewHolder`. |
| **Cuándo Utilizarlo** | Formularios estáticos o pantallas breves con altura conocida. | Colecciones dinámicas, feeds de datos, catálogos o listas de longitud indeterminada. |

#### Comparativa de Implementación:

```kotlin
@Composable
fun ListaBanderas(lista: List<Bandera>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Elemento estático individual (ej. cabecera)
        item {
            Text(
                text = "Países Disponibles",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )
        }

        // ==========================================================
        // DIFERENCIA CRÍTICA: items con o sin clave 'key'
        // ==========================================================
        // 1. SIN KEY: items(lista) { bandera -> ... }
        // Si la lista se reordena o se borra un elemento intermedio,
        // Compose no sabe qué elemento cambió y recompone toda la lista.

        // 2. CON KEY (Recomendado): Proporciona un ID único y estable
        // Compose preserva el estado interno de cada tarjeta (ej. si estaba expandida)
        // y ejecuta animaciones suaves de reordenación sin recomponer ítems intactos.
        items(
            items = lista,
            key = { bandera -> bandera.stringResourceId } // Clave primaria única
        ) { bandera ->
            TarjetaBandera(bandera = bandera)
        }
    }
}
```

---

## 2. Tarjetas y Tarjetas Expandibles (`Card`)

El componente `Card` agrupa contenido relacionado sobre una superficie elevada con sombra.

### 2.1 Tarjeta Básica de Elemento (`Ud3_Ejemplo1`)
En el temario oficial del profesor (`Android_Ud3_Listas.pdf`, Diapositivas 8 y 9), cada elemento se presenta mediante una `Card` con una `Row` interna donde el texto utiliza la tipografía oficial `MaterialTheme.typography.headlineSmall`:

```kotlin
@Composable
fun TarjetaBandera(bandera: Bandera, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(bandera.imagenResourceId),
                contentDescription = stringResource(bandera.stringResourceId)
            )
            Text(
                text = stringResource(bandera.stringResourceId),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineSmall // Estilo oficial del PDF
            )
        }
    }
}
```

---

### 2.2 Detección de Clics en la Lista y Logcat (`Ud3_Ejemplo2`)

#### A) Código Oficial del Repositorio (`Fpcarlosc/Ud3_Ejemplo2`):
En el repositorio original del examen, el profesor añade exclusivamente la detección del clic mediante **`Modifier.clickable`** en cada tarjeta para emitir un mensaje de nivel Verbose (`Log.v`) a la consola Logcat. En el repositorio oficial **NO hay Toasts ni variables de estado visual en pantalla**:

```kotlin
private const val TAG = "Ud3_Ejemplo2"

@Composable
fun ListaBanderas(
    lista: List<Bandera>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(lista) { bandera ->
            ItemBandera(bandera = bandera)
        }
    }
}

@Composable
fun ItemBandera(bandera: Bandera) {
    val context = LocalContext.current
    TarjetaBandera(
        bandera = bandera,
        modifier = Modifier
            .clickable {
                // Código exacto del repositorio oficial del profesor:
                val nombre = context.getString(bandera.stringResourceId)
                Log.v(TAG, "Bandera pulsada: $nombre (ID: ${bandera.stringResourceId})")
            }
            .padding(8.dp)
    )
}
```

#### B) Posible Ampliación de Examen (Toast y Feedback en Pantalla):
Si en el examen práctico se solicita notificar visualmente al usuario qué elemento ha seleccionado:
```kotlin
// Ampliación 1: Notificación emergente Toast
Toast.makeText(context, "Pulsaste: $nombre", Toast.LENGTH_SHORT).show()

// Ampliación 2: Estado visual retenido en pantalla
var ultimaBanderaPulsada by remember { mutableStateOf<String?>(null) }
// Dentro del clickable: ultimaBanderaPulsada = nombre
```

---

### 2.3 Tarjetas Expandibles con Animación (`Ud3_Ejemplo3`)

En este ejercicio se amplía el modelo de datos `Bandera` para incluir la capital del país, y se utiliza `Modifier.animateContentSize()` en la `Card` para mostrar u ocultar dicha información al pulsar un botón interactivo:

#### Modelo de Datos Oficial (`modelo/Bandera.kt` en `Ud3_Ejemplo3`):
```kotlin
data class Bandera(
    @StringRes val stringResourceId: Int,
    @DrawableRes val imagenResourceId: Int,
    @StringRes val capitalResourceId: Int // Tercera propiedad con el recurso string de la capital
)
```

#### Tarjeta Expandible (`TarjetaBanderaExpandible`):
```kotlin
@Composable
fun TarjetaBanderaExpandible(bandera: Bandera, modifier: Modifier = Modifier) {
    var abierto by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(), // Anima automáticamente la apertura suave
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(bandera.imagenResourceId),
                    contentDescription = stringResource(bandera.stringResourceId),
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = stringResource(bandera.stringResourceId),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(start = 16.dp).weight(1f)
                )
                IconButton(onClick = { abierto = !abierto }) {
                    Icon(
                        painter = if (abierto) painterResource(R.drawable.expand_circle_up) 
                                  else painterResource(R.drawable.expand_circle_down),
                        contentDescription = "Expandir información"
                    )
                }
            }

            // Muestra la capital del país resolviendo el recurso @StringRes oficial:
            if (abierto) {
                Text(
                    text = "Capital: ${stringResource(bandera.capitalResourceId)}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
```

---

## 3. Cuadrículas: `LazyVerticalGrid`

Permiten presentar elementos en varias columnas. El parámetro `columns` admite dos estrategias:

#### Tabla Comparativa: `GridCells.Fixed` vs `GridCells.Adaptive`

| Dispositivo / Orientación | `GridCells.Fixed(count = 2)` | `GridCells.Adaptive(minSize = 128.dp)` *(PDF Ud3)* |
| :--- | :--- | :--- |
| **Móvil en Vertical (Portrait)** | Muestra exactamente **2 columnas**. | Muestra **2 o 3 columnas** (si el ancho es ~360dp-400dp). |
| **Móvil en Horizontal (Landscape)** | Sigue forzando **2 columnas muy estiradas y desproporcionadas**. | Calcula el nuevo ancho (~800dp) y genera automáticamente **6 columnas equilibradas**. |
| **Tablet de 10" o Plegable desplegado** | Sigue forzando **2 columnas gigantescas**. | Se adapta fluidamente creando **8 o más columnas**. |
| **Veredicto de Diseño Responsive** | Rígido; adecuado solo si el diseño exige estrictamente 2 columnas fijas. | **Idóneo para aplicaciones universales y adaptativas** sin escribir código condicional. |

#### Comparativa de Implementación:

```kotlin
// ==========================================================
// 1. COLUMNAS FIJAS: GridCells.Fixed
// ==========================================================
LazyVerticalGrid(
    columns = GridCells.Fixed(count = 2), // Siempre 2 columnas fijas
    verticalArrangement = Arrangement.spacedBy(8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    items(listaBanderas) { bandera ->
        TarjetaGrid(bandera = bandera)
    }
}

// ==========================================================
// 2. COLUMNAS RESPONSIVAS: GridCells.Adaptive (minSize = 128.dp según PDF)
// ==========================================================
LazyVerticalGrid(
    columns = GridCells.Adaptive(minSize = 128.dp), // Calcula columnas según el espacio disponible
    verticalArrangement = Arrangement.spacedBy(8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    items(listaBanderas) { bandera ->
        TarjetaGrid(bandera = bandera)
    }
}
```

---

## 4. Sistema de Diseño Material 3 (M3)

El paquete `ui.theme/` contiene los archivos que definen la estética de la app:

### 4.1 Colores: Tema Claro (`LightColorScheme`) vs Tema Oscuro (`DarkColorScheme`)

Material 3 utiliza una paleta tonal basada en tres roles principales:
- **`primary`**: Color predominante para botones clave y barras de aplicación.
- **`secondary`**: Color de contraste para elementos menos destacados.
- **`tertiary`**: Color de equilibrio para acentuar detalles o llamar la atención del usuario.
- **`surface` / `surfaceVariant`**: Fondos de tarjetas y contenedores.
- **`error` / `errorContainer`**: Para avisos, advertencias y fallos.

> [!TIP]
> **Recomendación oficial del profesor (`Android_Ud3_Listas.pdf`, Diapositiva 14):**
> El profesor recomienda utilizar la herramienta oficial **Material Theme Builder** de Google (en la web o plugin).
> Permite seleccionar una paleta de colores y pulsar el botón de exportación eligiendo la opción **Jetpack Compose (Theme.kt)**.
> La descarga genera un archivo comprimido con dos ficheros listos: `Theme.kt` y `Color.kt`, con los que se actualizan directamente los archivos de nuestro paquete `ui.theme/`.

#### Conmutación Automática en `Theme.kt`:
Compose consulta el ajuste del sistema del dispositivo mediante `isSystemInDarkTheme()` para alternar de forma transparente:

```kotlin
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    secondary = Color(0xFFCCC2DC),
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF1C1B1F)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6650a4),
    secondary = Color(0xFF625b71),
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE)
)

@Composable
fun MiAplicacionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Detecta si el modo oscuro está activo en el móvil
    content: @Composable () -> Unit
) {
    // Selección dinámica de la paleta según el entorno:
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
```

### 4.2 Formas (`ui.theme/Shape.kt`): Redondeo y Recortes

En Compose, la mayoría de las formas (*Shapes*) se basan en redondear las esquinas de los elementos utilizando la clase `RoundedCornerShape(dp)`. Se pueden redondear las 4 esquinas por igual o cada una de forma independiente con `topStart`, `topEnd`, `bottomEnd` y `bottomStart`.

Además, Compose incluye dos formas predefinidas: **`RectangleShape`** y **`CircleShape`**.

#### Definición exacta del PDF oficial (`Android_Ud3_Listas.pdf`, Diapositiva 17):

```kotlin
// ui.theme/Shape.kt
val Shapes = Shapes(
    small = RoundedCornerShape(50.dp),
    medium = RoundedCornerShape(bottomStart = 50.dp, topEnd = 8.dp)
)
```

> [!IMPORTANT]
> **Advertencia Crítica de Examen (`Android_Ud3_Listas.pdf`):**
> En el PDF del profesor, el fichero se crea como `Shape.kt` con la variable:
> ```kotlin
> val Shapes = Shapes(...)
> ```
> La plantilla estándar de Android Studio genera `Theme.kt` invocando a `MaterialTheme(...)` solo con `colorScheme` y `typography`, **omitiendo `shapes`**. Es **estrictamente obligatorio pasar manualmente `shapes = Shapes`** dentro de `MaterialTheme(...)` en `ui.theme/Theme.kt`:
> ```kotlin
> MaterialTheme(
>     colorScheme = colorScheme,
>     typography = Typography,
>     shapes = Shapes, // ¡OBLIGATORIO para que se apliquen las formas de Shape.kt!
>     content = content
> )
> ```
> Si se omite este parámetro, Jetpack Compose utilizará las formas rectangulares por defecto del framework y ninguna forma personalizada de `Shape.kt` se aplicará en la aplicación.

#### Aplicación de Shapes a Componentes en Compose (PDF Diapositiva 18):

```kotlin
// 1. Recortar una imagen con una forma del tema (shapes.small):
Image(
    painter = painterResource(id = R.drawable.bandera1),
    contentDescription = null, // Imagen puramente decorativa
    modifier = Modifier
        .size(64.dp)
        .clip(MaterialTheme.shapes.small) // Aplica el redondeo de 50.dp
)

// 2. Recortar una imagen con la silueta predefinida CircleShape:
Image(
    painter = painterResource(id = R.drawable.avatar),
    contentDescription = "Avatar de usuario",
    modifier = Modifier
        .size(64.dp)
        .clip(CircleShape) // Silueta circular perfecta
)

// 3. Forzar a una Card a ser estrictamente rectangular sin bordes curvos:
Card(
    shape = RectangleShape, // Silueta predefinida rectangular
    modifier = Modifier.fillMaxWidth().padding(8.dp)
) {
    Text(text = "Tarjeta con bordes rectos", modifier = Modifier.padding(16.dp))
}
```

> [!NOTE]
> *(Nota extra: Clases como `CutCornerShape` permiten crear esquinas biseladas/cortadas en ángulo recto, pero no forman parte del temario evaluable del PDF oficial de la Unidad 3).*

### 4.3 Tipografías y Fuentes Personalizadas (`ui.theme/Type.kt`)
Material Design 3 define una escala tipográfica (`displayLarge`, `headlineMedium`, `titleSmall`, `bodyLarge`, `labelSmall`).

Para usar una fuente externa:
1. Crear el directorio de recursos `app/src/main/res/font/`.
2. Pegar los archivos de fuente en minúsculas y sin caracteres especiales (ej. `roboto_regular.ttf`, `roboto_bold.ttf`).
3. Definir la familia de fuentes en `Type.kt`:
   ```kotlin
   val RobotoFamily = FontFamily(
       Font(R.font.roboto_regular, FontWeight.Normal),
       Font(R.font.roboto_bold, FontWeight.Bold)
   )

   val Typography = Typography(
       headlineLarge = TextStyle(
           fontFamily = RobotoFamily,
           fontWeight = FontWeight.Bold,
           fontSize = 32.sp
       )
   )
   ```
