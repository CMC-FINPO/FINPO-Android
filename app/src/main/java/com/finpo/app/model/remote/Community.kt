package com.finpo.app.model.remote

data class PostWritingRequest(
    val content: String,
    val anonymity: Boolean = false
)