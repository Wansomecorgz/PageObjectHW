package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class TransactionPage {
    private SelenideElement headingh1 = $(byText("Пополнение карты"));
    private SelenideElement sumOfTransfer = $("[data-test-id=amount] input");
    private SelenideElement from = $("[data-test-id=from] input");
    private SelenideElement to = $("[data-test-id=to] input");
    private SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private SelenideElement errorMessage = $(withText("Недостаточно средств для выполнения операции"));

    public DashBoardPage makeTransfer(String amount, String number) {
        sumOfTransfer.setValue(amount);
        from.setValue(number);
        transferButton.click();
        return new DashBoardPage();
    }

    public void getErrorMessage() {
        errorMessage.shouldBe(text("Ошибка! Произошла ошибка"));
    }

}
