package pl.mg.authserver.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mg.authserver.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Base64;

@RestController
@CrossOrigin(origins = {"*"}, maxAge = 3600)
public class ResourceController {

    @RequestMapping("/login")
    public boolean login(@RequestBody User user) {
        return user.getUserName().equals("user") && user.getPassword().equals("password");
    }

    @RequestMapping("/user")
    public Principal user(HttpServletRequest request, HttpSession session) {
        String authToken = request.getHeader("Authorization").substring("Basic".length()).trim();

        System.out.println("Session id: " + session.getId());

        return () -> new String(Base64.getDecoder().decode(authToken)).split(":")[0];
    }
}
