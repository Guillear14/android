# 🚀 Cheat Sheet Definitivo: Android Nativo (DAM / Examen Práctico)

> **Guía rápida de consulta contra reloj para Android Studio.**  
> *Usa `Ctrl + F` para saltar directamente a la sección o código que necesitas.*

---

## 📑 Índice Rápido
1. [⚙️ Configuración Previa: Activar ViewBinding en Gradle](#️-configuración-previa-activar-viewbinding-en-gradle)
2. [📄 AndroidManifest.xml: Activities y Permisos](#-androidmanifestxml-activities-y-permisos)
3. [🔌 ViewBinding: Inflar Layouts en Actividades y Fragmentos](#-viewbinding-inflar-layouts-en-actividades-y-fragmentos)
4. [🚀 Intents y Navegación entre Pantallas](#-intents-y-navegación-entre-pantallas)
   - [Intent Explícito (Pasar datos y abrir Activity)](#intent-explícito-con-paso-de-datos)
   - [Recibir Datos en la Activity Destino](#recibir-datos-en-la-activity-destino)
   - [Retornar Resultados (`registerForActivityResult`)](#retornar-resultados-activityresultlauncher)
   - [Intents Implícitos (Web, Llamada, Compartir)](#intents-implícitos-frecuentes)
5. [🖱️ Eventos Básicos y Mensajes Rápidos](#️-eventos-básicos-y-mensajes-rápidos)
   - [Clicks y Lectura de Campos](#clicks-y-lectura-de-campos)
   - [Toast y Snackbar](#toast-y-snackbar)
   - [AlertDialog (Diálogo de Confirmación)](#alertdialog-de-confirmación)
6. [📋 RecyclerView: Adapter y ViewHolder con ViewBinding](#-recyclerview-adapter-y-viewholder-con-viewbinding)
   - [Modelo de Datos](#1-modelo-de-datos-dataclass)
   - [XML del Item (`item_elemento.xml`)](#2-diseño-del-item-reslayoutitem_elementoxml)
   - [Adapter + ViewHolder Completo](#3-código-completo-del-adapter-con-viewbinding)
   - [Implementación en la Activity](#4-inicialización-en-la-activity)
7. [📐 Tabla Resumen de Atributos XML Clave](#-tabla-resumen-de-atributos-xml-clave)
   - [ConstraintLayout](#constraintlayout)
   - [LinearLayout y Pesos](#linearlayout-y-distribución-de-pesos)
   - [Propiedades Generales Más Preguntadas](#propiedades-generales-más-preguntadas)
8. [⚠️ Errores Típicos de Examen y Solución Inmediata](#️-errores-típicos-de-examen-y-solución-inmediata)

---

## ⚙️ Configuración Previa: Activar ViewBinding en Gradle

En `app/build.gradle.kts` (o `app/build.gradle`):

```kotlin
// build.gradle.kts
android {
    ...
    buildFeatures {
        viewBinding = true
    }
}
```

*Si usas Groovy (`build.gradle` antiguo):*
```groovy
android {
    ...
    buildFeatures {
        viewBinding true
    }
}
```
> 💡 **Nota:** Haz clic en **Sync Now** en la barra superior tras editar Gradle.

---

## 📄 AndroidManifest.xml: Activities y Permisos

Ubicación: `app/src/main/AndroidManifest.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <!-- 1. PERMISOS HABITUALES (van fuera de <application>) -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.MiApp">

        <!-- 2. ACTIVIDAD PRINCIPAL (con Launcher) -->
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- 3. ACTIVIDADES SECUNDARIAS (obligatorio declararlas siempre) -->
        <activity
            android:name=".DetalleActivity"
            android:exported="false"
            android:screenOrientation="portrait" />

    </application>
</manifest>
```

---

## 🔌 ViewBinding: Inflar Layouts en Actividades y Fragmentos

El nombre de la clase generada es el nombre del XML en CamelCase + `Binding`  
*(Ejemplo: `activity_main.xml` -> `ActivityMainBinding`, `fragment_detalle.xml` -> `FragmentDetalleBinding`)*.

### En Activity (`MainActivity.kt`)
```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) // ¡OBLIGATORIO: no pasar R.layout.activity_main!

        // Acceso directo a vistas sin findViewById:
        binding.tvTitulo.text = "Bienvenido al Examen"
    }
}
```

### En Fragment (`DetalleFragment.kt`)
```kotlin
class DetalleFragment : Fragment() {

    private var _binding: FragmentDetalleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAceptar.setOnClickListener { /* Acción */ }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Previene fuga de memoria
    }
}
```

---

## 🚀 Intents y Navegación entre Pantallas

### Intent Explícito con paso de datos
```kotlin
val intent = Intent(this, DetalleActivity::class.java).apply {
    putExtra("EXTRA_NOMBRE", binding.etNombre.text.toString())
    putExtra("EXTRA_EDAD", 25)
    putExtra("EXTRA_ACTIVO", true)
}
startActivity(intent)
```

### Recibir Datos en la Activity Destino
```kotlin
// Dentro de onCreate() de la Activity receptora:
val nombre = intent.getStringExtra("EXTRA_NOMBRE") ?: "Desconocido"
val edad = intent.getIntExtra("EXTRA_EDAD", 0) // Segundo parámetro es el valor por defecto
val activo = intent.getBooleanExtra("EXTRA_ACTIVO", false)

binding.tvResultado.text = "Nombre: $nombre, Edad: $edad, Activo: $activo"
```

### Retornar Resultados (`ActivityResultLauncher`)
Forma moderna recomendada (sustituye al deprecado `startActivityForResult`):

#### 1. En la Activity que inicia:
```kotlin
// Registrar el launcher antes o en onCreate():
private val resultLauncher = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
) { result ->
    if (result.resultCode == RESULT_OK) {
        val respuesta = result.data?.getStringExtra("EXTRA_RESPUESTA")
        binding.tvEstado.text = respuesta
    }
}

// Al hacer click para lanzar:
binding.btnSeleccionar.setOnClickListener {
    val intent = Intent(this, SeleccionActivity::class.java)
    resultLauncher.launch(intent)
}
```

#### 2. En la Activity secundaria para devolver datos:
```kotlin
val data = Intent().apply {
    putExtra("EXTRA_RESPUESTA", "Operación completada con éxito")
}
setResult(RESULT_OK, data)
finish() // Cierra la pantalla y vuelve a la anterior
```

### Intents Implícitos Frecuentes
```kotlin
// 1. Abrir navegador web:
val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com"))
startActivity(webIntent)

// 2. Abrir marcador telefónico (no requiere permiso directo de llamada):
val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:123456789"))
startActivity(dialIntent)

// 3. Compartir texto simple:
val shareIntent = Intent(Intent.ACTION_SEND).apply {
    type = "text/plain"
    putExtra(Intent.EXTRA_TEXT, "Texto a compartir")
}
startActivity(Intent.createChooser(shareIntent, "Compartir con"))
```

---

## 🖱️ Eventos Básicos y Mensajes Rápidos

### Clicks y Lectura de Campos
```kotlin
binding.btnValidar.setOnClickListener {
    val texto = binding.etTexto.text.toString().trim()

    if (texto.isEmpty()) {
        binding.etTexto.error = "Este campo es obligatorio"
        return@setOnClickListener
    }
    
    // Obtener estado de Checkbox / Switch
    val estaMarcado = binding.cbAcepto.isChecked
}
```

### Toast y Snackbar
```kotlin
// Toast corto:
Toast.makeText(this, "Acción guardada", Toast.LENGTH_SHORT).show()

// Toast largo:
Toast.makeText(this, "Error al conectar", Toast.LENGTH_LONG).show()

// Snackbar con botón de acción (requiere vista raíz):
Snackbar.make(binding.root, "¿Deseas eliminar el elemento?", Snackbar.LENGTH_LONG)
    .setAction("DESHACER") {
        Toast.makeText(this, "Restaurado", Toast.LENGTH_SHORT).show()
    }
    .show()
```

### AlertDialog de Confirmación
```kotlin
AlertDialog.Builder(this)
    .setTitle("Confirmación")
    .setMessage("¿Estás seguro de continuar?")
    .setPositiveButton("Sí") { dialog, which ->
        // Acción confirmada
    }
    .setNegativeButton("Cancelar") { dialog, which ->
        dialog.dismiss()
    }
    .setCancelable(false)
    .show()
```

---

## 📋 RecyclerView: Adapter y ViewHolder con ViewBinding

Plantilla universal de 4 pasos lista para el examen.

### 1. Modelo de Datos (`data class`)
```kotlin
data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double
)
```

### 2. Diseño del Item (`res/layout/item_producto.xml`)
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:padding="12dp">

    <TextView
        android:id="@+id/tvNombreProducto"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:textSize="18sp"
        android:textStyle="bold"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintEnd_toStartOf="@+id/tvPrecioProducto" />

    <TextView
        android:id="@+id/tvPrecioProducto"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="16sp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

### 3. Código Completo del Adapter con ViewBinding
```kotlin
class ProductoAdapter(
    private var lista: List<Producto>,
    private val onProductoClick: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    // 1. ViewHolder que almacena el binding del ítem
    inner class ProductoViewHolder(val binding: ItemProductoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(producto: Producto) {
            binding.tvNombreProducto.text = producto.nombre
            binding.tvPrecioProducto.text = "${producto.precio} €"

            binding.root.setOnClickListener {
                onProductoClick(producto)
            }
        }
    }

    // 2. Inflado de la vista con ItemProductoBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val binding = ItemProductoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductoViewHolder(binding)
    }

    // 3. Vinculación de datos por posición
    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    // 4. Cantidad total de ítems
    override fun getItemCount(): Int = lista.size

    // Función auxiliar para actualizar la lista en tiempo de ejecución:
    fun actualizarDatos(nuevaLista: List<Producto>) {
        this.lista = nuevaLista
        notifyDataSetChanged()
    }
}
```

### 4. Inicialización en la Activity
```kotlin
class ListaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaBinding
    private lateinit var adapter: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val datosPrueba = listOf(
            Producto(1, "Portátil Gaming", 899.99),
            Producto(2, "Ratón Inalámbrico", 25.50),
            Producto(3, "Teclado Mecánico", 60.00)
        )

        // Configuración de LayoutManager y Adapter:
        binding.rvProductos.layoutManager = LinearLayoutManager(this)
        
        adapter = ProductoAdapter(datosPrueba) { productoSeleccionado ->
            Toast.makeText(this, "Seleccionado: ${productoSeleccionado.nombre}", Toast.LENGTH_SHORT).show()
        }
        
        binding.rvProductos.adapter = adapter
    }
}
```

---

## 📐 Tabla Resumen de Atributos XML Clave

### ConstraintLayout
| Atributo | Valores / Sintaxis | Propósito Examen |
| :--- | :--- | :--- |
| `app:layout_constraintTop_toTopOf` | `"parent"` o `"@id/vista"` | Alinea el borde superior con el borde superior de destino |
| `app:layout_constraintTop_toBottomOf` | `"@id/vista"` | Coloca la vista **debajo** de otra vista de referencia |
| `app:layout_constraintBottom_toBottomOf` | `"parent"` o `"@id/vista"` | Alinea el borde inferior con el fondo |
| `app:layout_constraintStart_toStartOf` | `"parent"` o `"@id/vista"` | Alinea a la izquierda (respetando RTL) |
| `app:layout_constraintEnd_toEndOf` | `"parent"` o `"@id/vista"` | Alinea a la derecha (respetando RTL) |
| `app:layout_constraintStart_toEndOf` | `"@id/vista"` | Coloca a la **derecha** de otra vista |
| `android:layout_width="0dp"` | `"0dp"` *(MATCH_CONSTRAINT)* | Ocupa todo el espacio delimitado entre sus restricciones |
| `app:layout_constraintHorizontal_bias` | `"0.5"` (rango `0.0` a `1.0`) | Desplaza horizontalmente hacia un lado (`0.0` izquierda, `1.0` derecha) |
| `app:layout_constraintDimensionRatio` | `"16:9"` o `"1:1"` | Fija proporción de aspecto (uno de los lados debe ser `0dp`) |

> 📌 **Centrado Absoluto en ConstraintLayout:**
> ```xml
> app:layout_constraintTop_toTopOf="parent"
> app:layout_constraintBottom_toBottomOf="parent"
> app:layout_constraintStart_toStartOf="parent"
> app:layout_constraintEnd_toEndOf="parent"
> ```

---

### LinearLayout y Distribución de Pesos
| Atributo | Valores | Propósito Examen |
| :--- | :--- | :--- |
| `android:orientation` | `"vertical"` \| `"horizontal"` | Dirección del flujo de componentes |
| `android:layout_weight` | Número (ej. `"1"`, `"2"`) | Proporción de reparto del espacio sobrante |
| `android:layout_width="0dp"` | Con `orientation="horizontal"` | Obligatorio poner `width="0dp"` al usar `layout_weight` |
| `android:layout_height="0dp"` | Con `orientation="vertical"` | Obligatorio poner `height="0dp"` al usar `layout_weight` |
| `android:weightSum` | Número (ej. `"3"`) | Define la suma total de pesos en el contenedor padre |

---

### Propiedades Generales Más Preguntadas
| Atributo | Valores | Diferencia / Efecto |
| :--- | :--- | :--- |
| `android:visibility` | `visible` \| `invisible` \| `gone` | `invisible` oculta pero conserva espacio; `gone` oculta y **elimina el espacio ocupado** |
| `android:gravity` | `center`, `bottom`, `start`, etc. | Alinea el **contenido interno** de la vista (ej. texto dentro del botón) |
| `android:layout_gravity` | `center`, `end`, etc. | Alinea la **propia vista dentro de su contenedor padre** |
| `android:padding` | `16dp` | Espacio **interior** entre el borde de la vista y su contenido |
| `android:layout_margin` | `16dp` | Espacio **exterior** entre la vista y los elementos vecinos |
| `android:inputType` | `textPassword`, `number`, `textEmailAddress` | Cambia el teclado y enmascara texto automáticamente |
| `android:elevation` | `4dp`, `8dp` | Proyecta sombra en tarjetas y vistas compatibles |

---

## ⚠️ Errores Típicos de Examen y Solución Inmediata

| Error / Síntoma en Logcat | Causa Frecuente | Solución Rápida en 1 Línea |
| :--- | :--- | :--- |
| **`ActivityNotFoundException: Unable to find explicit activity class`** | La Activity secundaria no está declarada en el Manifest. | Añadir `<activity android:name=".MiActivity" android:exported="false" />` dentro de `<application>` en `AndroidManifest.xml`. |
| **`UninitializedPropertyAccessException: lateinit property binding has not been initialized`** | Acceso a `binding` antes de inflarlo en `onCreate()`. | Asegurar que `binding = ActivityMainBinding.inflate(layoutInflater)` va **antes** de cualquier llamada a `binding.xxx`. |
| **`NullPointerException` al buscar vistas con `findViewById`** | `findViewById` ejecutado antes de `setContentView()` o ID inexistente en el layout actual. | Comprobar que `setContentView()` se ejecutó antes y verificar la coincidencia del ID `@+id/miId`. Con `ViewBinding` este error no ocurre. |
| **Pantalla en blanco con `setContentView(R.layout...)`** | Se infló con ViewBinding pero se llamó a `setContentView(R.layout.activity_main)` en vez de `setContentView(binding.root)`. | Cambiar siempre a `setContentView(binding.root)`. |
| **`Unresolved reference: ActivityMainBinding`** | No está activado ViewBinding en Gradle o falta sincronizar el proyecto. | Añadir `buildFeatures { viewBinding = true }` en `build.gradle.kts` y pulsar **Sync Project with Gradle Files**. |
| **El RecyclerView aparece completamente vacío** | Se olvidó asignar el `LayoutManager` o `getItemCount()` devuelve 0. | Añadir `rv.layoutManager = LinearLayoutManager(this)` y comprobar que la lista pasada al adapter tenga elementos (`lista.size > 0`). |
| **Crash al rotar pantalla (`ClassCastException` o datos perdidos)** | Destrucción de la Activity por cambio de configuración sin persistencia. | Usar `ViewModel` para mantener datos de UI o añadir `android:configChanges="orientation|screenSize"` si el enunciado lo permite. |
| **`SecurityException: Permission Denial`** | Operación restringida (Internet, cámara, GPS) sin permiso en el Manifest. | Declarar `<uses-permission android:name="android.permission.INTERNET" />` arriba en `AndroidManifest.xml`. |
| **`android.os.NetworkOnMainThreadException`** | Ejecutar peticiones de red o base de datos en el hilo principal (`Main Thread`). | Ejecutar la operación dentro de una corrutina `lifecycleScope.launch(Dispatchers.IO) { ... }`. |
| **Crash al pulsar botón con `android:onClick="miMetodo"` en XML** | El método en la Activity no tiene la firma exacta `fun miMetodo(view: View)`. | Usar listeners en Kotlin: `binding.btn.setOnClickListener { }` en vez de atributos en XML. |

---
*Hecho para el módulo de Programación Multimedia y Dispositivos Móviles (DAM).*
