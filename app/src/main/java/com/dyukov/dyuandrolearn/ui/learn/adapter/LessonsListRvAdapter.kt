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
import com.dyukov.dyuandrolearn.data.db.model.Lesson
import com.dyukov.dyuandrolearn.data.network.LessonModel
import com.dyukov.dyuandrolearn.utils.Constants.TYPE_LESSON_BASIC
import com.dyukov.dyuandrolearn.utils.Constants.TYPE_LESSON_INTRO

class LessonsListRvAdapter(private var context: Context) :
    RecyclerView.Adapter<LessonsListRvAdapter.LessonVieHolder>() {

    var lessons: List<Lesson>? = null

    fun setItems(lessons: List<Lesson>?) {
        this.lessons = lessons
        notifyDataSetChanged()
    }

    private var onClickListener: OnItemClicked? = null

    fun setOnClickListener(listener: OnItemClicked) {
        this.onClickListener = listener
    }

    interface OnItemClicked {
        fun onItemClick(position: Int, lesson: Lesson)
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
        var ivExperience: ImageView
        var expCount: TextView

        init {
            cl = itemView.findViewById(R.id.cl_task)
            taskImage = itemView.findViewById(R.id.iv_lesson_icon) as ImageView
            ivExperience = itemView.findViewById(R.id.iv_experience) as ImageView
            lessonName = itemView.findViewById(R.id.tv_lesson_name)
            expCount = itemView.findViewById(R.id.tv_exp_count)
        }
    }

    override fun getItemCount(): Int {
        return lessons?.size ?: 0
    }

    override fun onBindViewHolder(holder: LessonVieHolder, position: Int) {
        val lessonModel = lessons?.get(position)
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
        if (lessonModel?.done == true) {
            holder.cl.setBackgroundResource(R.drawable.ic_lesson_green)
            holder.expCount.text = lessonModel.points.toString()
            holder.ivExperience.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_experience_green_back
                )
            )
        }
        holder.lessonName.text = lessonModel?.name
        holder.expCount.text = lessonModel?.points.toString()
        holder.cl.setOnClickListener {
            lessonModel?.let {
                onClickListener?.onItemClick(position, it)
            }
        }
    }
}