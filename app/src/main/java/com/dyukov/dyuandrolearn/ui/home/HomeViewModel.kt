package com.dyukov.dyuandrolearn.ui.home

import androidx.lifecycle.MutableLiveData
import com.dyukov.dyuandrolearn.base.BaseViewModel
import com.dyukov.dyuandrolearn.data.db.Repository
import com.dyukov.dyuandrolearn.data.db.model.*
import com.dyukov.dyuandrolearn.data.network.DyuData
import com.dyukov.dyuandrolearn.data.network.UserModel
import com.dyukov.dyuandrolearn.ui.home.adapter.LessonsListAdapter
import kotlinx.coroutines.*

class HomeViewModel(private val repository: Repository) : BaseViewModel() {
    val lessonsFromDb = MutableLiveData<ArrayList<Lesson>>()
    var tasks = MutableLiveData<ArrayList<Task>>()
    var user = MutableLiveData<User>()
    var userProgress = MutableLiveData<String>()
    val points = MutableLiveData("")
    lateinit var lesson: Lesson
    var isLessonReady = MutableLiveData(false)
    var tasksDonePercent = MutableLiveData(0f)
    var taskNav: ArrayList<Task> = ArrayList()

    fun findLessonInProgress() = lessonsFromDb.value?.first {
        it.type == LessonsListAdapter.TYPE_AVAILABLE || it.type == LessonsListAdapter.TYPE_PROGRESS
    }

    fun toDbLessons(appData: DyuData) {
        val tasksTemp = ArrayList<Task>()
        val lessonsTemp = ArrayList<Lesson>()
        appData.lessons?.forEach {

            val lesson = Lesson(
                id = it.id,
                type = it.type ?: 1,
                name = it.name.orEmpty(),
                points = it.points ?: 0,
                theoryId = it.theoryId ?: 1,
                tasksId = it.tasksId ?: arrayListOf()
            )
            lessonsTemp.add(lesson)
        }
        appData.tasks?.forEach {
            val task = Task(
                id = it.id,
                data = it.data,
                theoryId = it.theoryId ?: 0,
                points = it.points ?: 0,
                isPrimary = it.primary,
                done = it.done
            )
            tasksTemp.add(task)
        }
        lessonsFromDb.value = ArrayList(lessonsTemp)
        tasks.value = ArrayList(tasksTemp)
        insertAllTasks(tasksTemp)
        insertAllLessons(lessonsTemp)
        insertAllTheories(appData.theories!!)
        initLessons()
    }


    fun getTasksByLesson(lesson: Lesson) {
        GlobalScope.launch(Dispatchers.IO) {
            val tasks = ArrayList<Task>()

            lesson.tasksId?.forEach {
                val task = repository.getTasksFromLesson(it)
                tasks.add(task)
            }
            taskNav.addAll(tasks)
            isLessonReady.postValue(true)
        }
    }

    private fun initLessons() {
        var isFound = false
        val list = lessonsFromDb.value?.map {
            val isHasProgressTask = checkProgressTasks(tasks.value.orEmpty(), it.tasksId!!) ?: false
            if (user.value?.doneLessonsId?.size ?: 0 > 0 && it.id in user.value?.doneLessonsId!!) {
                it.copy(type = LessonsListAdapter.TYPE_DONE)
            } else if (!isFound) {
                if (isHasProgressTask) {
                    isFound = true
                    tasksDonePercent.value =
                        tasks.value?.filter { task ->
                            (task.id!! in it.tasksId!!) && task.done == true
                        }?.size?.toFloat()?.div(it.tasksId?.size ?: 1)


                    it.copy(type = LessonsListAdapter.TYPE_PROGRESS)
                } else {
                    isFound = true
                    it.copy(type = LessonsListAdapter.TYPE_AVAILABLE)
                }
            } else {
                it.copy(type = LessonsListAdapter.TYPE_UNAVAILABLE)
            }
        }.orEmpty()
        lessonsFromDb.postValue(ArrayList(list))
    }

    fun checkProgressTasks(list: List<Task>, lessonsId: ArrayList<Int>) =
        list.any { it.done == true && it.id in lessonsId }


    fun insertAllLessons(lesson: ArrayList<Lesson>) {
        GlobalScope.launch(Dispatchers.IO) {
            lesson.forEach {
                repository.insert(it)
            }

        }
    }

    fun insertAllTheories(theoryList: ArrayList<Theory>) {
        GlobalScope.launch(Dispatchers.IO) {
            theoryList.forEach {
                repository.insert(it)
            }
        }
    }

    fun insertAllTasks(lesson: ArrayList<Task>) {
        GlobalScope.launch(Dispatchers.IO) {
            lesson.forEach {
                repository.insert(it)
            }

        }
    }

    fun updateLessonData() {
        GlobalScope.launch(Dispatchers.IO) {
            val list: ArrayList<Lesson> = ArrayList<Lesson>()
            for (lesson in repository.getAllLessons()) {
                list.add(lesson)
            }
            updateTasksData()
        }
    }

    fun prepareToQuiz(): Task {
        val practiceData = ArrayList<PracticeData>()
        tasks.value?.forEach {
            practiceData.addAll(it.data.orEmpty())
        }
        val quizTask = Task(
            id = 1,
            data = practiceData,
            points = 150
        )
        return quizTask
    }

    fun updateTasksData() {
        GlobalScope.launch(Dispatchers.IO) {
            val list: ArrayList<Task> = ArrayList<Task>()
            for (task in repository.getAllTasks()) {
                list.add(task)
            }
            withContext(Dispatchers.Main) {
                tasks.value = list
                initLessons()
            }
        }
    }

    fun getUserModel() {
        GlobalScope.launch(Dispatchers.IO) {
            val userDb = repository.getUserData()
            user.postValue(userDb)
        }
    }

    fun convertToDbUserAndInsert(userModel: UserModel?) {
        userModel?.let {
            val user = User(
                it.name,
                it.email,
                it.level,
                it.progress,
                it.doneLessonsId,
                it.doneTheoryId
            )
            GlobalScope.launch(Dispatchers.IO) {
                repository.insert(user)
            }
        }
    }

    fun updateUser(newLevel: Int, points: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val oldUser = repository.getUserData()
            val newUser = oldUser.copy(
                level = newLevel,
                progress = points
            )
            repository.update(
                newUser
            )

            user.postValue(newUser)

        }
    }
}