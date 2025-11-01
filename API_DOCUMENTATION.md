# Iron Gym API Documentation

API REST para gestión de gimnasio con Spring Boot.

## Características

- **Patrón MVC** (Model-View-Controller)
- **Herencia POO**: `Instructor` hereda de `Person`
- **Persistencia en CSV**: Archivos separados por comas
- **Principios**: SOLID, KISS, DRY
- **Nomenclatura**: Inglés

## Arquitectura

```
src/main/java/co/edu/umanizales/iron_gym/
├── model/           # Entidades de dominio
│   ├── Person.java         (clase base)
│   ├── Instructor.java     (hereda de Person)
│   └── Activity.java
├── service/         # Lógica de negocio y persistencia CSV
│   ├── InstructorService.java
│   └── ActivityService.java
├── controller/      # Endpoints REST
│   ├── InstructorController.java
│   └── ActivityController.java
└── dto/            # Data Transfer Objects
    ├── InstructorDTO.java
    └── ActivityWithInstructorDTO.java
```

## Endpoints API

### Instructores

#### GET /api/instructors
Obtiene todos los instructores.
```json
[
  {
    "id": "uuid",
    "name": "John Doe",
    "email": "john@gym.com",
    "phone": "123456789",
    "specialization": "Yoga"
  }
]
```

#### GET /api/instructors/{id}
Obtiene un instructor por ID.

#### POST /api/instructors
Crea un nuevo instructor.
```json
{
  "name": "John Doe",
  "email": "john@gym.com",
  "phone": "123456789",
  "specialization": "Yoga"
}
```

#### PUT /api/instructors/{id}
Actualiza un instructor existente.

#### DELETE /api/instructors/{id}
Elimina un instructor.

---

### Actividades

#### GET /api/activities
Obtiene todas las actividades con información del instructor.
```json
[
  {
    "id": "uuid",
    "name": "Morning Yoga",
    "description": "Relaxing yoga session",
    "schedule": "Monday 08:00-09:00",
    "maxCapacity": 20,
    "instructor": {
      "id": "uuid",
      "name": "John Doe",
      "email": "john@gym.com",
      "phone": "123456789",
      "specialization": "Yoga"
    }
  }
]
```

#### GET /api/activities/{id}
Obtiene una actividad por ID con su instructor.

#### GET /api/activities/instructor/{instructorId}
Obtiene todas las actividades de un instructor específico.

#### POST /api/activities
Crea una nueva actividad.
```json
{
  "name": "Morning Yoga",
  "description": "Relaxing yoga session",
  "schedule": "Monday 08:00-09:00",
  "instructorId": "uuid-del-instructor",
  "maxCapacity": 20
}
```

#### PUT /api/activities/{id}
Actualiza una actividad existente.

#### DELETE /api/activities/{id}
Elimina una actividad.

## Persistencia CSV

Los datos se guardan en archivos CSV:
- `src/main/resources/data/instructors.csv`
- `src/main/resources/data/activities.csv`

### Formato CSV

**instructors.csv:**
```
id,name,email,phone,specialization
uuid1,John Doe,john@gym.com,123456789,Yoga
```

**activities.csv:**
```
id,name,description,schedule,instructorId,maxCapacity
uuid1,Morning Yoga,Relaxing session,Monday 08:00-09:00,instructor-uuid,20
```

## Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La API estará disponible en: `http://localhost:9000`

## Principios aplicados

- **SOLID**: Separación de responsabilidades (Controller, Service, Model)
- **KISS**: Código simple y directo sin complejidad innecesaria
- **DRY**: Reutilización de código (métodos `toCsv()`, `fromCsv()`)
- **Herencia**: `Instructor extends Person`
- **MVC**: Modelo-Vista-Controlador bien definido
