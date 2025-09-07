package guru.qa.niffler.service.utils;

import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;

public class GqlVoidScalar {
  public static final GraphQLScalarType Void = GraphQLScalarType.newScalar()
      .name("Void")
      .description("A custom scalar that represents the null value")
      .coercing(new Coercing() {})
      .build();
}
