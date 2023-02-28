package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.CreditCardNumberGenerator;
import ru.netology.data.DataGenerator;
import ru.netology.page.PaymentsPage;
import ru.netology.sql.SqlHelperCredit;
import ru.netology.sql.SqlHelperPayment;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentsCreditTest {
    PaymentsPage paymentsPage = new PaymentsPage();
    DataGenerator dataGenerator = new DataGenerator();
    CreditCardNumberGenerator creditCardNumberGenerator = new CreditCardNumberGenerator();

    @BeforeAll
    static void SetUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:8080");
        paymentsPage.creditCardButton.click();
    }

    @AfterEach
    public void cleanBase() {
        SqlHelperCredit.cleanDefaultData();
    }

    @Test
    @DisplayName("Successfully filling out the form after clicking on button \"Buy in credit\" ")
    public void shouldPaymentCreditForm() {
        paymentsPage.form.should(appear);
        paymentsPage.creditHeader.should(appear);
    }

    //Have to be successful
    @Test
    @DisplayName("Correct filling of fields, but by clicking on \"Buy in credit\" an error pop up - BUG In Credit")
    public void successfulPayButAppHaveABugInCredit() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("5536", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.succeedNotification.should(appear, Duration.ofSeconds(20));
    }

    //Card Number
    @Test
    @DisplayName("Filling the short card number, as result: Bank rejected cart payment In Credit")
    public void invalidPayWithShortCardNumberInCredit() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("4567", 8));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.form.shouldHave(text("Номер карты")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    @Test
    @DisplayName("Checking the empty field \"Card number\" In Credit")
    public void numberFieldIsEmptyInCredit() {
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.form.shouldHave(text("Номер карты")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    //Month
    @Test
    @DisplayName("Filling \"Month\" field with Expired month of card In Credit")
    public void expiredYearOfCardInCredit() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("4276", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue("10");
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.form.shouldHave(text("Год")).shouldHave(text("Истёк срок действия карты")).shouldBe(visible);
    }

    @Test
    @DisplayName("Filling the \"Month\" field with one digit In Credit")
    public void fillingMonthWithOneDigitInCredit() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("3742", 16));
        paymentsPage.fieldMonth.setValue("1");
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.form.shouldHave(text("Месяц")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    @Test
    @DisplayName("Filling \"Month\" field with invalid month values In Credit")
    public void deniedWithInvalidMonthInCredit() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("5536", 16));
        paymentsPage.fieldMonth.setValue("99");
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.form.shouldHave(text("Месяц")).shouldHave(text("Неверно указан срок действия карты")).shouldBe(visible);
    }

    @Test
    @DisplayName("Checking the empty field \"Month\" In Credit")
    public void monthFieldIsEmpty() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("2205", 16));
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.form.shouldHave(text("Месяц")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    @Test
    @DisplayName("Checking the entry of \"00\" in the field \"Month\" - BUG In Credit")
    public void invalidMonthDoubleZeroInCredit() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("5623", 16));
        paymentsPage.fieldMonth.setValue("00");
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.succeedNotification.should(appear, Duration.ofSeconds(15));
        assertEquals("APPROVED", SqlHelperPayment.getCardStatusDeclined());
    }

    //Year
    @Test
    @DisplayName("Filling \"Year\" field with a value greater than the allowed value In Credit")
    public void invalidYearGreaterThanAllowedInCredit() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("5537", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue("99");
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        $(withText("Неверно указан срок действия карты")).should(appear);
    }

    @Test
    @DisplayName("Filling the \"Year\" field with one digit In Credit")
    public void fillingYearWithOneDigitInCredit() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("4289", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue("1");
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.form.shouldHave(text("Год")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    @Test
    @DisplayName("Checking the empty field \"Year\" In Credit")
    public void yearFieldIsEmptyInCredit() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("5509", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.form.shouldHave(text("Год")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    //Owner
    @Test
    @DisplayName("Filling \"Owner\" field with Russians letters In Credit")
    public void russianLettersInNameFieldInCredit() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("7653", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue("Ерохин Илья");
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.succeedNotification.should(appear, Duration.ofSeconds(15));
        assertEquals("Error", SqlHelperCredit.getCardStatusDeclined());
    }

    @Test
    @DisplayName("Checking filling the field \"Owner\" with symbols - BUG In Credit")
    public void symbolsInOwnerFieldInCredit() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("2455", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue("-?'`}]]");
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.succeedNotification.should(appear, Duration.ofSeconds(15));
        assertEquals("APPROVED", SqlHelperPayment.getCardStatusDeclined());
    }

    @Test
    @DisplayName("Filling the form without LustName - BUG In Credit")
    public void deniedFieldNameWithoutLustNameInCredit() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("5687", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue("Ilia");
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.succeedNotification.should(appear, Duration.ofSeconds(15));
        assertEquals("APPROVED", SqlHelperPayment.getCardStatusDeclined());
    }

    @Test
    @DisplayName("Submitting the \"Owner\" name field with the digits - BUG In Credit")
    public void deniedWithInvalidOwnerNameInCredit() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("3452", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.getCVV());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.succeedNotification.should(appear, Duration.ofSeconds(15));
        assertEquals("APPROVED", SqlHelperPayment.getCardStatusDeclined());
    }

    //CVC/CVV
    @Test
    @DisplayName("Filling the \\\"CVC/CVV\\\" field with one digit In Credit")
    public void fillingCvvFieldWithShortCvvInCredit() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("7608", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue("3");
        paymentsPage.proceedButton.click();
        paymentsPage.form.shouldHave(text("CVC/CVV")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    @Test
    @DisplayName("Checking the empty field \"CVC/CVV\" In Credit")
    public void cvvFieldIsEmptyInCredit() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("7192", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.proceedButton.click();
        paymentsPage.form.shouldHave(text("CVC/CVV")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    //Close button
    @Test
    @DisplayName("Checking clicking the \"X\" button on an unsuccessful notification In Credit")
    public void closeFailedNotificationInCredit() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("5242", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.succeedNotification.should(appear, Duration.ofSeconds(15));
        paymentsPage.closeFailedNotification.click();
        paymentsPage.succeedNotification.should(disappear);
    }

    //Sql
    @Test
    @DisplayName("Checking the entries data in the table In Credit")
    void shouldCreateItemInCredit() {
        SqlHelperPayment.cleanDefaultData();
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("4728", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.succeedNotification.should(appear, Duration.ofSeconds(15));
        assertEquals(SqlHelperPayment.getCardIdOrder(), SqlHelperCredit.getCardIdCredit());
    }

    @Test
    @DisplayName("Checking the entries data in the table \"APPROVED\" - BUG In Credit")
    void shouldCheckStatusApprovedInCredit() {
        SqlHelperPayment.cleanDefaultData();
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("5782", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.succeedNotification.should(appear, Duration.ofSeconds(15));
        assertEquals("Error", SqlHelperCredit.getCardStatusDeclined());
    }

    @Test
    @DisplayName("Checking the entries data in the table \"CARD DECLINED\" In Credit")
    void shouldCheckStatusDeclinedInCredit() {
        SqlHelperPayment.cleanDefaultData();
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("4572", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.succeedNotification.should(appear, Duration.ofSeconds(15));
        assertEquals("Error", SqlHelperCredit.getCardStatusDeclined());
    }
}