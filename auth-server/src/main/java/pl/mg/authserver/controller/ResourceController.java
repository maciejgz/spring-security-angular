package pl.mg.authserver.controller;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import pl.mg.authserver.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = {"*"}, maxAge = 3600)
public class ResourceController {

    @RequestMapping("/login")
    public boolean login(@RequestBody User user) {
        return user.getUserName().equals("user") && user.getPassword().equals("password");
    }

    @GetMapping(value = "/")
    public org.springframework.security.core.userdetails.User user(HttpServletRequest request, HttpSession session) {
        System.out.println("Session id: " + session.getId());
        System.out.println("creation time: " + session.getCreationTime());
        System.out.println("max inactive interval: " + session.getMaxInactiveInterval());
        SecurityContext secContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        return ((org.springframework.security.core.userdetails.User) secContext.getAuthentication().getPrincipal());
    }
}
