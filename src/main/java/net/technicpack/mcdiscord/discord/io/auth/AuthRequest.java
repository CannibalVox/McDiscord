package net.technicpack.mcdiscord.discord.io.auth;

public class AuthRequest {
    private String email;
    private String password;

    public AuthRequest(String username, String password) {
        this.email = username;
        this.password = password;
    }

    public String getUsername() { return this.email; }
    public String getPassword() { return this. password; }
}
