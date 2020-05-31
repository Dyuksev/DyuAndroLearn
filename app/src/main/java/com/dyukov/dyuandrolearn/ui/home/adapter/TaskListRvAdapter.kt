package com.dyukov.dyuandrolearn.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.data.network.TaskModel
import com.dyukov.dyuandrolearn.utils.Constants.TYPE_TASK_MORE
import com.dyukov.dyuandrolearn.utils.Constants.TYPE_TASK_PART_LESSON
import com.dyukov.dyuandrolearn.utils.Constants.TYPE_TASK_PRACTICE
import com.dyukov.dyuandrolearn.utils.Constants.TYPE_TASK_THEORY


class TaskListRvAdapter(context: Context) :
    RecyclerView.Adapter<TaskListRvAdapter.TaskViewHolder>() {

    var tasks: List<TaskModel>? = null
    private var context: Context = context

    fun setItems(tasks: List<TaskModel>?) {
        this.tasks = tasks
    }

    private var onClickListener: OnItemClicked? = null

    fun setOnClickListener(listener: OnItemClicked) {
        this.onClickListener = listener
    }

    interface OnItemClicked {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): TaskViewHolder {
        val v: View =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_task, viewGroup, false)
        return TaskViewHolder(v)
    }

    class TaskViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var cl: ConstraintLayout
        var taskName: TextView
        var taskImage: ImageView

        init {
            cl = itemView.findViewById(R.id.cl_task)
            taskImage = itemView.findViewById(R.id.iv_task_icon) as ImageView
            taskName = itemView.findViewById(R.id.tv_task_name)
        }
    }

    override fun getItemCount(): Int {
        return tasks?.size ?: 0
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val taskModel = tasks?.get(position)
        when (taskModel?.taskType) {
            TYPE_TASK_PART_LESSON -> {
                holder.cl.setBackgroundResource(R.drawable.ic_tast_blue)
                holder.taskImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_taks_introduction
                    )
                )
            }
            TYPE_TASK_THEORY -> {
                holder.cl.setBackgroundResource(R.drawable.ic_tast_yellow)
                holder.taskImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_task_android
                    )
                )
            }
            TYPE_TASK_PRACTICE -> {
                holder.cl.setBackgroundResource(R.drawable.ic_tast_red)
                holder.taskImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_quiz
                    )
                )
            }
            TYPE_TASK_MORE -> {
                holder.cl.setBackgroundResource(R.drawable.ic_tast_grey)
                holder.taskImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_task_more
                    )
                )
            }
        }
        holder.taskName.text = taskModel?.tastName
        holder.cl.setOnClickListener {
            onClickListener?.onItemClick(position)
        }
    }
}