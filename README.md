# "News portal" web application
Main features:
- Non-authorized users can create new account (or login if they have one) or visit main page and view single news without comments 
- Authorized users with role "USER" may create / update / delete their own news and comments and put likes (or dislikes) to other user's news and comments. They may also update their personal info and security settings and view any other profiles. 
- Authorized users with role "ADMIN" may create / update / delete any news and comments and put likes (or dislikes) to other user's news and comments. They may also update personal info and role of any user. Admins can create badges at the admin page and assign them to any user.  
- Certain user badges and rating (the total rating of his news and comments) can be found on user info page
- Application supports English and Russian languages

Used technologies ('spring-mvc' branch):
- Database: MySQL
- DAO layer: Spring Data, Hibernate (early versions on 'master' branch: JDBC)
- Web layer: Spring MVC / Security, JSP (early versions on 'master' branch: Servlets)

New functions are in development, please, check for updates) 

