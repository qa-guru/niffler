package constant;

public enum SpendsCategories {

    Test("Test"),
    Restoran("Рестораны"),
    Magazin("Продуктовые магазины"),
    Study("Обучение в QA.GURU ADVANCED");

    public final String spendsCategories;

    SpendsCategories(String spendsCategories) {
        this.spendsCategories = spendsCategories;
    }
}
