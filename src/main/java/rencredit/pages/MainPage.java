package rencredit.pages;

import com.codeborne.selenide.SelenideElement;
import rencredit.BaseTest;

import static com.codeborne.selenide.Selenide.$;
import static rencredit.NamedBy.css;

public class MainPage extends BaseTest {

    public SelenideElement cards = $(css("a[href=\"/cards/\"]").as("Пункт 'Карты'"));

    public SelenideElement contributions = $(css("a[href=\"/contributions/\"]").as("Пункт 'Вклады'"));

}
