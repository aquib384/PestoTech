package com.example.pestotech.views.home


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import com.example.pestotech.R
import com.example.pestotech.callbacks.TaskItemListener
import com.example.pestotech.databinding.FragmentCreateTaskBinding
import com.example.pestotech.model.Task
import com.example.pestotech.utils.showToast
import com.example.pestotech.viewmodel.TaskViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CreateTaskFragment(
    private val context: Context,
    private val callback: TaskItemListener<Task>,

    ) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCreateTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateTaskBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvStatus.setOnClickListener { setSpinner() }
        binding.tvAdd.setOnClickListener { addTask() }

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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet!!)
        val layoutParams = bottomSheet.layoutParams
        val windowHeight = getWindowHeight() - 200
        if (layoutParams != null) {
            layoutParams.height = windowHeight
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    private fun addTask() {
        val title = binding.etTitle.text.toString()
        val description = binding.etDescription.text.toString()
        val status = binding.tvStatus.text.toString()
        val task = Task("", title, description, status)
        if (isValidated(title, description, status)) {
            dialog?.dismiss()
            callback.onTaskAdded(task)

        } else {
            context.showToast(getString(R.string.please_enter_the_details))
        }

    }

    private fun isValidated(title: String, desc: String, status: String): Boolean {
        return !(title.isEmpty() || desc.isEmpty() || status.isEmpty())
    }

}