package bt.edu.gcit.usermicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bt.edu.gcit.usermicroservice.dao.RoleDAO;
import bt.edu.gcit.usermicroservice.entity.Role;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleDAO roleDAO;

    @Autowired
    public RoleServiceImpl(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Transactional
    @Override
    public void addRole(Role role) {
        roleDAO.addRole(role);
    }

    @Override
    @Transactional(readOnly = true)
    public Role findById(Integer id) {
        return roleDAO.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return roleDAO.findAll();
    }
}
