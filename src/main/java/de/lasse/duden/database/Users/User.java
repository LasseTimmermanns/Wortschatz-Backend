package de.lasse.duden.database.Users;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    private String uuid;

    private String username;
    private int password;

    public User(String username, String password) {
        this.uuid = UUID.randomUUID().toString();
        this.username = username;
        this.password = password.hashCode();
    }

    public User(){}

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid='" + uuid + '\'' +
                ", username='" + username + '\'' +
                ", password=" + password +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return password == user.password && Objects.equals(uuid, user.uuid) && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, username, password);
    }




}
