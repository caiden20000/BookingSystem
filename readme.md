# Booking Subsystem

## hello

To start the project, I used [Spring Initializr](https://start.spring.io/index.html).
Using https://github.com/SWE443/Demo/tree/master/demo as a guide, I determined the following dependencies to add in Spring Initializr:
- Lombok  
- Spring Web  
- Thymeleaf  
- H2 Database  
- Spring Data JPA  

In addition, I chose Java 21. This should be a good starting point for the project.  
Note that Lombok is absent from the demo project, but I include it because it makes getters and setters fast to implement.

---

Thymeleaf is a little confusing.  
Use this as a guide: https://www.thymeleaf.org/doc/tutorials/3.1/usingthymeleaf.html  
Ignore the Java parts, because it's different then what we're working with. We're using Spring Web, which is different.  
Make sure to use `<html xmlns:th="http://www.thymeleaf.org">` as the HTML tag, otherwise it might not work? I'm not sure.


---

## TODO list

- [x] Set up Spring Boot
- [x] Get JPA to store data in H2
- [x] Get JPA to retrieve data
- [x] Get Spring Web to serve a page
- [x] Get ThymeLeaf to inject data into a template
- [x] Coordinate JPA and Thymeleaf to dynamically pull data from the database and display it on a page
- [x] Templatize the view page
- [x] Templatize the list page
- [ ] Templatize the edit page
- [ ] Make the edit page able to post and modify data
- [ ] Make the edit page edit a booking if specified
- [ ] Make the edit page create a booking if specified
- [ ] Gray buttons out on the view page when not applicable
- [ ] Set up the repository