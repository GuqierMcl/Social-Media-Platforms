DROP DATABASE IF EXISTS smp;
CREATE DATABASE smp;
use smp;
CREATE TABLE `t_user`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(255) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `profile_pic` varchar(255) NULL COMMENT '用户头像图片',
  `nickname` varchar(255) NULL COMMENT '用户昵称',
  `twitter` varchar(255) NULL COMMENT '推特',
  `facebook` varchar(255) NULL COMMENT '脸书',
  `qq` varchar(255) NULL COMMENT 'QQ',
  `weibo` varchar(255) NULL COMMENT '微博',
  `email` varchar(255) NULL COMMENT '电子邮箱',
  `user_lang` varchar(255) NULL COMMENT '用户语言',
  `user_location` varchar(255) NULL COMMENT '用户地区',
  `cover_pic` varchar(255) NULL COMMENT '封面图片',
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `unique_username`(`username`) USING BTREE COMMENT '设置username唯一约束'
);
CREATE TABLE `t_post`  (
  `post_id` int NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `content` text  NOT NULL COMMENT '帖子内容',
  `post_time` datetime(0) NOT NULL COMMENT '发帖时间',
  `like_num` int NULL DEFAULT 0 COMMENT '点赞数量',
  `img` varchar(255)  DEFAULT NULL COMMENT '帖子图片',
  PRIMARY KEY (`post_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  FOREIGN KEY (`user_id`) REFERENCES `smp`.`t_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE `t_like`  (
  `post_id` int NOT NULL COMMENT '帖子ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `like_date` datetime NOT NULL NOT NULL COMMENT '点赞时间',
  PRIMARY KEY (`post_id`, `user_id`),
  FOREIGN KEY (`post_id`) REFERENCES `smp`.`t_post` (`post_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`user_id`) REFERENCES `smp`.`t_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE `t_comment` (
  `comment_id` int NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `post_id` int NOT NULL COMMENT '帖子ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `comment_content` varchar(255) NOT NULL COMMENT '评论内容',
  `comment_date` datetime NOT NULL COMMENT '评论时间',
  `pre_comment_id` int NULL DEFAULT NULL COMMENT '前置评论ID',
  PRIMARY KEY (`comment_id`),
  FOREIGN KEY (`user_id`) REFERENCES `smp`.`t_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`post_id`) REFERENCES `smp`.`t_post` (`post_id`) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE `t_follow`  (
  `follower_user_id` int NOT NULL COMMENT '关注用户ID',
  `followed_user_id` int NOT NULL COMMENT '被关注用户ID',
  `follow_time` datetime NOT NULL COMMENT '关注时间',
  PRIMARY KEY (`follower_user_id`, `followed_user_id`),
  FOREIGN KEY (`follower_user_id`) REFERENCES `smp`.`t_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`followed_user_id`) REFERENCES `smp`.`t_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
);