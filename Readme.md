# Web Services With Spring Boot : Find a car-park 

## To compile (run mvn command in carpark directory)

### To launch the project
* mvn spring-boot:run
### To launch unit and integration tests
* mvn test
### To build ans package the application in target directory
* mvn clean package

## Project overview
### Description
We want to find the car-parks near of a given position.   
We will use this data source web service to retrieve the car-park list: https://data.grandpoitiers.fr/api/records/1.0/search/?dataset=mobilite-parkings-grand-poitiers-donnees-metiers&rows=1000&facet=nom_du_parking&facet=zone_tarifaire&facet=statut2&facet=statut3   
The free locations of the car-park given in real time can be got in this data source web service : https://data.grandpoitiers.fr/api/records/1.0/search/?dataset=mobilites-stationnement-des-parkings-en-temps-reel&facet=nom

The application should be able to work with other towns with completely different URLs.   

This project is made to be consumed by an app which looks like this:
* [App screenshot](images/AppScreenshot.jpg)

Remark: Only the web service will be created, not the app.

### Retained solution
The web service will return a number N given (by the web service client) of free car-parks, the nearest from a given geolocation.
The list will be ordered from nearest to farthest.
If some car-parks are full, we will return them but also add new nearest car-parks to have N free car-parks.

### Other solution not retained
Another solution would have been to give the car-parks within a geographic rectangular zone corresponding to the screen.   
This solution have not been retained because the user would have had to zoom in or out to display car-parks.

### Remarks
* Information on car-parks which are not real time information have been saved in DB.
  They can be updated every day at night with low network traffic or every week by a cron process implemented with SpringBoot.
* To find the nearest geolocation, the formula of haversine distance, the distance in between two geographical points, have been implemented.
* In Poitiers URL for car-parks definition, geo_point_2d have been retained for its precision which is better than geo_shape.coordinates and geometry.coordinates have the same precision but is farest in the JSON tree.

## A SpringBoot API which exposes 3 web services GET (Return code 200 if Ok):
* one that displays the list of all car-parks : /car-parks
* one that displays a car-park by its id : /car-park/{id}
* one that display a list of car-parks ordered by position from nearest to farthest from a given position   


### This last rest service take the following JSON as entry.   
* nbFreeCarPark is the number N of free car parks we want to return
* countryCode is the country code (ex: fr) of the car park
* town name is the town name where to find the car parks
* geolocation is a geolocation of the position from where to search the car-parks   

### Here is an example of body:
{   
  "nbFreeCarPark": 5,   
  "countryCode": "fr",   
  "townName": "Poitiers",   
  "geoLocation": {   
    "longitude": 46.58349874703973,   
    "latitude": 0.3450022616476489   
  }   
}

### Here is an example of result
[   
{   
  "name": "NOTRE DAME",   
  "geoLocation": {   
    "longitude": 46.58349874703973,   
    "latitude": 0.3450022616476489   
  },   
  "capacity": 642,   
  "nbFreeLocations": 408,   
  "distanceInMeters": 0   
},   
...   
]


The file [data.sql](src/main/resources/data.sql) in resources directory set the default countries, town and URLs.   
Remark : Country and town controller web service have not been developed but CountryService and TownService exist.


## Architecture
### Layer architecture
The architecture is a layer architecture. The entry point of the web services is the controller layer which call the 
services layer which call the repository layer which interact with the model which defines the database and interact with it.   

* Controller package
* Service package
* Service.algorithm
* Repository package
* Model package
* Dto package
* Mapper package
* Aspect package for AOP

### DTO
The returned car-park List is made with a DTO (CarParkDto) List.   
The body of /car-parks-from-position GET web service uses (InputDto).   
The mapping from CarPark and Town to CarParkDto (the returned car-parks) and TownDto and the opposite is made with mapper classes in mapper directory.  
In a real project DTO would also have been done to encapsulate CarPark and Country classes.

## Algorithms implementation
The idea is to be able to add a new abstracts algorithm to retrieve car-parks,
one for each town depending on a URL List retrieved in database. Those URLs are used by each algorithm.
That's why towns are linked to a URL list in the DB model.

Service.algorithm package contain 2 abstract classes :   
AbstractSortedListCarPark which is injected with Spring as a List   
Its subclass AbstractSortedListCarParkTown which manage car parks by town.   
It's subclass SortedListCarParkPoitiers, which is a given algorithm, for Poitiers town. 
We can add several concretes subclasses to implement different algorithm by town.

AlgoChooserService contains the injected algorithm List of subclasses of AbstractSortedListCarPark and a method 
to choose the right algorithm in the list, depending on the town and it's country.   

### Error management
A GlobalExceptionHandler controller intercept the thrown exceptions and provides the server error message to be displayed
and the server return code depending on each exception.

The error message is returned like this if any error occurs:  
{  
"errorCode": An internal error code,  
"errorMessage": "The error message",  
"devErrorMessage": The stack trace,  
"additionalData": {} The additional data if needed. It could have been used in a real project.   
}


### Logs
A LoggingAspect class provide a log of all methods and their time to process. It uses AOP.

## Unit tests and integration tests

There is one test class by package with one test example :
* Controller : Integration test to call the web service
* Service : Test the call to service and verify its interaction with the DB.
* Mapper : Verify the mapping Model <-> DTO
* Repository : Tests have not been done as they look like service tests

In a real project a lot of test would have been implemented to cover 80% of code.
Here for this small project only examples have been made. More over several tests have been grouped in one function
(add, delete and list).  
In a real project several function would have been done to add, delete and retrieve a list of objects.   
In a real project TDD would have been done with tests first developed. They would have failed at the beginning and pass at the end.

### UML conception

In this schema we can see the package layer architecture and the main classes.

* [UML Schema](images/UML.png)

### SQL database

For this simple project, a H2 database have been used. In a real project a real database would have been chosen.  
The database access is set in the [application.properties](src/main/resources/application.properties) file in resources directory.   
The serialization of data in database enables a cold restart of the server and accelerate the process.
So that, CarParks containing car-park name, its town and its position are saved in DB.   
A schedule process enable to update car-parks in DB if any dat change or if some car-parks are added 
or even suppressed or if their name has changed.   
This process is made by the algorithms (see Algorithm implementation above).
For those algorithms depending on Town, a URL List is linked to each Town (see UML schema above).

* [DB Schema](images/Database.png)


### Remarks
### In a real project
* We could have do 2 web services : One giving the list of all car-park from nearest to farthest. An other one which would have do the filter.
* We would have found the town name depending on a position in another web service.

## Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.6/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.6/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.0.6/reference/htmlsingle/#web)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.0.6/reference/htmlsingle/#data.sql.jpa-and-spring-data)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.0.6/reference/htmlsingle/#using.devtools)

## Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
