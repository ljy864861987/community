package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPosts;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

	List<DiscussPosts> selectDiscussPosts(int userId, int offset, int limit);

	//@Param注解用于给参数取别名
	//如果只有一个参数，并且在<if>里使用，则必须加别名.
	int selectDiscussPostsRows(@Param("userId") int userId);

}
