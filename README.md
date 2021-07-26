# Apzumi
Apzumi Job Application App

To run the app go to the root folder where pom.xml is and then run:

mvn spring-boot:run

The job configures in memory database H2

H2 console available at 'localhost:8080/h2-console'. Database available at 'jdbc:h2:mem:testdb'
username: admin
password: password


How to use API:
1. run GET command for example with Postman: http://localhost:8080/api/posts - that endpoint gets data from external source and saves the initial databse
2. http://localhost:8080/api/posts endpoint loads data from our database
3. PATCH http://localhost:8080/api/post/1 - patches the record with id=1
3a. example of JSON:
    {
        "title":"Akademia Pana Kleksa",
        "body":"Przygody Pana Kleksa i jego uczniów"
    }
3b. example of JSON:
    {
        "title":"Akademia Pana Kleksa"
    }
3c. example of JSON:
    {
        "body":"Przygody Pana Kleksa i jego uczniów"
    }
4. DELETE http://localhost:8080/api/post/1 endpoint allows the User to delete post with given id
5. everyday at 8PM program runs scheduled job to update records. It only updates not updated records by the user (or deleted ones).
