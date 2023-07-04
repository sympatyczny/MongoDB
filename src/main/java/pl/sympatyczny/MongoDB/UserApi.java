package pl.sympatyczny.MongoDB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
public class UserApi {

   UserRepository userRepository;

   @Autowired
    public UserApi(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
       return userRepository.findAll();
   }

    @GetMapping("/user/{name}")
    public List<User> getByUserName(@PathVariable String name) {
        return userRepository.findByName(name);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user)
    {
        User savedUser = userRepository.save(user);
        URI location=ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {

        userRepository.deleteAll();
        User user1 = new User("John", "test@test.pl");
        User user2 = new User("Jerry", "test1@test1.pl");

        userRepository.save(user1);
        userRepository.save(user2);
    }
}
