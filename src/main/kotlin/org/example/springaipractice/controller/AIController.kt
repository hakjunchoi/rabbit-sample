package org.example.springaipractice.controller

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.example.springaipractice.dto.*
import org.example.springaipractice.service.AIService
import org.springframework.ai.document.Document
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/ai")
class AIController(private val aiService: AIService) {

    @PostMapping("/generate")
    suspend fun generateText(@RequestBody request: TextGenerationRequest): TextGenerationResponse {
        val generatedText = if (request.role != null) {
            aiService.generateTextWithTemplate(request.prompt, request.role)
        } else {
            aiService.generateText(request.prompt)
        }
        
        return TextGenerationResponse(generatedText)
    }
    
    @PostMapping("/embedding")
    suspend fun generateEmbedding(@RequestBody request: EmbeddingRequest): EmbeddingResponse {
        val embedding = aiService.generateEmbedding(request.text)
        return EmbeddingResponse(embedding, embedding.size)
    }
    
    @PostMapping("/documents")
    fun addDocument(@RequestBody request: DocumentRequest) {
        aiService.addDocument(request.id, request.content, request.metadata)
    }
    
    @PostMapping("/search")
    suspend fun searchDocuments(@RequestBody request: SearchRequest): SearchResponse {
        val documents = aiService.searchSimilarDocuments(request.query, request.topK)
        val documentDtos = documents.map { doc ->
            DocumentDto(
                id = doc.id ?: "",
                content = doc.content,
                metadata = doc.metadata,
                score = doc.score
            )
        }
        
        return SearchResponse(documentDtos)
    }
    
    @PostMapping(path = ["/stream"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun streamGeneratedText(@RequestBody request: TextGenerationRequest): Flow<String> = flow {
        // 실제 스트리밍 구현은 복잡하므로 간단한 예시로 대체
        val text = if (request.role != null) {
            aiService.generateTextWithTemplate(request.prompt, request.role)
        } else {
            aiService.generateText(request.prompt)
        }
        
        // 텍스트를 단어 단위로 나누어 스트리밍
        text.split(" ").forEach { word ->
            emit("$word ")
            kotlinx.coroutines.delay(100) // 스트리밍 효과를 위한 지연
        }
    }
} 