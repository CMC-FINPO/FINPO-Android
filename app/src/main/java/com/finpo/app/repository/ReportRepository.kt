package com.finpo.app.repository

import com.finpo.app.model.remote.*
import com.finpo.app.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getReportReason() = apiService.getReportReason()
    suspend fun reportComment(commentId: Int, reportType: Int) = apiService.reportComment(commentId, ReportRequest(Id(reportType.toString())))
    suspend fun reportPost(postId: Int, reportType: Int) = apiService.reportPost(postId, ReportRequest(Id(reportType.toString())))
}