# Unidad 4

## Ejercicios

### Ejercicio 1 — Ciclo de vida de una Activity

En [`Ejercicio1`](./Ejercicio1/) está el módulo Gradle `:ud4-ejercicio1`. Una pantalla Compose sencilla muestra un saludo y `MainActivity.kt` registra en Logcat los principales eventos del ciclo de vida (`onStart`, `onResume`, `onPause`, entre otros).

### Ejercicio 2 — Calculadora reactiva

En [`Ejercicio2`](./Ejercicio2/) está el módulo Gradle `:ud4-ejercicio2`. Se introducen dos números y se muestra su suma. `OperacionViewModel.kt` calcula el resultado y lo expone mediante `StateFlow`; `OperacionUIState.kt` define el estado que observa la interfaz.

### Ejercicio 3 — Juego del ahorcado

En [`Ejercicio3`](./Ejercicio3/) está el módulo Gradle `:ud4-ejercicio3`. Se juega a adivinar palabras introduciendo letras. La pantalla muestra la ronda y la puntuación, indica las letras incorrectas y permite reiniciar al terminar. El estado, las palabras, la lógica del juego y los recursos están separados.

### Ejercicio 4 — Pedidos de restaurante

En [`Ejercicio4`](./Ejercicio4/) está el módulo Gradle `:ud4-ejercicio4`. Hay un formulario para crear pedidos de hamburguesas o pizzas, elegir sus opciones y especificar cantidad y fecha. Los modelos de producto y pedido, el estado de la interfaz y el `ViewModel` están separados en `modelo/` y `ui/`.

### Ejercicio 5 — Módulos de DAM

En [`Ejercicio5`](./Ejercicio5/) está el módulo Gradle `:ud4-ejercicio5`. Se pueden registrar módulos (AD, DI o PMDM) con título, descripción y marca de urgencia, y consultar la lista. `ModuloViewModel.kt` mantiene la lista mediante `StateFlow`; `modelo/` contiene sus datos y estado.

### Ejercicio 6 — Gestión de tareas

En [`Ejercicio6`](./Ejercicio6/) está el módulo Gradle `:ud4-ejercicio6`. Se registran tareas con título, descripción, módulo y prioridad urgente. `TareaViewModel.kt` gestiona los cambios y la lista, `TareaUIState.kt` define el estado, `Tarea.kt` el modelo y `Datos.kt` los módulos disponibles.
