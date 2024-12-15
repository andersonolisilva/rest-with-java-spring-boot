package br.com.agymsistemas.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.agymsistemas.data.vo.v1.PersonVO;
import br.com.agymsistemas.exceptions.ResourceNotFoundException;
import br.com.agymsistemas.mapper.DozerMapper;
import br.com.agymsistemas.model.Person;
import br.com.agymsistemas.repositories.PersonRepository;

@Service
public class PersonServices {

	private Logger logger = Logger.getLogger(PersonServices.class.getName());

	@Autowired
	PersonRepository repository;

	public PersonVO findById(Long id) {
		logger.info("Finding one person!!!");

		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Nenhum registro foi encontrado com o id: " + id));
		
		return DozerMapper.parseObject(entity, PersonVO.class);
	}

	public List<PersonVO> findAll() {

		logger.info("Finding ALL PersonVO!!!");

		List<PersonVO> persons = DozerMapper.parseListObjects(repository.findAll(), PersonVO.class) ;

		return persons;
	}

	public PersonVO create(PersonVO person) {
		logger.info("Creating one person");

		Person entity = DozerMapper.parseObject(person, Person.class);
		var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
		return vo;
	}

	public PersonVO update(PersonVO person) {
		logger.info("Updating one person");

		var entity = repository.findById(person.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Nenhum registro foi encontrado com este ID."));

		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		
		var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
		return vo;
	}

	public void delete(Long id) {
		logger.info("Deleting one person");
		Person entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Nenhum registro foi encontrado com este ID."));

		repository.delete(entity);
	}
}
