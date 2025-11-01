package co.edu.umanizales.iron_gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador principal de la aplicación Iron Gym.
 * Proporciona endpoints básicos para verificar el estado de la API.
 */
@Tag(
    name = "Iron Gym API",
    description = "Endpoints principales para la gestión del gimnasio"
)
@RestController
@RequestMapping("/api/v1")
public class IronGymController {

    /**
     * Endpoint de bienvenida a la API de Iron Gym.
     * @return Mensaje de bienvenida
     */
    @Operation(
        summary = "Obtener mensaje de bienvenida",
        description = "Retorna un mensaje de bienvenida de la API"
    )
    @GetMapping("/welcome")
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok("Bienvenido a Iron Gym API 🏋️‍♂️");
    }

    /**
     * Endpoint de verificación de estado.
     * @return Estado actual de la API
     */
    @Operation(
        summary = "Verificar estado del servicio",
        description = "Retorna el estado actual de la API"
    )
    @GetMapping("/status")
    public ResponseEntity<ApiResponse> checkStatus() {
        return ResponseEntity.ok(
            new ApiResponse(
                "success",
                "Iron Gym API está activa y funcionando correctamente ✅",
                null
            )
        );
    }

    /**
     * Clase interna para respuestas estandarizadas de la API.
     */
    private static class ApiResponse {
        private final String status;
        private final String message;
        private final Object data;

        public ApiResponse(String status, String message, Object data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }

        // Getters
        public String getStatus() { return status; }
        public String getMessage() { return message; }
        public Object getData() { return data; }
    }
}
