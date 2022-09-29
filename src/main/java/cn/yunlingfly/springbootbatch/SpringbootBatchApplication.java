package cn.yunlingfly.springbootbatch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * modular true表示允许多job
 *
 * @author yunlingfly
 * @date 2022/9/27
 */
@SpringBootApplication
@EnableBatchProcessing
@MapperScan(value = {"cn.yunlingfly.springbootbatch.**.mapper*"})
public class SpringbootBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootBatchApplication.class, args);
    }

}
