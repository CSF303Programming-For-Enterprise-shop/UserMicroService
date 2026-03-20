package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.User;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public UserDAOImpl() {
        // default constructor for proxying
    }

    @Override
    public User save(User user) {
        return entityManager.merge(user);
    }

    @Override
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("from User", User.class).getResultList();
    }
}
