package com.behrend.contestmanager.repository;

import com.behrend.contestmanager.models.ERole;
import com.behrend.contestmanager.models.Role;
import com.behrend.contestmanager.models.Tournament;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.EnumType;
import java.util.List;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findRoleByName(String name);
}
