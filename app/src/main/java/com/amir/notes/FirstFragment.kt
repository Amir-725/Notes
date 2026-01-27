package com.amir.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.amir.notes.databinding.FragmentFirstBinding
import com.amir.notes.databinding.NoteItemBinding

class NotesFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteRepository: NoteRepository
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteRepository = NoteRepository(requireContext())

        notesAdapter = NotesAdapter(
            onNoteClick = { note -> showAddEditNoteBottomSheet(note) },
            onNoteLongClick = { note -> showNoteActionsBottomSheet(note) }
        )

        binding.notesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = notesAdapter
        }

        loadNotes()
    }

    fun loadNotes() {
        val notes = noteRepository.getNotes()
        notesAdapter.submitList(notes)
    }

    fun showAddEditNoteBottomSheet(note: Note?) {
        val bottomSheet = AddEditNoteBottomSheet.newInstance(note)
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun showNoteActionsBottomSheet(note: Note) {
        val bottomSheet = NoteActionsBottomSheet.newInstance(note)
        bottomSheet.onEdit = {
            showAddEditNoteBottomSheet(note)
        }
        bottomSheet.onDelete = {
            noteRepository.deleteNote(note.id)
            loadNotes()
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    override fun onResume() {
        super.onResume()
        loadNotes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class NotesAdapter(
    private val onNoteClick: (Note) -> Unit,
    private val onNoteLongClick: (Note) -> Unit
) : androidx.recyclerview.widget.RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private var notes: List<Note> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position], onNoteClick, onNoteLongClick)
    }

    override fun getItemCount(): Int = notes.size

    fun submitList(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }

    class NoteViewHolder(private val binding: NoteItemBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note, onNoteClick: (Note) -> Unit, onNoteLongClick: (Note) -> Unit) {
            binding.titleTextView.text = note.title
            binding.contentTextView.text = note.content
            binding.root.setOnClickListener { onNoteClick(note) }
            binding.root.setOnLongClickListener { onNoteLongClick(note); true }
        }
    }
}