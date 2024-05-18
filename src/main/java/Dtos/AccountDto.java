package Dtos;

public class AccountDto {
    private String username;
    private String password;

    public AccountDto(){

    }

    public AccountDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // toString method
    @Override
    public String toString() {
        return "AccountDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
