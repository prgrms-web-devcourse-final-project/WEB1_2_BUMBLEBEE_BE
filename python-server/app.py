from flask import Flask, request, jsonify
from flask_cors import CORS
import pickle
import pymysql
from decimal import Decimal

# 연령대별로 학습된 모델 로드
models = {}
age_groups = [10, 20, 30, 40]  # 10대, 20대, 30대, 40대 모델 로드

import os

import os

for age_group in age_groups:
    try:
        # 모델 파일 경로를 /app/collaborative_models 디렉토리로 수정
        model_path = os.path.join('/app/collaborative_models', f'collaborative_model_{age_group}s.pkl')

        # 파일을 열고 모델을 로드
        with open(model_path, 'rb') as f:
            models[age_group] = pickle.load(f)
    except FileNotFoundError:
        print(f"Model for age group {age_group}s not found at {model_path}. Skipping...")


# 데이터베이스 설정
DB_CONFIG = {
    'host': 'awseb-e-uxvp5ni4rj-stack-awsebrdsdatabase-mjdqrp222gju.c3ki4qoo6im1.ap-northeast-2.rds.amazonaws.com',
    'user': 'admin',
    'password': 'eofksalsrnr',
    'database': 'bumblebee'
}

app = Flask(__name__)
CORS(app)

@app.route('/')
def home():
    return "Recommendation System is running!"

@app.route('/predict', methods=['POST'])
def predict():
    data = request.get_json()
    user_id = data.get('user_id')
    workplace_id = data.get('workplace_id')

    if not user_id or not workplace_id:
        return jsonify({'error': 'user_id and workplace_id are required'}), 400

    connection = pymysql.connect(**DB_CONFIG)
    try:
        with connection.cursor() as cursor:
            cursor.execute("""
                SELECT workplace_name 
                FROM workplace 
                WHERE workplace_id = %s
            """, (workplace_id,))
            workplace = cursor.fetchone()

            if workplace is None:
                return jsonify({'error': 'Invalid workplace_id'}), 400

        # 나이대에 맞는 모델을 찾기
        age_group = get_age_group(user_id)
        if age_group not in models:
            return jsonify({'error': f'No model found for age group {age_group}s'}), 400

        model = models[age_group]
        prediction = model.predict(user_id, workplace_id)

        return jsonify({
            'user_id': user_id,
            'workplace_id': workplace_id,
            'workplace_name': workplace[0],
            'predicted_rating': round(prediction.est, 5)
        })
    finally:
        connection.close()

@app.route('/recommend', methods=['POST'])
def recommend():
    data = request.get_json()
    
    user_id = data.get('user_id')
    age = data.get('age')
    n = data.get('n', 5)  # Default to Top-5 recommendations

    if user_id is None or age is None:
        return jsonify({'error': 'user_id and age are required'}), 400

    # 나이대에 맞는 모델을 찾기
    age_group = get_age_group_from_age(age)
    if age_group not in models:
        return jsonify({'error': f'No model found for age group {age_group}s'}), 400

    connection = pymysql.connect(**DB_CONFIG)
    try:
        with connection.cursor() as cursor:
            cursor.execute("SELECT workplace_id, workplace_name FROM workplace")
            workplaces = cursor.fetchall()

        predictions = []
        model = models[age_group]
        for workplace in workplaces:
            predicted_rating = float(model.predict(user_id, workplace[0]).est)  # 협업 필터링 예측 평점 (Decimal -> float로 변환)

            # 콘텐츠 기반 필터링: 워크플레이스의 리뷰 점수 가져오기
            with connection.cursor() as cursor:
                cursor.execute("""
                    SELECT star_sum / NULLIF(review_count, 0) AS review_score
                    FROM workplace
                    WHERE workplace_id = %s
                """, (workplace[0],))
                review_score_result = cursor.fetchone()
                review_score = review_score_result[0] if review_score_result else 0

            # 하이브리드 방식: 협업 필터링 평점과 콘텐츠 기반 평점을 결합
            final_score = (predicted_rating * 0.3) + (float(review_score) * 0.7)  # review_score에 더 큰 가중치 부여
            final_score = min(max(final_score, 1), 5)  # final_score가 1과 5 사이로 제한
            predictions.append({
                'workplace_id': workplace[0],
                'workplace_name': workplace[1],
                'predicted_rating': round(predicted_rating, 5),
                'review_score': round(review_score, 2),
                'final_score': round(final_score, 2)  # final_score 포함
            })

        # 상위 n개의 추천 장소 반환 (final_score를 기준으로 내림차순 정렬)
        top_n = sorted(predictions, key=lambda x: round(x['final_score'], 4), reverse=True)[:n]

        # 반환할 추천 결과에 나이대 추가
        return jsonify({
            'age_group': age_group,
            'recommendations': top_n  # final_score 포함된 추천 결과 반환
        })
    finally:
        connection.close()

def get_age_group(user_id):
    """
    user_id로 데이터베이스에서 사용자의 나이를 계산하여 연령대를 반환
    """
    connection = pymysql.connect(**DB_CONFIG)
    try:
        with connection.cursor() as cursor:
            cursor.execute("""
                SELECT TIMESTAMPDIFF(YEAR, birth_day, CURDATE()) AS age
                FROM member
                WHERE member_id = %s
            """, (user_id,))
            result = cursor.fetchone()
            if result and result[0]:
                return get_age_group_from_age(result[0])
            else:
                return None  # 사용자가 없거나 나이를 계산할 수 없음
    finally:
        connection.close()

def get_age_group_from_age(age):
    """
    나이를 기반으로 연령대를 반환
    """
    if age < 20:
        return 10
    elif age < 30:
        return 20
    elif age < 40:
        return 30
    else:
        return 40  # 40대 이상 처리

if __name__ == '__main__':
    app.run(host='127.0.0.1', port=8070, debug=True)
