package com.volio.order_food.model

import android.graphics.Bitmap

data class ImageGalleryObj(
    val id: Long,
    var pathImage: String,
    var countSelected: Int=0,
    var positionInAdapter: Int = 0,
    var bitmap: Bitmap? = null
)