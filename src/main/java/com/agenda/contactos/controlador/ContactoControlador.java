package com.agenda.contactos.controlador;

import com.agenda.contactos.modelo.Contacto;
import com.agenda.contactos.repositorio.ContactoRepositorio;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ContactoControlador {


    @Autowired
    private ContactoRepositorio contactoRepositorio;

    @GetMapping({"/",""})
    public String verPaginaDeInicio(Model modelo){
        List<Contacto> contactos = contactoRepositorio.findAll();
        modelo.addAttribute("contactos", contactos);
        return "index";

    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeRegistrarContacto(Model modelo){
            modelo.addAttribute("contacto", new Contacto());
            return "nuevo";
    }

    @PostMapping("/nuevo")
    public String guardarContacto(@Validated Contacto contacto, BindingResult bindingResult, RedirectAttributes redirect, Model modelo){
        if(bindingResult.hasErrors()){
            modelo.addAttribute("contacto", contacto);
            return "nuevo";
        }

        contactoRepositorio.save(contacto);
        redirect.addAttribute("msgExito", "El contacto ha sido agregado con éxito");
        return "redirect:/";
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioDeEditarContacto(@PathVariable Integer id, Model modelo){
        Contacto contacto = contactoRepositorio.getById(id);
        modelo.addAttribute("contacto", contacto);
        return "nuevo";
    }

    @PostMapping("/{id}/editar")
    public String actualizarContacto(@PathVariable Integer id, @Validated Contacto contacto, BindingResult bindingResult, RedirectAttributes redirect, Model modelo){
        Contacto contactoDB = contactoRepositorio.getById(id);

       if(bindingResult.hasErrors()){
            modelo.addAttribute("contacto", contacto);
            return "nuevo";
        }
       contactoDB.setNombre(contacto.getNombre());
       contactoDB.setCelular(contacto.getCelular());
       contactoDB.setEmail(contacto.getEmail());
       contactoDB.setFechaNacimiento(contacto.getFechaNacimiento());

        contactoRepositorio.save(contacto);
        redirect.addFlashAttribute("msgExito", "El contacto ha sido actualizado correctamente");
        return "redirect:/";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarContacto(@PathVariable Integer id, RedirectAttributes redirectAttributes){
        Contacto contacto = contactoRepositorio.getById(id);
        contactoRepositorio.delete(contacto);
        redirectAttributes.addFlashAttribute("msgExito", "El contacto ha sido eliminado correctamente");
        return "redirect:/";
    }

}
