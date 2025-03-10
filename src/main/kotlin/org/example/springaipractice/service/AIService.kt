package org.example.springaipractice.service

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.ai.anthropic.AnthropicChatClient
import org.springframework.ai.anthropic.api.AnthropicApi
import org.springframework.ai.chat.ChatResponse
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.chat.prompt.SystemPromptTemplate
import org.springframework.ai.document.Document
import org.springframework.ai.embedding.EmbeddingClient
import org.springframework.ai.embedding.EmbeddingResponse
import org.springframework.ai.vectorstore.SimpleVectorStore
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AIService(
    private val anthropicChatClient: AnthropicChatClient,
    private val embeddingClient: EmbeddingClient
) {
    private val vectorStore = SimpleVectorStore(embeddingClient)
    
    // 텍스트 생성 기능
    suspend fun generateText(userPrompt: String): String {
        val prompt = Prompt(userPrompt)
        val response: ChatResponse = anthropicChatClient.call(prompt)
        return response.result.output.content
    }
    
    // 시스템 프롬프트 템플릿을 활용한 텍스트 생성
    suspend fun generateTextWithTemplate(userPrompt: String, role: String): String {
        val systemPrompt = SystemPromptTemplate("당신은 {role}입니다. 전문적인 답변을 제공해주세요.")
            .create(mapOf("role" to role))
        
        val prompt = Prompt(userPrompt, systemPrompt)
        val response: ChatResponse = anthropicChatClient.call(prompt)
        return response.result.output.content
    }
    
    // 임베딩 생성
    suspend fun generateEmbedding(text: String): List<Double> {
        val response: EmbeddingResponse = embeddingClient.embed(text)
        return response.embeddings[0].embedding
    }
    
    // 문서 저장 및 벡터 검색 기능
    fun addDocument(id: String, content: String, metadata: Map<String, Any> = emptyMap()) {
        val document = Document(content, metadata, id)
        vectorStore.add(listOf(document))
    }
    
    suspend fun searchSimilarDocuments(query: String, topK: Int = 3): List<Document> {
        return vectorStore.similaritySearch(query, topK)
    }
} 