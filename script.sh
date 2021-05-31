#!/bin/sh
# Deployment script for project26-server application
# https://github.com/abramov26/project26-server
echo "Checking Docker installations..."
if [ -x "$(command -v docker)" ]; then
	echo "Docker is installed"
else
    echo "Docker not found. To continue, you need to install the latest docker version first"
    echo "Exiting..."
    exit
fi
if [ -x "$(command -v docker-compose)" ]; then
	echo "Docker-compose is installed"
else
    echo "Docker-compose not found. To continue, you need to install the latest docker-compose version first"
    echo "Exiting..."
    exit
fi
echo "Creating application folder..."
if [ -d "project26" ] 
then
    echo "Directory project26 already exists"
    echo "You need to move or rename this directory"
    echo "Exiting..."
    exit
else
    mkdir project26
fi
cd project26
echo "Downloading the application..."
wget https://project26.usatu.su/download/docker-deploy/0.2/project26-0.2.tar.gz
tar xpvzf project26-0.2.tar.gz
echo "Deploying..."
docker-compose up -d
echo "Congratulations! The app has been installed"
echo "Here is links to go:"
echo "Application IP address:"
docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' project26_reverse_proxy
echo "phpmyadmin IP address:"
docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' project26_pma
echo "Administrator credentials:"
echo "admin:admin"
