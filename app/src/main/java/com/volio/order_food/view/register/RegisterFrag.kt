package com.volio.order_food.view.register

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.volio.order_food.R
import com.volio.order_food.base.BaseFrag

const val TAG = "RegisterFrag"

class RegisterFrag : BaseFrag(R.layout.fragment_register) {

    val db = Firebase.firestore

    var userName = ""
    var userPhone = ""
    var passWord = ""
    var passWordAgain = ""
    override fun onCreatedView() {
        initOnClick()

    }
}