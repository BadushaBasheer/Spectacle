# **E-Commerce Web Application**

**Overview**

This project is a fully functional E-commerce web application developed using Java Spring Boot, Thymeleaf, JavaScript, HTML, and PostgreSQL. The website is hosted on Amazon AWS to ensure high availability and scalability, providing users with a reliable platform for online shopping.

**Features**

- **User-friendly Interface**: The application offers a user-friendly interface with intuitive features for product browsing, purchasing, and management. Users can easily navigate through the website to find products, add them to their cart, and complete their purchase.

- **Secure Online Transactions**: To ensure the security of online transactions, the application integrates the Razor Pay payment gateway. This allows users to make payments securely, protecting their sensitive financial information.

- **Enhanced Security**: To enhance security further, the application implements OTP Verification using Java mail sender. This adds an extra layer of protection by requiring users to verify their identity via email before completing certain actions, such as account registration or password reset.

- **User-side Functionality**: On the user side, the application manages product listing, cart, and checkout processes. Users can easily view product details, add items to their cart, and proceed through the checkout process to complete their purchase.

- **Admin-side Functionality**: Administrators have access to additional features through the admin dashboard. This includes managing products, users, categories, and coupon offers. Additionally, the admin dashboard provides sales reports and custom chart/graph reports to gain insights into business performance.

**Installation**

1. **Clone the repository**: Begin by cloning the repository to your local machine using the following command:

    ```bash
    git clone https://github.com/your-username/e-commerce-project.git
    ```

2. **Install dependencies and build the project**: Navigate to the project directory and install dependencies by running:

    ```bash
    cd e-commerce-project
    ./mvnw clean package
    ```

3. **Run the application**: Once dependencies are installed, run the application using the following command:

    ```bash
    java -jar target/e-commerce-project.jar
    ```

4. **Access the application**: After the application is running, access it in your browser at `http://localhost:8080`.

**Usage**

- **Shopping**: Users can browse products, add items to their cart, and proceed to checkout for purchasing.
- **Administration**: Admin users can access the admin dashboard to manage products, users, categories, and coupon offers. They can also view sales reports and custom chart/graph reports to gain insights into business performance.

**Contributing**

Contributions to this project are welcome! Whether it's bug fixes, feature enhancements, or documentation improvements, feel free to submit pull requests to help improve the application for everyone.
