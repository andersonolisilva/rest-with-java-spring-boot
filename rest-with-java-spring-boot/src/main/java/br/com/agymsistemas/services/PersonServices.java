package br.com.agymsistemas.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.agymsistemas.exceptions.ResourceNotFoundException;
import br.com.agymsistemas.model.Person;
import br.com.agymsistemas.repositories.PersonRepository;

@Service
public class PersonServices {

	private Logger logger = Logger.getLogger(PersonServices.class.getName());

	@Autowired
	PersonRepository repository;

	public Person findById(Long id) {
		logger.info("Finding one person!!!");

		Person person = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Nenhum registro foi encontrado com o id: " + id));
		return person;
	}

	public List<Person> findAll() {

		logger.info("Finding ALL Person!!!");

		List<Person> persons = repository.findAll();

		return persons;
	}

	public Person create(Person person) {
		logger.info("Creating one person");

		return repository.save(person);
	}

	public Person update(Person person) {
		logger.info("Updating one person");

		Person entity = repository.findById(person.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Nenhum registro foi encontrado com este ID."));

		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		return repository.save(entity);
	}

	public void delete(Long id) {
		logger.info("Deleting one person");
		Person entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Nenhum registro foi encontrado com este ID."));

		repository.delete(entity);
	}
}
