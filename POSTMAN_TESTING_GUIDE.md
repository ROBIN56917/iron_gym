# Iron Gym API - Postman Testing Guide

This guide explains how to test the Iron Gym API endpoints using Postman with step-by-step instructions.

## 🚀 **Setup Instructions**

### 1. **Import the Collection**
- Download the Postman collection file (if available) or create requests manually
- Set base URL: `http://localhost:8080`

### 2. **Environment Variables (Optional)**
Create environment variables for easier testing:
- `baseUrl`: `http://localhost:8080`
- `clientId`: (will be set during tests)
- `membershipId`: (will be set during tests)

---

## 👥 **Client API Endpoints**

### **GET All Clients**
- **Method**: GET
- **URL**: `{{baseUrl}}/api/clients`
- **Description**: Gets all clients in the system
- **Expected Response**: Array of client objects
- **Test Script**:
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response is array", function () {
    pm.expect(pm.response.json()).to.be.an('array');
});
```

### **GET Client by ID**
- **Method**: GET
- **URL**: `{{baseUrl}}/api/clients/{{clientId}}`
- **Description**: Gets a specific client by ID
- **Expected Response**: Single client object or 404 if not found
- **Test Script**:
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Client has required fields", function () {
    var json = pm.response.json();
    pm.expect(json).to.have.property('id');
    pm.expect(json).to.have.property('name');
    pm.expect(json).to.have.property('email');
});
```

### **GET Clients by Membership Type**
- **Method**: GET
- **URL**: `{{baseUrl}}/api/clients/membership/BASIC`
- **Description**: Gets clients with BASIC membership (change BASIC to PREMIUM or VIP)
- **Expected Response**: Array of client objects with specified membership type

### **POST Create Client**
- **Method**: POST
- **URL**: `{{baseUrl}}/api/clients`
- **Headers**: `Content-Type: application/json`
- **Body** (raw JSON):
```json
{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "555-123-4567",
    "membership": {
        "type": "BASIC",
        "startDate": "2024-01-01",
        "endDate": "2024-12-31",
        "price": 29.99
    }
}
```
- **Expected Response**: Created client with generated ID (status 201)
- **Test Script**:
```javascript
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

pm.test("Client was created with ID", function () {
    var json = pm.response.json();
    pm.expect(json).to.have.property('id');
    pm.environment.set("clientId", json.id);
});
```

### **PUT Update Client**
- **Method**: PUT
- **URL**: `{{baseUrl}}/api/clients/{{clientId}}`
- **Headers**: `Content-Type: application/json`
- **Body** (raw JSON):
```json
{
    "name": "John Smith",
    "email": "john.smith@example.com",
    "phone": "555-987-6543",
    "membership": {
        "type": "PREMIUM",
        "startDate": "2024-01-01",
        "endDate": "2024-12-31",
        "price": 59.99
    }
}
```
- **Expected Response**: Updated client object (status 200)

### **DELETE Client**
- **Method**: DELETE
- **URL**: `{{baseUrl}}/api/clients/{{clientId}}`
- **Description**: Deletes a client by ID
- **Expected Response**: 204 No Content if successful, 404 if not found

### **GET Export Clients to CSV**
- **Method**: GET
- **URL**: `{{baseUrl}}/api/clients/export/csv`
- **Description**: Exports all clients as CSV file
- **Expected Response**: CSV file download
- **Test Script**:
```javascript
pm.test("Content-Type is CSV", function () {
    pm.expect(pm.response.headers.get("Content-Type")).to.include("text/csv");
});

pm.test("Content-Disposition has filename", function () {
    pm.expect(pm.response.headers.get("Content-Disposition")).to.include("clients.csv");
});
```

### **GET Client Statistics**
- **Method**: GET
- **URL**: `{{baseUrl}}/api/clients/stats`
- **Description**: Gets statistics about clients and memberships
- **Expected Response**: Statistics object with counts

---

## 💳 **Membership API Endpoints**

### **GET All Memberships**
- **Method**: GET
- **URL**: `{{baseUrl}}/api/memberships`
- **Description**: Gets all memberships in the system
- **Expected Response**: Array of membership objects

### **GET Membership by ID**
- **Method**: GET
- **URL**: `{{baseUrl}}/api/memberships/{{membershipId}}`
- **Description**: Gets a specific membership by ID

### **GET Memberships by Type**
- **Method**: GET
- **URL**: `{{baseUrl}}/api/memberships/type/PREMIUM`
- **Description**: Gets memberships with PREMIUM type

### **GET Active Memberships**
- **Method**: GET
- **URL**: `{{baseUrl}}/api/memberships/active`
- **Description**: Gets all currently active memberships

### **GET Expired Memberships**
- **Method**: GET
- **URL**: `{{baseUrl}}/api/memberships/expired`
- **Description**: Gets all expired memberships

### **POST Create Membership**
- **Method**: POST
- **URL**: `{{baseUrl}}/api/memberships`
- **Headers**: `Content-Type: application/json`
- **Body** (raw JSON):
```json
{
    "type": "BASIC",
    "startDate": "2024-01-01",
    "endDate": "2024-12-31",
    "price": 29.99
}
```
- **Expected Response**: Created membership with ID (status 201)

