package bs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private Servicer servicer;

    @GetMapping("/user")
    public List<User> queryUserInfo() {
        return servicer.findAllUsers();
    }

    @PostMapping("/user{userid}")
    public void addUser(@RequestBody UserDto userDto) {
        servicer.add(userDto);
    }

    @PutMapping("/user")
    public void updateUser(@RequestBody UserDto userDto) {
        servicer.update(userDto);
    }

    @DeleteMapping("/user/{userid}")
    public void removeUser(@PathVariable("userid") long userid) {
        servicer.delete(userid);
    }

}
