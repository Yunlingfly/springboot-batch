package cn.yunlingfly.springbootbatch.dynconfig;

import cn.yunlingfly.springbootbatch.config.MyReadListener;
import cn.yunlingfly.springbootbatch.config.MyWriteListener;
import cn.yunlingfly.springbootbatch.entity.BlogInfo;
import lombok.Data;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

/**
 * @author yunlingfly
 * @date 2022/9/28
 */
@Data
public class TheBlogInsertBatchJob {
    private int type;
    private JobBuilderFactory jobBuilderFactory;
    private DataSource dataSource;
    private ItemReader<BlogInfo> jobReader;
    private ItemWriter<BlogInfo> jobWriter;

    public TheBlogInsertBatchJob(int type, JobBuilderFactory jobBuilderFactory, DataSource dataSource) {
        this.type = type;
        this.jobBuilderFactory = jobBuilderFactory;
        this.dataSource = dataSource;
    }

    public Job getTheBlogInsertBatchJob() {
        if (this.type == 1) {
            this.jobReader = this.setReaderCsv();
            this.jobWriter = this.writerCommon();
        } else if (this.type == 2) {

        } else {
            System.out.println("文件未在定义的类型中");
            return null;
        }
    }

    public Job theBlogInsertBatchJob(JobBuilderFactory jobs, Step myStep) {
        if (type == 1) {
            return jobs.get("theBlogInsertBatchJob1")
                    .incrementer(new RunIdIncrementer())
                    .flow(myStep)
                    .end()
//                .listener(myJobListener())
                    .build();
        } else if (type == 2) {
            return jobs.get("theBlogInsertBatchJob2")
                    .incrementer(new RunIdIncrementer())
                    .flow(myStep)
                    .end()
//                .listener(myJobListener())
                    .build();
        } else {
            System.out.println("文件未在定义的类型中");
            return null;
        }
    }

    public Step myStep(StepBuilderFactory stepBuilderFactory, ItemReader<BlogInfo> reader,
                       ItemWriter<BlogInfo> writer, ItemProcessor<BlogInfo, BlogInfo> processor) {
        return stepBuilderFactory
                .get("myStep")
                .<BlogInfo, BlogInfo>chunk(65000) // Chunk的机制(即每次读取一条数据，再处理一条数据，累积到一定数量后再一次性交给writer进行写入操作)
                .reader(reader).faultTolerant().retryLimit(3).retry(Exception.class).skip(Exception.class).skipLimit(2)
                .listener(new MyReadListener())
                .processor(processor)
                .writer(writer).faultTolerant().skip(Exception.class).skipLimit(2)
                .listener(new MyWriteListener())
                .build();
    }

    private ItemReader<BlogInfo> setReaderCsv() {
        // 使用FlatFileItemReader去读cvs文件，一行即一条数据
        FlatFileItemReader<BlogInfo> reader = new FlatFileItemReader<>();
        // 设置文件处在路径
        reader.setResource(new ClassPathResource("static/blog_info.csv"));
        // entity与csv数据做映射
        reader.setLineMapper(new DefaultLineMapper<BlogInfo>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[]{"id", "blogAuthor", "blogUrl", "blogTitle", "blogItem"});
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<BlogInfo>() {
                    {
                        setTargetType(BlogInfo.class);
                    }
                });
            }
        });
        return reader;
    }

    private ItemWriter<BlogInfo> writerCommon() {
        // 使用jdbcBcatchItemWrite写数据到数据库中
        JdbcBatchItemWriter<BlogInfo> writer = new JdbcBatchItemWriter<>();
        // 设置有参数的sql语句
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<BlogInfo>());
        String sql = "insert into blog_info " + " (id, blog_author, blog_url,blog_title,blog_item) "
                + " values(:id,:blogAuthor,:blogUrl,:blogTitle,:blogItem)";
        writer.setSql(sql);
        writer.setDataSource(this.dataSource);
        return writer;
    }
}