### **POST Create Auto Membership**
- **Method**: POST
- **URL**: `{{baseUrl}}/api/memberships/auto`
- **Headers**: `Content-Type: application/json`
- **Body** (raw JSON):
```json
{
    "type": "VIP",
    "startDate": "2024-01-01",
    "durationMonths": 12
}
```
- **Description**: Creates membership with automatic pricing
- **Expected Response**: Created membership with calculated price

### **PUT Update Membership**
- **Method**: PUT
- **URL**: `{{baseUrl}}/api/memberships/{{membershipId}}`
- **Body** (raw JSON):
```json
{
    "type": "VIP",
    "startDate": "2024-01-01",
    "endDate": "2024-12-31",
    "price": 99.99
}
```

### **DELETE Membership**
- **Method**: DELETE
- **URL**: `{{baseUrl}}/api/memberships/{{membershipId}}`
- **Expected Response**: 204 No Content if successful

### **GET Membership Price**
- **Method**: GET
- **URL**: `{{baseUrl}}/api/memberships/price/VIP`
- **Description**: Gets the monthly price for VIP membership
- **Expected Response**: Price information object

---

## 🧪 **Testing Scenarios**

### **Scenario 1: Complete Client Workflow**
1. **Create Client**: POST to `/api/clients`
2. **Get Client**: GET using the returned ID
3. **Update Client**: PUT with new information
4. **Export CSV**: GET `/api/clients/export/csv`
5. **Delete Client**: DELETE the client

### **Scenario 2: Membership Management**
1. **Create Membership**: POST to `/api/memberships/auto`
2. **Check Active**: GET `/api/memberships/active`
3. **Update Membership**: PUT with new dates
4. **Check Price**: GET `/api/memberships/price/{type}`

### **Scenario 3: Error Handling Tests**
1. **Invalid Client**: POST with missing required fields
2. **Invalid ID**: GET with non-existent ID
3. **Invalid Email**: POST with invalid email format
4. **Invalid Dates**: POST with start date after end date

---

## 🔍 **Expected Response Formats**

### **Client Object**
```json
{
    "id": "uuid-string",
    "name": "Client Name",
    "email": "email@example.com",
    "phone": "555-123-4567",
    "membership": {
        "id": "uuid-string",
        "type": "BASIC",
        "startDate": "2024-01-01",
        "endDate": "2024-12-31",
        "price": 29.99
    }
}
```

### **Membership Object**
```json
{
    "id": "uuid-string",
    "type": "PREMIUM",
    "startDate": "2024-01-01",
    "endDate": "2024-12-31",
    "price": 59.99
}
```

### **Error Response**
```json
{
    "timestamp": "2024-01-01T12:00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation error message"
}
```

---

## 📊 **CSV Export Format**

The CSV export will have the following format:
```csv
ID,Name,Email,Phone,Membership Type,Start Date,End Date,Price
uuid-1,John Doe,john@example.com,555-1234,BASIC,2024-01-01,2024-12-31,29.99
uuid-2,Jane Smith,jane@example.com,555-5678,PREMIUM,2024-01-01,2024-12-31,59.99
```

---

## ✅ **Success Criteria**

Your API is working correctly if:

1. **All CRUD Operations Work**: Create, Read, Update, Delete for both clients and memberships
2. **CSV Export Works**: Can download client data as CSV file
3. **Error Handling Works**: Invalid requests return appropriate error codes
4. **Data Validation Works**: Invalid data is rejected with clear error messages
5. **File Storage Works**: Data persists between application restarts (CSV files)

---

## 🚨 **Common Issues & Solutions**

### **Issue: Connection Refused**
- **Solution**: Make sure the Spring Boot application is running on port 8080

### **Issue: 415 Unsupported Media Type**
- **Solution**: Set `Content-Type: application/json` header for POST/PUT requests

### **Issue: 400 Bad Request**
- **Solution**: Check JSON syntax and required fields
- **Common causes**: Missing required fields, invalid email format, invalid dates

### **Issue: CSV File Not Found**
- **Solution**: Create the `src/main/resources/data/` directory manually
- **Application will create it automatically, but ensure write permissions**

---

## 📝 **Test Results Template**

Use this template to document your test results:

```
Test Date: ___________
Tester: ______________

Client API Tests:
□ GET All Clients - Status: ____ Pass/Fail
□ GET Client by ID - Status: ____ Pass/Fail
□ POST Create Client - Status: ____ Pass/Fail
□ PUT Update Client - Status: ____ Pass/Fail
□ DELETE Client - Status: ____ Pass/Fail
□ Export CSV - Status: ____ Pass/Fail

Membership API Tests:
□ GET All Memberships - Status: ____ Pass/Fail
□ POST Create Membership - Status: ____ Pass/Fail
□ GET Active Memberships - Status: ____ Pass/Fail
□ Auto Membership Creation - Status: ____ Pass/Fail

Error Handling Tests:
□ Invalid Client Data - Status: ____ Pass/Fail
□ Invalid Membership Data - Status: ____ Pass/Fail

Notes/Observations:
__________________________________________________
__________________________________________________
```
