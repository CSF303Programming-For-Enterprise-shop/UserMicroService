package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.entity.Role;
import java.util.List;

public interface RoleService {
    void addRole(Role role);
    Role findById(Integer id);
    List<Role> findAll();
}
