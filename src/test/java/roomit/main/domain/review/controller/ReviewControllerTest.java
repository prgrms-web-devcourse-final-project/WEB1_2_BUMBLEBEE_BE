//package roomit.main.domain.review.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.jayway.jsonpath.JsonPath;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.transaction.annotation.Transactional;
//import roomit.main.domain.member.dto.CustomMemberDetails;
//import roomit.main.domain.member.entity.Member;
//import roomit.main.domain.member.entity.Sex;
//import roomit.main.domain.member.repository.MemberRepository;
//import roomit.main.domain.review.dto.request.ReviewRegisterRequest;
//import roomit.main.domain.review.dto.request.ReviewUpdateRequest;
//import roomit.main.domain.review.entity.Review;
//import roomit.main.domain.review.repository.ReviewRepository;
//import roomit.main.domain.token.dto.LoginRequest;
//import roomit.main.domain.token.dto.LoginResponse;
//import roomit.main.domain.workplace.entity.Workplace;
//import roomit.main.domain.workplace.repository.WorkplaceRepository;
//
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.IntStream;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//@AutoConfigureMockMvc
//@SpringBootTest
//@ActiveProfiles("test")
//class ReviewControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private ReviewRepository reviewRepository;
//
//    @Autowired
//    private WorkplaceRepository workplaceRepository;
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    private LocalDate date;
//
//    private Member member;
//
//    private Workplace workplace;
//
//
//
//    @BeforeEach
//    void setUp()  {
//        memberRepository.deleteAll();
//        reviewRepository.deleteAll();
//        workplaceRepository.deleteAll();
//
//        date = LocalDate.of(2024, 11, 22);
//
//        member =  Member.builder()
//                .birthDay(date)
//                .memberSex(Sex.FEMALE)
//                .memberPwd("Business1!")
//                .memberEmail("sdsd@naver.com")
//                .memberPhoneNumber("010-3323-4242")
//                .memberNickName("치킨유저")
//                .passwordEncoder(bCryptPasswordEncoder)
//                .build();;
//
//        workplace = Workplace.builder()
//                .workplaceName("사업장")
//                .workplacePhoneNumber("0507-1234-5678")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("대한민국입니다")
//                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
//                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
//                .build();
//
//    }
//
//    @Test
//    @DisplayName("리뷰 등록")
//    @Transactional
//    void test1() throws Exception{
//
//
//        memberRepository.save(member);
//
//        workplaceRepository.save(workplace);
//
//        LoginRequest loginRequest = LoginRequest.builder()
//                .email("sdsd@naver.com")
//                .password("Business1!")
//                .build();
//
//        String json = objectMapper.writeValueAsString(loginRequest);
//
//        MvcResult loginResult = mockMvc.perform(post("/login/member")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        LoginResponse loginResponse = objectMapper.readValue(loginResult.getResponse().getContentAsString(), LoginResponse.class);
//        String token = loginResponse.getToken();
//
//        ReviewRegisterRequest request = ReviewRegisterRequest.builder()
//                .reviewContent("좋은 장소네요")
//                .reviewRating(3.4)
//                .workplaceId(workplace.getWorkplaceId())
//                .build();
//
//        mockMvc.perform(post("/api/v1/review/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//                        .header("Authorization", "Bearer " + token))
//                .andExpect(status().isCreated())
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("리뷰 수정")
//    void test2() throws Exception{
//
//
//
//        memberRepository.save(member);
//
//        workplaceRepository.save(workplace);
//
//        LoginRequest loginRequest = LoginRequest.builder()
//                .email("sdsd@naver.com")
//                .password("Business1!")
//                .build();
//
//        String json = objectMapper.writeValueAsString(loginRequest);
//
//        MvcResult loginResult = mockMvc.perform(post("/login/member")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        LoginResponse loginResponse = objectMapper.readValue(loginResult.getResponse().getContentAsString(), LoginResponse.class);
//        String token = loginResponse.getToken();
//
//
//        Review review = Review.builder()
//                .reviewContent("치킨이 안보이네요..")
//                .reviewRating(1.3)
//                .member(member)
//                .workplace(workplace)
//                .build();
//        reviewRepository.save(review);
//
//
//        ReviewUpdateRequest request = ReviewUpdateRequest.builder()
//                .memberId(member.getMemberId())
//                .reviewContent("좋은 장소네요")
//                .reviewRating(2.4)
//                .workplaceId(workplace.getWorkplaceId())
//                .build();
//
//        mockMvc.perform(put("/api/v1/review/update/{reviewId}",review.getReviewId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//                        .header("Authorization", "Bearer " + token))
//                .andExpect(status().isNoContent())
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("리뷰 조회")
//    void test3() throws Exception{
//
//        memberRepository.save(member);
//
//        workplaceRepository.save(workplace);
//
//        LoginRequest loginRequest = LoginRequest.builder()
//                .email("sdsd@naver.com")
//                .password("Business1!")
//                .build();
//
//        String json = objectMapper.writeValueAsString(loginRequest);
//
//        MvcResult loginResult = mockMvc.perform(post("/login/member")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        LoginResponse loginResponse = objectMapper.readValue(loginResult.getResponse().getContentAsString(), LoginResponse.class);
//        String token = loginResponse.getToken();
//
//        Review review = Review.builder()
//                .reviewContent("치킨이 안보이네요..")
//                .reviewRating(1.3)
//                .member(member)
//                .workplace(workplace)
//                .build();
//        reviewRepository.save(review);
//
//        mockMvc.perform(get("/api/v1/review/{reviewId}",review.getReviewId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                .header("Authorization", "Bearer " + token)
//                        )
//                .andExpect(jsonPath("$.reviewContent").value(review.getReviewContent()))
//                .andExpect(jsonPath("$.reviewRating").value(review.getReviewRating()))
//                .andExpect(jsonPath("$.workplaceName").value(review.getWorkplace().getWorkplaceName().getValue()))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("리뷰 삭제")
//    void test4() throws Exception{
//
//        memberRepository.save(member);
//
//        workplaceRepository.save(workplace);
//
//        LoginRequest loginRequest = LoginRequest.builder()
//                .email("sdsd@naver.com")
//                .password("Business1!")
//                .build();
//
//        String json = objectMapper.writeValueAsString(loginRequest);
//
//        MvcResult loginResult = mockMvc.perform(post("/login/member")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        LoginResponse loginResponse = objectMapper.readValue(loginResult.getResponse().getContentAsString(), LoginResponse.class);
//        String token = loginResponse.getToken();
//
//        Review review = Review.builder()
//                .reviewContent("치킨이 안보이네요..")
//                .reviewRating(1.3)
//                .member(member)
//                .workplace(workplace)
//                .build();
//        reviewRepository.save(review);
//
//        mockMvc.perform(delete("/api/v1/review/{reviewId}",review.getReviewId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + token)
//                )
//                .andExpect(status().isNoContent())
//                .andDo(print());
//
//        assertEquals(0, reviewRepository.count());
//    }
//    @Test
//    @DisplayName("첫 페이지 요청 커서 없는 case")
//    void test5() throws Exception{
//
//        memberRepository.save(member);
//
//        workplaceRepository.save(workplace);
//
//        LoginRequest loginRequest = LoginRequest.builder()
//                .email("sdsd@naver.com")
//
//                .password("Business1!")
//                .build();
//
//        String json = objectMapper.writeValueAsString(loginRequest);
//
//        MvcResult loginResult = mockMvc.perform(post("/login/member")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        LoginResponse loginResponse = objectMapper.readValue(loginResult.getResponse().getContentAsString(), LoginResponse.class);
//        String token = loginResponse.getToken();
//
//        List<Review> list = IntStream.rangeClosed(1, 20).mapToObj(i -> Review.builder()
//                .reviewContent("치킨이 안보이네요.." + i)
//                .reviewRating(3.1 + i)
//                .member(member)
//                .workplace(workplace)
//                .build()).toList();
//
//        reviewRepository.saveAll(list);
//
//        mockMvc.perform(get("/api/v1/review")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + token)
//                )
//                .andExpect(jsonPath("$.nextCursor").isNotEmpty())
//                .andExpect(jsonPath("$.data[9].reviewContent").value("치킨이 안보이네요..11"))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("첫번쨰 페이지 요청시 나온 lastId를 사용하여  커사사용해서 페이징 처리")
//    void test6() throws Exception{
//
//        memberRepository.save(member);
//
//        workplaceRepository.save(workplace);
//
//        LoginRequest loginRequest = LoginRequest.builder()
//                .email("sdsd@naver.com")
//
//                .password("Business1!")
//                .build();
//
//        String loginJson = objectMapper.writeValueAsString(loginRequest);
//
//        MvcResult loginResult = mockMvc.perform(post("/login/member")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(loginJson))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        LoginResponse loginResponse = objectMapper.readValue(loginResult.getResponse().getContentAsString(), LoginResponse.class);
//        String token = loginResponse.getToken();
//
//        List<Review> list = IntStream.rangeClosed(0, 19).mapToObj(i -> Review.builder()
//                .reviewContent("치킨이 안보이네요.." + i)
//                .reviewRating(3.4 + i)
//                .member(member)
//                .workplace(workplace)
//                .build()).toList();
//
//        reviewRepository.saveAll(list);
//
//        MvcResult mvcResult = mockMvc.perform(get("/api/v1/review")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + token)
//
//                )
//                .andReturn();
//
//        String json = mvcResult.getResponse().getContentAsString();
//        Long nestCursor = JsonPath.parse(json).read("$.nextCursor", Long.class);
//
//        mockMvc.perform(get("/api/v1/review?lastId="+nestCursor)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + token)
//                )
//                .andExpect(jsonPath("$.nextCursor").isNotEmpty())
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("유효하지 않은 LastId 사용시 null 반환")
//    void test7() throws Exception{
//
//        memberRepository.save(member);
//
//        workplaceRepository.save(workplace);
//
//        LoginRequest loginRequest = LoginRequest.builder()
//                .email("sdsd@naver.com")
//
//                .password("Business1!")
//                .build();
//
//        String loginJson = objectMapper.writeValueAsString(loginRequest);
//
//        MvcResult loginResult = mockMvc.perform(post("/login/member")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(loginJson))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        LoginResponse loginResponse = objectMapper.readValue(loginResult.getResponse().getContentAsString(), LoginResponse.class);
//        String token = loginResponse.getToken();
//
//
//        List<Review> list = IntStream.rangeClosed(0, 19).mapToObj(i -> Review.builder()
//                .reviewContent("치킨이 안보이네요.." + i)
//                .reviewRating(4.5 + i)
//                .member(member)
//                .workplace(workplace)
//                .build()).toList();
//
//        reviewRepository.saveAll(list);
//
//        mockMvc.perform(get("/api/v1/review?lastId=232")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + token)
//                )
//                .andDo(print());
//
//    }
//}