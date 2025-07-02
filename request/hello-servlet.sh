#!/bin/bash

curl -v -X GET \
  -H "Host: b.com" \
  -H "User-Agent: curl/8.7.1" \
  -H "Accept: */*" \
  -H "Content-Type: application/json" \
  -H "Connection: keep-alive" \
  "http://localhost:8080/com/company/was/core/servlet/HelloServlet"