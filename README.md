<h1>SafeNet RESTful Workflow API</h1>

🚀 Get Started with Our SafeNet RESTful Workflow API on [Docker Hub](https://hub.docker.com/r/thalesdemo/safenet-workflow-api)!

👉 Alternately, deploy this project directly using our JAR file! Check out the [jar](https://github.com/thalesdemo/safenet-workflow-api/tree/main/jar) folder in our repository for step-by-step instructions.

<h2>Important Notice</h2>

Thank you for your interest in our demo project! It is important to note that the project is in development. Please also note that this project has been independently developed and is not an official software released by Thales for end-users or developers. <b>Please use the project at your own discretion and assume any potential risks associated with its use. </b>

<h2>Summary</h2>

The SafeNet Workflow API is a RESTful microservice that simplifies both user and token management by providing easy-to-use endpoints for token creation and revocation, along with user creation and deletion. The Workflow API streamlines the token management process for developers and offers advanced features such as flexible token enrollment methods in three possible ways: email, a direct URL link, or API calls.

With the email enrollment method, app developers can embed a unique enrollment link in an email and send it to users directly through the SafeNet authentication platform. In comparison, the direct URL link method allows app developers to embed the enrollment link directly in their application and redirect users to the enrollment page of the authentication server. With the third option, the API enrollment method offers a richer, more customizable user experience, allowing app developers to control the look and feel of the enrollment process while providing a native experience without ever having to expose the authentication service endpoints or for that matter without redirecting to the SafeNet enrollment page.

With the SafeNet Workflow API, you can efficiently manage both users and tokens, giving you full control over your user registration process. It ensures a streamlined user experience in enabling versatile multi-factor authentication options, such as our GrIDsure pattern-based authentication, our MobilePASS+ software authenticator with biometrics support and Push OTP authentication, along with various other OATH-based support options: SMS, Email, Voice, Hardware OTP, Google Authenticator, and more.

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

> **NOTE:** When using the Docker version of the application, the `settings.json` file serves as a virtual map to the host as per the `docker-compose.yml` file. This means that any changes made to `settings.json` are stored on the host machine. The launcher script within the Docker container checks if the `key_hash` key is present in `settings.json`. If the key is present, it is used to authenticate requests to the API. If the key is not present, the launcher script generates a new key and stores it in `settings.json`. This way, each deployment of the Docker container has a unique key, ensuring security and preventing unauthorized access. The `key_hash` is stored in `settings.json`, which is then mapped to the host machine, ensuring that the key is persistent across container restarts. If you want to create your own `X-API-Key` and associated hash `'key_hash'` then please follow the instructions referenced in [Step 1: Generate an API Key](https://github.com/thalesdemo/safenet-workflow-api/tree/main/jar#step-1-generate-an-api-key) of the JAR installation guide.

<h2>API Reference</h2>

To view the API documentation and all available endpoints, open your web browser and go to `http://localhost:8080`.

![example.gif](https://github.com/thalesdemo/safenet-workflow-api/raw/main/assets/static.png)

<h2>Contact Us</h2>
If you have any feedback to share or would like to request new features, please feel free to reach out to us at <a href="mailto:hello@onewelco.me">hello@onewelco.me</a>. We welcome your input!
