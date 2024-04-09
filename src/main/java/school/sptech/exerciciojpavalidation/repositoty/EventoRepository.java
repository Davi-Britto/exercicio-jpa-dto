package school.sptech.exerciciojpavalidation.repositoty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import school.sptech.exerciciojpavalidation.dto.EventoListagemDto;
import school.sptech.exerciciojpavalidation.entity.Evento;

import java.time.LocalDate;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Integer> {
    List<Evento> findAllByGratuitoTrue();
    List<Evento> findByDataEventoIs(LocalDate data);
    List<Evento> findByDataPublicacaoIs(LocalDate data);
    List<Evento>findAllByDataEventoBetween(LocalDate inicio, LocalDate fim);
}
