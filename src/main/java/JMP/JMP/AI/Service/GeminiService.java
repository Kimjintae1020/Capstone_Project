package JMP.JMP.AI.Service;

import JMP.JMP.AI.Handler.GeminiInterface;
import JMP.JMP.AI.Dto.GeminiRequest;
import JMP.JMP.AI.Dto.GeminiResponse;
import com.google.common.util.concurrent.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private final GeminiInterface geminiInterface;

    // 5초당 1번
    private final RateLimiter rateLimiter = RateLimiter.create(0.2);

    private GeminiResponse getCompletion(GeminiRequest request) {
        return geminiInterface.getCompletion(request);
    }

    public String getCompletion(String text) {
        rateLimiter.acquire();
        GeminiRequest geminiRequest = new GeminiRequest(text);
        GeminiResponse response = getCompletion(geminiRequest);

        return response.getCandidates()
                .stream()
                .findFirst().flatMap(candidate -> candidate.getContent().getParts()
                        .stream()
                        .findFirst()
                        .map(GeminiResponse.TextPart::getText))
                .orElse(null);
    }
}
