package ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selenide.*;

public class ResultPage {

    public final ElementsCollection tabs = $$x("//*[@class='YmvwI']");

    public final ElementsCollection linkResults =
            $$x("//div[@class='MjjYud' and not(.//div[@class='Wt5Tfe'])]//a/h3");

    public final ElementsCollection videoResults =
            $$x("//div[@class='MjjYud' and not(.//div[@class='Wt5Tfe'])]//a//span[@class='cHaqb QOGdqf']");

    public final SelenideElement currentPage = $x("//td[@class='YyVfkd NKTSme']");

    public final ElementsCollection images = $$x("//img[@class='YQ4gaf']");

    public final SelenideElement searchBar = $x("//*[@aria-label='Найти']");

    public final SelenideElement searchButton = $x("//*[@aria-label='Поиск']");

    public final SelenideElement toolsDropdown = $(byId("hdtb-tls"));
}
