package org.example.springaipractice

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ai-connect")
class AIConnectController {

    @GetMapping("/health")
    fun health(): String {
        return "OK"
    }
}