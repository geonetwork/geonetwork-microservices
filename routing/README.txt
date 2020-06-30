to run:
mvn spring-boot:run

to get touch both services using same gateway:
curl --header "Content-Type: application/json" '127.0.0.1:8080/authenticate' --header "Content-Type: application/json"   --request POST   --data '{"username":"momo","password":"password"}'
curl 127.0.0.1:8080/search -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtb21vIiwiX19fUk9MRV9VU0VSIjoiUk9MRV9VU0VSIiwiZXhwIjoxNTkzNDcwMDYzLCJpYXQiOjE1OTM0NTIwNjN9.8216lBMUmDzMwbd45230Cki6qJDh1XVk71VcRKyAnRcshqUPn6ufxlhUa28si6azFN1hzJB5Va6U5WXrroRF9Q"