package spbpu.clinic.vkr.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spbpu.clinic.vkr.entity.Bio;
import spbpu.clinic.vkr.entity.Role;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserWrapper {

    private Long id;

    private String username;

    private String password;

    private boolean active;

    private String role;

    private String roleLabel;

    private Bio bio;
}
