package cn.yunlingfly.springbootbatch.dynconfig;

import cn.yunlingfly.springbootbatch.config.MyItemProcessor;
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
import org.springframework.batch.item.validator.Validator;
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
    private StepBuilderFactory stepBuilderFactory;
    private DataSource dataSource;
    private ItemReader<BlogInfo> jobReader;
    private ItemWriter<BlogInfo> jobWriter;
    private ItemProcessor<BlogInfo, BlogInfo> processor;
    private Step myCsvStep;
    private Validator validator;

    public TheBlogInsertBatchJob(int type, JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DataSource dataSource, Validator validator) {
        this.type = type;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
        this.validator = validator;
    }

    public Job getTheBlogInsertBatchJob() {
        // 1 csv
        if (this.type == 1) {
            this.jobReader = this.setReaderCsv();
            this.jobWriter = this.writerCommon();
            this.processor = this.processor();
            this.myCsvStep = this.getMyCsvStep();
            return jobBuilderFactory.get("theBlogInsertBatchJobByCsv")
                    .incrementer(new RunIdIncrementer())
                    .flow(this.myCsvStep)
                    .end()
//                .listener(myJobListener())
                    .build();
        }
        // 2 json
        else if (this.type == 2) {
            System.out.println("??????????????????????????????");
            return null;
        } else {
            System.out.println("??????????????????????????????");
            return null;
        }
    }

    private Step getMyCsvStep() {
        return this.stepBuilderFactory
                .get("myCsvStep")
                .<BlogInfo, BlogInfo>chunk(65000) // Chunk?????????(????????????????????????????????????????????????????????????????????????????????????????????????writer??????????????????)
                .reader(this.jobReader).faultTolerant().retryLimit(3).retry(Exception.class).skip(Exception.class).skipLimit(2)
                .listener(new MyReadListener())
                .processor(this.processor)
                .writer(this.jobWriter).faultTolerant().skip(Exception.class).skipLimit(2)
                .listener(new MyWriteListener())
                .build();
    }

    private ItemReader<BlogInfo> setReaderCsv() {
        // ??????FlatFileItemReader??????cvs??????????????????????????????
        FlatFileItemReader<BlogInfo> reader = new FlatFileItemReader<>();
        // ????????????????????????
        reader.setResource(new ClassPathResource("static/blog_info.csv"));
        // entity???csv???????????????
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
        // ??????jdbcBcatchItemWrite????????????????????????
        JdbcBatchItemWriter<BlogInfo> writer = new JdbcBatchItemWriter<>();
        // ??????????????????sql??????
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<BlogInfo>());
        String sql = "insert into blog_info " + " (id, blog_author, blog_url,blog_title,blog_item) "
                + " values(:id,:blogAuthor,:blogUrl,:blogTitle,:blogItem)";
        writer.setSql(sql);
        writer.setDataSource(this.dataSource);
        return writer;
    }

    private ItemProcessor<BlogInfo, BlogInfo> processor() {
        MyItemProcessor myItemProcessor = new MyItemProcessor();
        // ???????????????
        myItemProcessor.setValidator(this.validator);
        return myItemProcessor;
    }
}
