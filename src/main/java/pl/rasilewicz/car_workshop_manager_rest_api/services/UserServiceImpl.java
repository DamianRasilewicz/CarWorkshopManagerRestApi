package pl.rasilewicz.car_workshop_manager_rest_api.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Role;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.User;
import pl.rasilewicz.car_workshop_manager_rest_api.repositories.RoleRepository;
import pl.rasilewicz.car_workshop_manager_rest_api.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findByUserName(String userName) {
        return userRepository.findUserByUserName(userName);
    }

    @Override
    public User findUserById(Integer id) {
        return userRepository.findUserById(id);
    }

    @Override
    public List<User> findAllUsersWithoutLogInUser(String userName) {
        return userRepository.findAllUsersWithoutLogInUser(userName);
    }

    @Override
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public Integer findNumberOfAllUsers() {
        return userRepository.findNumberOfAllUsers();
    }


    @Override
    public Integer findNumberOfMonthlyRegisteredUsers(Integer month, Integer year) {
        return userRepository.findNumberOfMonthlyRegisteredUsers(month, year);
    }

    @Override
    @Transactional
    public User editUser(User user) {
        User editedUser = userRepository.findById(user.getId()).orElseThrow();
        editedUser.setEnabled(user.getEnabled());
        editedUser.setRole(user.getRole());
        return editedUser;
    }

    @Override
    @Transactional
    public User editProfile(User user) {
        User editUserProfile = userRepository.findById(user.getId()).orElseThrow();
        editUserProfile.setUserName(editUserProfile.getUserName());
        editUserProfile.setFirstName(editUserProfile.getFirstName());
        editUserProfile.setLastName(editUserProfile.getLastName());
        editUserProfile.setPassword(editUserProfile.getPassword());
        editUserProfile.setEmail(editUserProfile.getEmail());
        editUserProfile.setPhoneNumber(editUserProfile.getPhoneNumber());
        return  editUserProfile;
    }
}
