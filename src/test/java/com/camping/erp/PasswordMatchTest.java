package com.camping.erp;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordMatchTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = "$2a$10$8H0OmIE98Xf90E76y0.5fO5Z7p.J88mP.J88mP.J88mP.J88mP";
        boolean matches = encoder.matches("1234", hash);
        System.out.println("Matches: " + matches);
    }
}
