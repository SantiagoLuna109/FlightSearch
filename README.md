# In this file are the instruction detailed for the correct work of the environment

## Install docker
For the correct deploy some commands of docker are needed, so please install it
you can get more information here: https://hub.docker.com/welcome

## For the API secret and Token, you will need to get them into this link:
https://developers.amadeus.com/self-service/apis-docs/guides/developer-guides/API-Keys/authorization/

## Create a .environment file (MANDATORY FOR YOUR USE)
in the root folder and ad this:

AMADEUS_API_KEY=your_API_KEY

AMADEUS_API_SECRET=your_API_SECRET

VITE_API_BASE_URL=http://backend:8080/api

SPRING_PROFILES_ACTIVE=prod


## Navigate to the root directory
To confirm this you can use:

$ ls

where you should see:

frontend/ backend/ docker-compose.ym

### Install 
Install java JDK 17 or newer (needed to built an image for the backend)

### Built the Spring Jar
cd backend

$ ./gradlew bootJar

cd ..


## Start everything
$ docker compose build

$ docker compose up -d

And after all of this you should be able to use the URL for pettions:

backend: http://localhost:8080/

frontend: http://localhost:3000/

#### If you ant to check tat everything works:

For the front end just everything up:

![image](https://github.com/user-attachments/assets/9eadc465-32e9-4cbf-9bee-29d8cff37b9f)


you should put this in your browser:

http://localhost:8080/api/v1/health

and see something like this:

![image](https://github.com/user-attachments/assets/8ed0b0b8-ad66-4cc5-b026-f4a2071b5937)


and to see the an example of the work:

http://localhost:8080/api/v1/flights/search?origin=SYD&destination=BKK&departureDate=2025-12-02&adults=1&max=5

and see something like this:

![image](https://github.com/user-attachments/assets/e37fa893-dd8e-4326-96c2-214391106ef8)



#### Important Notes:
If you are using a VPN please verify the correct work and filtrate of your request by the Amadeus API and the packages that the Docker images download :) 
