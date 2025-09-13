package rocks.vivek275.anitsresultshub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rocks.vivek275.anitsresultshub.models.BaseUser;
import rocks.vivek275.anitsresultshub.models.LogInWrapper;
import rocks.vivek275.anitsresultshub.repo.UserRepo;

@Service
public class UserLoginService {
    @Autowired
    UserRepo userRepo;
    public ResponseEntity<LogInWrapper> login(String email ,String username, String password) {
        BaseUser baseUser = userRepo.getBaseUserByEmail(email);
        if (baseUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if ((baseUser.getUsername().equals(username) && baseUser.getPassword().equals(password))) {
            LogInWrapper logInWrapper = new LogInWrapper();
            logInWrapper.setSuccess(true);
            logInWrapper.setTypeOfUser(baseUser.getTypeOfUser());
            return new ResponseEntity<>(logInWrapper, HttpStatus.OK);
        }
       else {
           return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
