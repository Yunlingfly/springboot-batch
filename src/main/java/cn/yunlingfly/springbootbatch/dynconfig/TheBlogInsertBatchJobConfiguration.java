package cn.yunlingfly.springbootbatch.dynconfig;

import cn.yunlingfly.springbootbatch.entity.BlogInfo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.Writer;

/**
 * @author yunlingfly
 * @date 2022/9/28
 */
@Configuration
public class TheBlogInsertBatchJobConfiguration {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job theBlogInsertBatchJob(@Qualifier("theBlogInsertBatchStep") Step theBlogInsertBatchStep) {
        return jobBuilderFactory.get("theBlogInsertBatchJob")
                .start(theBlogInsertBatchStep)
                .build();
    }

}
