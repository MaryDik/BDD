package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.*;

public class RechargePage {
    private SelenideElement sumField = $("div[data-test-id=amount] input");
    private SelenideElement accountField = $("span[data-test-id=from] input");
    private SelenideElement topUpButton = $("button[data-test-id=action-transfer]");
    private SelenideElement errorNotification = $("div[data-test-id=error-notification]");

    public DashboardPage deposit(int amountDeposit, String sourceCard) {
        setAmount(amountDeposit);
        setSourceCard(sourceCard);
        topUpButton.click();
        return new DashboardPage();
    }

    public void setSourceCard(String sourceCard) {
        accountField.sendKeys(Keys.CONTROL + "A");
        accountField.sendKeys(Keys.DELETE);
        accountField.setValue(sourceCard);
    }

    public void setAmount(int amountDeposit) {
        sumField.sendKeys(Keys.CONTROL + "A");
        sumField.sendKeys(Keys.DELETE);
        sumField.setValue(Integer.toString(amountDeposit));
    }

    public void notableError() {
        errorNotification.shouldBe(Condition.visible);
    }


}


