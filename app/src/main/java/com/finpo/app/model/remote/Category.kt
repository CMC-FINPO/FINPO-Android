package com.finpo.app.model.remote

data class ParentCategoryResponse(
    val data: List<ParentCategory>
)

data class ParentCategory(
    val id: Int,
    val name: String,
    val img: String,
    var isChecked: Boolean = false
)

data class CategoryChildFormatResponse(
    val data: List<CategoryChildFormat>
)

data class CategoryChildFormat(
    val id: Int,
    val name: String,
    val childs: List<CategoryChild>
)

data class CategoryChild(
    val id: Int,
    val name: String
)
