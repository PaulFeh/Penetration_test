package net.uncc.app.auth.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.uncc.app.auth.controller.dto.UserRegistrationDto;
import net.uncc.app.auth.model.User;
import net.uncc.app.auth.service.UserService;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        return "auth/registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
                                      BindingResult result){

        // empty check
        if (userDto.getFirstName().equals(null)){
            result.rejectValue("firstName", null, "** Required");
        }
        if (userDto.getLastName().equals(null)){
            result.rejectValue("lastName", null, "** Required");
        }
        if (userDto.getEmail().equals(null)){
            result.rejectValue("email", null, "** Required");
        }
        if (userDto.getConfirmEmail().equals(null)){
            result.rejectValue("confirmEmail", null, "** Required");
        }
        if (userDto.getPassword().equals(null)){
            result.rejectValue("password", null, "** Required");
        }
        if (userDto.getConfirmPassword().equals(null)){
            result.rejectValue("confirmPassword", null, "** Required");
        }
        if (!userDto.getTerms()){
            result.rejectValue("terms", null, "** Required");
        }
        // empty check

        User existing = userService.findByEmail(userDto.getEmail());
        if (existing != null){
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (!userDto.getEmail().equals(userDto.getConfirmEmail())){
            result.rejectValue("confirmEmail", null, "Emails do not match");
        }
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())){
            result.rejectValue("confirmPassword", null, "Passwords do not match");
        }

        if (result.hasErrors()){
            return "auth/registration";
        }

        userService.save(userDto);
        return "redirect:/registration?success";
    }

}
