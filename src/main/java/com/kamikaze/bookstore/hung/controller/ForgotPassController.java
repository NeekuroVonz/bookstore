package com.kamikaze.bookstore.hung.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kamikaze.bookstore.hung.entity.Users;
import com.kamikaze.bookstore.hung.exceptions.UserNotFoundException;
import com.kamikaze.bookstore.hung.services.UsersService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import net.bytebuddy.utility.RandomString;

@Controller
@RequestMapping("/auth")
public class ForgotPassController {

    @Autowired
    UsersService usersService;

    @Autowired
    private JavaMailSender javaMailSender;

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) throws UnsupportedEncodingException, MessagingException {
        String email = request.getParameter("email");
        String token = RandomString.make(30);
        
        try {
            usersService.updateResetPasswordToken(token, email);
            String resetPasswordLink = getSiteURL(request) + "/auth/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
            
        } catch (UserNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email");
        }
            
        return "forgot_password_form";
    }
    public String getSiteURL(HttpServletRequest req) {
        String siteURL = req.getRequestURL().toString();
        return siteURL.replace(req.getServletPath(), "");
    }
    
    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();              
        MimeMessageHelper helper = new MimeMessageHelper(message);
        
        helper.setFrom("contact@shopme.com", "Bookstore Support");
        helper.setTo(recipientEmail);
        
        String subject = "Here's the link to reset your password";
        
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
        
        helper.setSubject(subject);
        
        helper.setText(content, true);
        
        javaMailSender.send(message);
    }  
            
    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        Users users = usersService.getByResetPasswordToken(token);
        model.addAttribute("token", token);
        
        if (users == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }
     
        return "reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        
        Users users = usersService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");
        
        if (users == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        } else {           
            usersService.updatePassword(users, password);
            
            model.addAttribute("message", "You have successfully changed your password.");
        }
        
        return "message";
    }
}
