package br.com.jh.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.grpc.server.service.GrpcService;

import br.com.jh.contracts.*;
import io.grpc.stub.StreamObserver;

@GrpcService
public class AggregatorServiceImpl extends AggregatorServiceGrpc.AggregatorServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(AggregatorServiceImpl.class);

    private final ImageClientA imageClientA;
    private final ImageClientB imageClientB;

    public AggregatorServiceImpl(ImageClientA imageClientA, ImageClientB imageClientB) {
        this.imageClientA = imageClientA;
        this.imageClientB = imageClientB;
    }

    @Override
    public void getAggregatedImage(AggregatedImageRequest request,
                                   StreamObserver<ImageResponse> responseObserver) {

        String userId = request.getUserId();
        String source = request.getSource(); // "A", "B" ou outro

        log.info("Service-C recebeu pedido de imagem agregada. userId={}, source={}", userId, source);

        ImageResponse img;

        try {
            switch (source) {
                case "B" -> img = imageClientB.getImage("img-b-" + userId);
                case "A" -> img = imageClientA.getImage("img-a-" + userId);
                default  -> {
                    // regra default: tenta A primeiro
                    img = imageClientA.getImage("img-a-" + userId);
                }
            }

            responseObserver.onNext(img);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Erro ao agregar imagem em Service-C", e);
            responseObserver.onError(e);
        }
    }
}