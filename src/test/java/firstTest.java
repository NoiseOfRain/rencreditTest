import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class firstTest {

    @Test
    public void logic() throws IOException {
        open("https://rencredit.ru/");
        $("a[href=\"/contributions/\"]").click();//вклады
        $("[name=\"deposit_b_n\"]").parent().click();//чек бокс в отделении банка
        $("[name=\"amount\"]").setValue("777000");//ввести суммму вклада
        $("[data-property=\"period\"] .ui-slider-horizontal").getLocation();//ввести суммму вклада
        $("[data-property=\"period\"] .ui-slider-handle").getLocation();//ввести суммму вклада
        $("[data-property=\"period\"] .ui-slider-handle").getAttribute("style");//ввести суммму вклада

        WebElement sl = getWebDriver().findElement(By.cssSelector("[data-property=\"period\"] .ui-slider-handle"));

        Actions actions = new Actions(getWebDriver());
        Action move = actions.clickAndHold(sl).moveByOffset(200, 0   ).release().build();

        move.perform();        //5) Передвинуть ползунок &quot;На срок&quot;

        //https://rencredit.ru/upload/iblock/ac3/9_27_20.pdf - ссылка закоммичена в коде страницы
        //6) Выгрузить Печатную Форму &quot;Общие условия по вкладам&quot;
        FileUtils.copyURLToFile(new URL("https://rencredit.ru/upload/iblock/ac3/9_27_20.pdf"), new File("target/test.pdf"));
    }

}
