// 05-spring-ai/src/main/java/dio/budgeting/infrastructure/http/TransactionController.java
// package dio.budgeting.infrastructure.http;

// import dio.budgeting.application.ListTransactionsByCategoryUseCase;
// import dio.budgeting.application.PersistTransactionUseCase;
// import dio.budgeting.domain.Category;
// import dio.budgeting.infrastructure.http.request.TransactionRequest;
// import dio.budgeting.infrastructure.http.response.TransactionResponse;
// import org.springframework.ai.audio.transcription.TranscriptionModel;
// import org.springframework.ai.audio.tts.TextToSpeechModel;
// import org.springframework.ai.chat.client.ChatClient;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.core.io.ByteArrayResource;
// import org.springframework.core.io.Resource;
// import org.springframework.http.*;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;

// import java.io.IOException;
// import java.nio.charset.Charset;
// import java.util.List;

// @RestController
// @RequestMapping("/transactions")
// public class TransactionController {
//     private final PersistTransactionUseCase persistTransactionUseCase;
//     private final ListTransactionsByCategoryUseCase listTransactionsByCategoryUseCase;

//     private final TranscriptionModel transcriptionModel;
//     private final ChatClient chatClient;
//     private final TextToSpeechModel textToSpeechModel;

//     public TransactionController(PersistTransactionUseCase persistTransactionUseCase,
//                                  ListTransactionsByCategoryUseCase listTransactionsByCategoryUseCase,
//                                  TranscriptionModel transcriptionModel,
//                                  @Value("classpath:prompts/system-message.st") Resource systemPrompt,
//                                  ChatClient.Builder chatClientBuilder,
//                                  TextToSpeechModel textToSpeechModel) throws IOException {
//         this.persistTransactionUseCase = persistTransactionUseCase;
//         this.listTransactionsByCategoryUseCase = listTransactionsByCategoryUseCase;
//         this.transcriptionModel = transcriptionModel;
//         this.chatClient = chatClientBuilder
//                 .defaultSystem(systemPrompt.getContentAsString(Charset.defaultCharset()))
//                 .defaultTools(persistTransactionUseCase, listTransactionsByCategoryUseCase)
//                 .build();
//         this.textToSpeechModel = textToSpeechModel;
//     }

//     @PostMapping
//     @ResponseStatus(HttpStatus.CREATED)
//     public TransactionResponse createTransaction(@RequestBody TransactionRequest request) {
//         var transaction = persistTransactionUseCase.execute(request.toInput());
//         return TransactionResponse.from(transaction);
//     }

//     @GetMapping("/{category}")
//     public List<TransactionResponse> readTransactions(@PathVariable Category category) {
//         return listTransactionsByCategoryUseCase.execute(category).stream().map(TransactionResponse::from).toList();
//     }

//     @PostMapping(value = "/ai", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "audio/mp3")
//     ResponseEntity<Resource> transcribe(@RequestParam("file") MultipartFile file) {
//         var userMessage = transcriptionModel.transcribe(file.getResource());
//         var result = chatClient.prompt().user(userMessage).call().content();

//         byte[] audio = textToSpeechModel.call(result);
//         var resource = new ByteArrayResource(audio);

//         return ResponseEntity.ok()
//                 .header(HttpHeaders.CONTENT_DISPOSITION,
//                         ContentDisposition.attachment()
//                                 .filename("audio.mp3")
//                                 .build()
//                                 .toString())
//                 .body(resource);
//     }
// }

// Teste OFF
package dio.budgeting.infrastructure.http;

import dio.budgeting.application.ListTransactionsByCategoryUseCase;
import dio.budgeting.application.PersistTransactionUseCase;
import dio.budgeting.domain.Category;
import dio.budgeting.infrastructure.http.request.TransactionRequest;
import dio.budgeting.infrastructure.http.response.TransactionResponse;
import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final PersistTransactionUseCase persistTransactionUseCase;
    private final ListTransactionsByCategoryUseCase listTransactionsByCategoryUseCase;

    private final TranscriptionModel transcriptionModel;
    private final ChatClient chatClient;
    private final TextToSpeechModel textToSpeechModel;

    public TransactionController(PersistTransactionUseCase persistTransactionUseCase,
                                 ListTransactionsByCategoryUseCase listTransactionsByCategoryUseCase,
                                 TranscriptionModel transcriptionModel,
                                 @Value("classpath:prompts/system-message.st") Resource systemPrompt,
                                 ChatClient.Builder chatClientBuilder,
                                 TextToSpeechModel textToSpeechModel) throws IOException {
        this.persistTransactionUseCase = persistTransactionUseCase;
        this.listTransactionsByCategoryUseCase = listTransactionsByCategoryUseCase;
        this.transcriptionModel = transcriptionModel;
        this.chatClient = chatClientBuilder
                .defaultSystem(systemPrompt.getContentAsString(Charset.defaultCharset()))
                .defaultTools(persistTransactionUseCase, listTransactionsByCategoryUseCase)
                .build();
        this.textToSpeechModel = textToSpeechModel;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(@RequestBody TransactionRequest request) {
        var transaction = persistTransactionUseCase.execute(request.toInput());
        return TransactionResponse.from(transaction);
    }

    @GetMapping("/{category}")
    public List<TransactionResponse> readTransactions(@PathVariable Category category) {
        return listTransactionsByCategoryUseCase.execute(category).stream().map(TransactionResponse::from).toList();
    }

    @PostMapping(value = "/ai", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "audio/mp3")
    ResponseEntity<Resource> transcribe(@RequestParam("file") MultipartFile file) {
        // 1. Simulação local do Whisper (Pula o erro de áudio)
        String userMessage = "Gastei 50 reais com alimentação no mercado";
        System.out.println("🎙️ [Mock Whisper] Comando recebido localmente: " + userMessage);

        // 2. Simulação local da chamada de texto da IA (Pula o erro 429 do Chat)
        // Em vez de chamar a OpenAI via rede, chamamos diretamente as funções reais do seu sistema!
        System.out.println("🧠 [Mock IA] Interpretando a intenção do usuário...");
        
        // Criamos o input de dados real com o valor simulado da frase (50 reais = 5000 centavos)
        var inputMapeado = new dio.budgeting.application.input.PersistTransactionInput(
            "Alimentação no mercado", 
            5000, 
            Category.GROCERIES // Certifique-se de que GROCERIES ou outra categoria válida existe no seu Enum Category
        );

        // 3. EXECUÇÃO DO FLUXO REAL: Chama o Caso de Uso, que aciona o Construtor com a sua validação e grava no Docker!
        var transactionOutput = persistTransactionUseCase.execute(inputMapeado);
        System.out.println("💾 [Domínio & JPA] Transação validada e registrada no MySQL com sucesso! ID: " + transactionOutput.id());

        // 4. Retorno estável de arquivo binário para o Postman
        byte[] audio = new byte[]{0};
        var resource = new ByteArrayResource(audio);

        System.out.println("🤖 [API Inteligente] Fluxo concluído localmente com sucesso!");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("audio.mp3")
                                .build()
                                .toString())
                .body(resource);
    }

}
