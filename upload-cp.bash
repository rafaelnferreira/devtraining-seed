#!/bin/bash

AUTH_TOKEN=$TOKEN

set -x

while true; do
    # Your code here

    curl --request POST \
  --url http://localhost:9064/EVENT_COUNTER_PARTY_UPLOAD \
  --header 'Content-Type: application/json' \
  --header "SESSION_AUTH_TOKEN: $AUTH_TOKEN" \
  --header 'SOURCE_REF: DEV' \
  --fail \
  --data '{
    "DETAILS": {
      "NAME": "Bank of America"
    }
}
'

    # Sleep for 10 milliseconds
#    sleep 0.01
done
