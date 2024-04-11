package guru.qa.niffler.model.gql;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class GqlResponse<T> {
    protected T data;
    protected List<Error> errors;

    public record Error(String message,
                        List<Map<String, Integer>> locations,
                        List<String> path,
                        Map<String, String> extensions) {
    }
}
