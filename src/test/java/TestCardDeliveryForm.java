import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static org.apache.commons.lang3.StringUtils.*;

public class TestCardDeliveryForm {
  /**
   * генерация даты
   */
  String generateDate(int days, String pattern) {
    return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(pattern));
  }

  @BeforeEach
  void openUrlInBrowser() {
    open("http://localhost:9999");
  }

  @Test
  void shouldSuccessfulSentForm(){
    String date = generateDate(3, "dd.MM.yyyy");
    $("[data-test-id='city'] input").setValue("Москва");
    $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.BACK_SPACE);
    $("[data-test-id='date'] input").setValue(date);
    $("[data-test-id='name'] input").setValue("Василий Бонч-Бруевич");
    $("[data-test-id='phone'] input").setValue("+79269998888");
    $("[data-test-id='agreement']").click();
    $$("button").find(exactText("Забронировать")).click();
    $("[data-test-id='notification']  .notification__title").shouldBe(visible, Duration.ofSeconds(15)).shouldBe(exactText("Успешно!"));
    $("[data-test-id='notification']  .notification__content").shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Встреча успешно забронирована на " + date));
  }

  @Test
  void shouldSuccessfulSentFormWithComplexSelectors(){
    int period = 7;
    String date = generateDate(period, "dd.MM.yyyy");
    String day = generateDate(period, "dd");
    String month = generateDate(period, "MM");
    String currentMonth = generateDate(0, "MM");
    $("[data-test-id='city'] input").setValue("Ка");
    $$(".input__popup .menu-item_type_block").findBy(exactText("Казань")).click();
    $("[data-test-id='date']").click();
    if (Integer.parseInt(month) > Integer.parseInt(currentMonth)){
      $$(".calendar__arrow_direction_right").findBy(attribute("data-step","1")).click();
      $$(".calendar__day" ).find(exactText(stripStart(day,"0"))).click();
    } else {
    $$(".calendar__day" ).find(exactText(stripStart(day,"0"))).click();
    }
    $("[data-test-id='name'] input").setValue("Василий Бонч-Бруевич");
    $("[data-test-id='phone'] input").setValue("+79269998888");
    $("[data-test-id='agreement']").click();
    $$("button").find(exactText("Забронировать")).click();
    $("[data-test-id='notification']  .notification__title").shouldBe(visible, Duration.ofSeconds(15)).shouldBe(exactText("Успешно!"));
    $("[data-test-id='notification']  .notification__content").shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Встреча успешно забронирована на " + date));
  }
}
