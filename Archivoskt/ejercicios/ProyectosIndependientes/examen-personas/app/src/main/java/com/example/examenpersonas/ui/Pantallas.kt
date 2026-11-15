package com.example.examenpersonas.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.examenpersonas.R
import com.example.examenpersonas.datos.CURSOS
import com.example.examenpersonas.modelo.Alumno
import com.example.examenpersonas.modelo.Persona
import com.example.examenpersonas.modelo.Profesor
import com.example.examenpersonas.modelo.Rol

// ---------- PANTALLA LISTADO ----------

// Muestra la lista de personas.
@Composable
fun PantallaListado(
    personas: List<Persona>,
    onPersonaClick: (Persona) -> Unit,
    onAñadirClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalAlumnos = personas.count { it is Alumno }
    val totalProfesores = personas.count { it is Profesor }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = onAñadirClick) {
                Text("+", style = MaterialTheme.typography.headlineMedium)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Examen 2ºDAM",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )

            HorizontalDivider()

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(personas) { persona ->
                    TarjetaPersona(
                        persona = persona,
                        onClick = { onPersonaClick(persona) }
                    )
                }
            }

            HorizontalDivider()

            Text("Total: ${personas.size}", style = MaterialTheme.typography.titleMedium)
            Text("Total Alumnos: $totalAlumnos", style = MaterialTheme.typography.titleMedium)
            Text("Total Profesores: $totalProfesores", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
        }
    }
}

// Muestra los datos de una persona.
@Composable
fun TarjetaPersona(persona: Persona, onClick: () -> Unit) {
    val esAlumno = persona is Alumno

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, MaterialTheme.colorScheme.primaryContainer)
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(if (esAlumno) R.drawable.alumno else R.drawable.profesor),
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = if (esAlumno) "Alumno" else "Profesor",
            style = MaterialTheme.typography.titleMedium,
            fontStyle = FontStyle.Italic
        )
        Text(text = persona.nombre, style = MaterialTheme.typography.bodySmall)
    }
}

// ---------- PANTALLA AÑADIR PERSONA ----------

// Muestra esta parte de la pantalla.
@Composable
fun PantallaAñadirPersona(
    viewModel: PersonasViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Añadir Persona", style = MaterialTheme.typography.headlineMedium)

        Text(
            text = "Código: ${viewModel.codigo}",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = viewModel.nombre,
            onValueChange = viewModel::actualizarNombre,
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text("Elige el rol", style = MaterialTheme.typography.titleMedium)

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = viewModel.rol == Rol.ALUMNO,
                onClick = { viewModel.cambiarRol(Rol.ALUMNO) }
            )
            Text("Alumno")
            Spacer(Modifier.width(16.dp))
            RadioButton(
                selected = viewModel.rol == Rol.PROFESOR,
                onClick = { viewModel.cambiarRol(Rol.PROFESOR) }
            )
            Text("Profesor")
        }

        if (viewModel.rol == Rol.ALUMNO) {
            FormularioAlumno(viewModel)
        } else {
            FormularioProfesor(viewModel)
        }

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { if (viewModel.añadirPersona()) onVolver() },
                enabled = viewModel.puedeAñadir
            ) {
                Text("Añadir")
            }
            OutlinedButton(onClick = onVolver) {
                Text("Cancelar")
            }
        }
    }
}

@Composable
private fun FormularioAlumno(viewModel: PersonasViewModel) {
    OutlinedTextField(
        value = viewModel.nia,
        onValueChange = viewModel::actualizarNia,
        label = { Text("NIA") },
        isError = viewModel.niaErroneo,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )

    Text("Curso", style = MaterialTheme.typography.titleMedium)
    CURSOS.forEach { c ->
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = viewModel.curso == c,
                onClick = { viewModel.actualizarCurso(c) }
            )
            Text(c)
        }
    }
}

@Composable
private fun FormularioProfesor(viewModel: PersonasViewModel) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Tutor", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.width(8.dp))
        Switch(
            checked = viewModel.tutor,
            onCheckedChange = viewModel::actualizarTutor
        )
    }

    Text("Cursos que imparte", style = MaterialTheme.typography.titleMedium)
    CURSOS.forEach { c ->
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = c in viewModel.cursosProfesor,
                onCheckedChange = { viewModel.alternarCursoProfesor(c) }
            )
            Text(c)
        }
    }
}
