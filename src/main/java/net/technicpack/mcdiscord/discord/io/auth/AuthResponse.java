package net.technicpack.mcdiscord.discord.io.auth;

import java.util.List;

public class AuthResponse {
    private List<String> email;
    private List<String> password;
    private String token;

    public String getToken() { return this.token; }
    public List<String> getUsernameErrors() { return this.email; }
    public List<String> getPasswordErrors() { return this.password; }
}
