package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.Role;
import java.util.List;

public interface RoleDAO {
    void addRole(Role role);
    Role findById(Integer id);
    List<Role> findAll();
}
