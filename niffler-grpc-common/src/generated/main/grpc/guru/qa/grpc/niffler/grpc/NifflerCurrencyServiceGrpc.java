package guru.qa.grpc.niffler.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.52.1)",
    comments = "Source: niffler-currency.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class NifflerCurrencyServiceGrpc {

  private NifflerCurrencyServiceGrpc() {}

  public static final String SERVICE_NAME = "guru.qa.grpc.niffler.NifflerCurrencyService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      guru.qa.grpc.niffler.grpc.CurrencyResponse> getGetAllCurrenciesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAllCurrencies",
      requestType = com.google.protobuf.Empty.class,
      responseType = guru.qa.grpc.niffler.grpc.CurrencyResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      guru.qa.grpc.niffler.grpc.CurrencyResponse> getGetAllCurrenciesMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, guru.qa.grpc.niffler.grpc.CurrencyResponse> getGetAllCurrenciesMethod;
    if ((getGetAllCurrenciesMethod = NifflerCurrencyServiceGrpc.getGetAllCurrenciesMethod) == null) {
      synchronized (NifflerCurrencyServiceGrpc.class) {
        if ((getGetAllCurrenciesMethod = NifflerCurrencyServiceGrpc.getGetAllCurrenciesMethod) == null) {
          NifflerCurrencyServiceGrpc.getGetAllCurrenciesMethod = getGetAllCurrenciesMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, guru.qa.grpc.niffler.grpc.CurrencyResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAllCurrencies"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  guru.qa.grpc.niffler.grpc.CurrencyResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NifflerCurrencyServiceMethodDescriptorSupplier("GetAllCurrencies"))
              .build();
        }
      }
    }
    return getGetAllCurrenciesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<guru.qa.grpc.niffler.grpc.CalculateRequest,
      guru.qa.grpc.niffler.grpc.CalculateResponse> getCalculateRateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CalculateRate",
      requestType = guru.qa.grpc.niffler.grpc.CalculateRequest.class,
      responseType = guru.qa.grpc.niffler.grpc.CalculateResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<guru.qa.grpc.niffler.grpc.CalculateRequest,
      guru.qa.grpc.niffler.grpc.CalculateResponse> getCalculateRateMethod() {
    io.grpc.MethodDescriptor<guru.qa.grpc.niffler.grpc.CalculateRequest, guru.qa.grpc.niffler.grpc.CalculateResponse> getCalculateRateMethod;
    if ((getCalculateRateMethod = NifflerCurrencyServiceGrpc.getCalculateRateMethod) == null) {
      synchronized (NifflerCurrencyServiceGrpc.class) {
        if ((getCalculateRateMethod = NifflerCurrencyServiceGrpc.getCalculateRateMethod) == null) {
          NifflerCurrencyServiceGrpc.getCalculateRateMethod = getCalculateRateMethod =
              io.grpc.MethodDescriptor.<guru.qa.grpc.niffler.grpc.CalculateRequest, guru.qa.grpc.niffler.grpc.CalculateResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CalculateRate"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  guru.qa.grpc.niffler.grpc.CalculateRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  guru.qa.grpc.niffler.grpc.CalculateResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NifflerCurrencyServiceMethodDescriptorSupplier("CalculateRate"))
              .build();
        }
      }
    }
    return getCalculateRateMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static NifflerCurrencyServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NifflerCurrencyServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NifflerCurrencyServiceStub>() {
        @java.lang.Override
        public NifflerCurrencyServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NifflerCurrencyServiceStub(channel, callOptions);
        }
      };
    return NifflerCurrencyServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static NifflerCurrencyServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NifflerCurrencyServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NifflerCurrencyServiceBlockingStub>() {
        @java.lang.Override
        public NifflerCurrencyServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NifflerCurrencyServiceBlockingStub(channel, callOptions);
        }
      };
    return NifflerCurrencyServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static NifflerCurrencyServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NifflerCurrencyServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NifflerCurrencyServiceFutureStub>() {
        @java.lang.Override
        public NifflerCurrencyServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NifflerCurrencyServiceFutureStub(channel, callOptions);
        }
      };
    return NifflerCurrencyServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class NifflerCurrencyServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getAllCurrencies(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<guru.qa.grpc.niffler.grpc.CurrencyResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAllCurrenciesMethod(), responseObserver);
    }

    /**
     */
    public void calculateRate(guru.qa.grpc.niffler.grpc.CalculateRequest request,
        io.grpc.stub.StreamObserver<guru.qa.grpc.niffler.grpc.CalculateResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCalculateRateMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetAllCurrenciesMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.protobuf.Empty,
                guru.qa.grpc.niffler.grpc.CurrencyResponse>(
                  this, METHODID_GET_ALL_CURRENCIES)))
          .addMethod(
            getCalculateRateMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                guru.qa.grpc.niffler.grpc.CalculateRequest,
                guru.qa.grpc.niffler.grpc.CalculateResponse>(
                  this, METHODID_CALCULATE_RATE)))
          .build();
    }
  }

  /**
   */
  public static final class NifflerCurrencyServiceStub extends io.grpc.stub.AbstractAsyncStub<NifflerCurrencyServiceStub> {
    private NifflerCurrencyServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NifflerCurrencyServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NifflerCurrencyServiceStub(channel, callOptions);
    }

    /**
     */
    public void getAllCurrencies(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<guru.qa.grpc.niffler.grpc.CurrencyResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAllCurrenciesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void calculateRate(guru.qa.grpc.niffler.grpc.CalculateRequest request,
        io.grpc.stub.StreamObserver<guru.qa.grpc.niffler.grpc.CalculateResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCalculateRateMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class NifflerCurrencyServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<NifflerCurrencyServiceBlockingStub> {
    private NifflerCurrencyServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NifflerCurrencyServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NifflerCurrencyServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public guru.qa.grpc.niffler.grpc.CurrencyResponse getAllCurrencies(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAllCurrenciesMethod(), getCallOptions(), request);
    }

    /**
     */
    public guru.qa.grpc.niffler.grpc.CalculateResponse calculateRate(guru.qa.grpc.niffler.grpc.CalculateRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCalculateRateMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class NifflerCurrencyServiceFutureStub extends io.grpc.stub.AbstractFutureStub<NifflerCurrencyServiceFutureStub> {
    private NifflerCurrencyServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NifflerCurrencyServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NifflerCurrencyServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<guru.qa.grpc.niffler.grpc.CurrencyResponse> getAllCurrencies(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAllCurrenciesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<guru.qa.grpc.niffler.grpc.CalculateResponse> calculateRate(
        guru.qa.grpc.niffler.grpc.CalculateRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCalculateRateMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_ALL_CURRENCIES = 0;
  private static final int METHODID_CALCULATE_RATE = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final NifflerCurrencyServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(NifflerCurrencyServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_ALL_CURRENCIES:
          serviceImpl.getAllCurrencies((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<guru.qa.grpc.niffler.grpc.CurrencyResponse>) responseObserver);
          break;
        case METHODID_CALCULATE_RATE:
          serviceImpl.calculateRate((guru.qa.grpc.niffler.grpc.CalculateRequest) request,
              (io.grpc.stub.StreamObserver<guru.qa.grpc.niffler.grpc.CalculateResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class NifflerCurrencyServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    NifflerCurrencyServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return guru.qa.grpc.niffler.grpc.NifflerCurrencyProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("NifflerCurrencyService");
    }
  }

  private static final class NifflerCurrencyServiceFileDescriptorSupplier
      extends NifflerCurrencyServiceBaseDescriptorSupplier {
    NifflerCurrencyServiceFileDescriptorSupplier() {}
  }

  private static final class NifflerCurrencyServiceMethodDescriptorSupplier
      extends NifflerCurrencyServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    NifflerCurrencyServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (NifflerCurrencyServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new NifflerCurrencyServiceFileDescriptorSupplier())
              .addMethod(getGetAllCurrenciesMethod())
              .addMethod(getCalculateRateMethod())
              .build();
        }
      }
    }
    return result;
  }
}
