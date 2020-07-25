package rencredit.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.openqa.selenium.By.cssSelector;
import static rencredit.NamedBy.css;

public class ContributionsPage {

    public SelenideElement checkBoxInBank = $(css("[name=\"deposit_b_n\"]").as("Чек бокс 'В отделении банка")).parent();
    public SelenideElement inputAmount = $(css("name=\"amount\"]").as("Поле ввода 'Сумма вклада")).parent();

    private By slider = cssSelector("[data-property=\"period\"] .ui-slider-horizontal");//слайдер
    private By sliderHandler = cssSelector("[data-property=\"period\"] .ui-slider-handle");//бегунок слайдера

    @Step("Сдвинуть бегунок слайдера на один пункт влево")
    public void moveSliderHandlerLeft() {
        int width = getWebDriver().findElement(slider).getSize().width;
        WebElement sl = getWebDriver().findElement(sliderHandler);
        Actions actions = new Actions(getWebDriver());
        Action move = actions.clickAndHold(sl).moveByOffset(-width/6, 0).release().build();
        move.perform();
    }

}
