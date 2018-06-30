package lt.aejavap.springbatch.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SpringBatchService {

    private static final Logger logger = LoggerFactory.getLogger(SpringBatchService.class);

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    @Qualifier("importPersonJob")
    Job importPersonJob;

    // runs job every 45 seconds with initial delay of 30 seconds
    @Scheduled(initialDelay = 30000, fixedRate = 45000)
    public void executeJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
                                    JobRestartException, JobInstanceAlreadyCompleteException {
        String jobName = importPersonJob.getName();
        JobParameters parameters = new JobParametersBuilder()
                .addDate(jobName, new Date())
                .toJobParameters();
        JobExecution execution = jobLauncher.run(importPersonJob, parameters);
        logger.info("Scheduled batch job \'{}\' finished with status: {}", jobName, execution.getStatus());
    }
}
