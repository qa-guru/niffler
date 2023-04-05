package niffler.test.gql.model;

public abstract class GqlResponse<T> {
    protected T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
