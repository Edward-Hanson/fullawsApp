#!/bin/bash

# This script builds and pushes the initial Docker image to ECR
# Run this after deploying ECR but before deploying ECS

set -e

# Configuration
PROJECT_NAME="cloudbasedproject"
AWS_REGION="eu-west-1"
ECR_REPOSITORY="${PROJECT_NAME}-app"

echo "Starting initial image build and push..."

# Get AWS Account ID
echo "Getting AWS Account ID..."
AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
echo "AWS Account ID: $AWS_ACCOUNT_ID"

# Get ECR login token
echo "Logging into ECR..."
aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com

# Build the application
echo "Building Spring Boot application..."
mvn clean package -DskipTests

# Verify JAR was created
if [ ! -f target/*.jar ]; then
    echo "Error: JAR file not found in target directory"
    exit 1
fi

# Build Docker image
echo "Building Docker image..."
docker build -t $ECR_REPOSITORY:latest .

# Tag image for ECR
ECR_URI="$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPOSITORY"
docker tag $ECR_REPOSITORY:latest $ECR_URI:latest
docker tag $ECR_REPOSITORY:latest $ECR_URI:initial

# Push to ECR
echo "Pushing image to ECR..."
docker push $ECR_URI:latest
docker push $ECR_URI:initial

echo "✅ Initial image pushed successfully!"
echo "ECR URI: $ECR_URI:latest"