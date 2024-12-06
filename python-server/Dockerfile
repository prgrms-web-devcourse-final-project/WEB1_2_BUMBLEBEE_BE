FROM python:3.9.13

# Install system dependencies and build tools
RUN apt-get update && apt-get install -y \
    gcc g++ libatlas-base-dev python3-dev build-essential \
    libssl-dev libffi-dev libblas-dev liblapack-dev gfortran && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Upgrade pip, setuptools, and wheel to the latest version
RUN python -m pip install --upgrade pip setuptools wheel

# Install numpy first as it is a build dependency for scikit-surprise
RUN pip install --no-cache-dir numpy==1.21.6

# Install cython first, as it’s needed for some packages to compile
RUN pip install --no-cache-dir cython==0.29.35

# Install scikit-learn and scipy
RUN pip install --no-cache-dir scikit-learn==0.24.0 scipy==1.5.4

# Install pandas and Flask dependencies
RUN pip install --no-cache-dir pandas==1.1.5 Flask==2.2.3 Flask-Cors==3.0.0

# Install werkzeug to ensure compatibility with Flask
RUN pip install --no-cache-dir werkzeug==2.2.3

# Install pymysql first to isolate potential issues
RUN pip install --no-cache-dir pymysql==1.0.2

# Install scikit-surprise with no binary wheel (forces source installation)
RUN pip install --no-cache-dir --no-binary :all: scikit-surprise==1.1.0

# Set the working directory to /app
WORKDIR /app

# Copy only the necessary files first to optimize caching (avoid copying all files in one go)
COPY requirements.txt /app/requirements.txt

# Install dependencies from requirements.txt (if available)
RUN pip install --no-cache-dir -r /app/requirements.txt

# Copy the rest of the application files
COPY . /app

# Set Flask environment variables
ENV FLASK_APP=app.py
ENV FLASK_RUN_HOST=0.0.0.0
ENV FLASK_ENV=development
# Change this to 'production' for production environment

# Expose the application port
EXPOSE 8070

# Command to run the Flask app
CMD ["flask", "run", "--host=0.0.0.0", "--port=8070"]
