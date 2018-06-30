package lt.aejavap.springbatch.config;

import lt.aejavap.springbatch.domain.Person;
import lt.aejavap.springbatch.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public PersonRepository personRepository;

    @Bean
    public FlatFileItemReader<Person> reader() {

        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(new ClassPathResource("data.csv"))
                .linesToSkip(1)
                .delimited()
                .names(new String[]{"id", "firstName", "lastName"})
                .fieldSetMapper(fieldSetMapper())
                .build();
    }

    private BeanWrapperFieldSetMapper<Person> fieldSetMapper() {
        BeanWrapperFieldSetMapper<Person> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Person.class);
        return fieldSetMapper;
    }

    @Bean
    public Job importPersonJob() {
        return jobBuilderFactory.get("importPersonJob")
                .incrementer(new RunIdIncrementer())
                .start(step())
                .build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("step")
                .<Person, Person>chunk(100)
                .reader(reader())
                .writer(writer())
                .build();
    }

    @Bean
    public ItemWriter<Person> writer() {
        return items -> items.forEach(person -> {
            logger.info("Saving person: {}", person);
            personRepository.save(person);
        });
    }
}
