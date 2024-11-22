package roomit.web1_2_bumblebee_be.domain.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import roomit.web1_2_bumblebee_be.domain.member.entity.Age;
import roomit.web1_2_bumblebee_be.domain.member.entity.Member;
import roomit.web1_2_bumblebee_be.domain.member.entity.Role;
import roomit.web1_2_bumblebee_be.domain.member.entity.Sex;
import roomit.web1_2_bumblebee_be.domain.member.repository.MemberRepository;
import roomit.web1_2_bumblebee_be.domain.review.dto.request.ReviewRegisterRequest;
import roomit.web1_2_bumblebee_be.domain.review.dto.request.ReviewSearch;
import roomit.web1_2_bumblebee_be.domain.review.entity.Review;
import roomit.web1_2_bumblebee_be.domain.review.repository.ReviewRepository;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;
import roomit.web1_2_bumblebee_be.domain.workplace.repository.WorkplaceRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private WorkplaceRepository workplaceRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private LocalDate date = LocalDate.of(2024, 11, 22);

    private Workplace workplace;

    private Member member;
    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
        memberRepository.deleteAll();
        workplaceRepository.deleteAll();

        workplace = Workplace.builder()
                .workplaceName("사업장")
                .workplacePhoneNumber("010-1234-1234")
                .workplaceDescription("사업장 설명")
                .workplaceAddress("대한민국sa")
                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                .build();

        member = Member.builder()
                .birthDay(date)
                .memberSex(Sex.FEMALE)
                .memberRole(Role.ROLE_USER)
                .memberPwd("Bass!1123")
                .memberEmail("asd@naver.com")
                .memberPhoneNumber("010-3323-2323")
                .memberNickName("치킨유저")
                .passwordEncoder(bCryptPasswordEncoder)
                .build();

    }

    @Test
    @DisplayName("리뷰 등록")
    void test1() throws Exception{
        memberRepository.save(member);

        workplaceRepository.save(workplace);

        ReviewRegisterRequest request = ReviewRegisterRequest.builder()
                .reviewContent("좋은 장소네요")
                .reviewRating("별점2개")
                .memberId(member.getMemberId())
                .workplaceId(workplace.getWorkplaceId())
                .build();

        mockMvc.perform(post("/api/v1/review/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)
                ))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("리뷰 수정")
    void test2() throws Exception{



        memberRepository.save(member);



        workplaceRepository.save(workplace);

        Review review = Review.builder()
                .reviewContent("치킨이 안보이네요..")
                .reviewRating("별점4개")
                .member(member)
                .workplace(workplace)
                .build();
        reviewRepository.save(review);


        ReviewRegisterRequest request = ReviewRegisterRequest.builder()
                .reviewContent("좋은 장소네요")
                .reviewRating("별점2개")
                .memberId(member.getMemberId())
                .workplaceId(workplace.getWorkplaceId())
                .build();

        mockMvc.perform(put("/api/v1/review/update/{reviewId}",review.getReviewId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)
                        ))
                .andExpect(jsonPath("$.reviewContent").value(request.getReviewContent()))
                .andExpect(jsonPath("$.reviewRating").value(request.getReviewRating()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("리뷰 조회")
    void test3() throws Exception{

        memberRepository.save(member);

        workplaceRepository.save(workplace);

        Review review = Review.builder()
                .reviewContent("치킨이 안보이네요..")
                .reviewRating("별점4개")
                .member(member)
                .workplace(workplace)
                .build();
        reviewRepository.save(review);

        mockMvc.perform(get("/api/v1/review/{reviewId}",review.getReviewId())
                        .contentType(MediaType.APPLICATION_JSON)
                        )
                .andExpect(jsonPath("$.reviewContent").value(review.getReviewContent()))
                .andExpect(jsonPath("$.reviewRating").value(review.getReviewRating()))
                .andExpect(jsonPath("$.workplaceName").value(review.getWorkplace().getWorkplaceName().getValue()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("리뷰 삭제")
    void test4() throws Exception{

        memberRepository.save(member);

        workplaceRepository.save(workplace);

        Review review = Review.builder()
                .reviewContent("치킨이 안보이네요..")
                .reviewRating("별점4개")
                .member(member)
                .workplace(workplace)
                .build();
        reviewRepository.save(review);

        mockMvc.perform(delete("/api/v1/review/{reviewId}",review.getReviewId())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());

        assertEquals(0, reviewRepository.count());
    }
    @Test
    @DisplayName("첫 페이지 요청 커서 없는 case")
    void test5() throws Exception{

        memberRepository.save(member);

        workplaceRepository.save(workplace);

        List<Review> list = IntStream.rangeClosed(1, 20).mapToObj(i -> Review.builder()
                .reviewContent("치킨이 안보이네요.." + i)
                .reviewRating("별점4개" + i)
                .member(member)
                .workplace(workplace)
                .build()).toList();

        reviewRepository.saveAll(list);

        mockMvc.perform(get("/api/v1/review")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.nextCursor").isNotEmpty())
                .andExpect(jsonPath("$.data[9].reviewContent").value("치킨이 안보이네요..11"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("첫번쨰 페이지 요청시 나온 lastId를 사용하여  커사사용해서 페이징 처리")
    void test6() throws Exception{

        memberRepository.save(member);

        workplaceRepository.save(workplace);

        List<Review> list = IntStream.rangeClosed(0, 19).mapToObj(i -> Review.builder()
                .reviewContent("치킨이 안보이네요.." + i)
                .reviewRating("별점4개" + i)
                .member(member)
                .workplace(workplace)
                .build()).toList();

        reviewRepository.saveAll(list);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/review")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        Long nestCursor = JsonPath.parse(json).read("$.nextCursor", Long.class);

        mockMvc.perform(get("/api/v1/review?lastId="+nestCursor)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.nextCursor").isNotEmpty())
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("유효하지 않은 LastId 사용시 null 반환")
    void test7() throws Exception{

        memberRepository.save(member);

        workplaceRepository.save(workplace);

        List<Review> list = IntStream.rangeClosed(0, 19).mapToObj(i -> Review.builder()
                .reviewContent("치킨이 안보이네요.." + i)
                .reviewRating("별점4개" + i)
                .member(member)
                .workplace(workplace)
                .build()).toList();

        reviewRepository.saveAll(list);

         mockMvc.perform(get("/api/v1/review?lastId=232")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                 .andDo(print());

    }
}