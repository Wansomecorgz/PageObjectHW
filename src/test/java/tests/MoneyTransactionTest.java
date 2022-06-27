package tests;

import data.DataHelper;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.DashBoardPage;
import pages.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static data.DataHelper.getFirstCardInfo;
import static data.DataHelper.getSecondCardInfo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransactionTest {

    @BeforeEach
    void shouldOpen() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSuccessTransferMoneyFromFirstCardToSecond() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        val firstCardBalance = dashboardPage.getCardBalance(getFirstCardInfo().getNumber());
        val secondCardBalance = dashboardPage.getCardBalance(getSecondCardInfo().getNumber());
        val transferInfoPage = dashboardPage.startTransferFromSecondCard();
        val amount = 2000;
        val newDashboardPage = transferInfoPage.makeTransfer(String.valueOf(amount), getFirstCardInfo().getNumber());

        val actual = dashboardPage.getCardBalance(getSecondCardInfo().getNumber());
        val expected = secondCardBalance + amount;
        assertEquals(expected, actual);
        val actual2 = dashboardPage.getCardBalance(getFirstCardInfo().getNumber());
        val expected2 = firstCardBalance - amount;
        assertEquals(expected2, actual2);
    }

    @Test
    void shouldSuccessTransferMoneyFromSecondCardToFirst(){
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashBoardPage = verificationPage.validVerify(verificationCode);
        val firstCardBalance = dashBoardPage.getCardBalance(getFirstCardInfo().getNumber());
        val secondCardBalance = dashBoardPage.getCardBalance(getSecondCardInfo().getNumber());
        val transferInfoPage = dashBoardPage.startTransferFromFirstCard();
        val amount = 2000;
        val newDashboardPage = transferInfoPage.makeTransfer(String.valueOf(amount), getSecondCardInfo().getNumber());

        val actual = dashBoardPage.getCardBalance(getFirstCardInfo().getNumber());
        val expected = firstCardBalance + amount;
        assertEquals(expected, actual);
        val actual2 = dashBoardPage.getCardBalance(getSecondCardInfo().getNumber());
        val expected2 = secondCardBalance - amount;
        assertEquals(expected2, actual2);
    }

    @Test
    void shouldNotTransferMoneyFromFirstCardToFirst() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        val firstCardBalance = dashboardPage.getCardBalance(getFirstCardInfo().getNumber());
        val transferInfoPage = dashboardPage.startTransferFromFirstCard();
        val amount = 2000;
        val newDashboardPage = transferInfoPage.makeTransfer(String.valueOf(amount), getFirstCardInfo().getNumber());

        val actual = dashboardPage.getCardBalance(getFirstCardInfo().getNumber());
        assertEquals(firstCardBalance, actual);
    }

    @Test
    void shouldNotTransferMoneyOverBalance() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        val firstCardBalance = dashboardPage.getCardBalance(getFirstCardInfo().getNumber());
        val secondCartBalance = dashboardPage.getCardBalance(getSecondCardInfo().getNumber());
        val transferInfoPage = dashboardPage.startTransferFromFirstCard();
        val amount = 20000;
        val bigAmount = Math.abs(secondCartBalance) + amount;
        val newDashboardPage = transferInfoPage.makeTransfer(String.valueOf(bigAmount), getSecondCardInfo().getNumber());

        transferInfoPage.getErrorMessage();
        val actual = dashboardPage.getCardBalance(getFirstCardInfo().getNumber());
        assertEquals(firstCardBalance, actual);
        val actual2 = dashboardPage.getCardBalance(getSecondCardInfo().getNumber());
        assertEquals(secondCartBalance, actual2);
    }
}
