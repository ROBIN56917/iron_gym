package co.edu.umanizales.iron_gym.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/iron-gym")
public class IronGymController {

    @GetMapping("/saludo")
    public String saludo() {
        return "Bienvenido a Iron Gym 🏋️‍♂️";
    }

    @GetMapping("/info")
    public String info() {
        return "Iron Gym API activa y lista para entrenar 💪";
    }
}
