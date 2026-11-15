package com.example.ud4_ejemplo3.datos

// Numero de rondas que dura la partida.
const val RONDAS = 2

// Puntos que se ganan por cada acierto.
const val PUNTOS_POR_ACIERTO = 5

// Errores permitidos para cada palabra.
const val MAX_ERRORES = 5

// Palabras que pueden aparecer en el juego.
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
