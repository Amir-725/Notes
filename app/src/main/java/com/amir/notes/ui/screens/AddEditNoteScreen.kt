package com.amir.notes.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amir.notes.Note
import com.amir.notes.R
import com.amir.notes.viewmodel.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(note: Note?, onDismiss: () -> Unit, viewModel: NotesViewModel = viewModel()) {
    val sheetState = rememberModalBottomSheetState()
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    val context = LocalContext.current

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(modifier = Modifier.padding(16.dp)) {
            TextField(value = title, onValueChange = { title = it }, label = { Text(stringResource(id = R.string.title_hint)) }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = content, onValueChange = { content = it }, label = { Text(stringResource(id = R.string.content_hint)) }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (title.isNotBlank()) {
                    if (note == null) {
                        viewModel.addNote(title, content)
                    } else {
                        viewModel.updateNote(note.copy(title = title, content = content))
                    }
                    onDismiss()
                } else {
                    Toast.makeText(context, context.getString(R.string.title_empty_error), Toast.LENGTH_SHORT).show()
                }
            }) {
                Text(stringResource(id = R.string.save_button))
            }
        }
    }
}