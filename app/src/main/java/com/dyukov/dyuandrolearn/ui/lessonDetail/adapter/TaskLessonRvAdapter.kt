package com.dyukov.dyuandrolearn.ui.lessonDetail.adapter

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
import com.dyukov.dyuandrolearn.data.db.model.Task

class TaskLessonRvAdapter(private var context: Context) :
    RecyclerView.Adapter<TaskLessonRvAdapter.TaskViewHolder>() {

    var tasks: List<Task>? = null

    fun setItems(tasks: List<Task>?) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    private var onClickListener: OnItemClicked? = null

    fun setOnClickListener(listener: OnItemClicked) {
        this.onClickListener = listener
    }

    interface OnItemClicked {
        fun onItemClick(position: Int, task: Task)
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
        var ivExperience: ImageView
        var tvExpCount: TextView

        init {
            cl = itemView.findViewById(R.id.cl_task)
            taskImage = itemView.findViewById(R.id.iv_task_icon) as ImageView
            ivExperience = itemView.findViewById(R.id.iv_experience) as ImageView
            taskName = itemView.findViewById(R.id.tv_task_name)
            tvExpCount = itemView.findViewById(R.id.tv_exp_count)
        }
    }

    override fun getItemCount(): Int {
        return tasks?.size ?: 0
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
//        val taskModel = tasks?.get(position)
//        when (taskModel?.type) {
//            TYPE_TASK_PART_LESSON -> {
//                holder.taskImage.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        context,
//                        R.drawable.ic_task_android
//                    )
//                )
//                holder.taskName.text = taskModel.theoryData?.name
//
//            }
//            TYPE_TASK_PRACTICE -> {
//                holder.taskImage.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        context,
//                        R.drawable.ic_quiz
//                    )
//                )
//                holder.taskName.text = "Тест"
//            }
//        }
//        if (taskModel?.done == true) {
//            holder.cl.setBackgroundResource(R.drawable.ic_tast_green)
//            holder.ivExperience.visible()
//            holder.tvExpCount.visible()
//            holder.tvExpCount.text = taskModel.points.toString()
//            holder.ivExperience.setImageDrawable(
//                ContextCompat.getDrawable(
//                    context,
//                    R.drawable.ic_experience_green_back
//                )
//            )
//
//        } else {
//            holder.cl.setBackgroundResource(R.drawable.ic_tast_grey)
//            holder.ivExperience.visible()
//            holder.tvExpCount.visible()
//            holder.tvExpCount.text = taskModel?.points.toString()
//            holder.ivExperience.setImageDrawable(
//                ContextCompat.getDrawable(
//                    context,
//                    R.drawable.ic_experience_back
//                )
//            )
//        }
//        holder.cl.setOnClickListener {
//            onClickListener?.onItemClick(position, taskModel!!)
//        }
    }
}