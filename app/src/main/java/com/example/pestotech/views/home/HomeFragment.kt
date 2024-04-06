package com.example.pestotech.views.home

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pestotech.LinearLayoutManagerWrapper
import com.example.pestotech.R
import com.example.pestotech.adapter.MainAdapter
import com.example.pestotech.callbacks.ItemClickListener
import com.example.pestotech.callbacks.TaskItemListener
import com.example.pestotech.databinding.FragmentHomeBinding
import com.example.pestotech.model.Task
import com.example.pestotech.service.NetworkResult
import com.example.pestotech.utils.ProgressBarUtility
import com.example.pestotech.utils.showToast
import com.example.pestotech.viewmodel.TaskViewModel
import com.example.pestotech.views.acitvity.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), ItemClickListener<Task> {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var taskAdapter: MainAdapter
    private lateinit var progressDialog: Dialog
    private val viewModel: TaskViewModel by viewModels()
    private var taskList = mutableListOf<Task>()

    @Inject
    lateinit var mAuth: FirebaseAuth

    @Inject
    lateinit var firebaseDatabase: FirebaseDatabase


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentHomeBinding.inflate(inflater, container, false).also { it ->
        binding = it

    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        getTask()
        initClickLister()
        progressDialog = requireActivity().let { ProgressBarUtility.getProgressDialog(it) }

        binding.createTaskTv.setOnClickListener {
            val bottomSheet = CreateTaskFragment(requireContext(), object : TaskItemListener<Task> {
                override fun onTaskAdded(task: Task) {
                    viewModel.addTask(task)
                    taskAdapter.notifyItemRangeChanged(0, taskList.size)

                }

            })
            bottomSheet.show(childFragmentManager, getString(R.string.create_task))
        }
    }

    private fun initClickLister() {
        binding.apply {
            ivMenu.setOnClickListener { mainMenu() }
            ivFilter.setOnClickListener { filterMenu() }
        }
    }


    private fun initAdapter() {

        taskAdapter = MainAdapter(requireContext(), this)
        val layoutManager =
            LinearLayoutManagerWrapper(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.taskRv.layoutManager = layoutManager
        binding.taskRv.adapter = taskAdapter

    }

    private fun getTask() {
        viewModel.getTask()
        viewModel.task.observe(viewLifecycleOwner) { result ->

            kotlin.run {
                when (result) {
                    is NetworkResult.Loading -> {
                        progressDialog.show()
                    }

                    is NetworkResult.Success -> {
                        progressDialog.hide()
                        taskList.clear()
                        taskList = result.data as MutableList<Task>
                        if (taskList.isEmpty()) {
                            initAdapter()
                            binding.apply {
                                taskRv.visibility = View.GONE
                                listTaskTv.visibility = View.GONE
                                tvCreateTask.visibility = View.VISIBLE
                            }
                        } else {
                            binding.apply {
                                taskRv.visibility = View.VISIBLE
                                listTaskTv.visibility = View.VISIBLE
                                tvCreateTask.visibility = View.GONE
                            }
                            taskAdapter.setData(taskList)
                        }

                    }

                    is NetworkResult.Error -> {
                        progressDialog.hide()
                        context?.showToast(result.message)

                    }

                    else -> {}
                }
            }
        }
    }

    private fun mainMenu() {
        val popupMenu = PopupMenu(requireContext(), binding.ivMenu)
        popupMenu.menuInflater.inflate(R.menu.main_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->

            when (menuItem.title) {
                context?.getString(R.string.sign_out) -> {
                    mAuth.signOut()
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity((intent))
                    activity?.finish()

                }
            }

            true
        }
        popupMenu.show()
    }

    private fun filterMenu() {
        val popupMenu = PopupMenu(requireContext(), binding.ivMenu)
        popupMenu.menuInflater.inflate(R.menu.filter_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            taskAdapter.filter.filter(menuItem.title)

            true
        }
        popupMenu.show()
    }

    private fun updateTask(view: View, data: Task) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.task_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->

            when (menuItem.title) {
                context?.getString(R.string.update) -> {
                    val action = HomeFragmentDirections.actionNavigationHomeToUpdateTaskFragment(
                        task = data
                    )
                    findNavController().navigate(action)

                }

                context?.getString(R.string.delete) -> {
                    viewModel.deleteTask(data)

                }
            }

            true
        }
        popupMenu.show()

    }

    override fun onRecyclerItemClicked(position: Int, view: View, data: Task, type: String?) {
        updateTask(view, data)
    }


}