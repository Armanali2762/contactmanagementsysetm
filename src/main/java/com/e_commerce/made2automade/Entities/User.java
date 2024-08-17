package com.e_commerce.made2automade.Entities;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name="User")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotBlank(message="Name field is requried !!")
    @Size(min = 2, max = 20, message = "Name Should be 2 to 20 charcter")
    private String name;
    @Column(unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
    private String email;
    @NotBlank(message="Password field is requried !!")
    private String password;
    private String role;
    private boolean enabled;
    private String imageUrl;
    @Column(length = 500)
    @NotBlank(message="About field is requried !!")
    @Size(min = 10, max = 500, message = "Name Should be 10 to 500 charcter")
    private String about;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "user")
    private List<Product> contact = new ArrayList<>();
    public User(){}
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public void setAbout(String about) {
        this.about = about;
    }
    public void setContact(List<Product> contact) {
        this.contact = contact;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getRole() {
        return role;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getAbout() {
        return about;
    }
    public List<Product> getContact() {
        return contact;
    }
}
