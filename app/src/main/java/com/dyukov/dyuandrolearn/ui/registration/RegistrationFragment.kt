package com.dyukov.dyuandrolearn.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.data.db.DyuAndroDatabase_Impl
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class RegistrationFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: RegistrationViewModelFactory by instance<RegistrationViewModelFactory>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = factory.create(RegistrationViewModel::class.java)

    }

}