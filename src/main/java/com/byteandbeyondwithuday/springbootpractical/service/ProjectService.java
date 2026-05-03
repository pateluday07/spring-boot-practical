package com.byteandbeyondwithuday.springbootpractical.service;

import com.byteandbeyondwithuday.springbootpractical.entity.Project;

import java.util.List;

public interface ProjectService {

    Project save(Project project);

    Project update(Project project);

    Project findById(Long id);

    List<Project> findAll();

    void deleteById(Long id);
}
