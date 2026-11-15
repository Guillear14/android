# Material PMDM para repaso técnico — Android y Kotlin

> Guion de referencia para generar un podcast de repaso. Los proyectos son independientes: cada carpeta contiene una app Gradle distinta. Se conserva el código fuente íntegro de los archivos incluidos.

## Índice general

### Unidad 2 — Formularios y estado
- **Práctica 1 — Estados y formularios: cálculo de IVA.** Formulario declarativo para precio, tipo impositivo y total.
  Conceptos: campos controlados, selección, conversión segura y cálculo en Compose.

### Unidad 3 — Listas y componentes
- **Práctica 2 — Lista de banderas.** Modelo y datos separados de la representación en lista.
  Conceptos: `LazyColumn`, recursos, tarjetas y composición reutilizable.
- **Práctica 2 — Interacción y expansión.** Dos variantes incrementales: callbacks de clic y tarjetas con información condicional.
  Conceptos: eventos, elevación del estado, lambdas y composición condicional.

### Unidad 4 — Arquitectura y MVVM
- **Ciclo de vida.** Ejemplo para observar los callbacks de una Activity.
  Conceptos: estados de Activity, `onCreate` y Logcat.
- **Calculadora reactiva.** ViewModel mantiene operandos/resultado y Compose representa los cambios.
  Conceptos: MVVM, `StateFlow`, estado inmutable y flujo unidireccional.
- **Juego del ahorcado.** Lógica de rondas y puntuación separada de la pantalla.
  Conceptos: ViewModel, estado de UI, eventos y diálogo final.
- **Pedidos de restaurante.** Formulario conectado con modelos especializados de producto.
  Conceptos: POO, herencia, modelos y publicación de estado.
- **Gestor de tareas.** Edición/inserción de tareas y opciones de módulo.
  Conceptos: formularios, `copy`, ViewModel y estado reactivo.

### Ejercicio adicional
- **Gestor de módulos.** Formulario para describir y priorizar módulos del curso.
  Conceptos: modelos Kotlin, estado de UI y ViewModel.

### Contexto de proyecto y documentación
- Archivos `app/build.gradle.kts`: plugins, SDK y dependencias de cada app independiente.
  Se incluyen los diez archivos presentes; cada ficha identifica el proyecto al que aplica.
- `README.md`: orientación general del repositorio y explicación del ejercicio de tareas.
  Se incluyen íntegros; no se encontraron archivos con nombre de enunciado de práctica.
- Manifiestos: revisados y omitidos según el criterio solicitado; solo declaran una Activity lanzadora, sin permisos ni servicios.

## Práctica 1 — Estados y formularios: cálculo de IVA

### Práctica 1 — Estados y formularios: cálculo de IVA

Formulario declarativo para introducir un precio, elegir un porcentaje y mostrar el total calculado.
Repasa estado en Compose, entrada de texto, selección y cálculo derivado.

#### `Ud2_Ejercicio1-master/app/src/main/java/com/example/ud2_ejercicio1/MainActivity.kt`

> Qué hace este archivo: Activity de entrada que instala el tema Compose y presenta la pantalla de cálculo de IVA.
>
> Conceptos clave de Android/Kotlin que aplica: Activity de Android, `onCreate`, `setContent` y composición de la interfaz.

```kotlin
package com.example.ud2_ejercicio1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ud2_ejercicio1.ui.theme.Ud2_Ejercicio1Theme
import java.text.NumberFormat

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

private fun calcularIva(precio: Double, ivaRadio: Double): String {
    val iva = precio + (ivaRadio / 100 * precio)

    // Devolvemos el precio final en la moneda del país en el que está configurado el dispositivo.
    return NumberFormat.getCurrencyInstance().format(iva)
}
```

#### `Ud2_Ejercicio1-master/app/src/main/java/com/example/ud2_ejercicio1/ui/theme/Color.kt`

> Qué hace este archivo: Declara los colores usados por los esquemas del tema de Material 3.
>
> Conceptos clave de Android/Kotlin que aplica: Constantes Kotlin, `Color` y sistemas de color.

```kotlin
package com.example.ud2_ejercicio1.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
```

#### `Ud2_Ejercicio1-master/app/src/main/java/com/example/ud2_ejercicio1/ui/theme/Theme.kt`

> Qué hace este archivo: Configura tema claro/oscuro y aplica el esquema de Material 3 a sus descendientes.
>
> Conceptos clave de Android/Kotlin que aplica: Composable de tema, `MaterialTheme`, tema del sistema y color dinámico.

```kotlin
package com.example.ud2_ejercicio1.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun Ud2_Ejercicio1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

#### `Ud2_Ejercicio1-master/app/src/main/java/com/example/ud2_ejercicio1/ui/theme/Type.kt`

> Qué hace este archivo: Define la tipografía Material 3 que utiliza la aplicación.
>
> Conceptos clave de Android/Kotlin que aplica: `Typography`, `TextStyle`, familia, peso y tamaño tipográfico.

```kotlin
package com.example.ud2_ejercicio1.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
```

#### `Ud2_Ejercicio1-master/app/build.gradle.kts`

> Qué hace este archivo: Configura plugins, SDK, opciones de compilación y dependencias de esta aplicación Android.
>
> Conceptos clave de Android/Kotlin que aplica: Gradle Kotlin DSL, Android Gradle Plugin, Compose y dependencias declaradas.

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.ud2_ejercicio1"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.ud2_ejercicio1"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
```

## Práctica 2 — Listas y componentes

### Práctica 2 — Listas: catálogo de banderas

Presenta una colección de banderas con un modelo que referencia recursos de Android.
Repasa `LazyColumn`, listas de datos, imágenes locales y tarjetas de Compose.

#### `Ud3_Ejemplo1-master/app/src/main/java/com/example/ud3_ejemplo1/datos/Datos.kt`

> Qué hace este archivo: Proporciona la colección inicial de banderas que alimenta la interfaz.
>
> Conceptos clave de Android/Kotlin que aplica: Capa de datos sencilla, colecciones Kotlin y referencias a recursos.

