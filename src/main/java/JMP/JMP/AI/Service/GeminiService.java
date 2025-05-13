package JMP.JMP.AI.Service;

import JMP.JMP.AI.Handler.GeminiInterface;
import JMP.JMP.AI.Dto.GeminiRequest;
import JMP.JMP.AI.Dto.GeminiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private final GeminiInterface geminiInterface;

    private GeminiResponse getCompletion(GeminiRequest request) {
        return geminiInterface.getCompletion(request);
    }

    public String getCompletion(String text) {
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
