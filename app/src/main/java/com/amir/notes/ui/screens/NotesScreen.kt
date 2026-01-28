package com.amir.notes.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.amir.notes.Note
import com.amir.notes.ui.composables.GlassTopAppBar
import com.amir.notes.ui.composables.NoteActionsSheet
import com.amir.notes.ui.composables.NoteItem
import com.amir.notes.viewmodel.NotesViewModel

@Composable
fun NotesScreen(navController: NavController, viewModel: NotesViewModel = viewModel()) {
    val notes by viewModel.notes.collectAsState()
    var showAddSheet by remember { mutableStateOf(false) }
    var showActionsSheet by remember { mutableStateOf<Note?>(null) }
    var noteToEdit by remember { mutableStateOf<Note?>(null) }

    Scaffold(
        topBar = { GlassTopAppBar(title = "Notes", onSettingsClick = { navController.navigate("settings") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddSheet = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add note")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE0C3FC),
                            Color(0xFF8EC5FC)
                        )
                    )
                )
        ) {
            LazyColumn {
                items(notes) { note ->
                    NoteItem(
                        note = note,
                        onClick = { noteToEdit = note; showAddSheet = true },
                        onLongClick = { showActionsSheet = note }
                    )
                }
            }

            if (showAddSheet) {
                AddEditNoteScreen(
                    note = noteToEdit,
                    onDismiss = { showAddSheet = false; noteToEdit = null }
                )
            }

            showActionsSheet?.let { note ->
                NoteActionsSheet(
                    onDismiss = { showActionsSheet = null },
                    onEditClick = { showActionsSheet = null; noteToEdit = note; showAddSheet = true },
                    onDeleteClick = { viewModel.deleteNote(note.id); showActionsSheet = null }
                )
            }
        }
    }
}