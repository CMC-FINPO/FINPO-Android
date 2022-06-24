package com.finpo.app.model.remote

data class ParentCategoryResponse(
    val data: List<ParentCategory>
)

data class ParentCategory(
    val id: Int,
    val name: String,
    val img: String,
    val isChecked: Boolean = false
)
