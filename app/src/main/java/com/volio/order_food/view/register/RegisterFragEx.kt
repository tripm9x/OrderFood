package com.volio.order_food.view.register

import android.util.Log
import com.volio.order_food.util.AppUtil
import com.volio.order_food.util.setPreventDoubleClick
import kotlinx.android.synthetic.main.fragment_register.*

fun RegisterFrag.initOnClick() {

    btnBackRegister.setPreventDoubleClick {
        onBackPress()
    }

    btnRegister.setPreventDoubleClick {
        userName = edtUsername.text.toString().trim()
        userPhone = edtPhone.text.toString().trim()
        passWord = edtPassword.text.toString().trim()
        passWordAgain = edtPasswordAgain.text.toString().trim()

        if (validateData()) {
            val user = hashMapOf(
                "user_name" to userName,
                "user_phone" to userPhone,
                "password" to passWord
            )
            db.collection("users")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    onBackPress()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                    AppUtil.showToast(requireContext(), "Some Thing Went Wrong!!!")
                }
        }
    }
}

fun RegisterFrag.validateData(): Boolean {
    var a = true
    if (userName.isNotEmpty()) {
        a = false
        edtUsername.error = "Name can't empty!"
        edtUsername.requestFocus()

    }
    if (userPhone.isNotEmpty()) {
        a = false
        edtPhone.error = "Phone can't empty!"
        edtPhone.requestFocus()
    }
    if (passWord.isNotEmpty()) {
        a = false
        edtPassword.error = "Password can't empty!"
        edtPassword.requestFocus()
    }
    if (passWordAgain.isNotEmpty()) {
        a = false
        edtPasswordAgain.error = "Password can't empty!"
        edtPasswordAgain.requestFocus()
    }
    if (passWordAgain != passWord) {
        a = false
        edtPasswordAgain.error = "Password not match!"
        edtPasswordAgain.requestFocus()
    }
    if (!a) {
        AppUtil.showToast(requireContext(), "Check validate!")
    }
    return a
}
