package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CategoryService {

    @POST("/category")
//    Call<CategoryJson> addCategory(@Header("Content-Type") String contentType, @Body CategoryJson category);
    Call<CategoryJson> addCategory(@Body CategoryJson category);
}
