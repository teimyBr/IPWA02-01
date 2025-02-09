package business;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Publisher implements Serializable {

    @Id
    private String name;
    private String passcode;
    private boolean admin;

    public Publisher(String name, String passcode) {
        this.name = name;
        this.passcode = passcode;
        this.admin = false;
    }

    public Publisher() {
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Publisher) {
            Publisher p = (Publisher) obj;
            return (p.getName().equals(this.name) && p.getPasscode().equals(this.passcode));
        }
        return false;
    }

    public boolean equalsUsername(String username) {
        return (username != null && this.name != null) ? this.name.toLowerCase().equals(username.toLowerCase()) : false;
    }

    private String getPasscode() {
        return this.passcode;
    }

    public String toString() {
        return "" + name + ";" + passcode;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}