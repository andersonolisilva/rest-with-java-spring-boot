package br.com.agymsistemas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.agymsistemas.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

}
