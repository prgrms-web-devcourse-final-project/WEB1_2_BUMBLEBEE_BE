#!/bin/bash
exec > >(tee -a /var/log/eb-postdeploy.log | logger -t [eb-postdeploy] -s 2>/dev/console) 2>&1
echo [`date -u +"%Y-%m-%dT%H:%M:%SZ"`] Started EB Post-Deploy
set -x

# Variables
DOCKER_COMPOSE_DIR="/Docker/python-server"
S3_BUCKET="bumblebee.roomit"
S3_FOLDER="python-server"

# Install MySQL
echo "Installing MySQL..."
sudo wget https://dev.mysql.com/get/mysql80-community-release-el9-1.noarch.rpm
sudo dnf install mysql80-community-release-el9-1.noarch.rpm -y
sudo dnf update -y
sudo dnf install mysql-community-client -y
sudo dnf install mysql-community-server -y
sudo systemctl enable mysqld
sudo systemctl start mysqld
echo "MySQL installed and started."

# Install Redis
echo "Installing Redis..."
sudo dnf update -y
sudo dnf search redis
sudo dnf install redis6 -y
sudo systemctl restart redis6
sudo systemctl enable redis6
echo "Redis installed and started."

# Install Docker
echo "Installing Docker..."
sudo dnf install -y dnf-plugins-core
sudo dnf config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
sudo dnf install docker-ce docker-ce-cli containerd.io -y
sudo systemctl enable docker
sudo systemctl start docker
echo "Docker installed and started."

# Install Docker Compose
echo "Installing Docker Compose..."
DOCKER_COMPOSE_VERSION="2.20.2"
curl -L "https://github.com/docker/compose/releases/download/v$DOCKER_COMPOSE_VERSION/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
echo "Docker Compose installed."

# Copy python-server folder from S3 to EC2
echo "Copying python-server from S3..."
aws s3 cp s3://$S3_BUCKET/$S3_FOLDER/ /$DOCKER_COMPOSE_DIR/ --recursive
echo "S3 python-server/ folder copied to EC2 server."

# Build and run Docker Compose application
if [ -d "$DOCKER_COMPOSE_DIR" ]; then
  cd "$DOCKER_COMPOSE_DIR"
  docker-compose down
  docker-compose build
  docker-compose up -d
  echo "Docker Compose application built and started."
else
  echo "Error: Directory $DOCKER_COMPOSE_DIR does not exist."
  exit 1
fi

exit 0
