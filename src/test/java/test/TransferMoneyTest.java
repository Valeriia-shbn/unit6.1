package test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import data.DataHelper;
import org.openqa.selenium.chrome.ChromeOptions;
import page.DashboardPage;
import page.LoginPage;
import com.codeborne.selenide.Configuration;
import page.TransferMoneyPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {

    @BeforeAll
    public static void setupAll() {

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", new java.util.HashMap<String, Object>() {{
            put("credentials_enable_service", false);
            put("profile.password_manager_enabled", false);
        }});

        Configuration.browser = "chrome";
        Configuration.browserCapabilities = options;

    }

    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        open("http://localhost:9999");

        int transferAmount = 200;
        var authInfo = DataHelper.getAuthInfo();
        var firstCardNumber = DataHelper.Card.getFirstCard().getCardNumber();
        var secondCardNumber = DataHelper.Card.getSecondCard().getCardNumber();
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);

        var loginPage = new LoginPage();

        var verificationPage = loginPage.validLogin(authInfo);

        var dashBoardPage = verificationPage.validVerify(verificationCode);

        int initBalanceFirstCard = dashBoardPage.getFirstCardBalance();
        int initBalanceSecondCard = dashBoardPage.getSecondCardBalance();
        var transferMoneyPage = dashBoardPage.transferToFirstCard();

        var dashBoardPageAfterFirstTransfer = transferMoneyPage.transferMoney(secondCardNumber, transferAmount);
        int balanceAfterTransferFirstCard = dashBoardPageAfterFirstTransfer.getFirstCardBalance();
        int balanceAfterTransferSecondCard = dashBoardPageAfterFirstTransfer.getSecondCardBalance();

        int expectedAmountFirstCard = initBalanceFirstCard + transferAmount;
        int expectedAmountSecondCard = initBalanceSecondCard - transferAmount;

        assertEquals(expectedAmountFirstCard, balanceAfterTransferFirstCard);
        assertEquals(expectedAmountSecondCard, balanceAfterTransferSecondCard);

        dashBoardPageAfterFirstTransfer.transferToSecondCard();
        var dashBoardPageAfterSecondTransfer = transferMoneyPage.transferMoney(firstCardNumber, transferAmount);
        int balanceFinalFirstCard = dashBoardPageAfterSecondTransfer.getFirstCardBalance();
        int balanceFinalSecondCard = dashBoardPageAfterSecondTransfer.getSecondCardBalance();


        assertEquals(initBalanceFirstCard, balanceFinalFirstCard);
        assertEquals(initBalanceSecondCard, balanceFinalSecondCard);

    }


}
