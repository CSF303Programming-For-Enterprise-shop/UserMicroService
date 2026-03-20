package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.entity.User;
import java.util.List;

public interface UserService {
    User save(User user);
    User findById(Long id);
    List<User> findAll();
}
