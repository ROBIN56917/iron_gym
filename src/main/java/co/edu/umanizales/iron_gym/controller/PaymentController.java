package co.edu.umanizales.iron_gym.controller; // Declara el paquete donde se encuentran los controladores REST

import co.edu.umanizales.iron_gym.model.Payment; // Importa la clase Payment del paquete model
import co.edu.umanizales.iron_gym.service.PaymentService; // Importa el servicio de pagos
import co.edu.umanizales.iron_gym.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired; // Importa anotación para inyección de dependencias
import org.springframework.http.HttpStatus; // Importa códigos de estado HTTP
import org.springframework.http.ResponseEntity; // Importa clase para respuestas HTTP
import org.springframework.web.bind.annotation.*; // Importa anotaciones para controladores REST

import java.util.List; // Importa la interfaz List para trabajar con colecciones
import java.util.Map;
import java.util.HashMap;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Comparator;
import java.text.NumberFormat;
import java.util.Locale;

@RestController // Anotación que marca esta clase como un controlador REST
@RequestMapping("/api/payments") // Define la ruta base para todos los endpoints de este controlador
public class PaymentController { // Inicio de la clase PaymentController - maneja peticiones HTTP para pagos
    
    @Autowired // Anotación para inyección automática del servicio
    private PaymentService paymentService; // Servicio que contiene la lógica de negocio para pagos
    
    @Autowired
    private ClientService clientService;

    @GetMapping // Anotación que mapea peticiones HTTP GET a este método
    public ResponseEntity<List<Payment>> getAll() { // Método para obtener todos los pagos
        return ResponseEntity.ok(paymentService.getAll()); // Retorna respuesta HTTP 200 con la lista de pagos
    }

    @GetMapping("/by-client/{clientId}")
    public ResponseEntity<List<Payment>> getByClient(@PathVariable String clientId) {
        return ResponseEntity.ok(paymentService.getByClientId(clientId));
    }

    @GetMapping("/methods")
    public ResponseEntity<List<String>> getMethods() {
        return ResponseEntity.ok(paymentService.getMethods());
    }

