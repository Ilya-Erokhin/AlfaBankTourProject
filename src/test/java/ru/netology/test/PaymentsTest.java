package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.CreditCardNumberGenerator;
import ru.netology.data.DataGenerator;
import ru.netology.page.PaymentsPage;
import ru.netology.sql.SqlHelperPayment;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentsTest {
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
        paymentsPage.paymentButton.click();
    }

    @AfterEach
    public void cleanBase() {
        SqlHelperPayment.cleanDefaultData();
    }

    @Test
    @DisplayName("Successfully filling out the form after clicking on button \"Buy\" ")
    public void shouldPaymentForm() {
        paymentsPage.form.should(appear);
        paymentsPage.paymentHeader.should(appear);
    }

    //Have to be successful
    @Test
    @DisplayName("Correct filling of fields, but by clicking on \"Buy\" an error pop up")
    public void successfulPayButAppHaveABug() {
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
    @DisplayName("Filling the short card number as result: Bank rejected cart payment")
    public void invalidPayWithShortCardNumber() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("4567", 8));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.form.shouldHave(text("Номер карты")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    @Test
    @DisplayName("Checking the empty field \"Card number\"")
    public void numberFieldIsEmpty() {
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.form.shouldHave(text("Номер карты")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    //Month
    @Test
    @DisplayName("Filling \"Month\" field with Expired month of card")
    public void expiredYearOfCard() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("4276", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue("10");
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.form.shouldHave(text("Год")).shouldHave(text("Истёк срок действия карты")).shouldBe(visible);
    }

    @Test
    @DisplayName("Filling the \"Month\" field with one digit")
    public void fillingMonthWithOneDigit() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("3742", 16));
        paymentsPage.fieldMonth.setValue("1");
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.form.shouldHave(text("Месяц")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    @Test
    @DisplayName("Filling \"Month\" field with invalid month values")
    public void deniedWithInvalidMonth() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("5536", 16));
        paymentsPage.fieldMonth.setValue("99");
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.form.shouldHave(text("Месяц")).shouldHave(text("Неверно указан срок действия карты")).shouldBe(visible);
    }

    @Test
    @DisplayName("Checking the empty field \"Month\"")
    public void monthFieldIsEmpty() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("2205", 16));
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.form.shouldHave(text("Месяц")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    @Test
    @DisplayName("Checking the entry of \"00\" in the field \"Month\" - BUG")
    public void invalidMonthDoubleZero() {
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
    @DisplayName("Filling \"Year\" field with a value greater than the allowed value")
    public void invalidYearGreaterThanAllowed() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("5537", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue("99");
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        $(withText("Неверно указан срок действия карты")).should(appear);
    }

    @Test
    @DisplayName("Filling the \"Year\" field with one digit")
    public void fillingYearWithOneDigit() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("4289", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue("1");
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.form.shouldHave(text("Год")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    @Test
    @DisplayName("Checking the empty field \"Year\"")
    public void yearFieldIsEmpty() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("5509", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.form.shouldHave(text("Год")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    //Owner
    @Test
    @DisplayName("Filling \"Owner\" field with Russians letters")
    public void russianLettersInNameField() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("7653", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue("Ерохин Илья");
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.failedNotification.should(appear, Duration.ofSeconds(15));
        assertEquals("Error", SqlHelperPayment.getCardStatusDeclined());
    }

    @Test
    @DisplayName("Checking filling the field \"Owner\" with symbols - BUG")
    public void symbolsInOwnerField() {
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
    @DisplayName("Filling the form without LustName - BUG")
    public void deniedFieldNameWithoutLustName() {
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
    @DisplayName("Submitting the \"Owner\" name field with the digits - BUG")
    public void deniedWithInvalidOwnerName() {
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
    @DisplayName("Filling the \\\"CVC/CVV\\\" field with one digit")
    public void fillingCvvFieldWithShortCvv() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("7608", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue("3");
        paymentsPage.proceedButton.click();
        paymentsPage.form.shouldHave(text("CVC/CVV")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }
    @Test
    @DisplayName("Filling the \\\"CVC/CVV\\\" field with \"000\" - BUG")
    public void fillingCvvFieldWithIncorrectCvv() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("7608", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue("000");
        paymentsPage.proceedButton.click();
        paymentsPage.form.shouldHave(text("CVC/CVV")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    @Test
    @DisplayName("Checking the empty field \"CVC/CVV\"")
    public void cvvFieldIsEmpty() {
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("7192", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.proceedButton.click();
        paymentsPage.form.shouldHave(text("CVC/CVV")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    //Close button
    @Test
    @DisplayName("Checking clicking the \"X\" button on an unsuccessful notification ")
    public void closeFailedNotification() {
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
    @DisplayName("Checking the entries data in the table")
    void shouldCreateItem() {
        SqlHelperPayment.cleanDefaultData();
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("4728", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.succeedNotification.should(appear, Duration.ofSeconds(15));
        assertEquals(SqlHelperPayment.getCardIdOrder(), SqlHelperPayment.getCardIdPayment());
    }

    @Test
    @DisplayName("Checking the entries data in the table \"APPROVED\" - BUG")
    void shouldCheckStatusApproved() {
        SqlHelperPayment.cleanDefaultData();
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("5782", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.succeedNotification.should(appear, Duration.ofSeconds(15));
        assertEquals("Error", SqlHelperPayment.getCardStatusDeclined());
    }

    @Test
    @DisplayName("Checking the entries data in the table \"CARD DECLINED\"")
    void shouldCheckStatusDeclined() {
        SqlHelperPayment.cleanDefaultData();
        paymentsPage.fieldNumber.setValue(creditCardNumberGenerator.generate("4572", 16));
        paymentsPage.fieldMonth.setValue(dataGenerator.getRandomMonth());
        paymentsPage.fieldYear.setValue(dataGenerator.getRandomYear());
        paymentsPage.fieldCardOwner.setValue(dataGenerator.generateCardOwnerName());
        paymentsPage.fieldCardCode.setValue(dataGenerator.getCVV());
        paymentsPage.proceedButton.click();
        paymentsPage.succeedNotification.should(appear, Duration.ofSeconds(15));
        assertEquals("Error", SqlHelperPayment.getCardStatusDeclined());
    }
}