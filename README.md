# offer-assignment
Offer service

### How to run tests

Tests can be run by calling `mvn clean install`

### How to run application

OfferServer is the entry point for the application. The application can be run by running main method in OfferServer.
 
`mvn exec:java -Dexec.mainClass="com.worldpay.exercise.OfferServer"`

### Create offer

curl -X POST "http://localhost:4567/offers?description=aaaa&price=10.0&currency=GBP&validityInSeconds=10"

### Get offers

curl http://localhost:4567/offers

### Get offer by Id

curl localhost:4567/offers/<id>

Eg: curl localhost:4567/offers/56c183de-2bf5-474a-9ff7-fc2d12f103b9

### Cancel offer

curl -X POST localhost:4567/offers/cancel/<id>

Eg: curl -X POST "localhost:4567/offers/cancel/7ba3772e-2ace-4502-8112-dd2380f56900"

### Decisions on design

Spark:
    Spark is micro web framework for Java/Scala and is a quick tool for building web services.

MapDB:
    It is a simple embedded database engine.  It combines speed and simplicity of Java collection with scalability of database engine.
    Using File backed Map DB and clearing data on shutdown however if persistence is desired we can remove this. 

Offer validity period: 
    Offer validity is declared as time in seconds with Integer datatype for simplicity.


Further improvements:

- Cancelling offer already cancelled/expired should return a message Eg: Offer is already cancelled or expired
- Audit on whether offer was cancelled or automaticaly expired
- validation on offer validity, currency, price or other input parameter
- Special cases like offers scheduled for expiry and user trying to cancel at the same time