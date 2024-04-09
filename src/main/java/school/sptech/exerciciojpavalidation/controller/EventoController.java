package school.sptech.exerciciojpavalidation.controller;

import jakarta.validation.Valid;
import jdk.jfr.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.exerciciojpavalidation.dto.EventoAtualizacaoDto;
import school.sptech.exerciciojpavalidation.dto.EventoCriacaoDto;
import school.sptech.exerciciojpavalidation.dto.EventoListagemDto;
import school.sptech.exerciciojpavalidation.dto.EventoMapper;
import school.sptech.exerciciojpavalidation.entity.Evento;
import school.sptech.exerciciojpavalidation.repositoty.EventoRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    @Autowired
    private EventoRepository eventoRepository;

    @PostMapping
    public ResponseEntity<EventoListagemDto> criar(@RequestBody @Valid EventoCriacaoDto novoEvento){
        Evento evento = EventoMapper.toEntity(novoEvento);

        Evento eventoSalvo = eventoRepository.save(evento);

        EventoListagemDto listagemDto = EventoMapper.toDto(eventoSalvo);

        return ResponseEntity.status(201).body(listagemDto);
    }

    @GetMapping
    public ResponseEntity<List<EventoListagemDto>> listar(){
        List<Evento> eventos = eventoRepository.findAll();

        if(eventos.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<EventoListagemDto> listaAuxiliar = EventoMapper.toDto(eventos);
        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoListagemDto> buscaPorId(@PathVariable int id){
        Optional<Evento> eventoOpt = eventoRepository.findById(id);

        if(eventoOpt.isEmpty()){
            return ResponseEntity.status(404).build();
        }

        EventoListagemDto dto = EventoMapper.toDto(eventoOpt.get());

        return ResponseEntity.status(200).body(dto);
    }

    @GetMapping("/gratuitos")
    public ResponseEntity<List<EventoListagemDto>> listarEventosGratuitos(){
        List<Evento> eventosGratuitos = eventoRepository.findAllByGratuitoTrue();

        if(eventosGratuitos.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<EventoListagemDto> dto = EventoMapper.toDto(eventosGratuitos);

        return ResponseEntity.status(200).body(dto);
    }

    @GetMapping("/dataOcorrencia")
    public ResponseEntity<List<EventoListagemDto>> listarEventosPorData(@RequestParam LocalDate data){
        List<Evento> eventosPorDataEvento = eventoRepository.findByDataEventoIs(data);
        List<Evento> eventosPorDataPublicacao = eventoRepository.findByDataPublicacaoIs(data);

        if(!eventosPorDataEvento.isEmpty()){
            List<EventoListagemDto> dto = EventoMapper.toDto(eventosPorDataEvento);
            return ResponseEntity.status(200).body(dto);
        } else if(!eventosPorDataPublicacao.isEmpty()){
            List<EventoListagemDto> dto = EventoMapper.toDto(eventosPorDataPublicacao);
            return ResponseEntity.status(200).body(dto);
        } else{
            return ResponseEntity.status(204).build();
        }

    }

    @GetMapping("/periodo")
    public ResponseEntity<List<EventoListagemDto>> listarEventosPorPeriodo(@RequestParam LocalDate inicio, @RequestParam LocalDate fim){
        if(inicio.isAfter(fim)){
            return ResponseEntity.status(400).build();
        }
        List<Evento> eventosPorPeriodo = eventoRepository.findAllByDataEventoBetween(inicio, fim);

        if(eventosPorPeriodo.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        List<EventoListagemDto> dto = EventoMapper.toDto(eventosPorPeriodo);

        return ResponseEntity.status(200).body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id){
        Optional<Evento> eventoOpt = eventoRepository.findById(id);

        if(eventoOpt.isEmpty()){
            return ResponseEntity.status(404).build();
        }

        eventoRepository.delete(eventoOpt.get());
        return ResponseEntity.status(204).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoListagemDto> atualizar(@PathVariable int id, @RequestBody @Valid EventoAtualizacaoDto eventoAtualizado){
        Optional<Evento> eventoOpt = eventoRepository.findById(id);

        if(eventoOpt.isEmpty()){
            return ResponseEntity.status(404).build();
        }

        LocalDate hoje = LocalDate.now();

        if(eventoOpt.get().isCancelado() || hoje.isAfter(eventoOpt.get().getDataEvento())){
            return ResponseEntity.status(422).build();
        }

        Evento entity = EventoMapper.toEntityAtualizacao(id, eventoAtualizado);

        Evento eventoCadastrado = eventoRepository.save(entity);

        EventoListagemDto listagemDto = EventoMapper.toDto(eventoCadastrado);

        return ResponseEntity.status(200).body(listagemDto);
    }

    @PatchMapping("/{id}/cancelamento")
    public ResponseEntity<EventoListagemDto> cancelar(@PathVariable int id){
        Optional<Evento> eventoOpt = eventoRepository.findById(id);

        if(eventoOpt.isEmpty()){
            return ResponseEntity.status(404).build();
        }

        Evento evento = eventoOpt.get();

        LocalDate hoje = LocalDate.now();

        if (evento.isCancelado() || hoje.isAfter(evento.getDataEvento())) {
            return ResponseEntity.status(422).build();
        }

        evento.setCancelado(true);

        eventoRepository.save(evento);

        EventoListagemDto listagemDto = EventoMapper.toDto(evento);

        return ResponseEntity.status(204).body(listagemDto);
    }
}
