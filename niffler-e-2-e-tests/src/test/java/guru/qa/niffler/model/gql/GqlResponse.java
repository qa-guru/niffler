package guru.qa.niffler.model.gql;

import java.util.List;
import java.util.Map;

public abstract class GqlResponse<T> {
    protected T data;
    protected List<Error> errors;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public record Error(String message,
                        List<Map<String, Integer>> locations,
                        List<String> path,
                        Map<String, String> extensions) {
    }
}
