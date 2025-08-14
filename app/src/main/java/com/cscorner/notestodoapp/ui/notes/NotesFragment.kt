package com.cscorner.notestodoapp.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cscorner.notestodoapp.R
import com.cscorner.notestodoapp.databinding.FragmentNotesBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noteListFragment = NoteListFragment()
        childFragmentManager.beginTransaction().replace(R.id.note_list_fragment, noteListFragment)
            .commit()

        val addNote = view.findViewById<FloatingActionButton>(R.id.add_note_button)
        addNote.setOnClickListener {
            findNavController().navigate(R.id.action_notes_fragment_to_addNoteFragment)
        }

        binding.NotesSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText.orEmpty()

                val bundle = Bundle().apply {
                    putString("query", newText)
                }

                parentFragmentManager.setFragmentResult("searchQuery", bundle)
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}