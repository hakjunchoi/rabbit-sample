package org.example.springaipractice.dto

data class TextGenerationRequest(
    val prompt: String,
    val role: String? = null
)

data class TextGenerationResponse(
    val generatedText: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class EmbeddingRequest(
    val text: String
)

data class EmbeddingResponse(
    val embedding: List<Double>,
    val dimension: Int,
    val timestamp: Long = System.currentTimeMillis()
)

data class DocumentRequest(
    val id: String,
    val content: String,
    val metadata: Map<String, Any> = emptyMap()
)

data class SearchRequest(
    val query: String,
    val topK: Int = 3
)

data class SearchResponse(
    val documents: List<DocumentDto>,
    val timestamp: Long = System.currentTimeMillis()
)

data class DocumentDto(
    val id: String,
    val content: String,
    val metadata: Map<String, Any>,
    val score: Double? = null
) 