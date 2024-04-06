package com.example.pestotech.views.home


import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pestotech.R
import com.example.pestotech.databinding.FragmentUpdateTaskBinding
import com.example.pestotech.model.Task
import com.example.pestotech.utils.showToast
import com.example.pestotech.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateTaskFragment : Fragment() {
    private lateinit var binding: FragmentUpdateTaskBinding
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var task: Task

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentUpdateTaskBinding.inflate(inflater, container, false).also {
        binding = it
        arguments.let {
            val args = UpdateTaskFragmentArgs.fromBundle(requireArguments())
            task = args.task
            setUI(task)

        }
    }.root

    private fun setUI(task: Task) {
        binding.apply {
            etTitle.setText(task.title)
            etDescription.setText(task.description)
            tvStatus.text = task.status
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvStatus.setOnClickListener { setSpinner() }
        binding.tvUpdate.setOnClickListener { updateTask() }

    }

    private fun setSpinner() {
        val popupMenu = PopupMenu(requireContext(), binding.tvStatus)
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            binding.tvStatus.text = menuItem.title

            true
        }
        popupMenu.show()

    }


    private fun updateTask() {
        val title = binding.etTitle.text.toString()
        val description = binding.etDescription.text.toString()
        val status = binding.tvStatus.text.toString()
        val task = Task(task.taskId, title, description, status)
        if (isValidated(title, description, status)) {
            viewModel.updateTask(task)
            findNavController().popBackStack()

        } else {
            context?.showToast(getString(R.string.please_enter_the_details))
        }

    }

    private fun isValidated(title: String, desc: String, status: String): Boolean {
        return !(title.isEmpty() || desc.isEmpty() || status.isEmpty())
    }

}