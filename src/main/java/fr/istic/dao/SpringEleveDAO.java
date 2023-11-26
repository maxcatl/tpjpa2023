package fr.istic.dao;

import fr.istic.domain.Eleve;
import io.micrometer.common.lang.NonNullApi;
import jakarta.persistence.Entity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Transactional
@NonNullApi
public interface SpringEleveDAO extends JpaRepository<Eleve, Long>
{
    Eleve findById(final long id);

    List<Eleve> findAll();

    Eleve findByEmail(final String email);

    Eleve save(final Eleve entity);

    void delete(final Eleve entity);

    void deleteById(final long entityId);
}