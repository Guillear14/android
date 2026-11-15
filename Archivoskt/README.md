# Proyecto Android de `Archivoskt`

`Archivoskt` es un único proyecto Gradle con un módulo Android independiente para cada ejercicio. Abre esta carpeta en Android Studio y sincroniza Gradle; después, elige el módulo que quieras ejecutar.

## Módulos

| Unidad o grupo | Guía |
| --- | --- |
| Unidad 2 | [Ejercicios](./ejercicios/Ud2/README.md) |
| Unidad 3 | [Ejercicios](./ejercicios/Ud3/README.md) |
| Unidad 4 | [Ejercicios](./ejercicios/Ud4/README.md) |
| Proyectos independientes | [`ejercicios/ProyectosIndependientes`](./ejercicios/ProyectosIndependientes/) (`examen-personas` y `sorteos`) |

Cada ejercicio conserva sus fuentes, manifiesto, recursos y configuración dentro de su módulo `app`. Por ejemplo, `ejercicios/Ud4/Ejercicio2/app` corresponde al módulo Gradle `:ud4-ejercicio2`. `examen-personas` y `sorteos` también son módulos de este proyecto; los avatares de `examen-personas` son vectores provisionales que se pueden sustituir en `res/drawable/`.

Para compilar un módulo desde PowerShell:

```powershell
.\gradlew.bat :ud4-ejercicio2:assembleDebug
```
