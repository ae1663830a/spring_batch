package lt.aejavap.springbatch.service;

import lt.aejavap.springbatch.domain.Person;
import lt.aejavap.springbatch.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public Person savePerson(Person person) {
       return personRepository.save(person);
    }

    public Person findById(Long id) {
        return personRepository.getOne(id);
    }

    public void deletePerson(Long id) {
        personRepository.delete(findById(id));
    }
}
