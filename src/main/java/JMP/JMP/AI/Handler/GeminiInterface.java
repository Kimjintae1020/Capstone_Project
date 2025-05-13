package JMP.JMP.AI.Handler;

import JMP.JMP.AI.Dto.GeminiRequest;
import JMP.JMP.AI.Dto.GeminiResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface GeminiInterface {
    @PostExchange("/v1beta/models/gemini-1.5-flash:generateContent")
    GeminiResponse getCompletion(@RequestBody GeminiRequest request);
}
