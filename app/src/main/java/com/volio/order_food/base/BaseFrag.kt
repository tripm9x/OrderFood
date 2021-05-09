package com.volio.order_food.base


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.volio.order_food.R

abstract class BaseFrag(private val layout: Int) : Fragment() {
    protected abstract fun onCreatedView()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layout, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onCreatedView()
    }


    public fun gotoFrag(currentId: Int, action: Int, bundle: Bundle? = null) {
        try {
            if (findNavController().currentDestination!!.id == currentId) {
                findNavController().navigate(action, bundle)
            }
        } catch (e: Exception) {

        }
    }

    public fun onBackToHome() {
        try {
            findNavController().popBackStack(R.id.homeFrag, false)
        } catch (e: Exception) {

        }
    }

    public fun onBackPress() {
        try {
            findNavController().popBackStack()
        } catch (e: Exception) {

        }
    }

    public fun onBackFinish() {
        try {
            activity?.onBackPressedDispatcher?.addCallback(this, true) {
                activity?.finish()
            }
        } catch (e: Exception) {

        }
    }


}