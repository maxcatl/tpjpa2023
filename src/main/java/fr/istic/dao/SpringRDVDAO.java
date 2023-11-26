package fr.istic.dao;

import fr.istic.domain.RDV;
import io.micrometer.common.lang.NonNullApi;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Transactional
@NonNullApi
public interface SpringRDVDAO extends JpaRepository<RDV, Long>
{
    RDV findById(final long id);

    List<RDV> findAll();

    RDV save(final RDV entity);

    void delete(final RDV entity);

    void deleteById(final long entityId);

    @Query("select r from RDV as r where r.eleve.id = ?1")
    List<RDV> findAllWithEleve(final long eleveId);

    @Query("select r from RDV as r where r.professeur.id = ?1")
    List<RDV> findAllWithProfesseur (final long professeurId);
}