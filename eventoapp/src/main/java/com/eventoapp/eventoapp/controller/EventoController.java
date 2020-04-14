package com.eventoapp.eventoapp.controller;

import com.eventoapp.eventoapp.models.Convidado;
import com.eventoapp.eventoapp.models.Evento;
import com.eventoapp.eventoapp.repository.ConvidadoRepository;
import com.eventoapp.eventoapp.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class EventoController {
    @Autowired
    private EventoRepository er;
    @Autowired
    private ConvidadoRepository cr;

    @RequestMapping(value = "/cadastrarEvento", method = RequestMethod.GET)
    public String form() {
        return "evento/formEvento";
    }

    @RequestMapping(value = "/cadastrarEvento", method = RequestMethod.POST)
    public ResponseEntity<String> form(@RequestBody Evento evento ){
        er.save(evento);
        return ResponseEntity.ok("redirect:/cadastrarEvento");

    }

      @RequestMapping("/eventos")
    public ResponseEntity listaEventos() {
        Iterable<Evento> eventos = er.findAll();
        return ResponseEntity.ok(eventos);
    }
@GetMapping("/eventos/{codigo}")
public ResponseEntity eventosEspecificos(@PathVariable("codigo") long codigo) {
    Evento evento = er.findByCodigo(codigo);
    return ResponseEntity.ok(evento);
}

    @GetMapping("/convidados/{codigo}")
    public ResponseEntity detalhesEvento(@PathVariable("codigo") long codigo) {
        Evento evento = er.findByCodigo(codigo);
        Iterable<Convidado> convidados = cr.findByEvento(evento);
            return ResponseEntity.ok(convidados);
    }

    @RequestMapping(value = "cad/{codigo}", method = RequestMethod.POST)
    public ResponseEntity detalhesEventoPost(@PathVariable("codigo") long codigo, Convidado convidado) {

        Evento evento = er.findByCodigo(codigo);
        convidado.setEvento(evento);
        cr.save(convidado);
        return ResponseEntity.ok("redirect:/{codigo}");
    }








    @DeleteMapping("/deletar")
    public String deletarEvento(@RequestHeader long codigo) {
        Evento evento = er.findByCodigo(codigo);
        er.delete(evento);
        return "localhost:3000/listarEventos";
    }

    @RequestMapping("/deletarConvidado")
    public String deletarConvidado(String rg) {
        Convidado convidado = cr.findByRg(rg);
        cr.delete(convidado);
        Evento evento = convidado.getEvento();
        long codigoLong = evento.getCodigo();
        String codigo = "" + codigoLong;
        return "redirect: /" + codigo;

    }

}
