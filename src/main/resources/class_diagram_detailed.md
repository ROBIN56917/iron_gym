# Iron Gym - Class Diagram Documentation

This document explains all the classes in the Iron Gym system in simple terms for beginners.

## Class Relationships Overview

```
Person (Parent Class)
├── Client (Child Class)
└── Trainer (Child Class)

Main Classes:
- Attendance: Tracks when clients attend classes
- Client: Gym members with memberships
- Trainer: Gym employees who teach classes
- GroupClass: Fitness classes offered at the gym
- Membership: Client membership information
- Routine: Workout plans for clients
- Exercise: Individual exercises in routines
- Equipment: Gym machines and tools
- Payment: Payment records
- Supplement: Products sold at the gym
- Reservation: Class reservations

Enums (Fixed Values):
- MembershipTypeEnum: BASIC, PREMIUM, VIP
- EquipmentStatus: ACTIVE, MAINTENANCE, OUT_OF_SERVICE
- PaymentMethod: CASH, CARD, TRANSFER
```

## Detailed Class Descriptions

### 1. Person (Base Class)
**Purpose**: Base class for all people in the system
**Fields**:
- `id`: Unique identifier (String)
- `name`: Full name (String)
- `email`: Email address (String)
- `phone`: Phone number (String)
**Methods**:
- `getRole()`: Returns "PERSON" (overridden by child classes)

### 2. Client (Child of Person)
**Purpose**: Represents gym members
**Additional Fields**:
- `membership`: The client's membership (Membership object)
**Methods**:
- `getRole()`: Returns "CLIENT"

### 3. Trainer (Child of Person)
**Purpose**: Represents gym employees who teach classes
**Additional Fields**:
- `assignedClasses`: List of classes this trainer teaches (List<GroupClass>)
- `specialization`: What the trainer specializes in (String)
**Methods**:
- `addClass()`: Adds a class to trainer's schedule
- `getRole()`: Returns "TRAINER"

### 4. Attendance
**Purpose**: Tracks when clients attend classes
**Fields**:
- `id`: Unique identifier (String)
- `checkInDateTime`: When the client checked in (LocalDateTime)
- `client`: The client who attended (Client object)
- `groupClass`: The class that was attended (GroupClass object)
- `present`: Whether the client was present (boolean)

### 5. GroupClass
**Purpose**: Represents fitness classes offered at the gym
**Fields**:
- `id`: Unique identifier (String)
- `name`: Class name (String)
- `maxCapacity`: Maximum number of clients (int)
- `schedule`: When the class occurs (LocalTime)
- `trainer`: The instructor (Trainer object)
- `registeredClients`: Clients signed up for the class (List<Client>)
**Methods**:
- `addClient()`: Adds a client if there's space available

### 6. Membership
**Purpose**: Represents client membership information
**Fields**:
- `id`: Unique identifier (String)
- `type`: Type of membership (MembershipTypeEnum: BASIC, PREMIUM, VIP)
- `startDate`: When membership begins (LocalDate)
- `endDate`: When membership ends (LocalDate)
- `price`: Cost of membership (double)

### 7. MembershipTypeEnum (Enum)
**Purpose**: Fixed types of memberships available
**Values**:
- `BASIC`: Basic gym access
- `PREMIUM`: Includes additional features
- `VIP`: All-inclusive membership

### 8. Routine
**Purpose**: Workout plans created for clients
**Fields**:
- `id`: Unique identifier (String)
- `objective`: Goal of the routine (String)
- `exercises`: List of exercises in the routine (List<Exercise>)
- `difficultyLevel`: How hard the routine is (String)
- `durationMinutes`: How long the routine takes (int)
**Methods**:
- `addExercise()`: Adds an exercise to the routine

### 9. Exercise
**Purpose**: Individual exercises that make up routines
**Fields**:
- `name`: Exercise name (String)
- `repetitions`: Number of times to repeat (int)
- `sets`: Number of groups of repetitions (int)
- `restBetweenSets`: Rest time between sets (String)
- `instructions`: How to perform the exercise (String)
- `muscleGroup`: Which muscles this works (String)

### 10. Equipment
**Purpose**: Gym equipment like machines and weights
**Fields**:
- `id`: Unique identifier (String)
- `type`: What kind of equipment (String)
- `status`: Current condition (EquipmentStatus)
- `description`: Details about the equipment (String)

### 11. EquipmentStatus (Enum)
**Purpose**: Fixed status values for equipment
**Values**:
- `ACTIVE`: Available for use
- `MAINTENANCE`: Being repaired/serviced
- `OUT_OF_SERVICE`: Broken or unavailable

### 12. Payment
**Purpose**: Records of payments made by clients
**Fields**:
- `id`: Unique identifier (String)
- `amount`: How much was paid (double)
- `paymentDateTime`: When payment was made (LocalDateTime)
- `paymentMethod`: How payment was made (PaymentMethod)
- `reference`: Transaction reference number (String)
- `description`: What the payment was for (String)

### 13. PaymentMethod (Enum)
**Purpose**: Fixed ways clients can pay
**Values**:
- `CASH`: Physical money
- `CARD`: Credit/debit card
- `TRANSFER`: Bank transfer

### 14. Supplement
**Purpose**: Nutritional products sold at the gym
**Fields**:
- `id`: Unique identifier (String)
- `name`: Product name (String)
- `brand`: Manufacturer (String)
- `price`: Cost (double)
- `description`: Product details (String)
- `availableQuantity`: How many in stock (int)
- `type`: Category (protein, vitamins, etc.) (String)

### 15. Reservation
**Purpose**: Client reservations for group classes
**Fields**:
- `id`: Unique identifier (String)
- `dateTime`: When the reservation was made (LocalDateTime)
- `client`: Who made the reservation (Client object)
- `groupClass`: What class was reserved (GroupClass object)
- `active`: Whether reservation is still valid (boolean)
- `createdAt`: When reservation was created (LocalDateTime)
**Methods**:
- `cancel()`: Cancels the reservation

## How Classes Work Together

1. **Client Management**: Clients have memberships and can make reservations for classes
2. **Class System**: Trainers teach GroupClasses, Clients attend them (recorded in Attendance)
3. **Fitness Tracking**: Trainers create Routines with Exercises for Clients
4. **Equipment Management**: Equipment has status that changes over time
5. **Payment System**: Clients make Payments for memberships using different PaymentMethods
6. **Inventory**: Supplements track stock levels and pricing

## Data Types Used

- **String**: Text values (names, descriptions, IDs)
- **int**: Whole numbers (capacity, quantity, repetitions)
- **double**: Decimal numbers (prices, amounts)
- **boolean**: True/false values (present, active)
- **LocalDate**: Date without time (membership dates)
- **LocalTime**: Time without date (class schedules)
- **LocalDateTime**: Date and time together (payments, attendance)
- **List<T>**: Collections of objects (clients in a class, exercises in a routine)

## For LucidChart Implementation

When creating your class diagram in LucidChart:

1. Create boxes for each class with the class name at the top
2. List fields under the class name with their data types
3. List methods at the bottom of each class box
4. Draw inheritance arrows from Client and Trainer to Person
5. Draw association lines showing relationships (Client → Membership, Trainer → GroupClass, etc.)
6. Use different line styles to show composition (strong ownership) vs aggregation (weak ownership)
7. Include enum classes with their values listed

This structure provides a clean, organized system that's easy to understand and maintain!
