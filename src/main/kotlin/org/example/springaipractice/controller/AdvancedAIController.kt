package org.example.springaipractice.controller

import org.example.springaipractice.dto.*
import org.example.springaipractice.service.AdvancedAIService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/ai/advanced")
class AdvancedAIController(private val advancedAIService: AdvancedAIService) {

    @PostMapping("/code-review")
    suspend fun reviewCode(@RequestBody request: CodeReviewRequest): TextGenerationResponse {
        val review = advancedAIService.reviewCode(request.code, request.language)
        return TextGenerationResponse(review)
    }
    
    @PostMapping("/summarize")
    suspend fun summarizeText(@RequestBody request: SummarizationRequest): TextGenerationResponse {
        val summary = advancedAIService.summarizeText(request.text, request.maxWords)
        return TextGenerationResponse(summary)
    }
    
    @PostMapping("/sentiment")
    suspend fun analyzeSentiment(@RequestBody request: SentimentRequest): SentimentResponse {
        return advancedAIService.analyzeSentiment(request.text)
    }
    
    @PostMapping("/qa")
    suspend fun answerQuestion(@RequestBody request: QARequest): TextGenerationResponse {
        val answer = advancedAIService.answerQuestion(request.context, request.question)
        return TextGenerationResponse(answer)
    }
    
    @PostMapping("/rag")
    suspend fun retrieveAndGenerate(@RequestBody request: RAGRequest): RAGResponse {
        return advancedAIService.retrieveAndGenerate(request.query, request.topK)
    }
} 