package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.*;
import ui.pages.ResultPage;
import ui.steps.GoogleSteps;

import java.util.List;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.logevents.SelenideLogger.step;
import static org.assertj.core.api.Assertions.assertThat;

@Owner("SuvorovaTS")
@DisplayName("Автоматизация поиска в Google")
public class SearchInGoogleTest {

    private final GoogleSteps steps = new GoogleSteps();

    private final ResultPage resultPage = new ResultPage();

    private final String searchValue = "Selenide Java";

    @BeforeAll
    static void editConfig() {
        Configuration.pageLoadStrategy ="none";
        Configuration.pageLoadTimeout = 35_000L;
    }

    @BeforeEach
    void setUp() {
        step("Открыть главную страницу Google", () -> open("https://www.google.com"));
    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }

    @Test
    @DisplayName("Проверка работы поиска")
    void checkSearchTest() {
        steps.setValueInSearchBar(searchValue);
        steps.pressEnterInSearchBar();

        step("Заголовки первых 10 результатов содержат слово 'Selenide'", () -> {
            resultPage.linkResults
                    .asDynamicIterable()
                    .forEach(el -> assertThat(el.getText().toLowerCase()).contains("selenide"));
            resultPage.videoResults
                    .asDynamicIterable()
                    .forEach(el -> assertThat(el.getText().toLowerCase()).contains("selenide"));
        });
    }

    @Test
    @DisplayName("Проверка пагинации")
    void checkPaginationTest() {
        steps.setValueInSearchBar(searchValue);
        steps.pressEnterInSearchBar();
        steps.goToPage(2);

        step("На второй странице также отображаются результаты поиска", () ->
                resultPage.linkResults
                        .asDynamicIterable()
                        .forEach(el -> assertThat(el.getText().toLowerCase()).contains("selenide")));
    }

    @Test
    @DisplayName("Проверка изображений на странице поиска")
    void checkImagesTest() {
        steps.setValueInSearchBar(searchValue);
        steps.pressEnterInSearchBar();
        steps.goToTab("Картинки");
        steps.clickOnFirstImage();

        step("Открывается увеличенная версия изображения", () -> {
            SelenideElement smallImage = resultPage.images.first();

            int smallHeight = Integer.parseInt(Objects.requireNonNull(smallImage.getAttribute("height")));
            int smallWidth = Integer.parseInt(Objects.requireNonNull(smallImage.getAttribute("width")));
            String altSmallImage = smallImage.getAttribute("alt");

            SelenideElement bigImage =
                    $$x(String.format(".//a/img[@alt='%s']", altSmallImage))
                            .first()
                            .should(visible)
                            .$x("./following-sibling::span[@class='UWuvyf']");
            String[] bigImageSizes = bigImage.getOwnText().split(" × ");
            int bigWidth = Integer.parseInt(bigImageSizes[0]);
            int bigHeight = Integer.parseInt(bigImageSizes[1]);

            assertThat(smallHeight).isLessThan(bigHeight);
            assertThat(smallWidth).isLessThan(bigWidth);
        });
    }

    @Test
    @DisplayName("Проверка отображения других элементов на странице")
    void checkOtherElementsTest() {
        steps.setValueInSearchBar(searchValue);
        steps.pressEnterInSearchBar();
        steps.checkSearchBarAndSearchButton();
        steps.checkNavigationElements();
        steps.clickOnToolsButton();

        step("Проверить, что отображается выпадающий список при клике на кнопку 'Инструменты'", () ->
                List.of("На всех языках", "За всё время", "Все результаты", "Найти")
                        .forEach(str ->
                                assertThat(
                                        $$x(String.format("//*[text()='%s']", str))
                                                .first()
                                                .should(visible)
                                                .isDisplayed())
                                        .isTrue()));
    }
}
