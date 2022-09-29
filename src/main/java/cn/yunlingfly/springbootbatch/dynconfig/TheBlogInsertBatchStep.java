package cn.yunlingfly.springbootbatch.dynconfig;

import cn.yunlingfly.springbootbatch.config.MyReadListener;
import cn.yunlingfly.springbootbatch.config.MyWriteListener;
import cn.yunlingfly.springbootbatch.entity.BlogInfo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;

/**
 * @author yunlingfly
 * @date 2022/9/28
 */
public class TheBlogInsertBatchStep {
    public Step theBlogInsertBatchStep(StepBuilderFactory stepBuilderFactory, ItemReader<BlogInfo> reader,
                       ItemWriter<BlogInfo> writer, ItemProcessor<BlogInfo, BlogInfo> processor) {
        return stepBuilderFactory
                .get("theBlogInsertBatchStep")
                .<BlogInfo, BlogInfo>chunk(65000) // Chunk的机制(即每次读取一条数据，再处理一条数据，累积到一定数量后再一次性交给writer进行写入操作)
                .reader(reader).faultTolerant().retryLimit(3).retry(Exception.class).skip(Exception.class).skipLimit(2)
                .listener(new MyReadListener())
                .processor(processor)
                .writer(writer).faultTolerant().skip(Exception.class).skipLimit(2)
                .listener(new MyWriteListener())
                .build();
    }
}
