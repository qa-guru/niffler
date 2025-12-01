package guru.qa.niffler.service;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import guru.qa.niffler.ex.IllegalGqlFieldAccessException;
import guru.qa.niffler.ex.TooManySubQueriesException;
import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintViolationException;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import javax.annotation.ParametersAreNonnullByDefault;

import static guru.qa.niffler.service.utils.ValidationExceptionMessageResolver.resolveHandlerMethodValidationException;
import static guru.qa.niffler.service.utils.ValidationExceptionMessageResolver.resolveMethodArgumentNotValidException;

@Component
@ParametersAreNonnullByDefault
public class GraphQlExceptionResolver extends DataFetcherExceptionResolverAdapter {

  @Override
  @Nullable
  protected GraphQLError resolveToSingleError(Throwable ex,
                                              DataFetchingEnvironment env) {
    if (ex instanceof TooManySubQueriesException || ex instanceof IllegalGqlFieldAccessException) {
      return GraphqlErrorBuilder.newError()
          .errorType(ErrorType.BAD_REQUEST)
          .message(ex.getMessage())
          .path(env.getExecutionStepInfo().getPath())
          .location(env.getField().getSourceLocation())
          .build();
    } else if (ex instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
      return GraphqlErrorBuilder.newError()
          .errorType(ErrorType.BAD_REQUEST)
          .message(resolveMethodArgumentNotValidException(methodArgumentNotValidException))
          .path(env.getExecutionStepInfo().getPath())
          .location(env.getField().getSourceLocation())
          .build();
    } else if (ex instanceof HandlerMethodValidationException handlerMethodValidationException) {
      return GraphqlErrorBuilder.newError()
          .errorType(ErrorType.BAD_REQUEST)
          .message(resolveHandlerMethodValidationException(handlerMethodValidationException))
          .path(env.getExecutionStepInfo().getPath())
          .location(env.getField().getSourceLocation())
          .build();
    } else if (ex instanceof ConstraintViolationException constraintViolationException) {
      return GraphqlErrorBuilder.newError()
          .errorType(ErrorType.BAD_REQUEST)
          .message(constraintViolationException.getMessage())
          .path(env.getExecutionStepInfo().getPath())
          .location(env.getField().getSourceLocation())
          .build();
    } else {
      return super.resolveToSingleError(ex, env);
    }
  }
}
