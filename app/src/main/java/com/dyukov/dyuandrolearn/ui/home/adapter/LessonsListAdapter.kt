package com.dyukov.dyuandrolearn.ui.home.adapter

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseListAdapter
import com.dyukov.dyuandrolearn.base.BaseViewHolder
import com.dyukov.dyuandrolearn.data.db.model.Lesson
import com.dyukov.dyuandrolearn.databinding.ItemLessonBinding
import com.dyukov.dyuandrolearn.databinding.ItemLessonDoneBinding
import com.dyukov.dyuandrolearn.databinding.ItemLessonProgressBinding
import com.dyukov.dyuandrolearn.databinding.ItemLessonUnavailableBinding
import com.dyukov.dyuandrolearn.extensions.gone
import com.dyukov.dyuandrolearn.extensions.visible
import kotlin.math.roundToInt

class LessonsListAdapter : BaseListAdapter<LessonsItemState, Lesson>(DIFF_CALLBACK) {

    override fun itemState(item: Lesson, listener: Listener<Lesson>): LessonsItemState =
        LessonsItemState(item, listener as Listener<Lesson>)

    override fun layoutResId(): Int = R.layout.item_lesson
    private fun progressLayoutResId() = R.layout.item_lesson_progress
    private fun doneLayoutResId() = R.layout.item_lesson_done
    private fun unavailableLayoutResId() = R.layout.item_lesson_unavailable
    val tasksDonePercent = MutableLiveData(0f)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<LessonsItemState, Lesson> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_AVAILABLE -> {
                val itemBinding: ItemLessonBinding =
                    DataBindingUtil.inflate(inflater, layoutResId(), parent, false)
                BaseViewHolder(itemBinding)
            }
            TYPE_PROGRESS -> {
                val itemBinding: ItemLessonProgressBinding =
                    DataBindingUtil.inflate(inflater, progressLayoutResId(), parent, false)
                BaseViewHolder(itemBinding)
            }
            TYPE_DONE -> {
                val itemBinding: ItemLessonDoneBinding =
                    DataBindingUtil.inflate(inflater, doneLayoutResId(), parent, false)
                BaseViewHolder(itemBinding)
            }
            TYPE_UNAVAILABLE -> {
                val itemBinding: ItemLessonUnavailableBinding =
                    DataBindingUtil.inflate(inflater, unavailableLayoutResId(), parent, false)
                BaseViewHolder(itemBinding)
            }

            else -> throw RuntimeException("Invalid type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<LessonsItemState, Lesson>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (getItemViewType(position) == TYPE_PROGRESS) {
            val percent = tasksDonePercent.value ?: 0f
            if (currentList[position].isShowForUser == true) {
                (holder.binding as ItemLessonProgressBinding).apply {
                    val context = root.context
                    vLeft.visible()
                    vRight.visible()
                    vLeft.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake));
                    vRight.startAnimation(
                        AnimationUtils.loadAnimation(
                            context,
                            R.anim.shake
                        )
                    );
                }
            } else {
                (holder.binding as ItemLessonProgressBinding).apply {
                    vLeft.gone()
                    vRight.gone()
                }
            }
            (holder.binding as ItemLessonProgressBinding).tvAvailability.text =
                "${percent.times(100)?.roundToInt().toString()} % "
            val guideline = (holder.binding as ItemLessonProgressBinding).guideline
            val end = tasksDonePercent.value ?: 0f
            val valueAnimator = ValueAnimator.ofFloat(0f, end)
            valueAnimator.duration = 500
            valueAnimator.interpolator = AccelerateDecelerateInterpolator()
            valueAnimator.addUpdateListener { valueAnimator ->
                val lp = guideline.layoutParams as ConstraintLayout.LayoutParams
                lp.guidePercent = valueAnimator.animatedValue as Float
                guideline.layoutParams = lp
            }
            valueAnimator.start()
        } else if (getItemViewType(position) == TYPE_AVAILABLE) {
            if (currentList[position].isShowForUser == true) {
                (holder.binding as ItemLessonBinding).apply {
                    val context = root.context
                    vLeft.visible()
                    vRight.visible()
                    vLeft.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake));
                    vRight.startAnimation(
                        AnimationUtils.loadAnimation(
                            context,
                            R.anim.shake
                        )
                    )
                }
            } else {
                (holder.binding as ItemLessonBinding).apply {
                    vLeft.gone()
                    vRight.gone()
                }
            }

        }
    }

    override fun getItemViewType(position: Int) =
        when ((getItem(position))?.type) {
            1 -> TYPE_AVAILABLE
            2 -> TYPE_PROGRESS
            3 -> TYPE_DONE
            4 -> TYPE_UNAVAILABLE
            else -> throw RuntimeException("Invalid type")
        }

    companion object {
        const val TYPE_AVAILABLE = 1
        const val TYPE_PROGRESS = 2
        const val TYPE_DONE = 3
        const val TYPE_UNAVAILABLE = 4

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Lesson>() {
            override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson): Boolean =
                oldItem.id == newItem.id && oldItem.isShowForUser == newItem.isShowForUser

            override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson): Boolean =
                oldItem == newItem
        }
    }

}