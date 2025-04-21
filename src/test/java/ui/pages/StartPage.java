package ui.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selenide.$;

public class StartPage {

    public final SelenideElement searchBar = $(byId("APjFqb"));
}
