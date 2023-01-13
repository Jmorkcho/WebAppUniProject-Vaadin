package com.uniproject.application.data.repository;

import com.uniproject.application.data.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query("select c from Contact c " +
            "where lower(c.firstName) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.lastName) like lower(concat('%', :searchTerm, '%'))")
    List<Contact> searchByName(@Param("searchTerm") String searchTerm);

    @Query("select c from Contact c " +
            "where lower(c.email) like lower(concat('%', :searchTerm, '%')) ")
    List<Contact> searchByEmail(@Param("searchTerm") String searchTerm);

    @Query("select c from Contact c " +
            "where (lower(c.firstName) like lower(concat('%', :name, '%')) " +
            "or lower(c.lastName) like lower(concat('%', :name, '%'))) and " +
            "lower(c.email) like lower(concat('%', :email, '%')) " )
    List<Contact> searchByNameAndEmail(@Param("name") String name, @Param("email") String email);
}
