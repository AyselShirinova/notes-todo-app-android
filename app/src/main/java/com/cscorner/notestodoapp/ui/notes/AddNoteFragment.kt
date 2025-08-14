package com.cscorner.notestodoapp.ui.notes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.cscorner.notestodoapp.R
import com.cscorner.notestodoapp.data.AppDatabase
import com.cscorner.notestodoapp.data.Note
import com.cscorner.notestodoapp.data.NoteDao
import com.cscorner.notestodoapp.databinding.FragmentAddNoteBinding
import kotlinx.coroutines.launch

class AddNoteFragment : Fragment() {

    private lateinit var binding:FragmentAddNoteBinding

    private lateinit var db: AppDatabase
    private lateinit var dao: NoteDao

    private var existingNote: Note? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db=AppDatabase.getDatabase(requireContext())
        dao=db.noteDao()

        existingNote=arguments?.getSerializable("note") as? Note
        existingNote?.let{ note ->
            binding.etNoteTitle.setText(note.title)
            binding.etNoteContent.setText(note.note)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAddNoteBinding.inflate(inflater,container,false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_note,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.action_save){
            saveNote()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveNote() {
        val title=binding.etNoteTitle.text.toString()
        val content=binding.etNoteContent.text.toString()
        Log.d("AddNoteFragment","saveNote called with title: $title, Content: $content")

        if (content.isNotEmpty()){
            val note = if(existingNote!=null){
                existingNote!!.copy(
                    title=title,
                    note=content,
                    date=System.currentTimeMillis()
                )
            }else{
                Note(
                    title=title,
                    note=content,
                    date=System.currentTimeMillis()
                )
            }
            lifecycleScope.launch {
                if ( existingNote != null){
                    dao.updateNote(note)
                    Toast.makeText(requireContext(),"Note Updated",Toast.LENGTH_SHORT).show()
                }else{
                    dao.addNote(note)
                    Toast.makeText(requireContext(),"Note Added",Toast.LENGTH_SHORT).show()
                }
                findNavController().navigateUp()
            }
        }else{
            Toast.makeText(requireContext(),"Content cannot be empty",Toast.LENGTH_SHORT).show()
        }
    }
}
