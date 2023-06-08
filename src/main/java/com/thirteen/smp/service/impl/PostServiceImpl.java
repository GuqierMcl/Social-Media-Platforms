package com.thirteen.smp.service.impl;

import com.thirteen.smp.exception.PostNotExistException;
import com.thirteen.smp.mapper.*;
import com.thirteen.smp.pojo.Favorite;
import com.thirteen.smp.pojo.Post;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.PostService;
import com.thirteen.smp.utils.AccessTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PostServiceImpl implements PostService {

    // TODO 张力文 实现帖子业务接口
    @Autowired
    private CommentMapper commentMapper;// 使用Spring自动注入工具类
    @Autowired
    private UserMapper userMapper;// 使用Spring自动注入工具类
    @Autowired
    private PostMapper postMapper;// 使用Spring自动注入工具类
    @Autowired
    private LikeMapper likeMapper;// 使用Spring自动注入工具类

    @Autowired
    private FollowMapper followMapper;// 使用Spring自动注入工具类

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Override
    public Map<String, Object> getPostById(int userId, int postId) throws PostNotExistException {
        List<Post> posts = null;
        posts = postMapper.selectAllPost();
        Map<String, Object> result=null;
        for(Post post:posts){
            if(post.getPostId()==postId){
                User user = userMapper.selectById(post.getUserId());
                result = new LinkedHashMap<>();
                result.put("content", post.getContent());
                result.put("img", post.getImg());
                result.put("profilePic", userMapper.selectById(post.getUserId()).getProfilePic());
                result.put("userId", post.getUserId());
                result.put("name",user.getNickname());
                result.put("postId", post.getPostId());
                result.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(post.getPostTime()));
                System.out.println(post.getPostTime());
                result.put("likeNum", post.getLikeNum());
                result.put("isLike", likeMapper.judgeLiked(post.getPostId(), userId) != 0);
                result.put("commentNum",commentMapper.selectCountByPostId(post.getPostId()));
                Favorite favorite = new Favorite();
                favorite.setPostId(post.getPostId());
                favorite.setUserId(userId);
                result.put("isStaring",favoriteMapper.selectByUserIdAndPostId(favorite)!=null);
                break;
            }
        }
        if(result!=null){
            return result;
        }else {
            throw new PostNotExistException("帖子不存在");
        }
    }

    @Override
    @Transactional // 启用事务
    public int savePost(Post post) throws PostNotExistException {
        if(post.getContent()==null||post.getContent().equals("")){
            return -1; //表示帖子内容为空
        }
        post.setPostTime(new Timestamp(new Date().getTime()));
        int count =  postMapper.insertPost(post);

        // 自定义的异常必须继承RuntimeException才会被事务管理器回滚
        if(count==0) {//为0表示添加事务出错
            throw new PostNotExistException("添加帖子失败");
        }
        else {
            return count;
        }

    }

    @Override
    @Transactional // 启用事务
    public int deletePost(int postId) throws PostNotExistException {
        int count=0;
        count = postMapper.deletePostByPostId(postId);
        if(count==0){
            throw new PostNotExistException("删除帖子失败");
        } else {
            return count;
        }
    }

    @Override
    public List<Map<String,Object>> getPostSelf(int userId, HttpServletRequest request) throws PostNotExistException {
        List<Post> posts = null;
        posts = postMapper.selectByUserId(userId);
        if(posts==null||posts.size()==0){
            throw new PostNotExistException("该用户没有发布帖子");
        } else{
            List<Map<String,Object>> results=new ArrayList<>();
            posts.forEach(post->{
                User user = userMapper.selectById(post.getUserId());
                Map<String,Object> result=new LinkedHashMap<>();
                result.put("content",post.getContent());
                result.put("img",post.getImg());
                result.put("profilePic",userMapper.selectById(post.getUserId()).getProfilePic());
                result.put("userId",post.getUserId());
                result.put("name",user.getNickname());
                result.put("postId",post.getPostId());
                result.put("date",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(post.getPostTime()));
                result.put("likeNum",post.getLikeNum());
                result.put("isLike",likeMapper.judgeLiked(post.getPostId(), AccessTokenUtil.getUserId(request))!=0);
                result.put("commentNum",commentMapper.selectCountByPostId(post.getPostId()));
                Favorite favorite = new Favorite();
                favorite.setPostId(post.getPostId());
                favorite.setUserId(userId);
                result.put("isStaring",favoriteMapper.selectByUserIdAndPostId(favorite)!=null);
                results.add(result);
            });
            return results;
        }

    }

    @Override
    public List<Map<String,Object>> getPostSelfFollow(int userId) throws PostNotExistException {
        List<Post> posts = null;
        posts = postMapper.selectAllPost();
        List<User> Follows = followMapper.selectByFollowerUserId(userId);
        List<Integer> followIds = new ArrayList<>();
        Follows.forEach(follow->{
            followIds.add(follow.getUserId());
        });
        List<Post> finalPosts = new ArrayList<>();
        int count=3;
        for(int i=0;i<posts.size();i++){
            if(followIds.contains(posts.get(i).getUserId())||posts.get(i).getUserId()==userId){
                finalPosts.add(posts.get(i));
            } else if(count>0){
                finalPosts.add(posts.get(i));
                count--;
            }
        }
        if(finalPosts.size()==0){
            throw new PostNotExistException("该用户未发布帖子且关注用户未发布帖子或者未关注其他用户");
        } else{
            List<Map<String,Object>> results=new ArrayList<>();
            finalPosts.forEach(post-> {
                User user = userMapper.selectById(post.getUserId());
                Map<String, Object> result = new LinkedHashMap<>();
                result.put("content", post.getContent());
                result.put("img", post.getImg());
                result.put("profilePic", userMapper.selectById(post.getUserId()).getProfilePic());
                result.put("userId", post.getUserId());
                result.put("name",user.getNickname());
                result.put("postId", post.getPostId());
                result.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(post.getPostTime()));
                System.out.println(post.getPostTime());
                result.put("likeNum", post.getLikeNum());
                result.put("isLike", likeMapper.judgeLiked(post.getPostId(), userId) != 0);
                result.put("commentNum",commentMapper.selectCountByPostId(post.getPostId()));
                Favorite favorite = new Favorite();
                favorite.setPostId(post.getPostId());
                favorite.setUserId(userId);
                result.put("isStaring",favoriteMapper.selectByUserIdAndPostId(favorite)!=null);
                results.add(result);
            });
            return results;
        }
    }

    @Override
    public List<Post> queryPost(String query) throws PostNotExistException {
        List<Post> posts = null;
        posts = postMapper.selectByQuery(query);
        if(posts.size()==0){
            throw new PostNotExistException("未搜索到相关帖子");
        } else{
            return posts;
        }
    }
    @Override
    public List<Post> queryPostSelf(String query,int userId) throws PostNotExistException {
        List<Post> posts = null;
        posts = postMapper.selectByQuerySelf(query,userId);
        if(posts.size()==0){
            throw new PostNotExistException("未搜索到相关帖子");
        } else{
            return posts;
        }
    }
}
