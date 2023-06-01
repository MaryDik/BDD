package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.RechargePage;


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
    topUpBalanceCard1 = dashboardPage.getBalance(dashboardPage.card1);
    topUpBalanceCard2 = dashboardPage.getBalance(dashboardPage.card2);
  }

  @Test
  void shouldTransferMoneyFromSecondToFirstCard() {
    amount = 100;
    val topUpPage = dashboardPage.clickRechargePage(dashboardPage.card1);
    val cardNum = DataHelper.getSecondCard().getNumber();
    val dashboardPage2 = topUpPage.successfulRecharge(Integer.toString(amount), cardNum);
    endingBalanceCard1 = dashboardPage2.getBalance(dashboardPage2.card1);
    endingBalanceCard2 = dashboardPage2.getBalance(dashboardPage2.card2);
    assertEquals(topUpBalanceCard1 + amount, endingBalanceCard1);
    assertEquals(topUpBalanceCard2 - amount, endingBalanceCard2);
  }

  @Test
  void shouldTransferMoneyFromFirstToSecondCard() {
    amount = 100;
    val topUpPage = dashboardPage.clickRechargePage(dashboardPage.card2);
    val cardNum = DataHelper.getFirstCard().getNumber();
    val dashboardPage2 = topUpPage.successfulRecharge(Integer.toString(amount), cardNum);
    endingBalanceCard1 = dashboardPage2.getBalance(dashboardPage2.card1);
    endingBalanceCard2 = dashboardPage2.getBalance(dashboardPage2.card2);
    assertEquals(topUpBalanceCard1 - amount, endingBalanceCard1);
    assertEquals(topUpBalanceCard2 + amount, endingBalanceCard2);
  }

  @Test
  void shouldNotTransferMoreThanAvailable() {
    amount = topUpBalanceCard1 + 100;
    val topUpPage = dashboardPage.clickRechargePage(dashboardPage.card2);
    val cardNum = DataHelper.getFirstCard().getNumber();
    topUpPage.successfulRecharge(Integer.toString(amount), cardNum);
  }

}