    @GetMapping("/{id}") // Anotación que mapea peticiones HTTP GET con parámetro de ruta
    public ResponseEntity<Payment> getById(@PathVariable String id) { // Método para obtener pago por ID
        Payment payment = paymentService.getById(id);
        if (payment != null) {
            return ResponseEntity.ok(payment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Payment payment, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        Payment created = paymentService.create(payment);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody Payment payment, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        Payment updated = paymentService.update(id, payment);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}") // Anotación que mapea peticiones HTTP DELETE con parámetro de ruta
    public ResponseEntity<Void> delete(@PathVariable String id) { // Método para eliminar pago por ID
        boolean deleted = paymentService.delete(id); // Elimina el pago usando el servicio
        if (deleted) { // Si se eliminó correctamente
            return ResponseEntity.noContent().build(); // Retorna respuesta HTTP 204 (sin contenido)
        } else { // Si no se encontró el pago para eliminar
            return ResponseEntity.notFound().build(); // Retorna respuesta HTTP 404 (no encontrado)
        }
    }
    
    @GetMapping("/report")
    public ResponseEntity<?> getReport(
            @RequestParam(value = "start", required = false) String start,
            @RequestParam(value = "end", required = false) String end,
            @RequestParam(value = "method", required = false) String method
    ) {
        LocalDate startDate = null;
        LocalDate endDate = null;
        // Intentar parsear fechas si llegan en formato yyyy-MM-dd, pero no fallar si vienen mal
        if (start != null && !start.isBlank()) {
            try { startDate = LocalDate.parse(start.trim()); } catch (Exception ignored) {}
        }
        if (end != null && !end.isBlank()) {
            try { endDate = LocalDate.parse(end.trim()); } catch (Exception ignored) {}
        }

        // Obtener rango mínimo y máximo de fechas desde los pagos existentes
        LocalDate minDate = null;
        LocalDate maxDate = null;
        for (Payment p : paymentService.getAll()) {
            if (p.getDateTime() == null) continue;
            LocalDate d = p.getDateTime().toLocalDate();
            if (minDate == null || d.isBefore(minDate)) minDate = d;
            if (maxDate == null || d.isAfter(maxDate)) maxDate = d;
        }

        // Defaults: si no hay pagos, usar hoy para ambos extremos
        LocalDate today = LocalDate.now();
        if (startDate == null) startDate = (minDate != null) ? minDate : today;
        if (endDate == null) endDate = (maxDate != null) ? maxDate : startDate;
        // Corregir orden si viene invertido
        if (endDate.isBefore(startDate)) {
            LocalDate tmp = startDate; startDate = endDate; endDate = tmp;
        }

        // Normalizar/validar filtro de método: ignorar si no es permitido
        String methodFilter = null;
        if (method != null && !method.isBlank()) {
            String m = method.trim().toUpperCase();
            List<String> allowed = paymentService.getMethods();
            if (allowed != null && allowed.contains(m)) {
                methodFilter = m;
            }
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("fecha_inicial", startDate.toString());
        result.put("fecha_final", endDate.toString());

        Map<LocalDate, Map<String, List<Map<String, Object>>>> grouped = new LinkedHashMap<>();

        for (Payment p : paymentService.getAll()) {
            if (p.getDateTime() == null) continue;
            LocalDate d = p.getDateTime().toLocalDate();
            if (d.isBefore(startDate) || d.isAfter(endDate)) continue;
            if (methodFilter != null) {
                String pm = p.getPaymentMethod() == null ? null : p.getPaymentMethod().toUpperCase();
                if (pm == null || !pm.equals(methodFilter)) continue;
            }
            grouped.computeIfAbsent(d, k -> new LinkedHashMap<>());
            String clientId = p.getClientId();
            grouped.get(d).computeIfAbsent(clientId == null ? "" : clientId, k -> new ArrayList<>());
            Map<String, Object> pago = new LinkedHashMap<>();
            pago.put("medio_pago", p.getPaymentMethod());
            long amount = Math.round(p.getAmount());
            pago.put("valor", amount);
            pago.put("valor_formateado", formatCOP(amount));
            grouped.get(d).get(clientId == null ? "" : clientId).add(pago);
        }

        List<Map<String, Object>> reporte = new ArrayList<>();
        grouped.keySet().stream().sorted().forEach(date -> {
            Map<String, List<Map<String, Object>>> clientsMap = grouped.get(date);
            List<Map<String, Object>> clientesArr = new ArrayList<>();
            double totalFecha = 0;
            for (Map.Entry<String, List<Map<String, Object>>> entry : clientsMap.entrySet()) {
                String cid = entry.getKey();
                String name = null;
                if (cid != null && !cid.isBlank()) {
                    var c = clientService.getById(cid);
                    name = c != null ? c.getName() : null;
                }
                List<Map<String, Object>> pagos = entry.getValue();
                double totalCliente = 0;
                for (Map<String, Object> pg : pagos) {
                    Object v = pg.get("valor");
                    if (v instanceof Number) {
                        totalCliente += ((Number) v).doubleValue();
                    }
                }
                totalFecha += totalCliente;
                Map<String, Object> clienteObj = new LinkedHashMap<>();
                clienteObj.put("cliente", name);
                clienteObj.put("pagos", pagos);
                long totalClienteLong = Math.round(totalCliente);
                clienteObj.put("total_cliente", totalClienteLong);
                clienteObj.put("total_cliente_formateado", formatCOP(totalClienteLong));
                clientesArr.add(clienteObj);
            }
            Map<String, Object> fechaObj = new LinkedHashMap<>();
            fechaObj.put("fecha", date.toString());
            fechaObj.put("clientes", clientesArr);
            long totalFechaLong = Math.round(totalFecha);
            fechaObj.put("total_fecha", totalFechaLong);
            fechaObj.put("total_fecha_formateado", formatCOP(totalFechaLong));
            reporte.add(fechaObj);
        });

        result.put("reporte", reporte);
        return ResponseEntity.ok(result);
    }

    private String formatCOP(long amount) {
        try {
            NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
            nf.setMaximumFractionDigits(0);
            nf.setMinimumFractionDigits(0);
            String formatted = nf.format(amount);
            return formatted.replaceAll("\\s+", "");
        } catch (Exception e) {
            return "$" + String.format("%,d", amount).replace(',', '.');
        }
    }
} // Fin de la clase PaymentController
