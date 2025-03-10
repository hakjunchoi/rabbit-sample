package org.example.springaipractice.service

import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.chat.prompt.SystemPromptTemplate
import org.springframework.ai.chat.prompt.messages.Message
import org.springframework.ai.chat.prompt.messages.UserMessage
import org.springframework.stereotype.Service

@Service
class PromptTemplateService {

    // 코드 리뷰를 위한 프롬프트 템플릿
    fun createCodeReviewPrompt(code: String, language: String): Prompt {
        val systemTemplate = """
            당신은 숙련된 {language} 개발자입니다. 다음 코드를 검토하고 개선점을 제안해주세요:
            1. 코드 품질 및 가독성
            2. 성능 최적화
            3. 보안 취약점
            4. 모범 사례 준수
            
            코드 리뷰는 건설적이고 구체적이어야 합니다.
        """.trimIndent()
        
        val systemPrompt = SystemPromptTemplate(systemTemplate)
            .create(mapOf("language" to language))
        
        val userMessage = UserMessage("```$language\n$code\n```")
        
        return Prompt(listOf(systemPrompt, userMessage))
    }
    
    // 텍스트 요약을 위한 프롬프트 템플릿
    fun createSummarizationPrompt(text: String, maxWords: Int): Prompt {
        val systemTemplate = """
            당신은 전문적인 텍스트 요약 전문가입니다. 
            주어진 텍스트를 최대 {maxWords}단어로 요약해주세요.
            요약은 원문의 핵심 내용을 포함해야 합니다.
        """.trimIndent()
        
        val systemPrompt = SystemPromptTemplate(systemTemplate)
            .create(mapOf("maxWords" to maxWords.toString()))
        
        val userMessage = UserMessage(text)
        
        return Prompt(listOf(systemPrompt, userMessage))
    }
    
    // 감정 분석을 위한 프롬프트 템플릿
    fun createSentimentAnalysisPrompt(text: String): Prompt {
        val systemTemplate = """
            당신은 감정 분석 전문가입니다. 
            주어진 텍스트의 감정을 분석하고 다음 형식으로 응답해주세요:
            
            {
              "sentiment": "positive|negative|neutral",
              "confidence": 0.0-1.0,
              "explanation": "간단한 설명"
            }
            
            JSON 형식으로만 응답해주세요.
        """.trimIndent()
        
        val systemPrompt = SystemPromptTemplate(systemTemplate).create()
        val userMessage = UserMessage(text)
        
        return Prompt(listOf(systemPrompt, userMessage))
    }
    
    // 질문-답변을 위한 프롬프트 템플릿
    fun createQAPrompt(context: String, question: String): Prompt {
        val systemTemplate = """
            당신은 질문-답변 전문가입니다.
            주어진 컨텍스트를 기반으로 질문에 정확하게 답변해주세요.
            컨텍스트에 답변이 없는 경우 "주어진 정보로는 답변할 수 없습니다."라고 응답하세요.
            
            컨텍스트:
            {context}
        """.trimIndent()
        
        val systemPrompt = SystemPromptTemplate(systemTemplate)
            .create(mapOf("context" to context))
        
        val userMessage = UserMessage(question)
        
        return Prompt(listOf(systemPrompt, userMessage))
    }
} 