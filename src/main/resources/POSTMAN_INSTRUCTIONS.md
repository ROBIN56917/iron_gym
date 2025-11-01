# Instrucciones para Importar en Postman

## Archivos generados

- `Iron_Gym_API.postman_collection.json` - Colección con todos los endpoints
- `Iron_Gym_Environment.postman_environment.json` - Variables de entorno

## Pasos para importar

### 1. Importar la Colección

1. Abre Postman
2. Click en el botón **Import** (esquina superior izquierda)
3. Selecciona el archivo `Iron_Gym_API.postman_collection.json`
4. Click en **Import**

### 2. Importar el Environment (Opcional pero recomendado)

1. Click en el ícono de engranaje ⚙️ (Environments, esquina superior derecha)
2. Click en **Import**
3. Selecciona el archivo `Iron_Gym_Environment.postman_environment.json`
4. Click en **Import**
5. Selecciona el environment "Iron Gym - Local" en el dropdown superior derecho

## Endpoints incluidos

### Instructors
- ✅ GET - Obtener todos los instructores
- ✅ GET - Obtener instructor por ID
- ✅ POST - Crear instructor
- ✅ PUT - Actualizar instructor
- ✅ DELETE - Eliminar instructor

### Activities
- ✅ GET - Obtener todas las actividades
- ✅ GET - Obtener actividad por ID
- ✅ GET - Obtener actividades por instructor
- ✅ POST - Crear actividad
- ✅ PUT - Actualizar actividad
- ✅ DELETE - Eliminar actividad

### Health Check
- ✅ GET - Verificar que la API esté funcionando

## Variables de colección

- `base_url`: http://localhost:9000
- `instructor_id`: ID del instructor (copiar del response después de crear uno)
- `activity_id`: ID de la actividad (copiar del response después de crear una)

## Flujo de prueba recomendado

1. **Verificar API**: Ejecutar "Health Check"
2. **Crear Instructor**: POST /api/instructors
   - Copiar el `id` del response
   - Guardar en variable `instructor_id`
3. **Listar Instructores**: GET /api/instructors
4. **Crear Actividad**: POST /api/activities
   - Usar el `instructor_id` en el body
   - Copiar el `id` del response
   - Guardar en variable `activity_id`
5. **Listar Actividades**: GET /api/activities
6. **Consultar actividad específica**: GET /api/activities/{activity_id}
7. **Actualizar datos**: PUT /api/instructors/{id} o PUT /api/activities/{id}
8. **Eliminar**: DELETE según sea necesario

## Ejemplos de JSON

### Crear Instructor
```json
{
  "name": "John Doe",
  "email": "john.doe@gym.com",
  "phone": "3001234567",
  "specialization": "Yoga"
}
```

### Crear Actividad
```json
{
  "name": "Morning Yoga",
  "description": "Relaxing yoga session for beginners",
  "schedule": "Monday 08:00-09:00",
  "instructorId": "usar-id-del-instructor-creado",
  "maxCapacity": 20
}
```

## Notas importantes

- Asegúrate de que la aplicación Spring Boot esté ejecutándose en el puerto 9000
- Los IDs se generan automáticamente (UUID)
- Para actualizar, necesitas el ID del recurso existente
- Las actividades requieren un `instructorId` válido
