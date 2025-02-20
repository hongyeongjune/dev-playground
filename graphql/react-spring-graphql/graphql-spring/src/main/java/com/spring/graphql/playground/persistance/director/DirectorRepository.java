package com.spring.graphql.playground.persistance.director;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectorRepository extends JpaRepository<DirectorEntity, Long> {
}
