package br.com.meubolso.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;


@Entity
@Table(name = "usuario")
public class UsuarioEntity implements Serializable {


    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "role")
    private String role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "password_changed_at")
    private LocalDateTime passwordChangedAt;

    @Column(name = "password_changed_by")
    private String passwordChangedBy;

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // Getters e setters adicionados para os demais campos
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getPasswordChangedAt() { return passwordChangedAt; }
    public void setPasswordChangedAt(LocalDateTime passwordChangedAt) { this.passwordChangedAt = passwordChangedAt; }

    public String getPasswordChangedBy() { return passwordChangedBy; }
    public void setPasswordChangedBy(String passwordChangedBy) { this.passwordChangedBy = passwordChangedBy; }
}