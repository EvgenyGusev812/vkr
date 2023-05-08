package spbpu.clinic.vkr.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_DOCTOR("Врач"),
    ROLE_GENERAL("Управляющий");

    private String label;

    Role(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }


    @Override
    public String getAuthority() {
        return name();
    }
}