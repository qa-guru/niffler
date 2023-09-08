package com.github.dtitar.jupiter.extension;

import com.github.dtitar.api.SpendServiceClient;
import com.github.dtitar.jupiter.annotation.DeleteSpends;
import com.github.dtitar.model.rest.SpendJson;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class DeleteSpendsExtension implements BeforeEachCallback {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(DeleteSpendsExtension.class);
    private SpendServiceClient spendService = new SpendServiceClient();

    @Override
    public void beforeEach(ExtensionContext context) throws IOException {
        DeleteSpends annotation = context.getRequiredTestMethod()
                .getAnnotation(DeleteSpends.class);
        if (annotation != null) {
            List<SpendJson> allSpends = spendService.getSpends(annotation.username());
            List<String> spendsIds = allSpends.stream()
                    .map(SpendJson::getId)
                    .map(UUID::toString)
                    .toList();
            spendService.deleteSpends(annotation.username(), spendsIds);
        }
    }
}
