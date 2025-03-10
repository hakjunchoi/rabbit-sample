package org.example.springaipractice.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.springaipractice.dto.RAGResponse
import org.example.springaipractice.dto.SentimentResponse
import org.springframework.ai.anthropic.AnthropicChatClient
import org.springframework.ai.chat.ChatResponse
import org.springframework.ai.document.Document
import org.springframework.stereotype.Service

@Service
class AdvancedAIService(
    private val anthropicChatClient: AnthropicChatClient,
    private val promptTemplateService: PromptTemplateService,
    private val aiService: AIService,
    private val objectMapper: ObjectMapper
) {

    // 코드 리뷰 기능
    suspend fun reviewCode(code: String, language: String): String {
        val prompt = promptTemplateService.createCodeReviewPrompt(code, language)
        val response: ChatResponse = anthropicChatClient.call(prompt)
        return response.result.output.content
    }
    
    // 텍스트 요약 기능
    suspend fun summarizeText(text: String, maxWords: Int): String {
        val prompt = promptTemplateService.createSummarizationPrompt(text, maxWords)
        val response: ChatResponse = anthropicChatClient.call(prompt)
        return response.result.output.content
    }
    
    // 감정 분석 기능
    suspend fun analyzeSentiment(text: String): SentimentResponse {
        val prompt = promptTemplateService.createSentimentAnalysisPrompt(text)
        val response: ChatResponse = anthropicChatClient.call(prompt)
        val jsonResponse = response.result.output.content
        
        return try {
            objectMapper.readValue(jsonResponse, SentimentResponse::class.java)
        } catch (e: Exception) {
            // JSON 파싱 실패 시 기본 응답 반환
            SentimentResponse(
                sentiment = "neutral",
                confidence = 0.5,
                explanation = "응답 파싱 실패: $jsonResponse"
            )
        }
    }
    
    // 질문-답변 기능
    suspend fun answerQuestion(context: String, question: String): String {
        val prompt = promptTemplateService.createQAPrompt(context, question)
        val response: ChatResponse = anthropicChatClient.call(prompt)
        return response.result.output.content
    }
    
    // RAG (Retrieval Augmented Generation) 기능
    suspend fun retrieveAndGenerate(query: String, topK: Int = 3): RAGResponse {
        // 1. 쿼리와 관련된 문서 검색
        val documents = aiService.searchSimilarDocuments(query, topK)
        
        // 2. 검색된 문서를 컨텍스트로 사용하여 응답 생성
        val context = buildContext(documents)
        val answer = answerQuestion(context, query)
        
        return RAGResponse(
            query = query,
            answer = answer,
            sourceDocuments = documents.map { it.id ?: "" }
        )
    }
    
    private fun buildContext(documents: List<Document>): String {
        return documents.joinToString("\n\n") { doc ->
            """
            문서 ID: ${doc.id ?: "Unknown"}
            내용: ${doc.content}
            """.trimIndent()
        }
    }
} 