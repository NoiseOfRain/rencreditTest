package rencredit;

import rencredit.pages.ContributionsPage;
import rencredit.pages.MainPage;

public class BaseTest {

    public static BaseTest baseTest = new BaseTest();

    public MainPage mainPage() {
        return new MainPage();
    }

    public ContributionsPage contributionsPage() {
        return new ContributionsPage();
    }


}
