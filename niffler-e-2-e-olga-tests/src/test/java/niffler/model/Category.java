package niffler.model;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import lombok.AllArgsConstructor;

import java.lang.reflect.Type;

@AllArgsConstructor
@JsonAdapter(Category.Serializer.class)
public enum Category {
    RESTAURANTS("Рестораны"),
    PRODUCTS("Продукты"),
    EDUCATION("Обучение"),
    BARS("Прочее"),
    BOOKS("Книги");

    private final String description;

    @Override
    public String toString() {
        return description;
    }

    static class Serializer implements JsonSerializer<Category>, JsonDeserializer<Category> {

        @Override
        public JsonElement serialize(Category src, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(src.toString());
        }

        @Override
        public Category deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            for (Category category : Category.values()) {
                if (category.toString().equals(json.getAsString()))
                    return category;
            }
            return BOOKS;
        }
    }

}
