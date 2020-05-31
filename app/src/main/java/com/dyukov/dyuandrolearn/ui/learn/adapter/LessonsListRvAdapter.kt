package com.dyukov.dyuandrolearn.ui.learn.adapter

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
import com.dyukov.dyuandrolearn.data.network.LessonModel
import com.dyukov.dyuandrolearn.utils.Constants.TYPE_LESSON_BASIC
import com.dyukov.dyuandrolearn.utils.Constants.TYPE_LESSON_INTRO

class LessonsListRvAdapter(context: Context) :
    RecyclerView.Adapter<LessonsListRvAdapter.LessonVieHolder>() {

    var tasks: List<LessonModel>? = null
    private var context: Context = context

    fun setItems(tasks: List<LessonModel>?) {
        this.tasks = tasks
    }

    private var onClickListener: OnItemClicked? = null

    fun setOnClickListener(listener: OnItemClicked) {
        this.onClickListener = listener
    }

    interface OnItemClicked {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): LessonVieHolder {
        val v: View =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_lesson, viewGroup, false)
        return LessonVieHolder(v)
    }

    class LessonVieHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var cl: ConstraintLayout
        var lessonName: TextView
        var taskImage: ImageView
        var expCount: TextView

        init {
            cl = itemView.findViewById(R.id.cl_task)
            taskImage = itemView.findViewById(R.id.iv_lesson_icon) as ImageView
            lessonName = itemView.findViewById(R.id.tv_lesson_name)
            expCount = itemView.findViewById(R.id.tv_exp_count)
        }
    }

    override fun getItemCount(): Int {
        return tasks?.size ?: 0
    }

    override fun onBindViewHolder(holder: LessonVieHolder, position: Int) {
        val lessonModel = tasks?.get(position)
        when (lessonModel?.type) {
            TYPE_LESSON_INTRO -> {
                holder.taskImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_taks_introduction
                    )
                )
            }
            TYPE_LESSON_BASIC -> {
                holder.taskImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_task_android
                    )
                )
            }
        }

        holder.lessonName.text = lessonModel?.lessonName
        holder.expCount.text = lessonModel?.experienceCount.toString()
        holder.cl.setOnClickListener {
            onClickListener?.onItemClick(position)
        }
    }
}