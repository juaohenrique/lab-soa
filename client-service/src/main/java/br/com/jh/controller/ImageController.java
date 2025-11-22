package br.com.jh.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.jh.contracts.AggregatedImageRequest;
import br.com.jh.contracts.AggregatorServiceGrpc;
import br.com.jh.contracts.ImageResponse;

@RestController
@RequestMapping("/image")
public class ImageController {

    private static final Logger log = LoggerFactory.getLogger(ImageController.class);

    private final AggregatorServiceGrpc.AggregatorServiceBlockingStub aggregatorStub;

    public ImageController(AggregatorServiceGrpc.AggregatorServiceBlockingStub aggregatorStub) {
        this.aggregatorStub = aggregatorStub;
    }

    @GetMapping
    public ResponseEntity<byte[]> getImage(
            @RequestParam(defaultValue = "A") String source,
            @RequestParam(defaultValue = "user-1") String userId
    ) {
        log.info("Main: recebendo request HTTP. source={}, userId={}", source, userId);

        AggregatedImageRequest request = AggregatedImageRequest.newBuilder()
                .setUserId(userId)
                .setSource(source)
                .build();

        ImageResponse img = aggregatorStub.getAggregatedImage(request);

        String contentType = img.getContentType();
        byte[] bytes = img.getData().toByteArray();

        log.info("Main: imagem recebida do service-c. contentType={}, size={}", contentType, bytes.length);

        MediaType mediaType;
        try {
            mediaType = MediaType.parseMediaType(contentType);
        } catch (Exception e) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, mediaType.toString())
                .body(bytes);
    }
}