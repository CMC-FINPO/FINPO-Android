package com.finpo.app.repository

import com.finpo.app.network.ApiServiceWithoutToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilterRepository @Inject constructor(private val apiServiceWithoutToken: ApiServiceWithoutToken) {
    suspend fun getCategoryChildFormat() = apiServiceWithoutToken.getCategoryChildFormat()
}