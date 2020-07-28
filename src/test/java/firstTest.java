import com.codeborne.selenide.AssertionMode;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.codeborne.selenide.testng.SoftAsserts;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.model.StepResult;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import rencredit.allure.AllureSelenideCustom;
import rencredit.db.Driver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static java.lang.String.format;
import static rencredit.BaseTest.baseTest;

@Feature("Получение ПДФ с условиями по вкладу")
@Listeners({SoftAsserts.class})
public class firstTest {

    StringBuilder steps = new StringBuilder();

    @BeforeClass
    void setUp() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenideCustom()
                .screenshots(false)
        );
        Configuration.assertionMode = AssertionMode.SOFT;
        Configuration.startMaximized = true;
        Driver.clearDB();
    }

    @Test
    public void logic() throws IOException {
        open("http://rencredit.ru");
        baseTest.mainPage().contributions.click();
        baseTest.contributionsPage().checkBoxInBank.click();
        baseTest.contributionsPage().inputAmount.setValue("777000");
        baseTest.contributionsPage().moveSliderHandlerLeft();
        baseTest.contributionsPage().linkGeneralConditionsPDF.shouldBe(Condition.visible);
        downloadPdf();
        saveResults();
    }

    private void saveResults() {
        Allure.getLifecycle().updateTestCase(test -> {
            for (StepResult step : test.getSteps()) {
                steps.append(format("INSERT INTO RENES (STEP_START, STEP_END, STEP_NAME) VALUES ('%s', '%s', '%s');\n",
                        UnixTimeToDateTime(step.getStart()),
                        UnixTimeToDateTime(step.getStop()),
                        step.getName()));
            }
        });
    }

    @Step("Выгрузить Печатную Форму \"Общие условия по вкладам\"")
    private void downloadPdf() throws IOException {
        File file = new File("target/test.pdf");
        FileUtils.copyURLToFile(new URL("https://rencredit.ru/upload/iblock/ac3/9_27_20.pdf"), file);//- ссылка закоммичена в коде страницы
        Assert.assertTrue(file.delete());
    }

    @AfterClass
    void tearDown() {
        Driver.updateData(steps.toString());

        closeWebDriver();
        SelenideLogger.removeListener("AllureSelenide");
    }

    private String UnixTimeToDateTime(Long time) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS");
        return format.format(date);
    }

}
