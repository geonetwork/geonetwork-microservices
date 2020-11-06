# Authentication service

This service is in charge of checking user privileges using GeoNetwork user repository. Once authenticated, a JSON Web Token (JWT) is created. The token will contain necessary information about the user that may be required for other services to limit requests to the database (eg. user membership is required to build a search filter by the search application).

The auth service requires the following to be started first:
```shell script
docker-compose up -d database rabbitmq 
docker-compose up -d discovery config
docker-compose up -d gateway auth
```

GeoNetwork does not need to be started, but it MUST have started once to create the database (including the `users` table with the default `admin/admin` user).


1. Get an authentication token

```shell script
USERNAME=admin
PASSWORD=admin
gn_token=$( \
    curl test-client:noonewilleverguess@127.0.0.1:9900/oauth/token \
         -dgrant_type=password -dscope=any \
         -dusername=$USERNAME -dpassword=$PASSWORD \
        | jq -r '.access_token') 

echo $gn_token
```

2. The service response return the token `access_token`

```json
{
  "access_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDMxODcxODcsInVzZXJfbmFtZSI6Im1vbW8iLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwianRpIjoiM2ZmYjhkNjQtODJlMy00NTVhLWJlM2YtMGUzYzE3MjBlYjdlIiwiY2xpZW50X2lkIjoidGVzdC1jbGllbnQiLCJzY29wZSI6WyJhbnkiXX0.HiDDFl-Ze-Mjry4X4lVTe4E6j8zueUvDY5Fz_TET_8w",
  "token_type":"bearer",
  "refresh_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJtb21vIiwic2NvcGUiOlsiYW55Il0sImF0aSI6IjNmZmI4ZDY0LTgyZTMtNDU1YS1iZTNmLTBlM2MxNzIwZWI3ZSIsImV4cCI6MTYwNTczNTk4NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6IjRkNDFkNDJjLWIwMDYtNDQyYi1hZGU1LTkzMjQ1MGE5MDg5NCIsImNsaWVudF9pZCI6InRlc3QtY2xpZW50In0.wWxJ2QdHLamT7XGmMQ9VueLBKR0QELfelZplfDWmbrY",
  "expires_in":43199,
  "scope":"any",
  "jti":"3ffb8d64-82e3-455a-be3f-0e3c1720eb7e"
}
```

3. The token is used to access services

```shell script
gn_auth_header=$(echo "Authorization: Bearer $gn_token")

(as working when not passing thru frontal, also consider:)
gn_auth_header=$(echo $gn_token | sed -E "s#.*\"access_token\":\"(([A-Za-z0-9]|\.|-)+)\",.*#Authorization: Bearer \1#g")

curl 127.0.0.1:9900/search/secured -H "$gn_auth_header"
```

