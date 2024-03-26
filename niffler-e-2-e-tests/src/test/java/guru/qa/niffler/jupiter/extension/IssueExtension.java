package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.GhApiClient;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

public class IssueExtension implements ExecutionCondition {

  private final GhApiClient ghApiClient = new GhApiClient();

  @SneakyThrows
  @Override
  public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
    DisabledByIssue disabledByIssue = AnnotationSupport.findAnnotation(
        context.getRequiredTestMethod(),
        DisabledByIssue.class
    ).orElse(
        AnnotationSupport.findAnnotation(
            context.getRequiredTestClass(),
            DisabledByIssue.class
        ).orElse(null)
    );

    if (disabledByIssue != null) {
      return "open".equals(ghApiClient.getIssueState(disabledByIssue.value()))
          ? ConditionEvaluationResult.disabled("Disabled by issue #" + disabledByIssue.value())
          : ConditionEvaluationResult.enabled("Issue closed");
    }
    return ConditionEvaluationResult.enabled("Annotation not found");
  }
}
