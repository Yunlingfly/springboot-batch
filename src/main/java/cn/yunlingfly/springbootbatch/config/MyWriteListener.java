package cn.yunlingfly.springbootbatch.config;


import cn.yunlingfly.springbootbatch.entity.BlogInfo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import java.util.List;
import static java.lang.String.format;

/**
 * @author yunlingfly
 * @date 2022/9/27
 */
@Slf4j
public class MyWriteListener implements ItemWriteListener<BlogInfo> {

    @Override
    public void beforeWrite(List<? extends BlogInfo> items) {
    }

    @Override
    public void afterWrite(List<? extends BlogInfo> items) {
    }

    @Override
    public void onWriteError(Exception exception, List<? extends BlogInfo> items) {
        try {
            log.info(format("%s%n", exception.getMessage()));
            for (BlogInfo message : items) {
                log.info(format("Failed writing BlogInfo : %s", message.toString()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}