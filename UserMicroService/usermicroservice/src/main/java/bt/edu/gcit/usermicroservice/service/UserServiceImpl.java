package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.dao.UserDAO;
import bt.edu.gcit.usermicroservice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Lazy;

import bt.edu.gcit.usermicroservice.exception.FileSizeException;
import bt.edu.gcit.usermicroservice.exception.UserNotFoundException;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import org.springframework.util.StringUtils;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserDAO userDAO;
    private final BCryptPasswordEncoder passwordEncoder;
    private final String uploadDir = "src/main/resources/static/images";

    @Autowired
    @Lazy
    public UserServiceImpl(UserDAO userDAO, BCryptPasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDAO.save(user);
    }

    @Override
    public boolean isEmailDuplicate(String email) {
        User user = userDAO.findByEmail(email);
        return user != null;
    }

    @Override
    public User findByID(int theId) {
        return userDAO.findByID(theId);
    }

    @Transactional
    @Override
    public User updateUser(int id, User updatedUser) {
        User existingUser = userDAO.findByID(id);

        if (existingUser == null) {
            throw new UserNotFoundException("User not found with id: " + id);
        }

        // Update only if non-null
        if (updatedUser.getFirstName() != null) {
            existingUser.setFirstName(updatedUser.getFirstName());
        }
        if (updatedUser.getLastName() != null) {
            existingUser.setLastName(updatedUser.getLastName());
        }
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }

        // Update password only if provided and changed
        if (updatedUser.getPassword() != null &&
                !passwordEncoder.matches(updatedUser.getPassword(), existingUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        // Update roles if provided
        if (updatedUser.getRoles() != null && !updatedUser.getRoles().isEmpty()) {
            existingUser.setRoles(updatedUser.getRoles());
        }

        return userDAO.save(existingUser);
    }

    @Transactional
    @Override
    public void deleteById(int theId) {
        userDAO.deleteById(theId);
    }

    @Transactional
    @Override
    public void updateUserEnabledStatus(int id, boolean enabled) {
        userDAO.updateUserEnabledStatus(id, enabled);
    }

    @Transactional
    @Override
    public void uploadUserPhoto(int id, MultipartFile photo) throws IOException {

        User user = findByID(id);

        if (user == null) {
            throw new UserNotFoundException("User not found with id " + id);
        }

        if (photo.getSize() > 20 * 1024 * 1024) {
            throw new FileSizeException("File size must be < 20MB");
        }

        String originalFilename = StringUtils.cleanPath(photo.getOriginalFilename());

        String filenameExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String filenameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf("."));

        String timestamp = String.valueOf(System.currentTimeMillis());

        String filename = filenameWithoutExtension + "_" + timestamp + "." + filenameExtension;

        Path uploadPath = Paths.get(uploadDir, filename);

        Files.createDirectories(uploadPath.getParent());

        photo.transferTo(uploadPath);

        user.setPhoto(filename);

        // ✅ IMPORTANT: don't re-encode password
        userDAO.save(user);
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }
}
