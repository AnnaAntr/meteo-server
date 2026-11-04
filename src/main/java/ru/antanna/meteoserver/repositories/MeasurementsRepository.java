package ru.antanna.meteoserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.antanna.meteoserver.models.Measurement;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface MeasurementsRepository extends JpaRepository<Measurement, Long> {

//    List<Measurement> findByReceivedAtAfterOrderByReceivedAtAsc(Instant since);

    List<Measurement> findByReceivedAtBetweenOrderByReceivedAtAsc(Instant from, Instant to);

    List<Measurement> findTop50ByOrderByReceivedAtDesc();

    Optional<Measurement> findFirstByOrderByReceivedAtDesc();
}
