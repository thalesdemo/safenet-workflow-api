# SafeNet RESTful Workflow API Installation Guide (using JAR file)

## Table of Contents

- [Summary](https://github.com/thalesdemo/safenet-workflow-api/tree/main/jar#introduction)
- [Requirements](https://github.com/thalesdemo/safenet-workflow-api/tree/main/jar#requirements)
- [Deployment Steps](https://github.com/thalesdemo/safenet-workflow-api/tree/main/jar#instructions)
  - [Download Configuration File](https://github.com/thalesdemo/safenet-workflow-api/tree/main/jar#step-1-download-the-configuration-file)
  - [Generate API Key](https://github.com/thalesdemo/safenet-workflow-api/tree/main/jar#step-2-generate-an-api-key)
  - [Modify Configuration File](https://github.com/thalesdemo/safenet-workflow-api/tree/main/jar#step-3-modify-the-ini-configuration-file)
  - [Run JAR File](https://github.com/thalesdemo/safenet-workflow-api/tree/main/jar#step-5-run-the-jar-file)
- [Final Thoughts](https://github.com/thalesdemo/safenet-workflow-api/tree/main/jar#conclusion)

## Summary

This guide offers a comprehensive overview of utilizing the SafeNet RESTful Workflow API to effectively manage tokens and users for SafeNet Trusted Access (STA) or SafeNet Authentication Service (SAS-PCE) accounts. The SafeNet RESTful Workflow API, a Spring-based project, delivers a seamless RESTful interface that enables client applications to communicate with the SafeNet authentication service and carry out tasks such as token enrollment and user administration. In this document, the primary focus is on employing the JAR file directly, as opposed to using the Docker image.

## Requirements

To utilize the SafeNet RESTful Workflow API via the JAR file, ensure you have the following prerequisites in place:

1. Java Runtime Environment (JRE) version 11 installed on your system.
2. An operator account for your SafeNet Trusted Access (STA) or SafeNet Authentication Service (SAS-PCE) tenant, configured with a static password. Refer to this guide for assistance.

## Deployment Steps

After satisfying the prerequisites, you can effortlessly use the SafeNet RESTful Workflow API directly through the JAR file by following these steps:

### Step 1: Generate an API Key

Generate an API key for your client application:

- Option 1: Run our [keygen-1.0.jar](https://github.com/thalesdemo/safenet-auth-api/blob/main/tools/keygen-1.0.jar) tool using the command line:

**Output example:**

```
[user@linux tools]$ java -jar keygen-1.0.jar
{
    "apiKey":"0sMj_ylf-TVtkbn3E-kEPEXJ2e-2VJSBDiS-lBEq1fCQ-OtbPytek",
    "apiKeyHash":"$2a$10$eOSUL4ULDPPd/qXFxMmnOeFlRLgua5XWJQ8INmlnKk7A0JNemDKoi"
}
```

- Option 2: Write your own Java code using `BCryptPasswordEncoder`:

```
  import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

  public class ApiKeyGenerator {
 	 public static void main(String[] args) {
  		 String clientKey = "MySecureApiKey2023!";
 	 	 BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
 		 String serverKey = bcrypt.encode(clientKey);
 		 System.out.println("Client Key: " + clientKey);
 		 System.out.println("Server Key: " + serverKey);
 	 }
  }
```

> **Note:** Replace `MySecureApiKey2023!` with your desired client API key value. Running this Java code will output the server key (i.e., `X_API_KEY_HASH`) that you can use with the SafeNet Workflow API with either the JAR or Docker image.

### Step 2: Customize the Configuration File (`settings.json`)

Begin by downloading the configuration template:

- [settings.json](https://github.com/thalesdemo/safenet-workflow-api/raw/main/settings.json)

Alternatively, you can directly edit this JSON configuration and save it as a file:

```yaml
{
  "base_url": "https://cloud.us.safenetid.com",
  "username": "<operator_email>",
  "password": "<operator_password>",
  "key_hash": "<api_key_hash>",
}
```

Now, update each field as follows:

- `base_url`: Enter the SafeNet server base URL (e.g., https://cloud.us.safenetid.com for the US service zone)
- `username`: Enter the operator's email address
- `password`: Enter the operator's static password
- `key_hash`: Replace with the apiKeyHash generated in Step 1

### Step 3: Run the JAR File

Execute the JAR file using the command below:

    java -DSAFENET_WORKFLOW_LOG_LEVEL=INFO \
         -DSAFENET_WORKFLOW_PORT=8888 \
         -DSAFENET_WORKFLOW_CONFIG_PATH./settings.json \
         -jar safenet-workflow-api-[version].jar

This command defines several optional environment variables that the Java application can leverage:

- `SAFENET_WORKFLOW_LOG_LEVEL` (optional): Specifies the logging level for the Java application (default: INFO)
- `SAFENET_WORKFLOW_PORT` (optional): Designates the port number for the Java application server (default: 8080)
- `SAFENET_WORKFLOW_CONFIG_PATH` (optional): Indicates the file path for the settings.json configuration file (default: ./settings.json)
- `SAFENET_WORKFLOW_USER_DELIMITER` (optional): Sets the realm delimiter for the username (default: @)
- `safenet-workflow-api-[version].jar`: The filename of the JAR file to run

## Final Thoughts

Throughout this guide, we have offered comprehensive instructions on installing and executing the SafeNet RESTful Workflow API via the JAR file. By adhering to the outlined steps, you can efficiently establish and deploy a RESTful microservice that facilitates user administration and token enrollment in conjunction with the SafeNet authentication service.
