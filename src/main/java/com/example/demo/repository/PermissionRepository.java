package com.example.demo.repository;

import com.example.demo.entity.Permission;

public interface PermissionRepository {
    Permission findByName(String name);
}
