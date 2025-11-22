package br.com.jh.service;

import java.io.IOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.grpc.server.service.GrpcService;

import com.google.protobuf.ByteString;

import br.com.jh.contracts.ImageRequest;
import br.com.jh.contracts.ImageResponse;
import br.com.jh.contracts.ImageServiceGrpc;
import io.grpc.stub.StreamObserver;

@GrpcService
public class ImageServiceImpl extends ImageServiceGrpc.ImageServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Override
    public void getImage(ImageRequest request, StreamObserver<ImageResponse> responseObserver) {
        String imageId = request.getImageId();
        log.info("Service-B recebeu pedido de imagem. imageId={}", imageId);

        try {
            // aqui você poderia trocar o nome do arquivo conforme o imageId
            ClassPathResource resource = new ClassPathResource("images/image-b.png");
            byte[] bytes = Files.readAllBytes(resource.getFile().toPath());

            ImageResponse response = ImageResponse.newBuilder()
                    .setData(ByteString.copyFrom(bytes))
                    .setContentType("image/png")
                    .build();

            log.debug("Service-A retornando imagem. tamanho={} bytes", bytes.length);

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IOException e) {
            log.error("Erro ao carregar a imagem", e);
            responseObserver.onError(e);
        }
    }
}