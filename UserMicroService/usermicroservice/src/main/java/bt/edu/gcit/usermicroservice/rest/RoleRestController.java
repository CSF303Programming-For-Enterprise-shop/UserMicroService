package bt.edu.gcit.usermicroservice.rest;

import bt.edu.gcit.usermicroservice.entity.Role;
import bt.edu.gcit.usermicroservice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RoleRestController {
    private RoleService roleService;

    @Autowired
    public RoleRestController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    public void addRole(@RequestBody Role role) {
        roleService.addRole(role);
    }

    @GetMapping("/roles")
    public List<Role> findAll() {
        return roleService.findAll();
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> findById(@PathVariable Integer id) {
        Role role = roleService.findById(id);
        if (role == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(role);
    }
}
