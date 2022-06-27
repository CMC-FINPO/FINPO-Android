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

data class MyCategoryResponse(
    val data: List<MyCategory>
)

data class MyCategory(
    val category: Category
)

data class Category(
    val id: Int,
    val name: String
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
