//package roomit.web1_2_bumblebee_be.domain.business.entity.value;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Embeddable;
//import lombok.AccessLevel;
//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import java.io.Serializable;
//import java.util.UUID;
//
//@Embeddable
//@Getter
//@EqualsAndHashCode
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class BusinessId implements Serializable {
//
//    @Column(name = "business_id")
//    private String value;
//
//    private BusinessId(String value) {
//        this.value = value;
//    }
//
//    public static BusinessId create() {
//        return new BusinessId(UUID.randomUUID().toString());
//    }
//
//}


/*
UUID로 생성된 고유값 id값으로 생성
추후 변경될 가능성 존재
 */