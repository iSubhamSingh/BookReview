package subham.loginregdemo.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import subham.loginregdemo.models.LoginUser;
import subham.loginregdemo.models.User;
import subham.loginregdemo.services.UserService;


    
@Controller
public class HomeController {
    

    @Autowired
    private UserService userService;
    
    @GetMapping("/")
    public String index(Model model) {
    
        model.addAttribute("newUser", new User());
        model.addAttribute("newLogin", new LoginUser());
        return "index.jsp";
    }
    
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("newUser") User newUser, 
        BindingResult result, Model model, HttpSession session) {
        System.out.println(newUser);
        User user = userService.register(newUser, result);
        if(result.hasErrors()) {
            
            model.addAttribute("newLogin", new LoginUser());
            return "index.jsp";
        }
        
        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getUserName());
        return "redirect:/books";
    }
    
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("newLogin") LoginUser newLogin, 
    BindingResult result, Model model, HttpSession session) {
        
        
        User user = userService.login(newLogin, result);
        
        if(result.hasErrors()) {
            model.addAttribute("newUser", new User());
            return "index.jsp";
        }
        
        // log in. 
        session.setAttribute("userId", user.getId());
        session.setAttribute("user", user);
        
        return "redirect:/books";
    }

    @GetMapping("/home")
    public String home(HttpSession session){
        if(session.getAttribute("userId") == null){
            return "redirect:/logout";
        }
        return "home.jsp";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
    
    @GetMapping("/api/books") // api endpoint to get book list(In progress)
    public String bookList() {
    	return "";
    }
    
    @GetMapping("/api/users") // api endpoint to get user list(In progress)
    public String userList() {
    	return "";
    }
}
