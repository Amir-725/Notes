package com.amir.notes

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.amir.notes.databinding.BottomSheetAddEditNoteBinding

class AddEditNoteBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddEditNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteRepository: NoteRepository
    private var note: Note? = null

    override fun getTheme(): Int = R.style.ThemeOverlay_App_BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteRepository = NoteRepository(requireContext())
        note = arguments?.getParcelable(ARG_NOTE)

        note?.let {
            binding.titleEditText.setText(it.title)
            binding.contentEditText.setText(it.content)
        }

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()

            if (title.isNotBlank()) {
                if (note == null) {
                    noteRepository.addNote(title, content)
                } else {
                    val updatedNote = note!!.copy(title = title, content = content)
                    noteRepository.updateNote(updatedNote)
                }
                (parentFragment as? NotesFragment)?.loadNotes()
                dismiss()
            } else {
                // TODO: Show error
            }
        }

        val radius = 20f
        val decorView = requireActivity().window.decorView
        val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
        val windowBackground = decorView.background

        binding.blurView.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurRadius(radius)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_NOTE = "note"

        fun newInstance(note: Note? = null): AddEditNoteBottomSheet {
            val fragment = AddEditNoteBottomSheet()
            val args = Bundle()
            note?.let { args.putParcelable(ARG_NOTE, it) }
            fragment.arguments = args
            return fragment
        }
    }
}