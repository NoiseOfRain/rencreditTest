package rencredit.pages.products;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static rencredit.NamedBy.css;

public class CardsPage {

    public SelenideElement linkFillRequestForCard = $(css("a[href=\"/app/debetcard/365\"]").as("Ссылка 'Заполнить заявку на карту' для дебетовой карты"));

}
