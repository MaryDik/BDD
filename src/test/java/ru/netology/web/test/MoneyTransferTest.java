package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class MoneyTransferTest {
    int topUpBalanceCard1;
    int topUpBalanceCard2;
    int endingBalanceCard1;
    int endingBalanceCard2;
    int amount;
    DashboardPage dashboardPage;

    @BeforeEach
    void SetUp() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
        topUpBalanceCard1 = dashboardPage.getFirstCardBalance();
        topUpBalanceCard2 = dashboardPage.getSecondCardBalance();
    }

    @Test
    void shouldTransferMoneyFromSecondToFirstCard() {
        amount = 100;
        val topUpPage = dashboardPage.depositFirstCard();
        val cardNum = DataHelper.getSecondCard().getNumber();
        val dashboardPage2 = topUpPage.deposit(amount, cardNum);
        endingBalanceCard1 = dashboardPage2.getFirstCardBalance();
        endingBalanceCard2 = dashboardPage2.getSecondCardBalance();
        assertEquals(topUpBalanceCard1 + amount, endingBalanceCard1);
        assertEquals(topUpBalanceCard2 - amount, endingBalanceCard2);
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondCard() {
        amount = 100;
        val topUpPage = dashboardPage.depositSecondCard();
        val cardNum = DataHelper.getFirstCard().getNumber();
        val dashboardPage2 = topUpPage.deposit(amount, cardNum);
        endingBalanceCard1 = dashboardPage2.getFirstCardBalance();
        endingBalanceCard2 = dashboardPage2.getSecondCardBalance();
        assertEquals(topUpBalanceCard1 - amount, endingBalanceCard1);
        assertEquals(topUpBalanceCard2 + amount, endingBalanceCard2);
    }

    @Test
    void shouldNotTransferMoreThanAvailable() {
        amount = topUpBalanceCard1 + 100;
        val topUpPage = dashboardPage.depositSecondCard();
        val cardNum = DataHelper.getFirstCard().getNumber();
        topUpPage.deposit(amount, cardNum);
        topUpPage.notableError();
    }


}




