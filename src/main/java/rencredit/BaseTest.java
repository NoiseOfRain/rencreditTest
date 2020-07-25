package rencredit;

import rencredit.pages.MainPage;
import rencredit.pages.products.CardsPage;
import rencredit.pages.products.ContributionsPage;
import rencredit.pages.products.cards.DebetCardPage;

public class BaseTest {

    public static BaseTest baseTest = new BaseTest();

    public MainPage mainPage() {
        return new MainPage();
    }

    public ContributionsPage contributionsPage() {
        return new ContributionsPage();
    }

    public CardsPage cardsPage() {
        return new CardsPage();
    }

    public DebetCardPage debetCardPage() {
        return new DebetCardPage();
    }

}
