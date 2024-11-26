import http from 'k6/http';
import { sleep } from 'k6';


export const options = {
  vus: 20, // 가상 사용자 수
  duration: '30s', //테스트 시간
};

export default function () {
  http.get('http://host.docker.internal:8080/api/v1/business')
  sleep(1);
}