```kotlin
package com.example.ud3_ejemplo1.datos

import com.example.ud3_ejemplo1.R
import com.example.ud3_ejemplo1.modelo.Bandera

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

#### `Ud3_Ejemplo1-master/app/src/main/java/com/example/ud3_ejemplo1/MainActivity.kt`

> Qué hace este archivo: Punto de entrada y pantalla de lista; compone la lista de banderas a partir de los datos del proyecto.
>
> Conceptos clave de Android/Kotlin que aplica: Activity de Android, `onCreate`, `setContent` y composición de la interfaz.

```kotlin
package com.example.ud3_ejemplo1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ud3_ejemplo1.datos.Datos
import com.example.ud3_ejemplo1.modelo.Bandera
import com.example.ud3_ejemplo1.ui.theme.Ud3_Ejemplo1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ud3_Ejemplo1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Ud3Ejemplo1App(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

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

#### `Ud3_Ejemplo1-master/app/src/main/java/com/example/ud3_ejemplo1/modelo/Bandera.kt`

> Qué hace este archivo: Modelo de una bandera con referencias tipadas a recursos de imagen y texto.
>
> Conceptos clave de Android/Kotlin que aplica: Data class, `@DrawableRes` y `@StringRes`.

```kotlin
package com.example.ud3_ejemplo1.modelo

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Bandera(
    // Necesitamos dos ids, uno para el texto de la bandera y otro para la imagen
    @StringRes val stringResourceId: Int,
    @DrawableRes val imagenResourceId: Int
)
```

#### `Ud3_Ejemplo1-master/app/src/main/java/com/example/ud3_ejemplo1/ui/theme/Color.kt`

> Qué hace este archivo: Declara los colores usados por los esquemas del tema de Material 3.
>
> Conceptos clave de Android/Kotlin que aplica: Constantes Kotlin, `Color` y sistemas de color.

```kotlin
package com.example.ud3_ejemplo1.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
```

#### `Ud3_Ejemplo1-master/app/src/main/java/com/example/ud3_ejemplo1/ui/theme/Theme.kt`

> Qué hace este archivo: Configura tema claro/oscuro y aplica el esquema de Material 3 a sus descendientes.
>
> Conceptos clave de Android/Kotlin que aplica: Composable de tema, `MaterialTheme`, tema del sistema y color dinámico.

```kotlin
package com.example.ud3_ejemplo1.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun Ud3_Ejemplo1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

#### `Ud3_Ejemplo1-master/app/src/main/java/com/example/ud3_ejemplo1/ui/theme/Type.kt`

> Qué hace este archivo: Define la tipografía Material 3 que utiliza la aplicación.
>
> Conceptos clave de Android/Kotlin que aplica: `Typography`, `TextStyle`, familia, peso y tamaño tipográfico.

```kotlin
package com.example.ud3_ejemplo1.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
```

#### `Ud3_Ejemplo1-master/app/build.gradle.kts`

> Qué hace este archivo: Configura plugins, SDK, opciones de compilación y dependencias de esta aplicación Android.
>
> Conceptos clave de Android/Kotlin que aplica: Gradle Kotlin DSL, Android Gradle Plugin, Compose y dependencias declaradas.

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.ud3_ejemplo1"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.ud3_ejemplo1"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
```

### Práctica 2 — Listas: interacción y callbacks

Extiende la lista de banderas para responder a la selección de elementos.
Repasa eventos, callbacks tipados y separación entre el elemento y su contenedor.

#### `Ud3_Ejemplo2-master/app/src/main/java/com/example/ud3_ejemplo2/datos/Datos.kt`

> Qué hace este archivo: Carga los datos de banderas usados por el ejemplo de lista clicable.
>
> Conceptos clave de Android/Kotlin que aplica: Separación entre datos y UI, colecciones Kotlin y recursos.

```kotlin
package com.example.ud3_ejemplo2.datos

import com.example.ud3_ejemplo2.R
import com.example.ud3_ejemplo2.modelo.Bandera

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

#### `Ud3_Ejemplo2-master/app/src/main/java/com/example/ud3_ejemplo2/MainActivity.kt`

> Qué hace este archivo: Pantalla de la lista interactiva; conecta los datos de banderas con callbacks de selección.
>
> Conceptos clave de Android/Kotlin que aplica: Activity de Android, `onCreate`, `setContent` y composición de la interfaz.

```kotlin
package com.example.ud3_ejemplo2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ud3_ejemplo2.datos.Datos
import com.example.ud3_ejemplo2.modelo.Bandera
import com.example.ud3_ejemplo2.ui.theme.Ud3_Ejemplo2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ud3_Ejemplo2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Ud3Ejemplo2App(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Función que carga los datos y llama a la función ListaBanderas para crear la lista
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

@Composable
fun ItemBandera(bandera: Bandera, onItemClick: (Bandera) -> Unit) {
    TarjetaBandera(
        bandera = bandera,
        modifier = Modifier
            .clickable { onItemClick(bandera) }
            .padding(8.dp)
    )
}


// Función que dibuja la tarjeta de la bandera
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

#### `Ud3_Ejemplo2-master/app/src/main/java/com/example/ud3_ejemplo2/modelo/Bandera.kt`

> Qué hace este archivo: Modelo de una bandera con referencias tipadas a recursos de imagen y texto.
>
> Conceptos clave de Android/Kotlin que aplica: Data class, `@DrawableRes` y `@StringRes`.

```kotlin
package com.example.ud3_ejemplo2.modelo

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Bandera(
    // Necesitamos dos ids, uno para el texto de la bandera y otro para la imagen
    @StringRes val stringResourceId: Int,
    @DrawableRes val imagenResourceId: Int
)
```

#### `Ud3_Ejemplo2-master/app/src/main/java/com/example/ud3_ejemplo2/ui/theme/Color.kt`

> Qué hace este archivo: Declara los colores usados por los esquemas del tema de Material 3.
>
> Conceptos clave de Android/Kotlin que aplica: Constantes Kotlin, `Color` y sistemas de color.

```kotlin
package com.example.ud3_ejemplo2.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
```

#### `Ud3_Ejemplo2-master/app/src/main/java/com/example/ud3_ejemplo2/ui/theme/Theme.kt`

> Qué hace este archivo: Configura tema claro/oscuro y aplica el esquema de Material 3 a sus descendientes.
>
> Conceptos clave de Android/Kotlin que aplica: Composable de tema, `MaterialTheme`, tema del sistema y color dinámico.

```kotlin
package com.example.ud3_ejemplo2.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun Ud3_Ejemplo2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

#### `Ud3_Ejemplo2-master/app/src/main/java/com/example/ud3_ejemplo2/ui/theme/Type.kt`

> Qué hace este archivo: Define la tipografía Material 3 que utiliza la aplicación.
>
> Conceptos clave de Android/Kotlin que aplica: `Typography`, `TextStyle`, familia, peso y tamaño tipográfico.

```kotlin
package com.example.ud3_ejemplo2.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
```

#### `Ud3_Ejemplo2-master/app/build.gradle.kts`

> Qué hace este archivo: Configura plugins, SDK, opciones de compilación y dependencias de esta aplicación Android.
>
> Conceptos clave de Android/Kotlin que aplica: Gradle Kotlin DSL, Android Gradle Plugin, Compose y dependencias declaradas.

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.ud3_ejemplo2"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.ud3_ejemplo2"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
```

### Práctica 2 — Listas: tarjetas expandibles

Añade información expandible a los elementos de la lista de banderas.
Repasa estado local, contenido condicional y composición de componentes reutilizables.

#### `Ud3_Ejemplo3-master/app/src/main/java/com/example/ud3_ejemplo3/datos/Datos.kt`

> Qué hace este archivo: Carga los datos de banderas que se muestran en tarjetas expandibles.
>
> Conceptos clave de Android/Kotlin que aplica: Separación entre datos y UI, colecciones Kotlin y recursos.

```kotlin
package com.example.ud3_ejemplo3.datos

import com.example.ud3_ejemplo3.R
import com.example.ud3_ejemplo3.modelo.Bandera

class Datos {
    fun cargarBanderas(): List<Bandera> = listOf(
        Bandera(R.string.bandera1, R.drawable.bandera1, R.string.capital1),
        Bandera(R.string.bandera2, R.drawable.bandera2, R.string.capital2),
        Bandera(R.string.bandera3, R.drawable.bandera3, R.string.capital3),
        Bandera(R.string.bandera4, R.drawable.bandera4, R.string.capital4),
        Bandera(R.string.bandera5, R.drawable.bandera5, R.string.capital5)
    )
}
```

#### `Ud3_Ejemplo3-master/app/src/main/java/com/example/ud3_ejemplo3/MainActivity.kt`

> Qué hace este archivo: Pantalla de lista expandible; reúne lista, tarjetas y presentación condicional de información.
>
> Conceptos clave de Android/Kotlin que aplica: Activity de Android, `onCreate`, `setContent` y composición de la interfaz.

```kotlin
package com.example.ud3_ejemplo3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ud3_ejemplo3.datos.Datos
import com.example.ud3_ejemplo3.modelo.Bandera
import com.example.ud3_ejemplo3.ui.theme.Ud3_Ejemplo3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ud3_Ejemplo3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Ud3Ejemplo3App(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Ud3Ejemplo3App(modifier: Modifier = Modifier) {
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

#### `Ud3_Ejemplo3-master/app/src/main/java/com/example/ud3_ejemplo3/modelo/Bandera.kt`

> Qué hace este archivo: Modelo de una bandera con referencias tipadas a recursos de imagen y texto.
>
> Conceptos clave de Android/Kotlin que aplica: Data class, `@DrawableRes` y `@StringRes`.

```kotlin
package com.example.ud3_ejemplo3.modelo

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Bandera(
    // Necesitamos dos ids, uno para el texto de la bandera y otro para la imagen
    @StringRes val stringResourceId: Int,
    @DrawableRes val imagenResourceId: Int,
    @StringRes val capital: Int
)
```

#### `Ud3_Ejemplo3-master/app/src/main/java/com/example/ud3_ejemplo3/ui/theme/Color.kt`

> Qué hace este archivo: Declara los colores usados por los esquemas del tema de Material 3.
>
> Conceptos clave de Android/Kotlin que aplica: Constantes Kotlin, `Color` y sistemas de color.

```kotlin
package com.example.ud3_ejemplo3.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
```

#### `Ud3_Ejemplo3-master/app/src/main/java/com/example/ud3_ejemplo3/ui/theme/Theme.kt`

> Qué hace este archivo: Configura tema claro/oscuro y aplica el esquema de Material 3 a sus descendientes.
>
> Conceptos clave de Android/Kotlin que aplica: Composable de tema, `MaterialTheme`, tema del sistema y color dinámico.

```kotlin
package com.example.ud3_ejemplo3.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun Ud3_Ejemplo3Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

#### `Ud3_Ejemplo3-master/app/src/main/java/com/example/ud3_ejemplo3/ui/theme/Type.kt`

> Qué hace este archivo: Define la tipografía Material 3 que utiliza la aplicación.
>
> Conceptos clave de Android/Kotlin que aplica: `Typography`, `TextStyle`, familia, peso y tamaño tipográfico.

```kotlin
package com.example.ud3_ejemplo3.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
```

#### `Ud3_Ejemplo3-master/app/build.gradle.kts`

> Qué hace este archivo: Configura plugins, SDK, opciones de compilación y dependencias de esta aplicación Android.
>
> Conceptos clave de Android/Kotlin que aplica: Gradle Kotlin DSL, Android Gradle Plugin, Compose y dependencias declaradas.

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.ud3_ejemplo3"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.ud3_ejemplo3"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
```

## Práctica 3 — Arquitectura y MVVM

### Práctica 3 — Arquitectura: ciclo de vida

Registra los callbacks principales del ciclo de vida de una Activity.
Repasa la secuencia de estados y el uso de Logcat para observar transiciones.

#### `Ud4_Ejemplo1-master/app/src/main/java/com/example/ud4_ejemplo1/MainActivity.kt`

> Qué hace este archivo: Activity que registra callbacks del ciclo de vida y muestra una interfaz Compose sencilla.
>
> Conceptos clave de Android/Kotlin que aplica: Activity de Android, `onCreate`, `setContent` y composición de la interfaz.

```kotlin
package com.example.ud4_ejemplo1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ud4_ejemplo1.ui.theme.Ud4_Ejemplo1Theme

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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
```

#### `Ud4_Ejemplo1-master/app/src/main/java/com/example/ud4_ejemplo1/ui/theme/Color.kt`

> Qué hace este archivo: Declara los colores usados por los esquemas del tema de Material 3.
>
> Conceptos clave de Android/Kotlin que aplica: Constantes Kotlin, `Color` y sistemas de color.

```kotlin
package com.example.ud4_ejemplo1.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
```

#### `Ud4_Ejemplo1-master/app/src/main/java/com/example/ud4_ejemplo1/ui/theme/Theme.kt`

> Qué hace este archivo: Configura tema claro/oscuro y aplica el esquema de Material 3 a sus descendientes.
>
> Conceptos clave de Android/Kotlin que aplica: Composable de tema, `MaterialTheme`, tema del sistema y color dinámico.

```kotlin
package com.example.ud4_ejemplo1.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun Ud4_Ejemplo1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

#### `Ud4_Ejemplo1-master/app/src/main/java/com/example/ud4_ejemplo1/ui/theme/Type.kt`

> Qué hace este archivo: Define la tipografía Material 3 que utiliza la aplicación.
>
> Conceptos clave de Android/Kotlin que aplica: `Typography`, `TextStyle`, familia, peso y tamaño tipográfico.

```kotlin
package com.example.ud4_ejemplo1.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
```

#### `Ud4_Ejemplo1-master/app/build.gradle.kts`

> Qué hace este archivo: Configura plugins, SDK, opciones de compilación y dependencias de esta aplicación Android.
>
> Conceptos clave de Android/Kotlin que aplica: Gradle Kotlin DSL, Android Gradle Plugin, Compose y dependencias declaradas.

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.ud4_ejemplo1"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.ud4_ejemplo1"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
```

### Práctica 3 — Arquitectura: calculadora y StateFlow

Calculadora cuyo estado de interfaz y resultado se mantienen en un ViewModel.
Repasa flujo unidireccional de datos, `StateFlow` y observación reactiva desde Compose.

#### `Ud4_Ejemplo2-master/app/src/main/java/com/example/ud4_ejemplo2/MainActivity.kt`

> Qué hace este archivo: Conecta la Activity, la pantalla de calculadora y el ViewModel que conserva los operandos y el resultado.
>
> Conceptos clave de Android/Kotlin que aplica: Activity de Android, `onCreate`, `setContent` y composición de la interfaz.

```kotlin
package com.example.ud4_ejemplo2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ud4_ejemplo2.ui.OperacionViewModel
import com.example.ud4_ejemplo2.ui.theme.Ud4_Ejemplo2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ud4_Ejemplo2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Calculadora(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

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

#### `Ud4_Ejemplo2-master/app/src/main/java/com/example/ud4_ejemplo2/modelo/OperacionUIState.kt`

> Qué hace este archivo: Agrupa los operandos, la operación y el resultado que necesita la pantalla.
>
> Conceptos clave de Android/Kotlin que aplica: Data class como estado de UI inmutable.

```kotlin
package com.example.ud4_ejemplo2.modelo

data class OperacionUIState(
    val numero1: String = "",
    val numero2: String = "",
    val resultado: String = ""
)
```

#### `Ud4_Ejemplo2-master/app/src/main/java/com/example/ud4_ejemplo2/ui/OperacionViewModel.kt`

> Qué hace este archivo: Actualiza operandos y calcula el resultado, publicando el estado por un flujo observable.
>
> Conceptos clave de Android/Kotlin que aplica: `ViewModel`, `MutableStateFlow`, `StateFlow`, `update` y conversión segura.

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

#### `Ud4_Ejemplo2-master/app/src/main/java/com/example/ud4_ejemplo2/ui/theme/Color.kt`

> Qué hace este archivo: Declara los colores usados por los esquemas del tema de Material 3.
>
> Conceptos clave de Android/Kotlin que aplica: Constantes Kotlin, `Color` y sistemas de color.

```kotlin
package com.example.ud4_ejemplo2.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
```

#### `Ud4_Ejemplo2-master/app/src/main/java/com/example/ud4_ejemplo2/ui/theme/Theme.kt`

> Qué hace este archivo: Configura tema claro/oscuro y aplica el esquema de Material 3 a sus descendientes.
>
> Conceptos clave de Android/Kotlin que aplica: Composable de tema, `MaterialTheme`, tema del sistema y color dinámico.

```kotlin
package com.example.ud4_ejemplo2.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun Ud4_Ejemplo2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

#### `Ud4_Ejemplo2-master/app/src/main/java/com/example/ud4_ejemplo2/ui/theme/Type.kt`

> Qué hace este archivo: Define la tipografía Material 3 que utiliza la aplicación.
>
> Conceptos clave de Android/Kotlin que aplica: `Typography`, `TextStyle`, familia, peso y tamaño tipográfico.

```kotlin
package com.example.ud4_ejemplo2.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
```

#### `Ud4_Ejemplo2-master/app/build.gradle.kts`

> Qué hace este archivo: Configura plugins, SDK, opciones de compilación y dependencias de esta aplicación Android.
>
> Conceptos clave de Android/Kotlin que aplica: Gradle Kotlin DSL, Android Gradle Plugin, Compose y dependencias declaradas.

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.ud4_ejemplo2"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.ud4_ejemplo2"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.viewmodel)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
```

### Práctica 3 — Arquitectura: juego del ahorcado

Juego de varias rondas coordinado por un ViewModel y representado con Compose.
Repasa estado inmutable, lógica de juego, puntuación y diálogo de finalización.

#### `Ud4_Ejemplo3-master/app/src/main/java/com/example/ud4_ejemplo3/datos/Datos.kt`

> Qué hace este archivo: Proporciona la colección inicial de banderas que alimenta la interfaz.
>
> Conceptos clave de Android/Kotlin que aplica: Capa de datos sencilla, colecciones Kotlin y referencias a recursos.

```kotlin
package com.example.ud4_ejemplo3.datos

const val RONDAS = 2
const val PUNTOS_POR_ACIERTO = 5

val listaPalabras: Set<String> =
    setOf(
        "PIKACHU",
        "NARUTO",
        "ICHIGO",
        "SAITAMA",
        "LUFFY",
        "GOKU",
        "TSUBASA",
        "GUTS",
        "KINNIKUMAN",
        "KANEDA"
    )
```

#### `Ud4_Ejemplo3-master/app/src/main/java/com/example/ud4_ejemplo3/MainActivity.kt`

> Qué hace este archivo: Obtiene el ViewModel y enlaza su estado y eventos con la pantalla del juego.
>
> Conceptos clave de Android/Kotlin que aplica: Activity de Android, `onCreate`, `setContent` y composición de la interfaz.

```kotlin
package com.example.ud4_ejemplo3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ud4_ejemplo3.ui.theme.Ud4_Ejemplo3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ud4_Ejemplo3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaJuego(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
```

#### `Ud4_Ejemplo3-master/app/src/main/java/com/example/ud4_ejemplo3/modelo/JuegoUIState.kt`

> Qué hace este archivo: Representa los datos de la partida necesarios para dibujar y controlar el juego.
>
> Conceptos clave de Android/Kotlin que aplica: Data class y estado de UI.

```kotlin
package com.example.ud4_ejemplo3.modelo

data class JuegoUIState (
    val palabraActual: String = "",
    val isLetraIncorrecta: Boolean = false,
    val puntos: Int = 0,
    val ronda: Int = 1,
    val isFinJuego: Boolean = false
)
```

#### `Ud4_Ejemplo3-master/app/src/main/java/com/example/ud4_ejemplo3/PantallaJuego.kt`

> Qué hace este archivo: Compone la pantalla completa del juego, sus puntuaciones, rondas y diálogo final.
>
> Conceptos clave de Android/Kotlin que aplica: Composición por componentes, estado y diálogos de Material/Compose.

```kotlin
package com.example.ud4_ejemplo3

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ud4_ejemplo3.datos.RONDAS
import com.example.ud4_ejemplo3.ui.JuegoViewModel

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

@Composable
fun PuntuacionJuego(
    puntos: Int,
    modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
    ) {
        Text (
            text = stringResource(R.string.puntuacion, puntos),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun NumeroRondasJuego(
    modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
    ) {
        Text (
            text = stringResource(R.string.rondas, RONDAS),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(8.dp)
        )
    }
}

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

#### `Ud4_Ejemplo3-master/app/src/main/java/com/example/ud4_ejemplo3/ui/JuegoViewModel.kt`

> Qué hace este archivo: Controla rondas, palabra, letras, aciertos, puntuación y finalización.
>
> Conceptos clave de Android/Kotlin que aplica: `ViewModel`, `StateFlow`, actualizaciones de estado y lógica de dominio.

```kotlin
package com.example.ud4_ejemplo3.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ud4_ejemplo3.datos.PUNTOS_POR_ACIERTO
import com.example.ud4_ejemplo3.datos.RONDAS
import com.example.ud4_ejemplo3.datos.listaPalabras
import com.example.ud4_ejemplo3.modelo.JuegoUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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

#### `Ud4_Ejemplo3-master/app/src/main/java/com/example/ud4_ejemplo3/ui/theme/Color.kt`

> Qué hace este archivo: Declara los colores usados por los esquemas del tema de Material 3.
>
> Conceptos clave de Android/Kotlin que aplica: Constantes Kotlin, `Color` y sistemas de color.

```kotlin
package com.example.ud4_ejemplo3.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
```

#### `Ud4_Ejemplo3-master/app/src/main/java/com/example/ud4_ejemplo3/ui/theme/Theme.kt`

> Qué hace este archivo: Configura tema claro/oscuro y aplica el esquema de Material 3 a sus descendientes.
>
> Conceptos clave de Android/Kotlin que aplica: Composable de tema, `MaterialTheme`, tema del sistema y color dinámico.

```kotlin
package com.example.ud4_ejemplo3.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun Ud4_Ejemplo3Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

#### `Ud4_Ejemplo3-master/app/src/main/java/com/example/ud4_ejemplo3/ui/theme/Type.kt`

> Qué hace este archivo: Define la tipografía Material 3 que utiliza la aplicación.
>
> Conceptos clave de Android/Kotlin que aplica: `Typography`, `TextStyle`, familia, peso y tamaño tipográfico.

```kotlin
package com.example.ud4_ejemplo3.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
```

#### `Ud4_Ejemplo3-master/app/build.gradle.kts`

> Qué hace este archivo: Configura plugins, SDK, opciones de compilación y dependencias de esta aplicación Android.
>
> Conceptos clave de Android/Kotlin que aplica: Gradle Kotlin DSL, Android Gradle Plugin, Compose y dependencias declaradas.

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.ud4_ejemplo3"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.ud4_ejemplo3"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.viewmodel)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
```

### Práctica 3 — Arquitectura: pedidos de restaurante

Formulario de pedido respaldado por modelos de producto y un estado observable.
Repasa POO, herencia, selección de opciones y exposición del estado desde ViewModel.

#### `Ud4_Ejercicio4-master/app/src/main/java/com/example/ud4_ejercicio/MainActivity.kt`

> Qué hace este archivo: Pantalla de pedido; recoge las elecciones del usuario y las envía al estado de restaurante.
>
> Conceptos clave de Android/Kotlin que aplica: Activity de Android, `onCreate`, `setContent` y composición de la interfaz.

```kotlin
package com.example.ud4_ejercicio

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ud4_ejercicio.modelo.Hamburguesa
import com.example.ud4_ejercicio.modelo.Pedido
import com.example.ud4_ejercicio.modelo.Pizza
import com.example.ud4_ejercicio.ui.RestauranteViewModel
import com.example.ud4_ejercicio.ui.theme.Ud4_EjercicioTheme
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ud4_EjercicioTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FormularioPedido(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FormularioPedido(modifier: Modifier = Modifier){
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Restaurante DAM",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 5.dp)
        )
        Formulario(modifier)

    }
}


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
                                        })
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
            else -> {
            }
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

#### `Ud4_Ejercicio4-master/app/src/main/java/com/example/ud4_ejercicio/modelo/Hamburguesa.kt`

> Qué hace este archivo: Especializa el producto hamburguesa con los atributos propios del pedido.
>
> Conceptos clave de Android/Kotlin que aplica: Herencia y modelado de dominio con Kotlin.

```kotlin
package com.example.ud4_ejercicio.modelo

import com.example.ud4_ejercicio.R


data class Hamburguesa(
    val tipoCarne: String = "",
    val isQueso: Boolean = false
): Producto(imagen = R.drawable.hamburguesa, nombre = "Hamburguesa")
```

#### `Ud4_Ejercicio4-master/app/src/main/java/com/example/ud4_ejercicio/modelo/Pedido.kt`

> Qué hace este archivo: Agrupa la información que constituye un pedido del restaurante.
>
> Conceptos clave de Android/Kotlin que aplica: Data class y composición de modelos.

```kotlin
package com.example.ud4_ejercicio.modelo

data class Pedido(
    val producto: Producto,
    val cantidad: Int,
    val fecha: String
)
```

#### `Ud4_Ejercicio4-master/app/src/main/java/com/example/ud4_ejercicio/modelo/Pizza.kt`

> Qué hace este archivo: Especializa el producto pizza con los atributos propios de la selección.
>
> Conceptos clave de Android/Kotlin que aplica: Herencia y modelado de dominio con Kotlin.

```kotlin
package com.example.ud4_ejercicio.modelo

import com.example.ud4_ejercicio.R

data class Pizza(
    val tipoPizza: String = "",
    val tamanyo: String = ""
): Producto(imagen = R.drawable.pizza, nombre = "Pizza")
```

#### `Ud4_Ejercicio4-master/app/src/main/java/com/example/ud4_ejercicio/modelo/Producto.kt`

> Qué hace este archivo: Define la clase base compartida por los tipos de producto del restaurante.
>
> Conceptos clave de Android/Kotlin que aplica: POO, clase abierta y herencia.

```kotlin
package com.example.ud4_ejercicio.modelo

import androidx.annotation.DrawableRes

open class Producto(
    @DrawableRes val imagen: Int = 0,
    val nombre: String = ""
)
```

#### `Ud4_Ejercicio4-master/app/src/main/java/com/example/ud4_ejercicio/modelo/RestauranteUIState.kt`

> Qué hace este archivo: Mantiene el estado observable del formulario y de los pedidos del restaurante.
>
> Conceptos clave de Android/Kotlin que aplica: Data class, estado de UI y flujo de datos.

```kotlin
package com.example.ud4_ejercicio.modelo

data class RestauranteUIState(
    val pedidos: List<Pedido> = listOf()
)
```

#### `Ud4_Ejercicio4-master/app/src/main/java/com/example/ud4_ejercicio/ui/RestauranteViewModel.kt`

> Qué hace este archivo: Recibe pedidos y publica los cambios de estado del restaurante.
>
> Conceptos clave de Android/Kotlin que aplica: `ViewModel`, `MutableStateFlow` y separación UI/lógica.

```kotlin
package com.example.ud4_ejercicio.ui

import androidx.lifecycle.ViewModel
import com.example.ud4_ejercicio.modelo.Pedido
import com.example.ud4_ejercicio.modelo.RestauranteUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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

#### `Ud4_Ejercicio4-master/app/src/main/java/com/example/ud4_ejercicio/ui/theme/Color.kt`

> Qué hace este archivo: Declara los colores usados por los esquemas del tema de Material 3.
>
> Conceptos clave de Android/Kotlin que aplica: Constantes Kotlin, `Color` y sistemas de color.

```kotlin
package com.example.ud4_ejercicio.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF6686B2)
val Pink40 = Color(0xFF7D5260)
```

#### `Ud4_Ejercicio4-master/app/src/main/java/com/example/ud4_ejercicio/ui/theme/Theme.kt`

> Qué hace este archivo: Configura tema claro/oscuro y aplica el esquema de Material 3 a sus descendientes.
>
> Conceptos clave de Android/Kotlin que aplica: Composable de tema, `MaterialTheme`, tema del sistema y color dinámico.

```kotlin
package com.example.ud4_ejercicio.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0x956686B2)
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
            surfaceVariant = Color(0x956686B2)
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun Ud4_EjercicioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

#### `Ud4_Ejercicio4-master/app/src/main/java/com/example/ud4_ejercicio/ui/theme/Type.kt`

> Qué hace este archivo: Define la tipografía Material 3 que utiliza la aplicación.
>
> Conceptos clave de Android/Kotlin que aplica: `Typography`, `TextStyle`, familia, peso y tamaño tipográfico.

```kotlin
package com.example.ud4_ejercicio.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
```

#### `Ud4_Ejercicio4-master/app/build.gradle.kts`

> Qué hace este archivo: Configura plugins, SDK, opciones de compilación y dependencias de esta aplicación Android.
>
> Conceptos clave de Android/Kotlin que aplica: Gradle Kotlin DSL, Android Gradle Plugin, Compose y dependencias declaradas.

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.ud4_ejercicio"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.ud4_ejercicio"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.viewmodel)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
```

### Práctica 3 — Arquitectura: gestor de tareas

Formulario para editar y añadir tareas con módulo, descripción y prioridad.
Repasa `ViewModel`, `StateFlow`, copias de data classes y eventos de Compose.

#### `Ud4_EjercicioTarea-master/app/src/main/java/com/example/ud4_ejerciciotarea/datos/Datos.kt`

> Qué hace este archivo: Proporciona la colección inicial de banderas que alimenta la interfaz.
>
> Conceptos clave de Android/Kotlin que aplica: Capa de datos sencilla, colecciones Kotlin y referencias a recursos.

```kotlin
package com.example.ud4_ejerciciotarea.datos

val listaModulos = listOf("AD", "DI", "PMDM")
```

#### `Ud4_EjercicioTarea-master/app/src/main/java/com/example/ud4_ejerciciotarea/MainActivity.kt`

> Qué hace este archivo: Pantalla Compose del gestor de tareas; recoge interacción y observa el estado del ViewModel.
>
> Conceptos clave de Android/Kotlin que aplica: Activity de Android, `onCreate`, `setContent` y composición de la interfaz.

```kotlin
package com.example.ud4_ejerciciotarea

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ud4_ejerciciotarea.datos.listaModulos
import com.example.ud4_ejerciciotarea.modelo.Tarea
import com.example.ud4_ejerciciotarea.modelo.TareaUIState
import com.example.ud4_ejerciciotarea.ui.TareaViewModel
import com.example.ud4_ejerciciotarea.ui.theme.Ud4_EjercicioTareaTheme
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ud4_EjercicioTareaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TareasApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TareasApp(
    modifier: Modifier = Modifier,
    tareaViewModel: TareaViewModel = viewModel()
) {
    val uIState by tareaViewModel.tareaUIState.collectAsState()

    // La lista de módulos se encuentra en el fichero Datos.kt
    var eleccModulo by remember { mutableStateOf(listaModulos[0])}

    // Variable para poder establecer el foco en el elemento deseado cuando se pulsa el botón (TextField de título)
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(R.string.app_tareas),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 5.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(R.string.inserta_nueva_tarea),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            // El título a mostrar será el que está almacenado en el uIState
            value = uIState.tarea.titulo,
            label = {Text (text = stringResource(R.string.titulo)) },
            onValueChange = {
                // Al escribir el título lo actualizamos en nuestro ViewModel
                tareaViewModel.actualizarTitulo(it)
            },
            singleLine = true,
            modifier = Modifier.focusRequester(focusRequester) // Establecemos el foco en este elemento
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            // La descripción a mostrar será el que está almacenado en el uIState
            value = uIState.tarea.descripcion,
            label = {Text (text = stringResource(R.string.descripcion)) },
            onValueChange = {
                // Al escribir el título lo actualizamos en nuestro ViewModel
                tareaViewModel.actualizarDescripcion(it)
            },
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.modulo_asociado),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Row(
            Modifier
                .selectableGroup()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            listaModulos.forEach { modulo ->
                Row(
                    modifier = Modifier.selectable(
                        selected = (modulo == eleccModulo),
                        onClick = {
                            eleccModulo = modulo
                            // Al seleccionar el módulo lo actualizamos en nuestro ViewModel
                            tareaViewModel.actualizarModulo(modulo)
                        }
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (modulo == eleccModulo),
                        onClick = {
                            eleccModulo = modulo
                            // Al seleccionar el módulo lo actualizamos en nuestro ViewModel
                            tareaViewModel.actualizarModulo(modulo)
                        })
                    Text(text = modulo)
                }
            }

        }
        Spacer(modifier = Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.urgente),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(10.dp)
            )
            Switch(
                // El valor de urgente será el que está almacenado en el uIState
                checked = uIState.tarea.urgente,
                onCheckedChange = {
                    // Al inidicar o no si es urgente lo actualizamos en nuestro ViewModel
                    tareaViewModel.actualizarUrgente(it)
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                tareaViewModel.insertarTarea()
                focusRequester.requestFocus() // Una vez insertada la tarea asignamos el foco al TextField de título.
            },
            modifier = Modifier.padding(start = 10.dp)
        ) {
            Text(
                text = stringResource(R.string.insertar),
                fontWeight = FontWeight.Bold
            )
        }

        HorizontalDivider(modifier = Modifier.padding(5.dp))

        LazyColumn(modifier = Modifier) {
            items(uIState.listaTareas) { tarea ->
                ListItem(
                    modifier = Modifier
                        .padding(10.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RectangleShape
                        ),
                    leadingContent = {
                        Text(
                            text = tarea.modulo,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    headlineContent = {
                        Text(
                            text = tarea.titulo,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    supportingContent = {
                        Text(
                            text = tarea.descripcion,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    trailingContent = {
                        if(tarea.urgente) {
                            Image(
                                painter = painterResource(R.drawable.urgente),
                                contentDescription = stringResource(R.string.urgente),
                                Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(10.dp)
                            )
                        }
                    }
                )

            }
        }
    }
}
```

#### `Ud4_EjercicioTarea-master/app/src/main/java/com/example/ud4_ejerciciotarea/modelo/Tarea.kt`

> Qué hace este archivo: Modelo de una tarea con sus atributos de edición y prioridad.
>
> Conceptos clave de Android/Kotlin que aplica: Data class, tipos Kotlin y modelo de dominio.

```kotlin
package com.example.ud4_ejerciciotarea.modelo

data class Tarea(
    var titulo: String = "",
    var descripcion: String = "",
    var modulo: String = "",
    var urgente: Boolean = false
)
```

#### `Ud4_EjercicioTarea-master/app/src/main/java/com/example/ud4_ejerciciotarea/modelo/TareaUIState.kt`

> Qué hace este archivo: Agrupa la tarea en edición y la información necesaria para representar la pantalla.
>
> Conceptos clave de Android/Kotlin que aplica: Estado de interfaz inmutable con data classes.

```kotlin
package com.example.ud4_ejerciciotarea.modelo

data class TareaUIState(
    var tarea: Tarea = Tarea(),
    var listaTareas: List<Tarea> = listOf()
)
```

#### `Ud4_EjercicioTarea-master/app/src/main/java/com/example/ud4_ejerciciotarea/ui/TareaViewModel.kt`

> Qué hace este archivo: Actualiza campos de tarea y gestiona la inserción mediante estado observable.
>
> Conceptos clave de Android/Kotlin que aplica: `ViewModel`, `StateFlow`, `copy` y actualizaciones inmutables.

```kotlin
package com.example.ud4_ejerciciotarea.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ud4_ejerciciotarea.datos.listaModulos
import com.example.ud4_ejerciciotarea.modelo.Tarea
import com.example.ud4_ejerciciotarea.modelo.TareaUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TareaViewModel: ViewModel() {
    // Atributos realcionados con el uiState
    private val _tareaUItate = MutableStateFlow(TareaUIState())
    val tareaUIState: StateFlow<TareaUIState> = _tareaUItate.asStateFlow()

    // Atributos relacionados con la UI
    var titulo by mutableStateOf("")
        private set

    var descripcion by mutableStateOf("")
        private set

    var modulo by mutableStateOf("")
        private set

    var urgente by mutableStateOf(false)
        private set

    fun actualizarTitulo(titulo_ui: String) {
        titulo = titulo_ui

//        val tarea = Tarea(
//            titulo,
//            _tareaUItate.value.tarea.descripcion,
//            _tareaUItate.value.tarea.modulo,
//            _tareaUItate.value.tarea.urgente)

        val tarea = _tareaUItate.value.tarea.copy()
        tarea.titulo = titulo

        _tareaUItate.update { estadoActual ->
            estadoActual.copy(
                tarea = tarea
            )
        }
    }

    fun actualizarDescripcion(desc_ui: String) {
        descripcion = desc_ui

//        val tarea = Tarea(
//            _tareaUItate.value.tarea.titulo,
//            descripcion,
//            _tareaUItate.value.tarea.modulo,
//            _tareaUItate.value.tarea.urgente)

        val tarea = _tareaUItate.value.tarea.copy()
        tarea.descripcion = descripcion

        _tareaUItate.update { estadoActual ->
            estadoActual.copy(
                tarea = tarea
            )
        }
    }

    fun actualizarModulo(modulo_ui: String) {
        modulo = modulo_ui

//        val tarea = Tarea(
//            _tareaUItate.value.tarea.titulo,
//            _tareaUItate.value.tarea.descripcion,
//            modulo,
//            _tareaUItate.value.tarea.urgente)

        val tarea = _tareaUItate.value.tarea.copy()
        tarea.modulo = modulo

        _tareaUItate.update { estadoActual ->
            estadoActual.copy(
                tarea = tarea
            )
        }
    }

    fun actualizarUrgente(urgente_ui: Boolean) {
        urgente = urgente_ui

//        val tarea = Tarea(
//            _tareaUItate.value.tarea.titulo,
//            _tareaUItate.value.tarea.descripcion,
//            _tareaUItate.value.tarea.modulo,
//            urgente)

        val tarea = _tareaUItate.value.tarea.copy()
        tarea.urgente = urgente

        _tareaUItate.update { estadoActual ->
            estadoActual.copy(
                tarea = tarea
            )
        }
    }

    fun insertarTarea() {
        _tareaUItate.update { estadoActual ->
            estadoActual.copy(
                tarea = Tarea(),
                listaTareas = estadoActual.listaTareas + estadoActual.tarea
            )
        }
    }
}
```

#### `Ud4_EjercicioTarea-master/app/src/main/java/com/example/ud4_ejerciciotarea/ui/theme/Color.kt`

> Qué hace este archivo: Declara los colores usados por los esquemas del tema de Material 3.
>
> Conceptos clave de Android/Kotlin que aplica: Constantes Kotlin, `Color` y sistemas de color.

```kotlin
package com.example.ud4_ejerciciotarea.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
```

#### `Ud4_EjercicioTarea-master/app/src/main/java/com/example/ud4_ejerciciotarea/ui/theme/Theme.kt`

> Qué hace este archivo: Configura tema claro/oscuro y aplica el esquema de Material 3 a sus descendientes.
>
> Conceptos clave de Android/Kotlin que aplica: Composable de tema, `MaterialTheme`, tema del sistema y color dinámico.

```kotlin
package com.example.ud4_ejerciciotarea.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun Ud4_EjercicioTareaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

#### `Ud4_EjercicioTarea-master/app/src/main/java/com/example/ud4_ejerciciotarea/ui/theme/Type.kt`

> Qué hace este archivo: Define la tipografía Material 3 que utiliza la aplicación.
>
> Conceptos clave de Android/Kotlin que aplica: `Typography`, `TextStyle`, familia, peso y tamaño tipográfico.

```kotlin
package com.example.ud4_ejerciciotarea.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
```

#### `Ud4_EjercicioTarea-master/app/build.gradle.kts`

> Qué hace este archivo: Configura plugins, SDK, opciones de compilación y dependencias de esta aplicación Android.
>
> Conceptos clave de Android/Kotlin que aplica: Gradle Kotlin DSL, Android Gradle Plugin, Compose y dependencias declaradas.

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.ud4_ejerciciotarea"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.ud4_ejerciciotarea"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.viewmodel)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
```

## Ejercicio adicional — Gestor de módulos

### Práctica 4 — Ejercicio adicional: gestor de módulos

Formulario de módulos con campos de texto y selección de asignatura/prioridad.
Repasa modelado de datos, estado de UI y actualización reactiva desde un ViewModel.

#### `ejercicio2/app/src/main/java/com/example/ud4_ejercicio/MainActivity.kt`

> Qué hace este archivo: Activity y formulario de gestión de módulos; conecta la entrada Compose con el ViewModel correspondiente.
>
> Conceptos clave de Android/Kotlin que aplica: Activity de Android, `onCreate`, `setContent` y composición de la interfaz.

```kotlin
package com.example.ud4_ejercicio
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ud4_ejercicio.modelo.modulo
import com.example.ud4_ejercicio.ui.ModuloViewModel
import com.example.ud4_ejercicio.ui.theme.Ud4_EjercicioTheme
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ud4_EjercicioTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FormularioPedido(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
@Composable
fun FormularioPedido(modifier: Modifier = Modifier){
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Modulos DAM",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 5.dp)
        )
        Formulario(modifier)
    }
}
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

#### `ejercicio2/app/src/main/java/com/example/ud4_ejercicio/modelo/modulo.kt`

> Qué hace este archivo: Modelo de un módulo con nombre, prioridad, descripción y título.
>
> Conceptos clave de Android/Kotlin que aplica: Data class y modelado de formulario.

```kotlin
package com.example.ud4_ejercicio.modelo;

data class modulo(val nombre: String, val urgente: Boolean, val descripcion: String, val titulo: String) {}
```

#### `ejercicio2/app/src/main/java/com/example/ud4_ejercicio/modelo/ModuloUIState.kt`

> Qué hace este archivo: Representa los datos del módulo que la pantalla necesita mostrar o editar.
>
> Conceptos clave de Android/Kotlin que aplica: Data class y estado de interfaz.

```kotlin
package com.example.ud4_ejercicio.modelo

data class ModuloUIState(
    val listaModulo: List<modulo> = listOf(),
    val modulo: modulo = modulo("", false, "", "")
)
```

#### `ejercicio2/app/src/main/java/com/example/ud4_ejercicio/ui/ModuloViewModel.kt`

> Qué hace este archivo: Actualiza la lista y los campos de los módulos desde la lógica de presentación.
>
> Conceptos clave de Android/Kotlin que aplica: `ViewModel`, `StateFlow` y separación de responsabilidades.

```kotlin
package com.example.ud4_ejercicio.ui

import androidx.lifecycle.ViewModel
import com.example.ud4_ejercicio.modelo.ModuloUIState
import com.example.ud4_ejercicio.modelo.modulo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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

#### `ejercicio2/app/src/main/java/com/example/ud4_ejercicio/ui/theme/Color.kt`

> Qué hace este archivo: Declara los colores usados por los esquemas del tema de Material 3.
>
> Conceptos clave de Android/Kotlin que aplica: Constantes Kotlin, `Color` y sistemas de color.

```kotlin
package com.example.ud4_ejercicio.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF6686B2)
val Pink40 = Color(0xFF7D5260)
```

#### `ejercicio2/app/src/main/java/com/example/ud4_ejercicio/ui/theme/Theme.kt`

> Qué hace este archivo: Configura tema claro/oscuro y aplica el esquema de Material 3 a sus descendientes.
>
> Conceptos clave de Android/Kotlin que aplica: Composable de tema, `MaterialTheme`, tema del sistema y color dinámico.

```kotlin
package com.example.ud4_ejercicio.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0x956686B2)
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
            surfaceVariant = Color(0x956686B2)
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun Ud4_EjercicioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

#### `ejercicio2/app/src/main/java/com/example/ud4_ejercicio/ui/theme/Type.kt`

> Qué hace este archivo: Define la tipografía Material 3 que utiliza la aplicación.
>
> Conceptos clave de Android/Kotlin que aplica: `Typography`, `TextStyle`, familia, peso y tamaño tipográfico.

```kotlin
package com.example.ud4_ejercicio.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
```

#### `ejercicio2/app/build.gradle.kts`

> Qué hace este archivo: Configura plugins, SDK, opciones de compilación y dependencias de esta aplicación Android.
>
> Conceptos clave de Android/Kotlin que aplica: Gradle Kotlin DSL, Android Gradle Plugin, Compose y dependencias declaradas.

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.ud4_ejercicio"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.ud4_ejercicio"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.storage)
    implementation(libs.androidx.viewmodel)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
```

## Documentación del repositorio

#### `README.md`

> Qué hace este archivo: Describe el material, la organización de las unidades y los proyectos disponibles.
>
> Conceptos clave de Android/Kotlin que aplica: mapa de prácticas, estructura de proyectos y contexto didáctico.

````markdown
# Android Nativo — Kotlin y Jetpack Compose

Material de **Programación Multimedia y Dispositivos Móviles (DAM)** organizado por unidades, siguiendo la estructura numerada de `DI` y adaptándola a los contenidos disponibles de Android.

## Accesos rápidos

| Recurso | Contenido |
| --- | --- |
| [Índice Android](./IndiceAndroid.html) | Índice visual con búsqueda por unidad, tema y proyecto. |
| [CHEATSHEET.md](./CHEATSHEET.md) | Chuleta técnica basada en los proyectos: Compose, listas, estado, `ViewModel` y `StateFlow`. |
| [CheatSheet_Android_Examen.md](./02_Cheatsheets_y_Chuletas/CheatSheet_Android_Examen.md) | Repaso de `ViewBinding`, `Manifest`, `Intent`, eventos, `RecyclerView` y layouts XML. |

## Estructura

```text
android/
├── 01_Teoria_y_Enunciados_PDF/          # Programación Kotlin y unidades 1–4
├── 02_Cheatsheets_y_Chuletas/           # Repaso general de Android
├── 03_Unidad2_DIU_y_Formularios/        # Ejercicio de cálculo de IVA en Compose
├── 04_Unidad3_Listas_y_Componentes/     # Listas, callbacks y tarjetas expandibles
├── 05_Unidad4_Arquitectura_y_MVVM/      # Ciclo de vida, StateFlow, MVVM y formularios
├── 06_Ejercicios_Adicionales/           # Gestor de módulos (ejercicio2)
├── CHEATSHEET.md                       # Referencia técnica de los proyectos Compose
├── IndiceAndroid.html                  # Índice visual, filtrable y offline
└── README.md
```

No se incluye un `00_Plantilla_Examen`: el material disponible contiene proyectos de ejemplo independientes, pero no una plantilla Android genérica.

## Teoría

- [Programación Kotlin](./01_Teoria_y_Enunciados_PDF/Android_ProgramacionKotlin.pdf)
- [Unidad 1 — Introducción](./01_Teoria_y_Enunciados_PDF/Android_Ud1_Introduccion.pdf)
- [Unidad 2 — DIU](./01_Teoria_y_Enunciados_PDF/Android_Ud2_DIU.pdf)
- [Unidad 3 — Listas](./01_Teoria_y_Enunciados_PDF/Android_Ud3_Listas.pdf)
- [Unidad 4 — Arquitectura](./01_Teoria_y_Enunciados_PDF/Android_Ud4_Arquitectura.pdf)

## Proyectos

Cada carpeta es un proyecto Gradle independiente; ábrela por separado en Android Studio.

| Bloque | Proyecto | Temas principales |
| --- | --- | --- |
| Unidad 2 | [Ud2_Ejercicio1-master](./03_Unidad2_DIU_y_Formularios/Ud2_Ejercicio1-master/) | Calculadora de IVA, campos de entrada y selección del tipo impositivo en Compose. |
| Unidad 3 | [Ud3_Ejemplo1-master](./04_Unidad3_Listas_y_Componentes/Ud3_Ejemplo1-master/) | Lista de banderas con `LazyColumn`, modelos, recursos e imágenes. |
| Unidad 3 | [Ud3_Ejemplo2-master](./04_Unidad3_Listas_y_Componentes/Ud3_Ejemplo2-master/) | Elementos clicables y callbacks en una lista. |
| Unidad 3 | [Ud3_Ejemplo3-master](./04_Unidad3_Listas_y_Componentes/Ud3_Ejemplo3-master/) | Tarjetas expandibles y contenido condicional. |
| Unidad 4 | [Ud4_Ejemplo1-master](./05_Unidad4_Arquitectura_y_MVVM/Ud4_Ejemplo1-master/) | Ciclo de vida de una Activity. |
| Unidad 4 | [Ud4_Ejemplo2-master](./05_Unidad4_Arquitectura_y_MVVM/Ud4_Ejemplo2-master/) | Calculadora reactiva con `ViewModel` y `StateFlow`. |
| Unidad 4 | [Ud4_Ejemplo3-master](./05_Unidad4_Arquitectura_y_MVVM/Ud4_Ejemplo3-master/) | Juego del ahorcado con `ViewModel`, estado y diálogo. |
| Unidad 4 | [Ud4_Ejercicio4-master](./05_Unidad4_Arquitectura_y_MVVM/Ud4_Ejercicio4-master/) | Restaurante: formularios, POO, herencia y estado reactivo. |
| Unidad 4 | [Ud4_EjercicioTarea-master](./05_Unidad4_Arquitectura_y_MVVM/Ud4_EjercicioTarea-master/) | App Tareas: eventos de Compose, `ViewModel`, `TareaUIState` y `StateFlow`. [Explicación interactiva](https://fpcarlosc.github.io/SimuladorAppTareas/). |
| Adicional | [ejercicio2](./06_Ejercicios_Adicionales/ejercicio2/) | Gestor de módulos con estado de interfaz y `ViewModel`. |

## Abrir, compilar y ejecutar

1. Instala Android Studio con un JDK y Android SDK compatibles con el proyecto.
2. Abre en Android Studio la carpeta del proyecto que quieras ejecutar.
3. Espera a que termine la sincronización de Gradle y ejecuta la configuración `app`.

También puedes compilar desde PowerShell, entrando primero en la carpeta del proyecto:

```powershell
.\gradlew.bat assembleDebug
```

Los proyectos no forman una única solución Gradle: ejecuta el comando dentro de cada carpeta.
````

#### `Ud4_EjercicioTarea-master/README.md`

> Qué hace este archivo: Apunta a la explicación interactiva del ejercicio de tareas.
>
> Conceptos clave de Android/Kotlin que aplica: mapa de prácticas, estructura de proyectos y contexto didáctico.

````markdown
La explicación del flujo de la aplicación se encuentra en https://fpcarlosc.github.io/SimuladorAppTareas/
````
