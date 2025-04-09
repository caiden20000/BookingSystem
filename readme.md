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
Actually this one might be better: https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html  
Make sure to use `<html xmlns:th="http://www.thymeleaf.org">` as the HTML tag, otherwise it might not work? I'm not sure.

## BookingSystem info

Buttons that should be visible depending on status and identity:  

Status           | Employer      | Chef          |
-----------------|---------------|---------------|
PENDING_CHEF     |Cancel         |Accept, Reject |
PENDING_EMPLOYER |Accept, Reject |Cancel         |
ACCEPTED         |Cancel, Modify |Cancel         |
REJECTED         |N/A            |N/A            |
CANCELLED        |N/A            |N/A            |
COMPLETED        |N/A            |N/A            |


Professor says the API service needs to be a separate Java Project. We'll have to figure out how to connect to the same database from 2 different instances, and whether we want the BookingSystem to use the microservice for data or not.




## TODO list

- [x] Set up Spring Boot
- [x] Get JPA to store data in H2
- [x] Get JPA to retrieve data
- [x] Get Spring Web to serve a page
- [x] Get ThymeLeaf to inject data into a template
- [x] Coordinate JPA and Thymeleaf to dynamically pull data from the database and display it on a page
- [x] Templatize the view page
- [x] Templatize the list page
- [x] Templatize the edit page
- [x] Create a BookingForm to map submitted values to model values
- [x] Make the edit page able to post and modify data
- [x] Make the edit page edit a booking if specified
- [x] Change buttons for links where they should be
- [x] Store a ref in the page queries so you can always get back to where you came from
- [x] Move that ref into cookies
- [x] Set up the repository
- [ ] Make the edit page create a booking if specified
- [ ] Gray buttons out on the view page when not applicable
- [ ] Make editing not possible depending on the status
- [ ] Change buttons based on who's viewing it (Chef can accept depending on status)
- [ ] Allow defining of `id` and `idType` cookies on the `/view` route
- [ ] Create a status-modifying route `/status`
- [ ] Create a confirmation modal on `/view` for status changing options (Accept, Reject, Cancel)
- [ ] Create an Employer name resolver service
- [ ] Create a Chef name resolver service
- [ ] Integrate the name resolvers into the controller  

- [x] Begin working on the API "Microservice" Project
- [x] Make it connect to the same H2 instance

... How are we going to decide who is viewing the view page in order to validate interactions with it?  
Just include it in the view params? eg `/view/B001?id=CH1` and if CH1 matches chef ID for the view then enable chef buttons?  
hmhmhmhm...  
I think we do server-side identifying and just pass individual parameters to the template for which button to show.  
EG. `showCancelButton`, `showEditButton`, `showRejectButton`, `showAcceptButton`  
Also, for identification, we should allow passing of `id` and `idType` on view and list, and store them in a cookie, then use that to auto-populate the HREFs like we already do for just the view->list links. That way we can rely on it for identification when editing, and we allow linking to the views from other systems.  
Nice!