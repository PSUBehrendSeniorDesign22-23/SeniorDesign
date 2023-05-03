# Senior Design

This application is intended to be run and tested in a linux environment. All instructions below assume that you are in a linux
environment.

## Building and Running Instructions

Build and running the application is simple:

This command will build and test the application:

`./gradlew build`

This command will build and run the application:

`./gradlew bootRun`

Gradle caches the build state and only will rebuild the application if it detects changes, and it will only rebuild the parts that have been changed.
If you would like to force gradle to completely rebuild the application, please use the following command:

`./gradlew clean build`

If you would prefer not to use the command line, all of these gradle tasks are able to be located in the gradle for java extension.
## Testing Instructions

NOTE: Configuration of the testing database will need to be completed prior to running tests. Please see the file:
`/src/test/resources/application.properties` and configure it accordingly.

The simplest method to run tests is by running the command:

`./gradlew test`

This will require that a Java SDK is installed, and the appropriate extension pack is installed in VSCode.

Using the Gradle for Java extension is also an option. Look for task in the extension labeled "test". It should be under
`/contestmanager/Tasks/verification/test`. Then click the "Run" icon on it.

## Deployment Instructions

Below are general deployment steps, along with some notes regarding deployment.

These directions are specific to deployment on the Microsoft Azure Platform through VSCode.
If another platform is going to be used,
the instructions must be adjusted accordingly.

Prerequisites:
* You have an Azure account, and have set up an Azure subscription
* The VSCode extensions below are installed, installing one of them may install some of the others.
    * Extension Pack for Java
    * Gradle for Java
    * Azure Spring Apps
    * Azure Databases
    * Azure Account
    * Azure Resources

Steps:
1. Create a Postgresql database resource in Azure. There are a few ways to do this. The free option should be enough.
    * Create a PostgreSQL server (Flexible) through Microsoft Azure's web interface (Recommended)
    * Create a PostgreSQL server (Flexible) through the Azure VSCode extension
2. Update the database connection string in the application.properties file, found at /src/main/resources
    * The portion that needs updated is the line reading `spring.datasource.url=jdbc:postgresql://<Enter new URL here>`
3. Additional configuration will be needed for the database to work properly.
    * Connect to the database and configure an account that can login. It is best that the base "postgres" user account
    is not used as the primary account
    * The Spring Application's environment will need to be configured with the login credentials that you create (more on this later)
4. Create a Spring Application resource in Azure. **THIS WILL BEGIN TO ACCRUE COSTS**
    * This can be done through the web interface or through the VSCode extension. These directions will start from the VSCode extension.
    * To create it using the VSCode extension, open the extension, open the Azure subscription, find Spring Apps
    right-click on it and select "Create Spring Apps in Portal". This will open a web browser window and take you
    to the creation screen in Azure. Follow those steps, selecting the desired settings (These will affect the cost of Azure)
5. After the resource is created, return to VSCode, open the Spring Apps drop-down and look for the app that you created and open its submenu. Under this menu, you will accomplish two things: First you will need to configure a couple environment variables. Use
the "Environment Variables" option to create these. Two environment variables will be needed. These are the login credentials that
your application will use to connect to the database.
    * The first needs to be configured with variable name "AZ_POSTGRES_USERNAME" along with the username you created earlier
    * The second needs to be configured with variable name "AZ_POSTGRES_PASSWORD" along with the password you created earlier
    * If you would like to change these variable names, please edit their corresponding properties under the aforementioned
    application.properties file
After addressing the environment variables, right-click on "App Instances" and click "Deploy".

More details are available at: https://code.visualstudio.com/docs/java/java-spring-apps#_deploy-to-azure-spring-apps

### ADDITIONAL NOTES FOR DEPLOYMENT

At the time of writing, this application is not properly configured to use HTTPS. As such, anybody using the application will be passing login credentials over base HTTP which is unsecure on its own. Passwords are currently passed as plaintext in the HTTP request body. This will need to be resolved prior to deploying the application to be public facing.

Additionally, a proper domain should be configured for this web application. This is achievable through Azure, but a domain will need to obtained first.

Finally, though user roles exist in the system, they are not fully configured in the security keychain. As such anybody can currently access all endpoints. This should be resolved prior to deployment. There is also currently no readily available way to assign roles to a new user. All new users are given the default user role. Creating admins or superadmins currently requires the manual insertion of the role into the "role" database table.