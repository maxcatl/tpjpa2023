package fr.istic.DAO;

import fr.istic.jpa.Client;
import fr.istic.jpa.Professional;
import io.micrometer.common.lang.NonNullApi;
import jakarta.transaction.Transactional;
import fr.istic.jpa.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Transactional
@NonNullApi
public interface SpringUserDAO extends JpaRepository<User, Long>
{
    List<User> findAll();
    User findById(long id);

    void deleteById(long id);

    Professional save(Professional professional);
    Client save(Client client);
}
