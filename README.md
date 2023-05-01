<h1>SafeNet RESTful Workflow API</h1>

🚀 Get Started with Our SafeNet RESTful Workflow API on [Docker Hub](https://hub.docker.com/r/thalesdemo/safenet-workflow-api)!

👉 Alternately, deploy this project directly using our JAR file! Check out the [jar](https://github.com/thalesdemo/safenet-workflow-api/tree/main/jar) folder in our repository for step-by-step instructions.

<h2>Important Notice</h2>

Thank you for your interest in our demo project! It is important to note that the project is in development. <b>Please use the project at your own discretion and assume any potential risks associated with its use</b>.

<h2>Summary</h2>

The SafeNet Workflow API is a RESTful microservice that simplifies both user and token management by providing easy-to-use endpoints for token creation and revocation. The Workflow API streamlines the token management process for developers and offers advanced features such as flexible token enrollment methods triggered by three possible ways: email, a direct URL link, or API calls. The email enrollment method allows app developers to embed a unique enrollment link in an email and send it to users. The direct URL link method allows app developers to embed the enrollment link directly in their application and redirect users to the enrollment page of the authentication server. The API enrollment method offers a richer, more customizable user experience, allowing app developers to control the look and feel of the enrollment process while providing a native experience without having to expose the authentication service endpoints or enrollment page. With the SafeNet Workflow API, you can efficiently manage both users and tokens, giving you full control over your user registration process and ensuring a seamless user experience for your customers.

<h2>Deployment Guidelines</h2>

To successfully deploy the SafeNet Workflow API Docker image, you need to configure a settings file named `settings.json` in your working directory with the necessary information. No modifications are necessary to the `docker-compose.yml` file, and by default, it should be stored in the same directory as the settings file.

<h2>Configuration Files</h2>

<b>settings.json</b> - copy and modify this file

```json
{
  "base_url": "https://cloud.us.safenetid.com",
  "username": "your_operator_email",
  "password": "your_operator_password"
}
```

<b>docker-compose.yml</b> - copy this file without making any changes

```yaml
version: "3"

services:
  safenet-workflow-api:
    image: thalesdemo/safenet-workflow-api
    container_name: safenet-workflow-api
    environment:
      SAFENET_WORKFLOW_PORT: ${SAFENET_WORKFLOW_PORT-8080}
      SAFENET_WORKFLOW_LOG_LEVEL: ${SAFENET_WORKFLOW_LOG_LEVEL-INFO}
      SAFENET_WORKFLOW_USER_DELIMITER: ${SAFENET_WORKFLOW_USER_DELIMITER-@}
    volumes:
      - type: bind
        source: ${HOST_SAFENET_WORKFLOW_CONFIG_PATH-./settings.json}
        target: /app/settings.json
    ports:
      - ${SAFENET_WORKFLOW_PORT-8080}:${SAFENET_WORKFLOW_PORT-8080}
```

<b>.env</b> (optional)

```text
HOST_SAFENET_WORKFLOW_CONFIG_PATH=./settings.json
SAFENET_WORKFLOW_PORT=8080
SAFENET_WORKFLOW_USER_DELIMITER='@'
SAFENET_WORKFLOW_LOG_LEVEL=INFO
```

> Optionally, you can create a `.env` file with environment variables for the different fields shown above. If the `.env` file is not present, the application uses the default values shown in this `.env` file above.

<h2>Starting the Docker Container</h2>

When you are ready, go to your working directory and run the Docker by typing:

```
docker-compose up
```

Then, follow the instructions that appear on the screen to get your unique client header key. You must supply this key in the `X-API-Key` header for every HTTP request.

<h2>API Reference</h2>

To view the API documentation and all available endpoints, open your web browser and go to `http://localhost:8080`.

![example.gif](https://github.com/thalesdemo/safenet-workflow-api/raw/main/assets/static.png)

<h2>Contact Us</h2>
If you have any feedback to share or would like to request new features, please feel free to reach out to us at <a href="mailto:hello@onewelco.me">hello@onewelco.me</a>. We welcome your input!
