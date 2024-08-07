<p align="center">
<img src="Client/public/images/logo.png" width="250px"/>
</p>

# Lettuce Cook

Lettuce Cook is a recipe-sharing website that allows users to explore, save favorites, and search for recipes easily. The application is built using Java Spring Boot for the backend and React for the frontend, and it utilizes a microservices architecture for enhanced scalability and maintainability.


## Features

1. **Register & Login:** Users can create an account or log in to access personalized features.
2. **Authentication with JWT Token:** Secured authentication using JSON Web Token, with backend security implemented through filters and security chain (web security).
3. **Comprehensive Backend Testing:** Thorough testing using mockups to ensure robust backend functionality.
4. **View Recipes:** Users can view detailed information about recipes, including ingredients and instructions.
5. **Favorite Recipes:** Mark and unmark favorite recipes, and view all recipes favorited by the user.
6. **Search using Elasticsearch:** Powerful search functionality to find recipes based on keywords.
7. **Caching with Redis:** Utilizes Redis for caching recipes to enhance performance.
8. **Category-based Organization:** Users can order recipes by categories and explore or search within specific categories.
9. **Logging:** Backend activities are logged for monitoring and debugging purposes.
10. **Data Storage in PostgreSQL:** Data persistence and retrieval powered by PostgreSQL for a robust and scalable solution.

## Microservices Architecture

The application has been split into the following microservices:

1. **API Gateway:** Uses Eureka Gateway for routing and load balancing.
2. **Naming Server:** Eureka server for service discovery.
3. **Auth Service:** Handles user authentication (login, register) using JWT.
4. **Recipe Service:** Manages recipes, including adding, deleting, retrieving, and handling all favorite actions.
5. **Search Service:** Uses Elasticsearch to provide search functionality for recipes.

## Getting Started

Follow these steps to get Lettuce Cook up and running:

1. Clone the repository: `git clone https://github.com/ShahafErez/lettuce-cook.git`
2.Install dependencies for the frontend and backend:
   ```bash
    # Client
    cd client
    npm install

    # Server (using Maven)
    cd ../api-gateway
    ./mvnw install
    cd ../naming-server
    ./mvnw install
   cd ../auth-service
    ./mvnw install
   cd ../recipe-service
    ./mvnw install
   cd ../search-service
    ./mvnw install
    ```

3. Set up Backend Dependencies:
    - PostgreSQL: Ensure PostgreSQL is installed and running. You can download it [here](https://www.postgresql.org/download).
    - Elasticsearch: Ensure Elasticsearch is installed and running. You can download it [here](https://www.elastic.co/downloads/elasticsearch).
    - Redis: Ensure Redis is installed and running. You can download it [here](https://redis.io/download).
      
4. Start the application:
    ```bash
    # Client
    cd ../client
    npm start

    # Server (running with Maven)
    cd ../server
    ./mvnw spring-boot:run -pl api-gateway
    ./mvnw spring-boot:run -pl naming-server
    ./mvnw spring-boot:run -pl auth-service
    ./mvnw spring-boot:run -pl recipe-service
    ./mvnw spring-boot:run -pl search-service
    ```

## Demos

1. **Home Page:**
   <img src="Client/public/demo/home.gif" width="800px"/>

   - Explore randomized, newest, and category-based recipes.
   - Start with the default category ("Dinner"), switch to another ("Lunch"), and navigate to a dedicated page for all recipes in that category.

2. **Recipe Page:**
   <img src="Client/public/demo/recipe-page.png" width="800px"/>

   - Display a recipe with comprehensive details, including ingredients, instructions, dietary restrictions, ingredient count, making time, and serving size.

3. **Search Page:**
   <img src="Client/public/demo/search.gif" width="800px"/>

   - Perform a search by term (e.g., "lettuce") and customize results by adding a category filter.

4. **Register Page:**
   <img src="Client/public/demo/register.png" width="800px"/>

   - Sign up for an account using a user-friendly registration page.
   - Note: The login page follows a similar design and functionality for a seamless user experience.

5. **Favorite Recipes Page:**
   <img src="Client/public/demo/favorite-recipes.png" width="800px"/>

   - View a personalized list of all favorite recipes.
   - Note: Favorite recipes are personal to each logged-in user and are accessible only to users with an active account.


## License

This project is licensed under the [MIT License](LICENSE).
