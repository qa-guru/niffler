package guru.qa.niffler.service;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import guru.qa.niffler.ex.IllegalGqlFieldAccessException;
import guru.qa.niffler.ex.TooManySubQueriesException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class GraphQlExceptionResolver extends DataFetcherExceptionResolverAdapter {

  @Override
  @Nullable
  protected GraphQLError resolveToSingleError(@Nonnull Throwable ex, @Nonnull DataFetchingEnvironment env) {
    if (ex instanceof TooManySubQueriesException || ex instanceof IllegalGqlFieldAccessException) {
      return GraphqlErrorBuilder.newError()
          .errorType(ErrorType.BAD_REQUEST)
          .message(ex.getMessage())
          .path(env.getExecutionStepInfo().getPath())
          .location(env.getField().getSourceLocation())
          .build();
    } else {
      return super.resolveToSingleError(ex, env);
    }
  }
}
