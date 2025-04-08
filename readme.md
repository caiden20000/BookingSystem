# Booking Subsystem

## hello

We are working from Spring Initializr.
I am currently using https://github.com/SWE443/Demo/tree/master/demo as a guide.  
On BookingSystem-v2 I have:
- Lombok  
- Spring Web  
- Thymeleaf  
- H2 Database  
- Spring Data JPA 

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