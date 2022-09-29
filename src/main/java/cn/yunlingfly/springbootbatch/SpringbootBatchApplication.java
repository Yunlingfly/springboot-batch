package cn.yunlingfly.springbootbatch;

import cn.yunlingfly.springbootbatch.dynconfig.TheBlogInsertBatchJobConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * modular true表示允许多job
 *
 * @author yunlingfly
 * @date 2022/9/27
 */
@SpringBootApplication
@EnableBatchProcessing(modular = true)
@MapperScan(value = {"cn.yunlingfly.springbootbatch.**.mapper*"})
public class SpringbootBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootBatchApplication.class, args);
    }

    @Bean
    public ApplicationContextFactory theBlogInsertBatchJobContext() {
        return new GenericApplicationContextFactory(TheBlogInsertBatchJobConfiguration.class);
    }
}
