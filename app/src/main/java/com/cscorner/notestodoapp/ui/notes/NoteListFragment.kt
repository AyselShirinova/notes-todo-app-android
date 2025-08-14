package com.cscorner.notestodoapp.ui.notes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cscorner.notestodoapp.R
import com.cscorner.notestodoapp.data.AppDatabase
import com.cscorner.notestodoapp.data.Note
import com.cscorner.notestodoapp.databinding.FragmentNoteListBinding
import kotlinx.coroutines.launch

class NoteListFragment : Fragment() {

    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NoteAdapter
    private var allNotes: List<Note> = emptyList()

    private fun filterNotes(query: String) {
        val filteredNotes = if (query.isEmpty()) {
            allNotes
        } else {
            allNotes.filter {
                it.title.contains(query, ignoreCase = true) || it.note.contains(
                    query,
                    ignoreCase = true
                )
            }
        }
        adapter.submitList(filteredNotes)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = NoteAdapter { selectedNote ->
            val bundle = Bundle().apply {
                putSerializable("note", selectedNote)
            }

            findNavController().navigate(
                R.id.action_notes_fragment_to_addNoteFragment, bundle
            )
        }

        binding.rvNoteList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNoteList.adapter = adapter

        val dao = AppDatabase.getDatabase(requireContext()).noteDao()

        lifecycleScope.launch {
            dao.getAllNotes().collect { notes ->
                Log.d("NoteListFragment", "Notes received: ${notes.size}")
                allNotes = notes
                adapter.submitList(notes)
            }
        }
        parentFragmentManager.setFragmentResultListener(
            "searchQuery",
            viewLifecycleOwner
        ) { _, bundle ->
            val query = bundle.getString("query") ?: ""
            filterNotes(query)
        }

//swipe to delete
        val itemTouchHelper = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val noteToDelete = adapter.currentList[viewHolder.adapterPosition]
                lifecycleScope.launch {
                    dao.deleteNote(noteToDelete)
                    Toast.makeText(requireContext(), "Note Deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvNoteList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}