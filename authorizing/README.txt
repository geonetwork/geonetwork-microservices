
to run:
mvn spring-boot:run

to get the token:
curl --header "Content-Type: application/json" '127.0.0.1:8181/authenticate' --header "Content-Type: application/json"   --request POST   --data '{"username":"momo","password":"password"}'