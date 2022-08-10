package com.kamikaze.bookstore.hung.services;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.kamikaze.bookstore.hung.dao.UsersDao;
import com.kamikaze.bookstore.hung.entity.Users;
import com.kamikaze.bookstore.hung.exceptions.BadRequestException;
import com.kamikaze.bookstore.hung.exceptions.ResourceNotFoundException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import net.bytebuddy.utility.RandomString;

@Service
public class UsersService {
    
    @Autowired
    private UsersDao usersDao;
    @Autowired
    private JavaMailSender javaMailSender;

    public Users findById(String id) {
        return usersDao.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Users with id: " + id + " not found!"));
    }

    public List<Users> findAll() {
        return usersDao.findAll();
    }

    public Users create(Users users, String siteURL) throws UnsupportedEncodingException, MessagingException {
        // check id / username
        if (!StringUtils.hasText(users.getId())) {
            throw new BadRequestException("Username must be filled!");
        }
        if (usersDao.existsById(users.getId())) {
            throw new BadRequestException("Username " + users.getId() + " has already registered!");
        }

        // check email
        if (!StringUtils.hasText(users.getEmail())) {
            throw new BadRequestException("Email must be filled!");
        }
        if (usersDao.existsByEmail(users.getEmail())) {
            throw new BadRequestException("Email " + users.getEmail() + " has already registered!");
        }

        // check password
        if (!StringUtils.hasText(users.getPassword())) {
            throw new BadRequestException("Password must be filled!");
        }

        // check name
        if (!StringUtils.hasText(users.getName())) {
            throw new BadRequestException("Name must be filled!");
        }

        String randomCode = RandomString.make(64);
        users.setVerificationCode(randomCode);
        users.setIsActive(false);
         
        // usersDao.save(users);
         
        sendVerificationEmail(users, siteURL);

        return usersDao.save(users);
    }

    private void sendVerificationEmail(Users users, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String toAddress = users.getEmail();
        String fromAddress = "hungspeedpro2017@gmail.com";
        String senderName = "Book Store Company";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Book Store Company.";
        
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        
        content = content.replace("[[name]]", users.getName());
        String verifyURL = siteURL + "/verify?code=" + users.getVerificationCode();
        
        content = content.replace("[[URL]]", verifyURL);
        
        helper.setText(content, true);
        
        javaMailSender.send(message);
    }

    public boolean verify(String verificationCode) {
        Users users = usersDao.findByVerificationCode(verificationCode);
         
        if (users == null || users.getIsActive()) {
            return false;
        } else {
            users.setVerificationCode(null);
            users.setIsActive(true);
            usersDao.save(users);
             
            return true;
        }
    }

    public Users edit(Users users) {
        // check id / username
        if (!StringUtils.hasText(users.getId())) {
            throw new BadRequestException("Username must be filled!");
        }

        // check email
        if (!StringUtils.hasText(users.getEmail())) {
            throw new BadRequestException("Email must be filled!");
        }
        

        return usersDao.save(users);
    }

    public void deleteById(String id) {
        usersDao.deleteById(id);
    }

}
