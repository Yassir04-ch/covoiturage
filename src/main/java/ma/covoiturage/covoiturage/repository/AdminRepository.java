package ma.covoiturage.covoiturage.repository;

import ma.covoiturage.covoiturage.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin , UUID>{

}
