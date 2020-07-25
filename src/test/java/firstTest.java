import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import rencredit.AllureSelenideCustom;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static com.codeborne.selenide.Selenide.open;
import static rencredit.BaseTest.baseTest;

public class firstTest {

    @BeforeTest
    void setUp() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenideCustom()
        );
    }

    @Test
    public void logic() throws IOException {
        open("http://rencredit.ru");
        baseTest.mainPage().contributions.click();
        baseTest.contributionsPage().checkBoxInBank.click();
        baseTest.contributionsPage().inputAmount.setValue("777000");
        baseTest.contributionsPage().moveSliderHandlerLeft();
        downloadPdf();
    }

    @Step("Выгрузить Печатную Форму \"Общие условия по вкладам\"")
    private void downloadPdf() throws IOException {
        File file = new File("target/test.pdf");
        FileUtils.copyURLToFile(new URL("https://rencredit.ru/upload/iblock/ac3/9_27_20.pdf"), file);//- ссылка закоммичена в коде страницы
        Assert.assertTrue(file.delete());
    }

}
