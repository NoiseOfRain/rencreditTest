import com.codeborne.selenide.AssertionMode;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Feature;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import rencredit.allure.AllureSelenideCustom;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static rencredit.BaseTest.baseTest;
import static rencredit.pages.products.cards.DebetCardPage.Regions.samara;

@Feature("Заказ дебетовой карты")
public class secondTest {

    @BeforeClass
    void setUp() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenideCustom()
                .screenshots(true)
        );
        Configuration.assertionMode = AssertionMode.STRICT;
        Configuration.startMaximized = true;
    }

    @AfterClass
    void tearDown() {
        closeWebDriver();
        SelenideLogger.removeListener("AllureSelenide");
    }

    @Test
    public void logic() {
        open("http://rencredit.ru");
        baseTest.mainPage().cards.click();
        baseTest.cardsPage().linkFillRequestForCard.click();
        baseTest.debetCardPage().inputClientLastName.setValue("Cтар");
        baseTest.debetCardPage().inputClientName.setValue("Вит");
        baseTest.debetCardPage().inputClientMobilePhone.setValue("9093652211");
        baseTest.debetCardPage().inputEmail.setValue("star@gmail.com");
        baseTest.debetCardPage().checkboxNoSecondName.click();
        baseTest.debetCardPage().selectRegionForGettingCard(samara);
    }

}
