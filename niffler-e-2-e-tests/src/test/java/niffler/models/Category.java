package niffler.models;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Category {
    @SerializedName("Рестораны")
    RESTAURANTS("Рестораны"),
    @SerializedName("Продукты")
    PRODUCTS("Продукты"),
    @SerializedName("Обучение")
    EDUCATION("Обучение");

    private final String description;

    @Override
    public String toString() {
        return description;
    }

}
