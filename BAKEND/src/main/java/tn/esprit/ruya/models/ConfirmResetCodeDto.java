package tn.esprit.ruya.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmResetCodeDto {
    private String email;
    private String resetCode;
    private String newPassword;

    // Constructeurs
    public ConfirmResetCodeDto() {}

    public ConfirmResetCodeDto(String email, String resetCode, String newPassword) {
        this.email = email;
        this.resetCode = resetCode;
        this.newPassword = newPassword;
    }

    // Getters et Setters manuels (au cas où Lombok ne fonctionne pas)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResetCode() {
        return resetCode;
    }

    public void setResetCode(String resetCode) {
        this.resetCode = resetCode;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    // Méthodes utiles
    @Override
    public String toString() {
        return "ConfirmResetCodeDto{" +
                "email='" + email + '\'' +
                ", resetCode='[HIDDEN]'" +
                ", newPassword='[HIDDEN]'" +
                '}';
    }

    // Validation simple
    public boolean isValid() {
        return email != null && !email.trim().isEmpty() &&
                resetCode != null && !resetCode.trim().isEmpty() &&
                newPassword != null && !newPassword.trim().isEmpty();
    }
}