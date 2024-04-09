package school.sptech.exerciciojpavalidation.dto;

import school.sptech.exerciciojpavalidation.entity.Evento;

import java.util.List;

public class EventoMapper {
    public static Evento toEntity(EventoCriacaoDto dto){
        if(dto == null){
            return null;
        }

        Evento evento = new Evento();
        evento.setNome(dto.getNome());
        evento.setLocal(dto.getLocal());
        evento.setDataEvento(dto.getDataEvento());
        evento.setGratuito(dto.isGratuito());

        return evento;
    }

    public static EventoListagemDto toDto(Evento entity){
        if(entity == null) return null;

        EventoListagemDto listagemDto = new EventoListagemDto();
        listagemDto.setId(entity.getId());
        listagemDto.setNome(entity.getNome());
        listagemDto.setLocal(entity.getLocal());
        listagemDto.setDataEvento(entity.getDataEvento());
        listagemDto.setGratuito(entity.isGratuito());
        listagemDto.setCancelado(entity.isCancelado());
        listagemDto.setDataPublicacao(entity.getDataPublicacao());

        return listagemDto;
    }

    // COnveter umas lista para DTO
    public static List<EventoListagemDto> toDto(List<Evento> entities){
        return entities.stream().map(EventoMapper::toDto).toList();
    }

    // Atualizar
    public static Evento toEntityAtualizacao(int id, EventoAtualizacaoDto dto){
        if(dto == null){
            return null;
        }

        Evento evento = new Evento();
        evento.setId(id);
        evento.setNome(dto.getNome());
        evento.setLocal(dto.getLocal());
        evento.setDataEvento(dto.getDataEvento());
        evento.setGratuito(dto.isGratuito());
        evento.setDataPublicacao(evento.getDataPublicacao());

        return evento;
    }

    // Cancelar
    public static Evento toEntityCancelamento(int id, EventoAtualizacaoDto dto){
        if(dto == null){
            return null;
        }

        Evento evento = new Evento();
        evento.setId(id);
        evento.setNome(dto.getNome());
        evento.setLocal(dto.getLocal());
        evento.setDataEvento(dto.getDataEvento());
        evento.setGratuito(dto.isGratuito());
        evento.setDataPublicacao(evento.getDataPublicacao());

        return evento;
    }
}
