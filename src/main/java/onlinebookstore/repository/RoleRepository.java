package onlinebookstore.repository;

import onlinebookstore.model.Role;
import onlinebookstore.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByName(RoleName name);
}
