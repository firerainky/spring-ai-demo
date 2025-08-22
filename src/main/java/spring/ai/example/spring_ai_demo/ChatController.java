package spring.ai.example.spring_ai_demo;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    private final DeepSeekChatModel chatModel;

    public ChatController(DeepSeekChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/ai/role")
    public String roleGenerate(@RequestParam(value = "message", defaultValue = "获取之江智算平台上的用户角色") String message) {
        var prompt = new Prompt(new UserMessage(message));
        String response = ChatClient.create(chatModel)
            .prompt(prompt)
            .tools(new ZjicRoleTools())
            .call()
            .content();
        return response;
    }

    @GetMapping("/ai/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("generation", chatModel.call(message));
    }

    @GetMapping("/ai/generateStream")
    public Flux<ChatResponse> generateStream(
            @RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        var prompt = new Prompt(new UserMessage(message));
        return chatModel.stream(prompt);
    }

    @GetMapping("/ai/weather")
    public String getWeather(@RequestParam(value = "param", defaultValue = "Beijing") String param) {
        String response = ChatClient.create(chatModel)
                .prompt("What day is tomorrow? ")
                // .tools(new qqDateTimeTools())
                .call()
                .content();
        return response;
    }

    @GetMapping("/ai/setAlarm")
    public String setAlarm() {
        String response = ChatClient.create(chatModel)
        .prompt("Can you set an alarm 10 minutes from now?")
        .tools(new DateTimeTools())
        .call()
        .content();
        return response;
    }
    
    @GetMapping("/ai/mcpWeather")
    public void mcpWeather() {
        
    }
    
}
