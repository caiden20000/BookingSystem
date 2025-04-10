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
- [x] Make the edit page create a booking if specified
- [ ] Switch to auth entrypoint and cookies as identification
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

## Identification

... How are we going to decide who is viewing the view page in order to validate interactions with it?  
Just include it in the view params? eg `/view/B001?id=CH1` and if CH1 matches chef ID for the view then enable chef buttons?  
hmhmhmhm...  
I think we do server-side identifying and just pass individual parameters to the template for which button to show.  
EG. `showCancelButton`, `showEditButton`, `showRejectButton`, `showAcceptButton`  
Also, for identification, we should allow passing of `id` and `idType` on view and list, and store them in a cookie, then use that to auto-populate the HREFs like we already do for just the view->list links. That way we can rely on it for identification when editing, and we allow linking to the views from other systems.  
Nice!

---

There are some inherent issues with the way we "verify" an identity. The security isn't an issue, since there is no pretense that we are building a _real_ system. However, we have to draw the activity diagram through which screens an individual will access and ask, is setting the `refId` and `refType` cookies on `list` and `view` enough?  
If someone wants to create a booking, they're redirected from an external system to the `/create` page. At that point, they will create the page. When they submit and get redirected to the `/view` page, we have no way of knowing their identity and what to show them. That means we need ID on the create page too.  
If we need `id` and `idType` on practically all pages, why even store it in a cookie? It might even be safer to always keep it in the URL, it makes the system more stateless.  
So what we're proposing is the routes to look like this:  
- `/list?id={id}&idType={idType}`
- `/view/{bookingId}?id={id}&idType={idType}`
- `/edit?bookingId={bookingId}&id={id}&idType={idType}`
- `/create?employerId={employerId}&chefId={chefId}&id={id}&idType={idType}`
- `/save/{bookingId}?id={id}&idType={idType}`  
  
This may seem overkill, but that's the most robust, fail-safe way to keep identity consistent across pages.  
The other option I see is a sort of "portal" URL that external systems would link to. This portal page would set the cookies and redirect to the appropriate page.
That way, we have one path of entry into the system and we can guarantee that the user has the appropriate identification cookies.
It would look something like this:  
`/auth?id={id}&idType={idType}&page={page}&...(page specific parameters)`  
An example would be creating a new booking:  
`/auth? id=EMP1 & idType=employer & page=create & employerId=EMP1 & chefId=CH2`  
The problem with _this_ method of authorization is that links are not shareable. However, with the previous system, shared links would copy the identity of the source user. Sharing links is inherently a problem when identification is tied to the page contents. The only link in this system that should be shareable is the `/view` page, and at least with this authorization method, the copied link doesn't give the recipient unwanted access to identity-specific actions.  
  
OK so we're going with the latter: `/auth` entrypoint.
Let's lay out what happens on each page if you visit it without cookies:  
- `/list`: Identity Missing Error
- `/edit`: Identity Missing Error
- `/save`: Identity Missing Error
- `/create`: Identity Missing Error
- `/view`: Valid without identity, but potential actions (reject, accept, edit, cancel) are absent.  
  
If you visit a page without the cookies, the error page should tell you to log in to their appropriate system first. We have to assume that any broken links are due to user behaviour. If all links in all systems are formatted correctly, then the only time a user should get the error IdentityMissingErrorPage is if they manually visited a page without going through the Employer or Chef system.

---

The `/create` route shouldn't need an `employerId` parameter, since the employer always creates the booking and we'll have the ID of the user at the time of creation.