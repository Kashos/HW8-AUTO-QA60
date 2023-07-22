package ru.netology.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import static com.codeborne.selenide.Selenide.*;

import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.pages.LoginPage;

import static ru.netology.data.SQLHelper.cleanDB;

public class DaedlineTest {

    @AfterAll
    static void teardown() {
        cleanDB();
    }

    @Test
    void shouldSuccessfulLogin() {
        var loginPage = open("http://localhost:9999/", LoginPage.class);
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    void ShouldGetErrorInvalidUser() {
        var loginPage = open("http://localhost:9999/", LoginPage.class);
        var authInfo = DataHelper.generateRandomUser();
        loginPage.validLogin(authInfo);
        loginPage.verifyError();
    }

    @Test
    void ShouldGetErrorInvalidVerificationCode() {
        var loginPage = open("http://localhost:9999/", LoginPage.class);
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotificationVisibility();
    }

}
