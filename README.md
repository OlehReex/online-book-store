<h1 align="center">📚 Online Book Store 📚</h1>
This code implements a system for buying and selling books online. The buyer can register, log in, find the necessary books, put them in the shopping cart and complete the order. All books, as well as other entities, are stored in the database, and their management is handled by the administrator. The application is developed in compliance with SOLID principles, so it is easy to scale, make changes and implement new features.

<a name="Navigation"></a>
## :mag: Navigation
0. [Technologies used](#Technologies_used)
1. [Registration and Authentication](#Registration_and_Authentication)
2. [Authorities and Capabilities](#Users_roles)
3. [Structure of Class relations](#Class_relations)
4. [Controllers and Endpoints](#Management_and_Endpoints)
5. [Launch with Docker](#Launch_with_Docker)
6. [Project Demonstration](#Project_Demonstration)

<a name="Technologies_used"></a>
## :gear: Technology Stack

[![t-stack.jpg](https://i.postimg.cc/L8bhNZcv/t-stack.jpg)](https://postimg.cc/rD58pzFt)
<p align="right">
    <a href="#Navigation">:arrow_up: Navigation</a>
</p>

<a name="Registration_and_Authentication"></a>
## :handshake: Registration and Authentication
Registration, authentication and authorization processes are implemented using Spring Security:
- In order to gain access to the system, the user registers - enters a unique username,
  enter the password and repeat it.
- A registered user enters data to log in and receives a JWT token to work with the system.

<p align="right">
    <a href="#Navigation">:arrow_up: Navigation</a>
</p>

<a name="Users_roles"></a>
## :busts_in_silhouette: Authorities and capabilities
Every registered user is a buyer by default and has the USER authority.
To get advanced features, ADMIN needs to add the appropriate authority for the regular user.

- ### USER capabilities:
    - browsing all books on the resource
    - browsing a specific book
    - search books by category
    - view all categories on the resource
    - adding books to the shopping cart
    - changing the quantity of a specific book in the shopping cart
    - removing the book from the shopping cart
    - browsing books in the shopping cart
    - creating an order
    - browsing information about the order
    - receiving a list of previous orders
- ### ADMIN capabilities:
    - adding a new book to the resource
    - updating books
    - deleting the book
    - adding a new category
    - updating categories
    - deleting a category
    - changing of order status

<p align="right">
    <a href="#Navigation">:arrow_up: Navigation</a>
</p>


<a name="Class_relations"></a>
## :key: Structure of class relations

[![diagram-r.jpg](https://i.postimg.cc/59rF0Lr1/diagram-r.jpg)](https://postimg.cc/Btxv7Lsw)
<p align="right">
    <a href="#Navigation">:arrow_up: Navigation</a>
</p>

<a name="Management_and_Endpoints"></a>
## :dart: Controllers and Endpoints
- ### Authentication Controller:
    - **POST:** /auth/register - new user registration
    - **POST:** /api/auth/login - login user in the system

- ### Book Controller:
    - **GET** (USER) : /books - getting a list of all books
    - **GET** (USER): /books/{id} - getting a specific book
    - **POST** (ADMIN): /books - adding a new book to the system
    - **PUT** (ADMIN): /books/{id} - updating the selected book
    - **DELETE** (ADMIN): /books/{id} - deleting the selected book

- ### Category Controller:
    - **GET** (USER) : /categories - getting a list of all categories
    - **GET** (USER): /categories/{id} - getting a specific category
    - **GET** (USER): /categories/{id}/books - getting a list of all books by category
    - **POST** (ADMIN): /categories - adding a new category to the system
    - **PUT** (ADMIN): /categories/{id} - updating the selected category
    - **DELETE** (ADMIN): /categories/{id} - deleting the selected category

- ### ShoppingCart Controller:
    - **GET** (USER) : /cart - getting shopping cart information
    - **POST** (USER): /cart - adding new item to shopping cart
    - **PUT** (USER): /cart/cart-items/{cartItemId} - updating item quantity
    - **DELETE** (USER): /cart/books/{id} - deleting item from shopping cart

- ### Order Controller:
    - **GET** (USER) : /orders - getting orders history
    - **GET** (USER): /orders/{orderId}/items - getting list of all items from order
    - **GET** (USER): /orders/{orderId}/items/{id} - getting specific item from order
    - **POST** (USER): /orders - creating new order
    - **PATCH** (ADMIN): /orders/{id} - updating order status

<p align="right">
    <a href="#Navigation">:arrow_up: Navigation</a>
</p>


<a name="Launch_with_Docker"></a>
## :whale: Launch with Docker
You can test the functionality of the application in more detail on your own
computer. In order to run the Online Book Store locally, take the following steps:
1. [Download Docker](https://www.docker.com/products/docker-desktop/) and install it.
2. Clone this repo from GitHub using command in Terminal:  
   `git clone https://github.com/OlehReex/online-book-store.git`
3. Create an `.env` file in the root directory.
4. Copy all variables from `.env.sample` file into your `.env` file and
   add your values for the required variables.
5. Run `mvn clean package` command.
6. Run `docker-compose build` and `docker-compose up` commands.
7. To test application functionality you can use Swagger:  
   just open [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html) to start.

<p align="right">
    <a href="#Navigation">:arrow_up: Navigation</a>
</p>

___
<p align="center"><strong style="font-size: 18px;">Contact me in a convenient way:</strong></p>

<p align="center">
   <a href="https://t.me/OliverReex"><img src="https://i.postimg.cc/8P7kr9S8/telega.png" alt="telega" width="15" height="15" /> Telegram</a> |
   <a href="https://www.linkedin.com/in/oliiarnyx/"><img src="https://i.postimg.cc/rpkQrB7Q/li.png" alt="linked_in" width="15" height="15" /> LinkedIn</a> |  
   <a href="https://mail.google.com/mail/?view=cm&to=oliiarnyx@gmail.com"><img src="https://i.postimg.cc/9fBHS59n/gmail.png" alt="gmail" width="15" height="15" /> Gmail</a> 
</p>
