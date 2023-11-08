package fr.istic.dao;

import fr.istic.domain.Professeur;
import io.micrometer.common.lang.NonNullApi;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Transactional
@NonNullApi
public interface SpringProfesseurDAO extends JpaRepository<Professeur, Long>
{
    Professeur findById(final long id);

    List<Professeur> findAll();

    Professeur findByEmail(final String email);

    Professeur save(final Professeur entity);

    void delete(final Professeur entity);

    void deleteById(final long entityId);
}
