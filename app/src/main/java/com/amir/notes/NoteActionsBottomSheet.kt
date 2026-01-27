package com.amir.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.amir.notes.databinding.BottomSheetNoteActionsBinding

class NoteActionsBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetNoteActionsBinding? = null
    private val binding get() = _binding!!

    private var note: Note? = null

    var onEdit: (() -> Unit)? = null
    var onDelete: (() -> Unit)? = null

    override fun getTheme(): Int = R.style.ThemeOverlay_App_BottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomSheetNoteActionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        note = arguments?.getParcelable(ARG_NOTE)

        binding.actionEdit.setOnClickListener {
            onEdit?.invoke()
            dismiss()
        }

        binding.actionDelete.setOnClickListener {
            onDelete?.invoke()
            dismiss()
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

        fun newInstance(note: Note): NoteActionsBottomSheet {
            val fragment = NoteActionsBottomSheet()
            val args = Bundle()
            args.putParcelable(ARG_NOTE, note)
            fragment.arguments = args
            return fragment
        }
    }
}