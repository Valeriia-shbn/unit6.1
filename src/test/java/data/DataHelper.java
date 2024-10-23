package data;

import lombok.Value;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }


    @Value
    public static class VerificationCode {
        String code;
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        return new VerificationCode("12345");
    }

    @Value
    public static class Card {
        String cardNumber;

        public static Card getCard(String cardNumber) {
            return new Card(cardNumber);
        }

        public static Card getFirstCard() {
            return getCard("5559000000000001");
        }

        public static Card getSecondCard() {
            return getCard("5559000000000002");
        }
    }

}