import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static com.codeborne.selenide.Selenide.open;
import static rencredit.BaseTest.baseTest;

public class firstTest {

    @Test
    public void logic() throws IOException {
        open("https://rencredit.ru/");
        baseTest.mainPage().contributions.click();
        baseTest.contributionsPage().checkBoxInBank.click();
        baseTest.contributionsPage().inputAmount.setValue("777000");
        baseTest.contributionsPage().moveSliderHandlerLeft();
        downloadPdf();
    }

    @Step("Выгрузить Печатную Форму \"Общие условия по вкладам\"")
    private void downloadPdf() throws IOException {
        FileUtils.copyURLToFile(new URL("https://rencredit.ru/upload/iblock/ac3/9_27_20.pdf"), new File("target/test.pdf"));//- ссылка закоммичена в коде страницы
    }

}
