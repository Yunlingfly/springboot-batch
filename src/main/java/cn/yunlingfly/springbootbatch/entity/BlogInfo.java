package cn.yunlingfly.springbootbatch.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author yunlingfly
 * @date 2022/9/27
 */
@Data
@TableName("blog_info")
public class BlogInfo {
    @TableId
    private String id;
    private String blogAuthor;
    private String blogUrl;
    private String blogTitle;
    private String blogItem;

    @Override
    public String toString() {
        return "BlogInfo{" +
                "id=" + id +
                ", blogAuthor='" + blogAuthor + '\'' +
                ", blogUrl='" + blogUrl + '\'' +
                ", blogTitle='" + blogTitle + '\'' +
                ", blogItem='" + blogItem + '\'' +
                '}';
    }
}