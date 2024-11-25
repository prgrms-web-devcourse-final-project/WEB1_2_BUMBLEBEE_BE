//package roomit.web1_2_bumblebee_be.domain.business.service;
//
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import roomit.web1_2_bumblebee_be.domain.business.entity.Business;
//import roomit.web1_2_bumblebee_be.domain.business.repository.BusinessRepository;
//import roomit.web1_2_bumblebee_be.domain.business.dto.request.BusinessRegisterRequest;
//import roomit.web1_2_bumblebee_be.domain.business.dto.request.BusinessUpdateRequest;
//import java.util.NoSuchElementException;
//import java.util.stream.IntStream;
//
//import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
//@ActiveProfiles("test")
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class BusinessServiceTest {
//
//    @Autowired
//    private BusinessService businessService;
//
//    @Autowired
//    private BusinessRepository businessRepository;
//
//    @BeforeAll
//    static void setUp(BusinessService businessService, BusinessRepository businessRepository) {
//        businessRepository.deleteAll(); // 전체 데이터 삭제
//
//        IntStream.rangeClosed(1, 9).forEach(i -> {
//            BusinessRegisterRequest businessRegisterRequest = BusinessRegisterRequest.builder()
//                    .businessName("테스트사업자" + i)
//                    .businessEmail("business" + i + "@gmail.com")
//                    .businessPwd("Business1!")
//                    .businessNum("123-12-1234" + i)
//                    .build();
//
//            businessService.signUpBusiness(businessRegisterRequest); // 데이터 생성
//
//        });
//    }
//
//    @Test
//    @Order(1)
//    @DisplayName("사업자 수정")
//    void testModify(){
//        //given
//        BusinessUpdateRequest businessUpdateRequest = BusinessUpdateRequest.builder()
//                .businessEmail("businessModify@gmail.com")
//                .businessName("ModifyTest")
//                .businessNum("999-99-99999")
//                .build();
//
//        Long businessId = 1L;
//
//        //When
//        businessService.updateBusinessInfo(businessId, businessUpdateRequest);
//
//        Business business = businessRepository.findByBusinessEmail("businessModify@gmail.com")
//                .orElseThrow(NoSuchElementException::new);
//
//        //Then
//        assertEquals("ModifyTest",business.getBusinessName());
//        assertEquals("999-99-99999",business.getBusinessNum());
//    }
//
//    @Test
//    @Order(2)
//    @DisplayName("사업자 삭제")
//    void testDelete(){
//        //Given
//        Long businessId = 2L;
//
//        //When
//        businessService.deleteBusiness(businessId);
//
//
//        //Then
//        assertThrows(NoSuchElementException.class, () -> {
//            businessRepository.findById(businessId).orElseThrow(NoSuchElementException::new); // 이미 삭제된 비즈니스 조회
//        });
//    }
//
//}
