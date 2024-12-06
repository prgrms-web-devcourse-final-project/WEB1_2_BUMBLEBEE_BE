# 공식 Python 런타임을 부모 이미지로 사용
FROM python:3.9-slim

# 컨테이너 내에서 작업 디렉토리 설정
WORKDIR /app

# 현재 디렉토리의 모든 파일을 /app 디렉토리에 복사
COPY . /app

# requirements.txt에 지정된 패키지 설치
RUN pip install --no-cache-dir -r requirements.txt

# 컨테이너 실행 시 Flask 애플리케이션 실행
CMD ["python", "app.py"]

# 컨테이너가 실행될 포트 노출
EXPOSE 8070