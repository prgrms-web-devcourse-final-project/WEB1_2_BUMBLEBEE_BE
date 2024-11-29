package roomit.main.domain.reservation.controller;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomit.main.domain.reservation.dto.request.CreateReservationRequest;
import roomit.main.domain.reservation.dto.request.UpdateReservationRequest;
import roomit.main.domain.reservation.dto.response.MyWorkPlaceReservationResponse;
import roomit.main.domain.reservation.dto.response.ReservationResponse;
import roomit.main.domain.reservation.service.ReservationService;
import roomit.main.domain.workplace.entity.value.WorkplaceName;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;  // JSON 직렬화/역직렬화

    private CreateReservationRequest createRequest;
    private UpdateReservationRequest updateRequest;
    private ReservationResponse reservationResponse;
    private MyWorkPlaceReservationResponse workplaceReservationResponse;

    @BeforeEach
    void setUp() {
        createRequest = new CreateReservationRequest(
                "John Doe",
                "010-1234-5678",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(2)
        );

        updateRequest = new UpdateReservationRequest(
                "Jane Doe",
                "010-9876-5432",
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(2).plusHours(2)
        );

        reservationResponse = new ReservationResponse(
                new WorkplaceName("사업장"),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(2),
                10,
                50000,
                "http://example.com/image.jpg"
        );

        workplaceReservationResponse = new MyWorkPlaceReservationResponse(
                new WorkplaceName("사업장"),
                "John Doe",
                "010-1234-5678",
                "Study Room 1",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(2),
                10,
                "http://example.com/image.jpg"
        );
    }

    // 예약 생성 테스트
    @Test
    void createReservationTest() throws Exception {
        doNothing().when(reservationService).createReservation(anyLong(), anyLong(), any(CreateReservationRequest.class));

        mockMvc.perform(post("/api/v1/reservations/{memberId}/{studyRoomId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated());
    }

    // 예약 삭제 테스트
    @Test
    void deleteReservationTest() throws Exception {
        doNothing().when(reservationService).deleteReservation(anyLong());

        mockMvc.perform(delete("/api/v1/reservations/{reservationId}", 1L))
                .andExpect(status().isNoContent());
    }

    // 예약 수정 테스트
    @Test
    void updateReservationTest() throws Exception {
        doNothing().when(reservationService).updateReservation(anyLong(), any(UpdateReservationRequest.class));

        mockMvc.perform(put("/api/v1/reservations/{reservationId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNoContent());
    }

    // 특정 멤버의 최근 예약 조회 테스트
    @Test
    void findRecentReservationByMemberIdTest() throws Exception {
        when(reservationService.findByMemberId(anyLong())).thenReturn(reservationResponse);

        mockMvc.perform(get("/api/v1/reservations/member/{memberId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workplaceName").value(reservationResponse.workplaceName()));
    }

    // 특정 멤버의 모든 예약 조회 테스트
    @Test
    void findReservationsByMemberIdTest() throws Exception {
        when(reservationService.findReservationsByMemberId(anyLong())).thenReturn(List.of(reservationResponse));

        mockMvc.perform(get("/api/v1/all/reservations/member/{memberId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].workplaceName").value(reservationResponse.workplaceName()));
    }

    // 특정 사업장 예약 조회 테스트
    @Test
    void findReservationByWorkplaceIdTest() throws Exception {
        when(reservationService.findReservationByWorkplaceId(anyLong())).thenReturn(List.of(workplaceReservationResponse));

        mockMvc.perform(get("/api/v1/reservations/workplace/{workplaceId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].workplaceName").value(workplaceReservationResponse.workplaceName()));
    }
}
