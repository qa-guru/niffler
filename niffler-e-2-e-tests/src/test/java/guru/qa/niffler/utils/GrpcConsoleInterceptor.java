package guru.qa.niffler.utils;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.ForwardingClientCallListener;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrpcConsoleInterceptor implements ClientInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcConsoleInterceptor.class);
    private static final JsonFormat.Printer jsonPrinter = JsonFormat.printer();

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor,
                                                               CallOptions callOptions, Channel channel) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
                channel.newCall(methodDescriptor, callOptions.withoutWaitForReady())) {


            @Override
            public void sendMessage(ReqT message) {
                LOG.info("Send gRPC request to: {}", methodDescriptor.getFullMethodName());
                try {
                    LOG.info("Request body:\n\n{}", jsonPrinter.print((MessageOrBuilder) message));
                } catch (InvalidProtocolBufferException e) {
                    LOG.warn("Can`t parse gRPC request", e);
                }
                super.sendMessage(message);
            }

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                ClientCall.Listener<RespT> listener = new ForwardingClientCallListener<RespT>() {
                    @Override
                    public void onClose(Status status, Metadata trailers) {
                        super.onClose(status, trailers);
                    }

                    @Override
                    protected Listener<RespT> delegate() {
                        return responseListener;
                    }

                    @Override
                    public void onMessage(RespT message) {
                        try {
                            LOG.info("Response body:\n\n{}", jsonPrinter.print((MessageOrBuilder) message));
                        } catch (InvalidProtocolBufferException e) {
                            LOG.warn("Can`t parse gRPC response", e);
                        }
                        super.onMessage(message);
                    }
                };
                super.start(listener, headers);
            }
        };
    }
}
