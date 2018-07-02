package lt.aejavap.springbatch.service;

import lt.aejavap.springbatch.domain.Person;
import lt.aejavap.springbatch.repository.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PersonServiceTest {

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private Person person;


    @Test
    public void getPerson() {
        when(personRepository.getOne(5L)).thenReturn(person);
        Person retrievedPerson = personService.findById(5L);
        assertThat(retrievedPerson, is(equalTo(person)));
    }

    @Test
    public void savePerson() {
        when(personRepository.save(person)).thenReturn(person);
        Person retrievedPerson = personService.savePerson(person);
        assertThat(retrievedPerson, is(equalTo(person)));
    }

    @Test
    public void deletePerson() {
        doNothing().when(personRepository).delete(person);
        when(personRepository.getOne(5L)).thenReturn(person);
        personService.deletePerson(5L);
        verify(personRepository, times(1)).delete(person);
        verify(personRepository, never()).save(person);
        verify(personRepository, never()).findAll();

    }
}
