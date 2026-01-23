# REST Bridge Testing Guide

## Quick Start

```bash
# Build
cd ~/projects/rest-bridge
mvn package -q -DskipTests

# Start
java -jar target/rest-bridge-*.jar

# Verify
curl http://localhost:9877/health
```

## Connection Details

| Field | Value |
|-------|-------|
| REST Endpoint | http://192.168.0.110:9080/dctm-rest |
| Repository | EDMS |
| Username | dmadmin |
| Password | dmadmin |

## API Endpoints

- Swagger UI: http://localhost:9877/swagger-ui.html
- OpenAPI JSON: http://localhost:9877/api-docs

## Test Commands

### Connect

```bash
curl -s -X POST http://localhost:9877/api/v1/connect \
  -H "Content-Type: application/json" \
  -d '{
    "endpoint": "http://192.168.0.110:9080/dctm-rest",
    "repository": "EDMS",
    "username": "dmadmin",
    "password": "dmadmin"
  }' | jq .
```

Save the `sessionId` from the response for subsequent calls.

### DQL Query

> **Note:** DQL may be disabled on some REST Services deployments. Check availability first.

```bash
SESSION_ID="<your-session-id>"

# Check if DQL is available
curl -s "http://localhost:9877/api/v1/dql/available?sessionId=$SESSION_ID" | jq .

# Execute DQL (if available)
curl -s -X POST http://localhost:9877/api/v1/dql \
  -H "Content-Type: application/json" \
  -d "{
    \"sessionId\": \"$SESSION_ID\",
    \"query\": \"SELECT r_object_id, object_name FROM dm_document WHERE FOLDER('/Temp')\"
  }" | jq .
```

### Get Object

```bash
SESSION_ID="<your-session-id>"
OBJECT_ID="0904719980000200"

curl -s "http://localhost:9877/api/v1/objects/$OBJECT_ID?sessionId=$SESSION_ID" | jq .
```

### Get Types

```bash
curl -s "http://localhost:9877/api/v1/types?sessionId=$SESSION_ID" | jq '.[0:5]'
```

### Get Type Details

```bash
curl -s "http://localhost:9877/api/v1/types/dm_document?sessionId=$SESSION_ID" | jq '{name, superType, attrCount: (.attributes | length)}'
```

### Get Groups

```bash
curl -s "http://localhost:9877/api/v1/groups?sessionId=$SESSION_ID" | jq .
```

### Get Users

```bash
curl -s "http://localhost:9877/api/v1/users?sessionId=$SESSION_ID" | jq .
```

### Get Cabinets

```bash
curl -s "http://localhost:9877/api/v1/cabinets?sessionId=$SESSION_ID" | jq .
```

## Stop Bridge

```bash
pkill -f "RestBridgeApplication"
```

## Run Unit Tests

```bash
cd ~/projects/rest-bridge
mvn test
```

## Run Specific Test Class

```bash
mvn test -Dtest=TypeServiceImplTest
```

## Differences from DFC Bridge

| Feature | DFC Bridge (9876) | REST Bridge (9877) |
|---------|-------------------|-------------------|
| Connection | Docbroker + port | REST Services endpoint |
| DQL | Always available | May be disabled |
| Users/Groups | Via DQL | Native REST endpoints |
| Prerequisites | DFC libraries | None (pure Java) |
