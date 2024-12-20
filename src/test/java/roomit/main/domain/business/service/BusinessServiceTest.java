package roomit.main.domain.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import roomit.main.domain.business.dto.request.BusinessRegisterRequest;
import roomit.main.domain.business.dto.request.BusinessUpdateRequest;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.business.repository.BusinessRepository;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 클래스 인스턴스를 하나만 생성하도록 설정
public class BusinessServiceTest {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private BusinessRepository businessRepository;

    private static long businessId;

    @BeforeAll
    void setUp() {
        IntStream.rangeClosed(1, 5).forEach(i -> {
            BusinessRegisterRequest businessRegisterRequest = BusinessRegisterRequest.builder()
                .businessName("테스트사업자" + i)
                .businessEmail("business" + i + 1 + "@gmail.com")
                .businessPwd("Business1!")
                .businessNum("123-99-1234" + i)
                .build();

            businessService.signUpBusiness(businessRegisterRequest); // 데이터 생성

            businessId = businessRepository.findByBusinessEmail("business" + i + 1 + "@gmail.com").orElseThrow(NoSuchElementException::new).getBusinessId();
        }
        );
    }

    @Test
    @Order(1)
    @DisplayName("사업자 수정")
    void testModify() {
        // Given
        BusinessUpdateRequest businessUpdateRequest = BusinessUpdateRequest.builder()
            .businessEmail("businessModify@gmail.com")
            .businessName("ModifyTest")
            .businessNum("999-99-99999")
            .build();

        // When
        businessService.updateBusinessInfo(businessId, businessUpdateRequest);

        Business business = businessRepository.findByBusinessEmail("businessModify@gmail.com")
            .orElseThrow(NoSuchElementException::new);

        // Then
        assertEquals("ModifyTest", business.getBusinessName());
        assertEquals("999-99-99999", business.getBusinessNum());
    }

    @Test
    @Order(2)
    @DisplayName("사업자 삭제")
    void testDelete() {
        // When
        businessService.deleteBusiness(businessId);

        // Then
        assertThrows(NoSuchElementException.class, () -> {
            businessRepository.findById(businessId).orElseThrow(NoSuchElementException::new); // 이미 삭제된 비즈니스 조회
        });
    }
}
