# README - Déploiement du Projet Angular + Spring Boot avec Docker

## Prérequis

Avant de commencer, assurez-vous d'avoir installé les outils suivants :

- **Node.js** v22.12.0 ([Télécharger ici](https://nodejs.org/))

- **Angular** CLI v19.1.5 (installer avec npm install -g @angular/cli)

- **Java** 21.0.5 (LTS) ([Télécharger ici](https://adoptium.net/))

- **Maven** (installé avec Java)

- **MySQL** (en local ou via Docker)

- **Docker** ([Installer Docker](https://www.docker.com/get-started/))



Installation et exécution en local

1. Backend (Spring Boot)

# Cloner le projet
cd backend

# Configurer MySQL (si non utilisé via Docker)
# Assurez-vous d'avoir une base de données MySQL nommée `epiczone_db`
# Modifier `application.properties` avec vos identifiants MySQL

# Lancer le projet
mvn spring-boot:run

2. Frontend (Angular)

# Cloner le projet
cd frontend

# Installer les dépendances
npm install

# Lancer l'application
ng serve

L'application sera accessible sur http://localhost:4200.

Déploiement avec Docker

1. Création des Dockerfiles

Backend - Dockerfile (backend/Dockerfile)

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

Frontend - Dockerfile (frontend/Dockerfile)

FROM node:22.12.0-alpine
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build --configuration=production
CMD ["npx", "http-server", "dist/frontend"]
EXPOSE 4200

2. Création du fichier docker-compose

Créer un fichier docker-compose.yml à la racine du projet :

    version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: mysql_container
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: epiczone_db
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  backend:
    build: ./backend
    container_name: backend_container
    depends_on:
      - mysql
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/epiczone_db?useSSL=false&allowPublicKeyRetrieval=true
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
    ports:
      - "8080:8080"

  frontend:
    build: ./frontend
    container_name: frontend_container
    depends_on:
      - backend
    ports:
      - "4200:4200"

volumes:
  mysql_data:


3. Lancer les services

# Construire et lancer les conteneurs
docker-compose up --build -d

L'application sera accessible sur http://localhost:4200 et l'API sur http://localhost:8080.

4. Arrêter les conteneurs

docker-compose down