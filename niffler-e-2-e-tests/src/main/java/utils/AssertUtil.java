package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ValidatableResponse;
import lombok.experimental.UtilityClass;
import net.javacrumbs.jsonunit.core.Option;
import org.opentest4j.AssertionFailedError;
import io.qameta.allure.jsonunit.JsonPatchMatcher;

@UtilityClass
public class AssertUtil {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static void assertEquals(Object expected, ValidatableResponse actual, String... ignorable) {
		String expectedNode = objectMapper.valueToTree(expected).toPrettyString();
		String actualNode = actual.extract().body().jsonPath().prettify();
		if (!JsonPatchMatcher.jsonEquals(expectedNode)
				.whenIgnoringPaths(ignorable).when(Option.IGNORING_ARRAY_ORDER).matches(actualNode)) {
			throw new AssertionFailedError("ќжидаемый ответ не совпадает с фактическим", expectedNode, actualNode);
		}
	}

	public static void assertEquals(Object expected, Object actual, String... ignorable) {
		String expectedNode = objectMapper.valueToTree(expected).toPrettyString();
		String actualNode = objectMapper.valueToTree(actual).toPrettyString();
		if (!JsonPatchMatcher.jsonEquals(expectedNode)
				.whenIgnoringPaths(ignorable).when(Option.IGNORING_ARRAY_ORDER).matches(actualNode)) {
			throw new AssertionFailedError("ќжидаемый ответ не совпадает с фактическим", expectedNode, actualNode);
		}
	}

}
