package com.volio.order_food.model

data class FolderGalleryObj(
    val id: Long,
    val name: String,
    var path: String,
    var size: Int =0
)
