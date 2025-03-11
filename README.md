## Docker related commands

Ensure target exists
mvn clean install

docker build -t autoboard .

docker run --rm -p 5000:8080 autoboard

You can access the API from port 5000 on your machine:
http://localhost:5000/api/misc/health

It is essentially the same as running the API locally, however we now have a way to package it into a container.
