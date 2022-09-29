package cn.yunlingfly.springbootbatch.controller;

import cn.yunlingfly.springbootbatch.entity.BlogInfo;
import cn.yunlingfly.springbootbatch.mapper.BlogMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author yunlingfly
 * @date 2022/9/27
 */
@RestController
public class TestController {
    @Autowired
    SimpleJobLauncher jobLauncher;
    @Autowired
    BlogMapper blogMapper;

    @Autowired
    Job myJob;

    @GetMapping(value = "/test")
    public String test() {
        System.out.println("启动测试。。。");
        return "test";
    }

    @GetMapping(value = "/insertDate")
    public void insertDate() {
        for (int i = 1; i <= 5000; i++) {
            BlogInfo blogInfo = new BlogInfo();
            blogInfo.setId(UUID.randomUUID().toString());
            blogInfo.setBlogAuthor("yunlingfly");
            blogInfo.setBlogItem("study");
            blogInfo.setBlogTitle("中文测试");
            blogInfo.setBlogUrl("https://www.yunlingfly.cn/");
            blogMapper.insert(blogInfo);
        }
        System.out.println("插入完成");
    }

    /**
     * 静态文件导入测试
     */
    @GetMapping("/testJob")
    public void testJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        // 后置参数：使用JobParameters中绑定参数 addLong  addString 等方法
        JobParameters jobParameters = new JobParametersBuilder().toJobParameters();

        jobLauncher.run(myJob, jobParameters);
        System.out.println("插入完成");
    }
}
