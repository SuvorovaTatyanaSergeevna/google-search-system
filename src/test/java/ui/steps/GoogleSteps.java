package ui.steps;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;
import ui.pages.ResultPage;
import ui.pages.StartPage;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.logevents.SelenideLogger.step;
import static org.assertj.core.api.Assertions.assertThat;

public class GoogleSteps {

    private final StartPage startPage = new StartPage();

    private final ResultPage resultPage = new ResultPage();

    private final List<String> expectedNameTabs =
            List.of("Все", "Видео", "Картинки", "Покупки", "Новости", "Короткие видео", "Карты");


    @Step("Найти поле поиска и ввести запрос '{value}'")
    public void setValueInSearchBar(String value) {
        startPage.searchBar.should(visible).type(value);
    }

    @Step("Нажать Enter")
    public void pressEnterInSearchBar() {
        startPage.searchBar.pressEnter();
        Selenide.sleep(15000); // необходим для ручного обхода ReCaptcha

        step("Убедиться, что загрузилась страница с результатами поиска", () -> {
           List<String> nameTabs =
                   resultPage.tabs
                           .asDynamicIterable()
                           .stream()
                           .map(el -> el.should(visible, Duration.ofSeconds(80)).getText())
                           .collect(Collectors.toList());
           assertThat(nameTabs).isEqualTo(expectedNameTabs);
           assertThat(resultPage.linkResults).isNotEmpty();
           assertThat(resultPage.videoResults).isNotEmpty();
        });
    }

    @Step("Переключиться на {numberPage} страницу результатов поиска")
    public void goToPage(int numberPage) {
        $x(String.format("//a[@aria-label='Page %s']", numberPage)).hover().should(visible).click();

        step(String.format("Убедиться, что номер текущей страницы изменился на '%s'", numberPage), () ->
                assertThat(resultPage.currentPage.should(visible).getText()).isEqualTo(String.valueOf(numberPage)));
    }

    @Step("Переключиться на вкладку '{tabName}'")
    public void goToTab(String tabName) {
        $$(byText(tabName)).first().should(visible).click();

        step("На странице отображаются изображения", () -> {
            resultPage.images.first().should(visible);
            assertThat(resultPage.images).hasSizeGreaterThan(10);
        });
    }

    @Step("Кликнуть на первое изображение")
    public void clickOnFirstImage() {
        resultPage.images.first().should(visible).click();
    }

    @Step("Проверить, что на странице результатов присутствует поле поиска и кнопка 'Поиск'")
    public void checkSearchBarAndSearchButton() {
        assertThat(resultPage.searchBar.isDisplayed()).isTrue();
        assertThat(resultPage.searchButton.isDisplayed()).isTrue();
    }

    @Step("Убедиться, что внизу страницы есть элементы навигации по страницам")
    public void checkNavigationElements() {
        List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Следующая")
                .forEach(str ->
                        assertThat(
                                $x(String.format("//*[text()='%s']", str))
                                        .hover()
                                        .should(visible)
                                        .isDisplayed())
                                .isTrue());
    }

    @Step("Кликнуть на кнопку 'Инструменты'")
    public void clickOnToolsButton() {
        resultPage.toolsDropdown.should(visible).click();
    }
}
