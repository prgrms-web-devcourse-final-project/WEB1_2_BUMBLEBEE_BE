# 공식 Python 런타임을 부모 이미지로 사용
FROM python:3.9.13

# 시스템 패키지 업데이트 및 필요한 빌드 도구 설치
RUN apt-get update && apt-get install -y \
    gcc g++ libatlas-base-dev python3-dev build-essential \
    libssl-dev libffi-dev

# 컨테이너 내에서 작업 디렉토리 설정
WORKDIR /app

# 현재 디렉토리의 모든 파일을 /app 디렉토리에 복사
COPY . /app

# collaborative_models 폴더를 컨테이너에 복사
COPY ./collaborative_models /app/collaborative_models

# pip 최신 버전으로 업그레이드
RUN pip install --upgrade pip

# Cython 0.29.x 버전 설치
RUN pip install cython==0.29.35

# requirements.txt에 지정된 패키지 설치
RUN pip install --no-cache-dir -r requirements.txt

# Flask 앱 실행에 필요한 환경변수 설정
ENV FLASK_APP=app.py
ENV FLASK_RUN_HOST=0.0.0.0

# 컨테이너 실행 시 Flask 애플리케이션 실행
CMD ["flask", "run"]

# 컨테이너가 실행될 포트 8070 노출
EXPOSE 8070
