package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.User;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.persistence.TypedQuery;
import java.util.List;
import bt.edu.gcit.usermicroservice.exception.UserNotFoundException;

@Repository
public class UserDAOImpl implements UserDAO {

    private EntityManager entityManager;

    @Autowired
    public UserDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // @Override
    // public User save(User user) {
    // // Prevent duplicate email
    // User existingUser = findByEmail(user.getEmail());
    // if (existingUser != null && !existingUser.getId().equals(user.getId())) {
    // throw new RuntimeException("Email already exists: " + user.getEmail());
    // }
    // return entityManager.merge(user);
    // }
    @Override
    public User save(User user) {
        // Check if the email already exists for a different user
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", user.getEmail());
        List<User> existingUsers = query.getResultList();

        if (!existingUsers.isEmpty()) {
            User existingUser = existingUsers.get(0);
            if (user.getId() == null || !existingUser.getId().equals(user.getId())) {
                throw new RuntimeException("Email already exists: " + user.getEmail());
            }
        }

        if (user.getId() == null) {
            entityManager.persist(user);
            return user;
        } else {
            return entityManager.merge(user);
        }
    }

    @Override
    public User findByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery(
                "from User where email = :email", User.class);
        query.setParameter("email", email);
        List<User> users = query.getResultList();
        if (users.isEmpty()) {
            return null;
        } else {
            return users.get(0);
        }
    }

    @Override
    public User findByID(int theId) {
        // Implement the logic to find a user by their ID in the database
        // and return the user object
        User user = entityManager.find(User.class, theId);
        return user;
    }

    @Override
    public void deleteById(int theId) {
        // Implement the logic to delete a user by their ID from the database
        // find user by id
        User user = findByID(theId);
        // remove user
        entityManager.remove(user);
    }

    @Override
    public void updateUserEnabledStatus(int id, boolean enabled) {
        User user = entityManager.find(User.class, id);
        System.out.println(user);
        if (user == null) {
            throw new UserNotFoundException("User not found with id " + id);
        }
        user.setEnabled(enabled);
        entityManager.persist(user);
    }

    @Override
    public List<User> findAll() {
        TypedQuery<User> query = entityManager.createQuery("from User", User.class);
        return query.getResultList();
    }

}