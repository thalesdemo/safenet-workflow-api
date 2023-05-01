#!/usr/bin/env bash

DOCKER_API_CLIENT_KEY_PATH="/app/client.key"
DOCKER_SETTINGS_PATH="/app/settings.json"
TOOL_LOCATION="/app/tools/keygen.jar"

function print_welcome_message() {
  local message=$(
    cat <<"EOF"


\e[1;32m             Welcome to the SafeNet RESTful Workflow API\e[0m


\e[0m\e[32m  This is your first time launching the app, so you'll need to retrieve an
\e[0m\e[32m  API client key to use in the `X-API-Key` header for authorization. 


\e[0m\e[32m  Here's how:

\e[1;33m    Step 1: \e[0m\e[32mOpen a new terminal window

\e[1;33m    Step 2: \e[0m\e[32mIn the new terminal window, run the following commands: 
\e[0m
        docker exec safenet-workflow-api cat $DOCKER_API_CLIENT_KEY_PATH

        docker exec safenet-workflow-api rm $DOCKER_API_CLIENT_KEY_PATH


\e[33m  WARNING: 

\e[33m   > Do not kill the 'docker-compose up' session in the original terminal
\e[33m     window, as the API server will stop running.

\e[33m   > The container will delete any clear-text client key file found upon
\e[33m     restart, since it only requires the hashed value of this key. The hash
\e[33m     is automatically stored in settings.json.\e[0m
EOF
  )
  echo -e "$message" | sed -e "s|\$DOCKER_API_CLIENT_KEY_PATH|$DOCKER_API_CLIENT_KEY_PATH|g"

}

function generate_api_key() {
  print_welcome_message
  json=$(java -jar $TOOL_LOCATION)
  api_key=$(echo "$json" | jq -r '.apiKey')
  echo $api_key >"$DOCKER_API_CLIENT_KEY_PATH"
  chmod 600 "$DOCKER_API_CLIENT_KEY_PATH"

  api_key_hash=$(echo "$json" | jq -r '.apiKeyHash')
  updated_settings=$(jq ".key_hash=\"$api_key_hash\"" "$DOCKER_SETTINGS_PATH")
  echo "$updated_settings" >"$DOCKER_SETTINGS_PATH"
  unset updated_settings api_key_hash api_key json
}

# Script starts here
printf '%.0s=' {1..80}
echo -e "\n"

# Phase 0: Check settings.json exists
if [ ! -s "$DOCKER_SETTINGS_PATH" ] || [ ! -e "$DOCKER_SETTINGS_PATH" ]; then
  # file is empty or does not exist
  echo -e "\e[33m[KEY] settings.json does not exist or is empty.\e[0m"
  echo -e "       Please check your configuration and try again. exit(1)"
  exit 1
fi

# Phase 1: Create or register the api key
if jq -e '.key_hash' $DOCKER_SETTINGS_PATH >/dev/null 2>&1; then
  # The key exists in the settings file
  echo -e "\033[34m[KEY] 'key_hash' exists in configuration.\e[0m"
  if [ -f "$DOCKER_API_CLIENT_KEY_PATH" ]; then
    rm -f "$DOCKER_API_CLIENT_KEY_PATH"
    echo -e "\033[34m       > Removed file secret $DOCKER_API_CLIENT_KEY_PATH.\e[0m"
  fi
else
  # The key does not exist in the settings file
  echo -e "\e[33m[KEY] 'key_hash' does not exist in configuration.\e[0m"
  echo -e "\e[33m       > Generating keypair ...\e[0m\n"
  printf '%.0s=' {1..80}
  generate_api_key
fi

# if [ ! -n "$API_KEY_HASH" ]; then
#   if [ -f "$DOCKER_API_SERVER_KEY_PATH" ]; then
#     source "$DOCKER_API_SERVER_KEY_PATH"
#     echo -e "\033[34m [KEY] API_KEY_HASH exists in configuration.\e[0m"
#     if [ -f "$DOCKER_API_CLIENT_KEY_PATH" ]; then
#       rm -f "$DOCKER_API_CLIENT_KEY_PATH"
#       echo -e "\033[34m       > Removed file secret $DOCKER_API_CLIENT_KEY_PATH.\e[0m"
#     fi
#   else
#     echo -e "\e[33m [KEY] API_KEY_HASH does not exist in environment or in configuration.\e[0m"
#     echo -e "\e[33m       > Generating keypair ...\e[0m\n"
#     printf '%.0s=' {1..80}
#     generate_api_key
#   fi
# else
#   echo -e "\033[34m [KEY] Loading API_KEY_HASH defined from environment variable.\e[0m"
# fi

# Phase 2: Set primary URL in config.ini based on environment variable
# sed -i "/PrimaryServer=/c\PrimaryServer=$SAFENET_SERVER_HOST" $JCRYPTO_INI_PATH

# Phase 3: Set log level in config.ini based on environment variable
# if [ "$API_LOG_LEVEL" = "DEBUG" ]; then
#  sed -i "/LogLevel=/c\LogLevel=5" $JCRYPTO_INI_PATH
# else
#  sed -i "/LogLevel=/c\LogLevel=3" $JCRYPTO_INI_PATH
# fi

# Phase 4: Launch tomcat web service
exec "$@"
