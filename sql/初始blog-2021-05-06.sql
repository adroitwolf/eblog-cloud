-- 博客config表
CREATE TABLE `e_blog` (
  `ID` bigint(20) unsigned NOT NULL COMMENT '博客id',
  `BLOGGER_ID` bigint(20) unsigned NOT NULL COMMENT '作者id',
  `STATUS` varchar(10) NOT NULL DEFAULT '' COMMENT '博客状态',
  `TITLE` varchar(80) NOT NULL COMMENT '博客标题',
  `PICTURE_ID` bigint(20) DEFAULT NULL COMMENT '图片id',
  `SUMMARY` varchar(400) NOT NULL COMMENT '博客总结',
  `RELEASE_DATE` datetime NOT NULL COMMENT '发布日期',
  `NEAREST_MODIFY_DATE` datetime NOT NULL COMMENT '最近修改时间',
  `TAG_TITLE` varchar(1024) DEFAULT NULL COMMENT '标签',
  `COLUMN_10` char(10) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `BLOGGER_ID` (`BLOGGER_ID`,`TITLE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
--  博客内容表
CREATE TABLE `e_blog_content` (
  `ID` bigint(20) NOT NULL COMMENT '博客id',
  `CONTENT` longtext NOT NULL COMMENT '博客内容',
  `CONTENT_MD` longtext NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
-- 博客标签表
CREATE TABLE `e_blog_label` (
  `ID` bigint(20) unsigned NOT NULL COMMENT '标签id',
  `TITLE` varchar(20) NOT NULL COMMENT '标签名称',
  `CREATE_DATE` datetime NOT NULL COMMENT '创建日期',
  `CITE_NUM` int(11) NOT NULL COMMENT '引用人数',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `BLOGGER_ID_2` (`TITLE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
-- 博客日志表
CREATE TABLE `e_blog_log` (
  `ID` bigint(20) NOT NULL COMMENT '博客日志id',
  `ROMOTEIP` varchar(20) NOT NULL COMMENT '远程登陆ip',
  `ROMOTETIME` datetime NOT NULL,
  `OPERATION` varchar(255) DEFAULT NULL COMMENT '远程操作指令',
  `USERNAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- 博客状态表
CREATE TABLE `e_blog_status` (
  `ID` bigint(20) NOT NULL COMMENT '博客id',
  `CLICKCOUNT` int(11) DEFAULT NULL COMMENT '点击数',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='博客状态表 记录点赞数等';

-- 博客标签表
CREATE TABLE `e_blog_tag_map` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `TAG_ID` bigint(20) NOT NULL COMMENT '标签id',
  `BLOG_ID` bigint(20) NOT NULL COMMENT '博客id',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- 博客账号表
CREATE TABLE `e_account` (
                             `ID` bigint(20) unsigned NOT NULL COMMENT '用户id',
                             `PASSWORD` varchar(100) NOT NULL COMMENT '用户密码',
                             `REGISTER_DATE` datetime NOT NULL COMMENT '注册日期',
                             `EMAIL` varchar(50) DEFAULT NULL COMMENT '邮箱',
                             `IS_ENABLED` varchar(5) DEFAULT NULL COMMENT '账号是否可用',
                             `USERNAME` varchar(100) NOT NULL COMMENT '用户名',
                             PRIMARY KEY (`ID`) USING BTREE,
                             UNIQUE KEY `email` (`EMAIL`) USING BTREE COMMENT '注册邮箱'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;




-- 用户信息表
CREATE TABLE `e_user_profile` (
  `BLOGGER_ID` bigint(20) unsigned NOT NULL COMMENT '用户id',
  `ABOUT_ME` varchar(1024) DEFAULT NULL COMMENT '个人介绍',
  `NICKNAME` varchar(20) NOT NULL COMMENT '用户名',
  `AVATAR_ID` bigint(20) DEFAULT NULL COMMENT '用户头像',
  PRIMARY KEY (`BLOGGER_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- 初始化用户信息 admin / aa12356789
INSERT INTO `e_account` VALUES (576226178340225024, '989ceb5bd91b256d775b89af4ce1c7d1', '2021-04-04 23:18:12', '', 'YES', 'admin');
INSERT INTO `e_user_profile` VALUES (576226178340225024, NULL, 'ADMIN', NULL);


-- 用户图片表
CREATE TABLE `e_blogger_picture` (
  `ID` bigint(20) unsigned NOT NULL COMMENT '照片id',
  `BLOGGER_ID` bigint(20) DEFAULT NULL COMMENT '照片原作者id',
  `PATH` varchar(1024) NOT NULL COMMENT '物理硬盘地址',
  `TITLE` varchar(200) NOT NULL COMMENT '照片标题',
  `UPLOAD_DATE` datetime NOT NULL COMMENT '照片上传日期',
  `SUFFX` varchar(50) NOT NULL COMMENT '照片后缀',
  `SIZE` bigint(20) DEFAULT NULL COMMENT '照片规格大小',
  `WIDTH` int(11) DEFAULT NULL COMMENT '照片宽度',
  `HEIGHT` int(11) DEFAULT NULL COMMENT '照片高度',
  `UPDATE_DATE` datetime NOT NULL COMMENT '更新日期',
  `CITE_NUM` int(11) NOT NULL DEFAULT '0' COMMENT '被引用次数',
  `THUMB_PATH` varchar(2048) DEFAULT NULL COMMENT '文章缩略图',
  `MEDIA_TYPE` varchar(50) NOT NULL COMMENT '媒体类型',
  `FILE_KEY` varchar(200) NOT NULL COMMENT '物理磁盘的名称',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- 用户角色表
CREATE TABLE `e_role` (
  `ID` bigint(20) NOT NULL COMMENT '角色id',
  `ROLE_NAME` varchar(50) DEFAULT NULL COMMENT '角色名称',
  `ROLE_ZH` varchar(50) DEFAULT NULL COMMENT '角色中文名称',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `AK_KEY_2` (`ROLE_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- 角色初始化信息
INSERT INTO `e_role` VALUES (387055195109982208, 'ADMIN', '管理员');
INSERT INTO `e_role` VALUES (387055486085627904, 'USER', '用户');

-- 用户和角色的关联表
CREATE TABLE `e_role_map` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` bigint(20) NOT NULL COMMENT '用户id',
  `ROLE_ID` bigint(20) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='用户和角色的关联表';

-- 初始化角色用户关联表信息
INSERT INTO `eblog`.`e_role_map` (`USER_ID`, `ROLE_ID`) VALUES (576226178340225024, 387055486085627904)
    INSERT INTO `eblog`.`e_role_map` (`USER_ID`, `ROLE_ID`) VALUES (576226178340225024, 387055195109982208)

-- 用户评论表
CREATE TABLE `e_comments` (
  `id` bigint(20) NOT NULL COMMENT '评论表主键',
  `author_id` bigint(20) DEFAULT NULL COMMENT '作者id 用来统一管理',
  `object_id` bigint(20) DEFAULT NULL COMMENT '被评论的总资源id 一般都是博客id',
  `root` bigint(20) DEFAULT NULL COMMENT '层的评论id',
  `pid` bigint(20) DEFAULT '0' COMMENT '上一层评论的主键',
  `content` varchar(255) DEFAULT NULL COMMENT '评论的内容',
  `type` tinyint(4) DEFAULT NULL COMMENT '评论的类别 即对博客评论还是对博客评论',
  `from_id` bigint(20) DEFAULT NULL COMMENT '评论者的id',
  `to_id` bigint(20) DEFAULT NULL COMMENT '被评论者的id，如果这是一条父评论 则这个一定是null',
  `create_time` datetime DEFAULT NULL COMMENT '创建的时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新的时间',
  `ip_address` varchar(127) DEFAULT NULL COMMENT '评论者的ip地址',
  `is_del` tinyint(4) DEFAULT NULL COMMENT '评论是否被删除 0 没有删，2删除',
  `is_author` tinyint(4) DEFAULT NULL COMMENT '是否为作者0不是，1则是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='评论表';

