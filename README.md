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

## Fuentes Kotlin en `Archivoskt`

`Archivoskt` es un único proyecto Gradle con un módulo Android independiente para cada ejercicio, agrupado en `Archivoskt/ejercicios/UdX/EjercicioN`. Los proyectos sin unidad identificada se incluyen como módulos en `Archivoskt/ejercicios/ProyectosIndependientes`. Abre la carpeta `Archivoskt` en Android Studio para sincronizar y ejecutar cualquiera de sus módulos.

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
