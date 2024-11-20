package roomit.web1_2_bumblebee_be.domain.member.entity;

public enum Age {
    TEN("10대"),
    TWENTY("20대"),
    THIRTY("30대"),
    FORTY("40대"),
    FIFTY("50대");

    private final String description;

    Age(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
