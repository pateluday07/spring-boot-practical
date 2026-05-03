package com.byteandbeyondwithuday.springbootpractical.repository;

import com.byteandbeyondwithuday.springbootpractical.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
