package br.com.jh.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.jh.contracts.ImageRequest;
import br.com.jh.contracts.ImageResponse;
import br.com.jh.contracts.ImageServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;


@Service
public class ImageClientA {

    private static final Logger log = LoggerFactory.getLogger(ImageClientA.class);

    private final ImageServiceGrpc.ImageServiceBlockingStub stub;

    public ImageClientA() {
        // Cria o canal manualmente (porta do Service-A)
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 9091) // ajuste para a porta gRPC do Service-A
                .usePlaintext()
                .build();

        this.stub = ImageServiceGrpc.newBlockingStub(channel);
    }

    public ImageResponse getImage(String imageId) {
        log.info("Chamando Service-A via gRPC. imageId={}", imageId);

        ImageRequest request = ImageRequest.newBuilder()
                .setImageId(imageId)
                .build();

        return stub.getImage(request);
    }
}
