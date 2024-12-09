import http from 'k6/http';
import { sleep } from 'k6';


// export const options = {
//   vus: 50, // 가상 사용자 수
//   duration: '30s', //테스트 시간
// };

// export default function () {
//   http.get('http://host.docker.internal:8080/api/v1/business')
//   sleep(1);
// }

// 거리별 사업장 조회
// export default function () {
//   const url = 'http://host.docker.internal:8080/api/v1/workplace/distance';
//
//   // 요청 본문
//   const payload = JSON.stringify({
//     topRight: {
//       lat: 37.7200,
//       lng: 127.05
//     },
//     bottomLeft: {
//       lat: 37.4997,
//       lng: 127.02
//     },
//     latitude: 37.49972431351040,
//     longitude: 127.02896610336000
//   });
//
//   // HTTP 요청 헤더 설정
//   const params = {
//     headers: {
//       'Content-Type': 'application/json', // 요청 본문 타입
//     },
//   };
//
//   // POST 요청
//   http.post(url, payload, params);
//
//   // 1초 대기
//   sleep(1);
// }
// import http from 'k6/http';
// import { check, sleep } from 'k6';
//
// export const options = {
//   vus: 100, // 가상 사용자 수
//   duration: '30s', // 테스트 시간
// };
//
// // 로그인 요청에서 받은 토큰을 전역에서 사용하도록
// let token = '';
//
// export function setup() {
//   const loginPayload = JSON.stringify({
//     email: 'member1@gmail.com',
//     password: 'Test1234!',
//   });
//
//   // 로그인 요청 보내기
//   const loginResponse = http.post('http://host.docker.internal:8080/login/member', loginPayload, {
//     headers: { 'Content-Type': 'application/json' },
//   });
//
//   // 로그인 응답에서 JWT 토큰 추출
//   token = loginResponse.headers['Authorization']; // Authorization 헤더에서 토큰 추출
//   console.log('JWT Token: ' + token);  // 확인용 로그 출력
//
//   // 토큰이 존재하는지 확인
//   if (!token) {
//     throw new Error('Token not found in the response');
//   }
//
//   // setup 함수에서 반환된 데이터는 테스트 내에서 공유할 수 있음
//   return { token };
// }
//
// export default function (data) {
//   if (data.token) {  // setup에서 반환된 데이터 사용
//     const apiUrl = 'http://host.docker.internal:8080/api/v1/studyroom/available';
//
//     const payload = JSON.stringify({
//       address: "경기 의정부시 용현로105번길 19",
//       startDateTime: "2024-12-01T10:00:00",
//       endDateTime: "2024-12-01T12:00:00",
//       reservationCapacity: 4,
//     });
//
//     const params = {
//       headers: {
//         'Content-Type': 'application/json',
//         'Authorization': `Bearer ${data.token}`,  // 수정된 부분
//       },
//     };
//
//     // API 요청 보내기
//     const response = http.post(apiUrl, payload, params);
//
//     // 응답 상태 코드가 200인지 확인
//     check(response, {
//       'request successful': (r) => r.status === 200,
//     });
//   } else {
//     console.log('Token not available');
//   }
//
//   sleep(1); // 1초 대기
// }
