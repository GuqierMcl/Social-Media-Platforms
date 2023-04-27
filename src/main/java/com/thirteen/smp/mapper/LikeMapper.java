package com.thirteen.smp.mapper;

import java.sql.Timestamp;
import java.util.List;

/**
 * 数据库t_like表Mapper接口
 *
 * @author 张力文
 * @version 1.0
 * @since 1.0
 */

public interface LikeMapper {
    /**
     * 通过帖子ID获取点赞数
     * @param postid 帖子ID
     * @return 点赞数量
     */
    List<Integer> getLikeUserIdByPostId(int postid);

    int giveLike(int postid, int userid, Timestamp likeDate);

    int jugeLiked(int postId,int userid);

    int deleteLike(int postId,int userId);

}
