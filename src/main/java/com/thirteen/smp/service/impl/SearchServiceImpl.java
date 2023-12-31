package com.thirteen.smp.service.impl;

import com.thirteen.smp.mapper.*;
import com.thirteen.smp.pojo.Favorite;
import com.thirteen.smp.pojo.Post;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.SearchService;
import com.thirteen.smp.utils.AnsjUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    PostMapper postMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    LikeMapper likeMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    FollowMapper followMapper;

    @Autowired
    FavoriteMapper favoriteMapper;
    @Override
    public Map<String, Object> globalSearch(String query, Integer userId) {
        Map<String,Object> datas = new LinkedHashMap<>();
        List<Post> posts = postMapper.selectByQuery("");
        List<User> users = userMapper.selectByQuery("");
        List<Map<String,Object>> finalPosts =new ArrayList<>();
        List<String> querys = AnsjUtil.startParticiple(query);
        posts.forEach(post -> {
            for(String q : querys){
                if(post.getContent().contains(q)){
                    User user = userMapper.selectById(post.getUserId());
                    Map<String,Object> data = new LinkedHashMap<>();
                    data.put("content",post.getContent());
                    data.put("img",post.getImg());
                    data.put("profilePic",user.getProfilePic());
                    data.put("userId",post.getUserId());
                    data.put("name",user.getNickname());
                    data.put("postId",post.getPostId());
                    SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    data.put("date",dateformat.format(post.getPostTime()));
                    data.put("likeNum",post.getLikeNum());
                    data.put("isLike",likeMapper.judgeLiked(post.getPostId(), userId)!=0);
                    data.put("commentNum",commentMapper.selectByPostId(post.getPostId()).size());
                    Favorite favorite = new Favorite();
                    favorite.setPostId(post.getPostId());
                    favorite.setUserId(userId);
                    data.put("isStaring",favoriteMapper.selectByUserIdAndPostId(favorite)!=null);
                    finalPosts.add(data);
                    break;
                }
            }
        });
        List<Map<String,Object>> finalUsers =new ArrayList<>();
        users.forEach(user -> {
            for (String q:querys){
                if(user.getUsername().contains(q)||user.getNickname().contains(q)){
                    Map<String,Object> data = new LinkedHashMap<>();
                    data.put("coverPic",user.getCoverPic());
                    data.put("email",user.getEmail());
                    data.put("facebook",user.getFacebook());
                    data.put("nickname",user.getNickname());
                    data.put("password",user.getPassword());
                    data.put("profilePic",user.getProfilePic());
                    data.put("qq",user.getQq());
                    data.put("twitter",user.getTwitter());
                    data.put("userId",user.getUserId());
                    data.put("userLang",user.getUserLang());
                    data.put("userLocation",user.getUserLocation());
                    data.put("username",user.getUsername());
                    data.put("weibo",user.getWeibo());
                    data.put("isFollowing",followMapper.selectByUserId(userId,user.getUserId())!=null);
                    finalUsers.add(data);
                }
            }
        });
        Collections.reverse(finalPosts);
        datas.put("posts",finalPosts);
        datas.put("users",finalUsers);
        return datas;
    }
}
