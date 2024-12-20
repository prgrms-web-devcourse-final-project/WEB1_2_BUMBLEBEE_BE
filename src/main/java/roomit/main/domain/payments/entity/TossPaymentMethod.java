package roomit.main.domain.payments.entity;

public enum TossPaymentMethod {
    CARD("신용카드"),
    VIRTUAL_ACCOUNT("가상계좌"),
    SIMPLE_PAYMENT("간편결제"),
    MOBILE_PHONE("휴대폰"),
    BANK_TRANSFER("계좌이체"),
    CULTURAL_TICKET("문화상품권"),
    BOOK_TICKET("도서문화상품권"),
    GAME_TICKET("게임문화상품권");

    private final String description;

    TossPaymentMethod(String description) {
        this.description = description;
    }

    // 설명을 반환하는 메서드
    public String getDescription() {
        return description;
    }
}
