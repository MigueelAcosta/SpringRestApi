package aplicacion.controllers;

import aplicacion.entidades.CatEstatus;
import aplicacion.servicios.CatalogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CatalogoController {
    @Autowired
    CatalogoService catalogoService;

    @GetMapping("/estatus")
    public List<CatEstatus> getEstatus() {
        return catalogoService.getCatEstatus();
    }
}
