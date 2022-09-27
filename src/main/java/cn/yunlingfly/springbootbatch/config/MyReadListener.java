package cn.yunlingfly.springbootbatch.config;

import cn.yunlingfly.springbootbatch.entity.BlogInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;

import static java.lang.String.format;

/**
 * @author yunlingfly
 * @date 2022/9/27
 */

@Slf4j
public class MyReadListener implements ItemReadListener<BlogInfo> {
    @Override
    public void beforeRead() {
    }

    @Override
    public void afterRead(BlogInfo item) {
    }

    @Override
    public void onReadError(Exception ex) {
        try {
            log.info(format("%s%n", ex.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}