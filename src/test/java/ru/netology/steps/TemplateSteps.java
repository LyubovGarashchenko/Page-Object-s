package ru.netology.steps;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.MoneyTransferPage;
import ru.netology.page.VerificationPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static ru.netology.data.DataHelper.generateInvalidAmount;

public class TemplateSteps {

    private static LoginPage loginPage;
    private static DashboardPage dashboardPage;
    private static VerificationPage verificationPage;
    private static MoneyTransferPage moneyTransferPage;

    @Пусть("пользователь залогинен с именем {string} и паролем {string}")
    public void loginWithNameAndPassword(DataHelper.AuthInfo info) {
        verificationPage = loginPage.validLogin(new DataHelper.AuthInfo(info.getLogin(), info.getPassword()));
    }

    @И("пользователь вводит проверочный код 'из смс' {string}")
    public void enterValidCode(DataHelper.VerificationCode code) {
        dashboardPage = verificationPage.validVerify(new DataHelper.VerificationCode("12345"));
    }

    @Когда("пользователь переводит 5 000 рублей с карты с номером 5559 0000 0000 0002 на свою 1 карту с главной страницы")
    public void validMoneyTransfer(DataHelper.CardInfo cardInfo) {
        moneyTransferPage = dashboardPage.selectCardToTransfer(Condition.attribute("data-test-id",
                cardInfo.getTestId())).$("button").click();
    }

    @Тогда("тогда баланс его 1 карты из списка на главной странице должен стать 15 000 рублей.")
    public void setMoneyTransferPage() {
        dashboardPage.getCardBalance(15_000);
    }

    @Когда("пользователь переводит 50 000 рублей с карты с номером 5559 0000 0000 0002 на свою 1 карту с главной страницы")
    public void validMoneyTransfer(DataHelper.CardInfo cardInfo) {
        moneyTransferPage = dashboardPage.selectCardToTransfer(Condition.attribute("data-test-id",
                cardInfo.getTestId())).$("button").click();
    }

    @Тогда("Появится ошибка о выполнении попытки перевода суммы, превышающей остаток на карте списания")
    public void findErrorMessage(String) {
        dashboardPage = moneyTransferPage.findErrorMessage("Появится ошибка о выполнении попытки " +
                "перевода суммы, превышающей остаток на карте списания").shouldHave(text(expectedText),
                Duration.ofSeconds(15)).shouldBe(visible);
    }
}