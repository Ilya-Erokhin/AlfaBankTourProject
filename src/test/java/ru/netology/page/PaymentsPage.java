package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentsPage {
    public SelenideElement paymentButton = $(withText("Купить"));
    public SelenideElement paymentHeader = $(withText("Оплата по карте"));
    public SelenideElement creditCardButton = $(withText("Купить в кредит"));
    public SelenideElement creditHeader = $(withText("Кредит по данным карты"));
    public SelenideElement form = $("[class='form form_size_m form_theme_alfa-on-white']");
    public SelenideElement form2 = $$("[class='input-group input-group_width_available input-group_theme_alfa-on-white control-group' ]").last();
    public SelenideElement fieldNumber = form.$("[placeholder='0000 0000 0000 0000']");
    public SelenideElement fieldMonth = form.$("[placeholder='08']");
    public SelenideElement fieldYear = form.$("[placeholder='22']");
    public SelenideElement fieldCardOwner = form2.$("[class='input__control']");
    public SelenideElement fieldCardCode = form.$("[placeholder='999']");
    public SelenideElement proceedButton = $(withText("Продолжить"));
    public SelenideElement succeedNotification = $(withText("Операция одобрена Банком."));
    public SelenideElement failedNotification = $(withText("Ошибка! Банк отказал в проведении операции."));
    public SelenideElement closeSucceedNotification = succeedNotification.$("[type='button']");
    public SelenideElement closeFailedNotification = failedNotification.$("[type='button']");
}