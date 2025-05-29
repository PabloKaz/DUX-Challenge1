package com.dux.challenge.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "app_user")
public class User {
	  @Id @GeneratedValue
	  private Long id;
	  
	  private String username;
	  private String password;
	  private boolean enabled;                 // activa/desactiva cuenta
	  private boolean accountNonLocked;        // bloqueo por seguridad
	  private LocalDate credentialsExpiryDate; // fecha de caducidad de contraseña
	  private LocalDate accountExpiryDate;     // fecha de caducidad de cuenta
	  
	  
	  public boolean isCredentialsNonExpired() {
		    return credentialsExpiryDate == null || credentialsExpiryDate.isAfter(LocalDate.now());
		  }
	  
	  public boolean isAccountNonExpired() {
	    return accountExpiryDate == null || accountExpiryDate.isAfter(LocalDate.now());
	  }
}
