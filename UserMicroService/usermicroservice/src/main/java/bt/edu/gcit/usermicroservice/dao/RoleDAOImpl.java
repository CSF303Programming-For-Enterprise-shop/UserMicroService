package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.Role;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RoleDAOImpl implements RoleDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public RoleDAOImpl() {
        // default constructor for proxying
    }

    @Override
    public void addRole(Role role) {
        entityManager.persist(role);
    }

    @Override
    public Role findById(Integer id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    public List<Role> findAll() {
        return entityManager.createQuery("from Role", Role.class).getResultList();
    }
}
