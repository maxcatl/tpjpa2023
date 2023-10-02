package fr.istic.DAO;

import fr.istic.jpa.RDV;
import io.micrometer.common.lang.NonNullApi;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Transactional
@NonNullApi
public interface SpringRDVDAO extends JpaRepository<RDV, Long>
{
    List<RDV> findAll();
    RDV findById(long id);

    RDV save(RDV rdv);
    void deleteById(long id);
}
