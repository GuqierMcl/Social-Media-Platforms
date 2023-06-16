package com.thirteen.smp.service.impl;

import com.thirteen.smp.exception.PostNotExistException;
import com.thirteen.smp.exception.PubBannedWordsException;
import com.thirteen.smp.mapper.*;
import com.thirteen.smp.pojo.Favorite;
import com.thirteen.smp.pojo.Msg;
import com.thirteen.smp.pojo.Post;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.PostService;
import com.thirteen.smp.service.global.GlobalVariables;
import com.thirteen.smp.utils.AccessTokenUtil;
import com.thirteen.smp.utils.BannedWordUtil;
import com.thirteen.smp.utils.ProvinceMapperUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PostServiceImpl implements PostService {

    // TODO 张力文 实现帖子业务接口
    @Autowired
    private ChatMapper chatMapper;
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

    /**
     * 基于用户城市/语言的协同过滤算法
     * @param userId
     * @return 帖子Id列表
     */
    public  List<Integer> userCF(int userId){
        User user = userMapper.selectById(userId);
        Map<String,Integer> cityNumber = new LinkedHashMap<>();
        List<User> users = userMapper.selectAll();
        List<Integer> postIds = new ArrayList<>();
        List<Favorite> relatedFavorite = new ArrayList<>();
        //初始化所有城市人数为0
        for(Map<String,Object> city:ProvinceMapperUtil.getProvinceMapList()){
            cityNumber.put((String) city.get("name"),0);
        }
        //获取距离其他省份的距离
        List<Map<String, Object>> nearestProvinceDistance = ProvinceMapperUtil.getNearestProvinceDistance(user.getUserLocation());
        //获取距离当前用户的城市和附近城市的用户列表
        //如果该用户城市距离当前用户较近，则纳入考虑范围,并且最多将会添加来自不同的几个城市的一百个用户
        Collections.shuffle(users);//打乱列表，增加随机性
        for (User u :users){
            for(int i=0;i< GlobalVariables.NearestProvinceNum+1;i++){
                if(u.getUserLocation().equals(nearestProvinceDistance.get(i).get("name"))&&
                        (cityNumber.replace(u.getUserLocation(),cityNumber.get(u.getUserLocation()))+1)<=100/GlobalVariables.NearestProvinceNum){
                    List<Favorite> favorites = favoriteMapper.selectByUserId(u.getUserId());
                    //添加推荐用户的收藏夹的帖子id
                    if(favorites.size()!=0){
                        cityNumber.replace(u.getUserLocation(),cityNumber.get(u.getUserLocation())+1);
                        Collections.shuffle(favorites);//打乱增加随机性
                        int cnt= GlobalVariables.recommendPostNum;//每个人最多提供推荐收藏帖子数量
                        for(Favorite favorite: favorites){
                            if(!postIds.contains(favorite.getPostId())){
                                postIds.add(favorite.getPostId());
                                cnt--;
                            }
                            if(cnt==0) break;
                        }
                    }
                    //添加推荐用户喜欢的帖子id
                    List<Post> posts = likeMapper.selectLikePostByUserId(user.getUserId());
                    if(posts.size()!=0){
                        Collections.shuffle(posts);//打乱增加随机性
                        int cnt = GlobalVariables.recommendPostNum;//每个人最多提供推荐点赞帖子数量
                        for (Post post :posts){
                            if(!postIds.contains(post.getPostId())){
                                postIds.add(post.getPostId());
                                cnt--;
                            }
                            if(cnt==0) break;
                        }
                    }
                    //添加推荐用户发布的帖子id
                    List<Post> posts1 = postMapper.selectByUserId(user.getUserId());
                    if(posts1.size()!=0){
                        Collections.shuffle(posts1);//打乱增加随机性
                        int cnt = GlobalVariables.recommendPostNum;//每个人最多提供推荐点赞帖子数量
                        for (Post post :posts1){
                            if(!postIds.contains(post.getPostId())){
                                postIds.add(post.getPostId());
                                cnt--;
                            }
                            if(cnt==0) break;
                        }
                    }
                }
                if(postIds.size()>100) break;
            }
        }
        if(postIds.size()<GlobalVariables.recommendPostNum*2)//如果帖子数量小于10个,启动基于语言的协同算法
        {
            for(User u: users){
                if(u.getUserLang().equals(user.getUserLang())){
                    List<Favorite> favorites = favoriteMapper.selectByUserId(u.getUserId());
                    //添加推荐用户的收藏夹的帖子id
                    if(favorites.size()!=0){
                        cityNumber.replace(u.getUserLocation(),cityNumber.get(u.getUserLocation())+1);
                        Collections.shuffle(favorites);//打乱增加随机性
                        int cnt= GlobalVariables.recommendPostNum;//每个人最多提供推荐收藏帖子数量
                        for(Favorite favorite: favorites){
                            if(!postIds.contains(favorite.getPostId())){
                                postIds.add(favorite.getPostId());
                                cnt--;
                            }
                            if(cnt==0) break;
                        }
                    }
                    //添加推荐用户喜欢的帖子id
                    List<Post> posts = likeMapper.selectLikePostByUserId(user.getUserId());
                    if(posts.size()!=0){
                        Collections.shuffle(posts);//打乱增加随机性
                        int cnt = GlobalVariables.recommendPostNum;//每个人最多提供推荐点赞帖子数量
                        for (Post post :posts){
                            if(!postIds.contains(post.getPostId())){
                                postIds.add(post.getPostId());
                                cnt--;
                            }
                            if(cnt==0) break;
                        }
                    }
                    //添加推荐用户发布的帖子id
                    List<Post> posts1 = postMapper.selectByUserId(user.getUserId());
                    if(posts1.size()!=0){
                        Collections.shuffle(posts1);//打乱增加随机性
                        int cnt = GlobalVariables.recommendPostNum;//每个人最多提供推荐点赞帖子数量
                        for (Post post :posts1){
                            if(!postIds.contains(post.getPostId())){
                                postIds.add(post.getPostId());
                                cnt--;
                            }
                            if(cnt==0) break;
                        }
                    }
                }
                if(postIds.size()>100) break;
            }
        }
        if(postIds.size()<GlobalVariables.recommendPostNum*2){
            List<Map<String, Object>> allPost = getAllPost(userId);
            int cnt = GlobalVariables.recommendPostNum*2-postIds.size();
            for(Map<String,Object> post:allPost){
                postIds.add((int)post.get("postId"));
                cnt--;
                if(cnt==0) break;
            }
        }
        Collections.shuffle(postIds);//再次随机，增加随机性
        return postIds.subList(0,GlobalVariables.recommendPostNum <= postIds.size() ? GlobalVariables.recommendPostNum : postIds.size());//随机返回五个经过推荐的帖子
    }

    public List<Map<String,Object>> getAllPost(int userId){
        List<Post> posts = postMapper.selectAllPost();
        List<Map<String,Object>> datas = new ArrayList<>();
        for(Post post:posts){
            Map<String,Object> result = new LinkedHashMap<>();
            User user = userMapper.selectById(post.getUserId());
            result = new LinkedHashMap<>();
            result.put("content", post.getContent());//帖子内容
            result.put("img", post.getImg());//帖子图片
            result.put("profilePic", userMapper.selectById(post.getUserId()).getProfilePic());//发布帖子用户的头像
            result.put("userId", post.getUserId());//发布帖子用户Id
            result.put("name",user.getNickname());//发布帖子用户名
            result.put("postId", post.getPostId());//帖子Id
            result.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(post.getPostTime()));//帖子发布时间
            result.put("likeNum", post.getLikeNum());//帖子点赞数
            result.put("isLike", likeMapper.judgeLiked(post.getPostId(), userId) != 0);//当前用户帖是否点赞帖子
            result.put("commentNum",commentMapper.selectCountByPostId(post.getPostId()));//帖子评论数
            Favorite favorite = new Favorite();
            favorite.setPostId(post.getPostId());
            favorite.setUserId(userId);
            result.put("isStaring",favoriteMapper.selectByUserIdAndPostId(favorite)!=null);//当前用户是否收藏帖子
            datas.add(result);
        }
        return datas;
    }

    @Override
    public Map<String, Object> getPostById(int userId, int postId) throws PostNotExistException {
        List<Map<String,Object>> datas = getAllPost(userId);
        Map<String,Object> result = null;
        for(Map<String,Object> data :datas){
            if((int)data.get("postId")==postId){
                result = data;
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
        try {
            if(BannedWordUtil.isBannedWord(post.getContent())){
                throw  new PubBannedWordsException("帖子内容不合法");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            List<Post> posts = postMapper.selectByUserId(post.getUserId());
            /**
             * 如果该用户是第一次发布帖子，则管理员会对其发送相关信息
             */
            if(posts.size()==1){
                Msg msg = new Msg();
                msg.setToUserId(post.getUserId());//发给第一次发帖的用户
                msg.setUserId(1);//用户id为1，即管理员账号，发起聊天
                msg.setContent("亲爱的" + userMapper.selectById(post.getUserId()).getNickname() + ":\n" +
                        "  你好呀！\n" +
                        "  欢迎来到SMP网络社区，您可以在这里发布您的日常生活，您的兴趣爱好，也可以查看其他用户所发布的帖子。\n" +
                        "  但是在本社区游玩的过程中，也需要遵守相关的规定。\n" +
                        "  规定如下:\n" +
                        "  1.帖子、评论内容禁止涉及黄赌毒\n" +
                        "  2.评论禁止引战\n" +
                        "  3.待后续补充\n" +
                        "  以上规则如有违反，社区将会对其进行封号处理！\n" +
                        "  祝您使用愉快，再见！");
                msg.setTime(new Timestamp(new Date().getTime()));
                msg.setIsRead(0);
                int cou =  chatMapper.insertMsg(msg);
                System.out.println(cou);
            }
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
    public List<Map<String,Object>> getPostSelf(int userId, int nowUserId) throws PostNotExistException {
        List<Map<String ,Object>> datas = getAllPost(nowUserId);
        List<Map<String ,Object>> results = new ArrayList<>();
        for(Map<String,Object> data :datas){
            if((int)data.get("userId")==userId){
                results.add(data);
            }
        }
        if(results.size()==0){
            throw new PostNotExistException("该用户没有发布帖子");
        }
        return results;
    }

    @Override
    public List<Map<String,Object>> getPostSelfFollow(int userId) throws PostNotExistException {
        List<Map<String,Object>> datas = getAllPost(userId);
        //获取当前用户关注用户id列表
        List<User> Follows = followMapper.selectByFollowerUserId(userId);
        List<Integer> followIds = new ArrayList<>();
        Follows.forEach(follow->{
            followIds.add(follow.getUserId());
        });
        List<Integer> recommendPostIds = userCF(userId);
        List<Map<String,Object>> finalPosts = new ArrayList<>();
        for(Map<String,Object> data:datas){
            if(followIds.contains(data.get("userId"))||(int)data.get("userId")==userId||recommendPostIds.contains((int)data.get("postId"))){//如果是当前用户关注用户或者是当前用户发布的帖子或者是经过推荐算法推荐的帖子则加入最终返回列表
                finalPosts.add(data);
            }
        }
        return finalPosts;
    }

}
