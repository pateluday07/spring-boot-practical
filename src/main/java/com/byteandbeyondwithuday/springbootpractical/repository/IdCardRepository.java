package com.byteandbeyondwithuday.springbootpractical.repository;

import com.byteandbeyondwithuday.springbootpractical.entity.IdCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdCardRepository extends JpaRepository<IdCard, Long> {
}
