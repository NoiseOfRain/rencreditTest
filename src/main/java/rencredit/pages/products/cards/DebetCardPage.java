package rencredit.pages.products.cards;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static rencredit.NamedBy.css;
import static rencredit.NamedBy.xpath;

public class DebetCardPage {

    public SelenideElement inputClientLastName = $(css("[name=\"ClientLastName\"]").as("Поле ввода 'Фамилия'"));
    public SelenideElement inputClientName = $(css("[name=\"ClientName\"]").as("Поле ввода 'Имя'"));
    public SelenideElement inputClientMobilePhone = $(css("[name=\"ClientMobilePhone\"]").as("Поле ввода 'Мобильный телефон'"));
    public SelenideElement inputEmail = $(css("[name=\"AdditionalEmail\"]").as("Поле ввода 'E-mail'"));

    public SelenideElement checkboxNoSecondName = $(css(".jq-checkbox").as("Чек бокс 'Нет отчества'"));
    private SelenideElement selectCreditRegion = $(xpath("//div[./select[@name=\"CreditRegion\"]]").as("Список 'Где Вы желаете получить карту?'"));
    private ElementsCollection regionsList = $$(css(".jq-selectbox__dropdown li").as("Список регионов"));

    public void selectRegionForGettingCard(Regions regions) {
        selectCreditRegion.click();
        regionsList.findBy(Condition.text(regions.toString())).click();
    }

    public enum Regions {
        samara("Самарская область"),
        saratov("Саратовская область"),
        ylanovsk("Ульяновская область");
        private String name;

        Regions(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
