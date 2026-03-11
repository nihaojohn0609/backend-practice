package org.example.backendpractice.controller;

import lombok.RequiredArgsConstructor;
import org.example.backendpractice.entity.User;
import org.example.backendpractice.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

        private final UserService userService;

        // add mapping for "/list"
        @GetMapping("/list")
        public String listUsers(Model theModel) {

            // get the users from db
            List<User> users = userService.findAll();

            // add to the spring model
            theModel.addAttribute("users", users);

            return "users/list-users";
        }

        @GetMapping("/showFormForAdd")
        public String showFormForAdd(Model theModel) {

            // create model attribute to bind form data
            User user = new User();

            theModel.addAttribute("user", user);

            return "users/user-form";
        }

        @GetMapping("/showFormForUpdate")
        public String showFormForUpdate(@RequestParam("userId") int id, Model theModel) {

            // get the employee from the service
            User user = userService.findById(id);

            // set employee in the model to prepopulate the form
            theModel.addAttribute("user", user);

            // send over to our form
            return "users/user-form";
        }

        @PostMapping("/save")
        public String saveUser(@ModelAttribute("user") User user) {

            // save the employee
            userService.save(user);

            // use a redirect to prevent duplicate submissions
            return "redirect:/users/list";
        }

        @GetMapping("/delete")
        public String delete(@RequestParam("userId") int id) {

            // delete the employee
            userService.deleteById(id);

            // redirect to the /users/list
            return "redirect:/users/list";
        }
}
