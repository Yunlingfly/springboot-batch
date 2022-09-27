package cn.yunlingfly.springbootbatch.mapper;

import cn.yunlingfly.springbootbatch.entity.BlogInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
 * @author yunlingfly
 * @date 2022/9/27
 */
@Component
public interface BlogMapper extends BaseMapper<BlogInfo> {
}