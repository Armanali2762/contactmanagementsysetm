package com.e_commerce.made2automade.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="Contact")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotBlank(message = "firstname field is required !!")
    @Size(min = 2, max = 20, message="field should be 2 to 10 character !!")
    private String firstname;
    @NotBlank(message = "secondname field is required !!")
    @Size(min = 2, max = 20, message="field should be 2 to 10 character !!")
    private String secondName;
    @NotBlank(message = "work field is required !!")
    @Size(min = 2, max = 15, message="work should be 2 to 10 character !!")
    private String work;
    @NotBlank(message = "Email field is required !!")
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
    private String email;
    @NotBlank(message = "Phone field is required !!")
    @Size(min = 10, max = 10, message="field should be 10 digit !!")
    private String phone;
    private String image;
    @Column(length = 20000)
    @NotBlank(message = "Description field is required !!")
    @Size(min = 10, max = 2000, message="Description should be 10 to 200 character !!")
    private String description;
    @ManyToOne
    private User user;

    public Product(){}
    public void setId(int id) {
        this.id = id;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }
    public void setWork(String work) {
        this.work = work;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getId() {
        return id;
    }
    public String getFirstname() {
        return firstname;
    }
    public String getSecondName() {
        return secondName;
    }
    public String getWork() {
        return work;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }
    public String getImage() {
        return image;
    }
    public String getDescription() {
        return description;
    }
    
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Product(int id, String firstname, String secondName, String work, String email, String phone, String image,
            String description) {
        this.id = id;
        this.firstname = firstname;
        this.secondName = secondName;
        this.work = work;
        this.email = email;
        this.phone = phone;
        this.image = image;
        this.description = description;
    }
}

