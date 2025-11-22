package by.nikiforova.crud.service;

import by.nikiforova.crud.dao.UserDao;
import by.nikiforova.crud.entity.User;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User createUser(String name, String email, int age) {
        User user = new User(name, email, age);
        userDao.save(user);
        return user;
    }

    public Optional<User> getUserById(Integer id) {
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public void updateUser(User user) {
        Optional<User> optionalUser = userDao.findById(user.getId());
        if (optionalUser.isPresent()) {
            User userToUpdate = optionalUser.get();
            if (user.getName() != null) userToUpdate.setName(user.getName());
            if (user.getEmail() != null) userToUpdate.setEmail(user.getEmail());
            if (user.getAge() != null) userToUpdate.setAge(user.getAge());
            userDao.update(user);
        }
    }

    public void deleteUser(Integer id) {
        Optional<User> optionalUser = userDao.findById(id);
        optionalUser.ifPresent(userDao::delete);
    }
}
