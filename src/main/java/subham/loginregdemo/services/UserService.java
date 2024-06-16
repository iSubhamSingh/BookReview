package subham.loginregdemo.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import subham.loginregdemo.models.LoginUser;
import subham.loginregdemo.models.User;
import subham.loginregdemo.repositories.UserRepository;
    

    
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    
    
    //register and login methods!
    public User register(User newUser, BindingResult result) {
        System.out.println(newUser);
        
        if(userRegistered(newUser.getEmail())){
            result.rejectValue("email", "Email", "email taken");
            return null;
        }
        
        //check if password doesn't match then reject the values.
        if(!newUser.getPassword().equals(newUser.getConfirm())){
            result.rejectValue("confirm", "Confirm", "passwords must match");
            return null;
        }
        
        // Return null if result has errors
        if(result.hasErrors()){
            return null;
        }
    
        // Hash and set password, save user to database
        String hashedpw = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
        newUser.setPassword(hashedpw);
        return userRepository.save(newUser);
    }

    public User login(LoginUser newLoginObject, BindingResult result) {
        
        
    	
        if(!userRegistered(newLoginObject.getEmail())){
            result.rejectValue("email", "EmailLogin", "Invalid Credentials");
            return null;
        }
        User user = findUserByEmail(newLoginObject.getEmail());
        // Reject if BCrypt password match fails
        if(!BCrypt.checkpw(newLoginObject.getPassword(), user.getPassword())){
            result.rejectValue("password", "Password", "Invalid Credentials");
            return null;
        }
        // Return null if result has errors
        if(result.hasErrors()){
            return null;
        }
        // Otherwise, return the user object
        return user;
    }

    
    private boolean userRegistered(String email){ 
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }
    
    // find user by email id
    private User findUserByEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);

    }
    // find user by id
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }
}

