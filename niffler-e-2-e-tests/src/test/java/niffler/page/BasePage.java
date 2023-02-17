package niffler.page;

public class BasePage<T extends BasePage> {

   public T waitForPageLoaded() {
       //do smth
       return (T) this;
   }
}
