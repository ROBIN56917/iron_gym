# Iron Gym API - Guía de Pruebas Postman

## Configuración Base
- **URL Base**: `http://localhost:8081`
- **Content-Type**: `application/json`

## Endpoints Disponibles

### 1. Actividades (/api/v1/activities)

#### GET - Listar todas las actividades
```
GET http://localhost:8081/api/v1/activities
```
**Respuesta esperada**: `[]` (lista vacía inicialmente)

#### POST - Crear nueva actividad
```
POST http://localhost:8081/api/v1/activities
Content-Type: application/json

{
    "id": "ACT001",
    "name": "Yoga",
    "description": "Clases de yoga para todos los niveles",
    "schedule": "Lunes y Miércoles 6:00 PM",
    "duration": 60,
    "capacity": 20,
    "instructorId": "INS001"
}
```

#### GET - Obtener actividad por ID
```
GET http://localhost:8081/api/v1/activities/ACT001
```

#### PUT - Actualizar actividad
```
PUT http://localhost:8081/api/v1/activities/ACT001
Content-Type: application/json

{
    "name": "Yoga Avanzado",
    "description": "Clases de yoga nivel avanzado",
    "schedule": "Lunes y Miércoles 7:00 PM",
    "duration": 75,
    "capacity": 15,
    "instructorId": "INS001"
}
```

#### DELETE - Eliminar actividad
```
DELETE http://localhost:8081/api/v1/activities/ACT001
```

#### GET - Actividades por instructor
```
GET http://localhost:8081/api/v1/activities/instructor/INS001
```

### 2. Instructores (/api/v1/instructors)

#### GET - Listar todos los instructores
```
GET http://localhost:8081/api/v1/instructors
```
**Respuesta esperada**: `[]` (lista vacía inicialmente)

#### POST - Crear nuevo instructor
```
POST http://localhost:8081/api/v1/instructors
Content-Type: application/json

{
    "id": "INS001",
    "name": "Juan Pérez",
    "email": "juan@iron_gym.com",
    "specialization": "Entrenamiento Personal",
    "phone": "3001234567"
}
```

#### GET - Obtener instructor por ID
```
GET http://localhost:8081/api/v1/instructors/INS001
```

#### PUT - Actualizar instructor
```
PUT http://localhost:8081/api/v1/instructors/INS001
Content-Type: application/json

{
    "name": "Juan Pérez García",
    "email": "juan.perez@iron_gym.com",
    "specialization": "Entrenamiento Personal y Nutrición",
    "phone": "3001234567"
}
```

#### DELETE - Eliminar instructor
```
DELETE http://localhost:8081/api/v1/instructors/INS001
```

### 3. Endpoints Principales (/api/v1)

#### GET - Mensaje de bienvenida
```
GET http://localhost:8081/api/v1/welcome
```
**Respuesta esperada**: `"Bienvenido a Iron Gym API"`

#### GET - Estado del servidor
```
GET http://localhost:8081/api/v1/status
```
**Respuesta esperada**: `"Servidor Iron Gym funcionando correctamente"`

## Orden Sugerido para Pruebas

1. **Probar endpoints básicos**:
   - GET `/api/v1/welcome`
   - GET `/api/v1/status`

2. **Crear un instructor**:
   - POST `/api/v1/instructors` (con datos de ejemplo)
   - GET `/api/v1/instructors` (verificar que se creó)

3. **Crear una actividad**:
   - POST `/api/v1/activities` (usando el ID del instructor creado)
   - GET `/api/v1/activities` (verificar que se creó)

4. **Probar operaciones CRUD**:
   - GET por ID
   - PUT para actualizar
   - DELETE para eliminar

5. **Probar endpoints adicionales**:
   - GET actividades por instructor

## Notas Importantes

- **NO agregar espacios al final de las URLs** (esto causa el error 404)
- Los IDs deben ser únicos
- Para crear actividades, el instructorId debe existir
- Los datos se persisten en archivos CSV en `src/main/resources/data/`

## Códigos de Respuesta Esperados

- `200 OK` - GET exitoso, PUT exitoso
- `201 Created` - POST exitoso
- `204 No Content` - DELETE exitoso
- `400 Bad Request` - Datos inválidos o instructor no encontrado
- `404 Not Found` - Recurso no encontrado

## Colección Postman

Puedes importar estos endpoints como una colección en Postman para facilitar las pruebas.
