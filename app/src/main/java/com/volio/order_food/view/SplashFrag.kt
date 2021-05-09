package com.volio.order_food.view

import android.os.Handler
import android.os.Looper
import com.volio.order_food.R
import com.volio.order_food.base.BaseFrag

class SplashFrag :BaseFrag(R.layout.fragment_splash) {
    override fun onCreatedView() {
        Handler(Looper.getMainLooper()).postDelayed({
            gotoFrag(R.id.splashFrag,R.id.action_splashFrag_to_loginFag)
        },2000)
    }
}