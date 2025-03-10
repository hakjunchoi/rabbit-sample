package org.example.springaipractice.dto

data class CodeReviewRequest(
    val code: String,
    val language: String
)

data class SummarizationRequest(
    val text: String,
    val maxWords: Int = 100
)

data class SentimentRequest(
    val text: String
)

data class SentimentResponse(
    val sentiment: String,
    val confidence: Double,
    val explanation: String
)

data class QARequest(
    val context: String,
    val question: String
)

data class RAGRequest(
    val query: String,
    val topK: Int = 3
)

data class RAGResponse(
    val query: String,
    val answer: String,
    val sourceDocuments: List<String>,
    val timestamp: Long = System.currentTimeMillis()
) 