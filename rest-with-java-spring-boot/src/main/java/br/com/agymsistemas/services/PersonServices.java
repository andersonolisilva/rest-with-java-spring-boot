package br.com.agymsistemas.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.agymsistemas.controllers.PersonController;
import br.com.agymsistemas.data.vo.v1.PersonVO;
import br.com.agymsistemas.data.vo.v2.PersonVOV2;
import br.com.agymsistemas.exceptions.ResourceNotFoundException;
import br.com.agymsistemas.mapper.DozerMapper;
import br.com.agymsistemas.mapper.custom.PersonMapper;
import br.com.agymsistemas.model.Person;
import br.com.agymsistemas.repositories.PersonRepository;

@Service
public class PersonServices {

	private Logger logger = Logger.getLogger(PersonServices.class.getName());

	@Autowired
	PersonRepository repository;

	@Autowired
	PersonMapper mapper;

	public PersonVO findById(Long id) throws Exception {
		logger.info("Finding one person!!!");

		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Nenhum registro foi encontrado com o id: " + id));
		var vo = DozerMapper.parseObject(entity, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return vo;
	}

	public List<PersonVO> findAll() {

		logger.info("Finding ALL PersonVO!!!");

		var persons = DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
		persons.stream()
				.forEach(p -> {
						try {
							p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel());
						} catch (Exception e) {
							e.printStackTrace();
						}
				});

		return persons;
	}

	public PersonVO create(PersonVO person) throws Exception {
		logger.info("Creating one person");

		var entity = DozerMapper.parseObject(person, Person.class);
		var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

	public PersonVOV2 createV2(PersonVOV2 person) {
		logger.info("Creating one person");

		Person entity = mapper.convertVoToEntity(person);
		var vo = mapper.convertEntityToVo(repository.save(entity));
		return vo;
	}

	public PersonVO update(PersonVO person) throws Exception {
		logger.info("Updating one person");

		var entity = repository.findById(person.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("Nenhum registro foi encontrado com este ID."));

		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());

		var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

	public void delete(Long id) {
		logger.info("Deleting one person");
		Person entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Nenhum registro foi encontrado com este ID."));

		repository.delete(entity);
	}

}
