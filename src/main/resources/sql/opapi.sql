CREATE TABLE op_developer (
	developer_id 		INT(11)  NOT NULL AUTO_INCREMENT,
   dev_name             varchar(32)                    null,
   dev_password         varchar(32)                    null,
   phone                varchar(32)                    null,
   email                varchar(128)                   null,
   status         		smallint(6)       not null default '1' comment '用户状态，1:有效，0:无效',
   create_date          datetime                       null,
   last_modified        timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (developer_id)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT '开发者信息'; 

create table op_other_platform_user 
(
   user_id              INT(11)  NOT NULL AUTO_INCREMENT,
   platform_name        varchar(128)  not null,
   user_login_name      varchar(128)  not null comment '其它平台用户的登录用户名',
   developer_id         int(11)       not null comment '开发者ID',
   create_date          datetime      null,
   last_modified        timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (user_id)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT '其它平台授权访问开放平台的用户信息'; 
CREATE INDEX idx_op_other_platform_user ON op_other_platform_user(developer_id);
CREATE unique INDEX idx_op_other_platform_user_2 ON op_other_platform_user(platform_name, user_login_name);

create table op_application 
(
   app_id               int(11)      not null AUTO_INCREMENT,
   app_key              varchar(128) not null,
   app_name             varchar(256) null,
   app_url              varchar(256) null,
   developer_id         int(11)      not null,
   create_date          datetime     null,
   last_modified        timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (app_id)
);
CREATE INDEX idx_op_application ON op_application(developer_id);
CREATE INDEX idx_op_application_key ON op_application(app_key);

create table op_app_token 
(
   token_id             int(11)      not null AUTO_INCREMENT,
   app_name             varchar(64)  not null comment '应用名称',
   app_user_key         varchar(32)  not null comment '应用用户的唯一主键',
   app_token            varchar(128) not null comment 'token 信息',
   app_secret           varchar(128) not null comment '密钥',
   create_date          datetime     null,
   last_modified        timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (token_id)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT 'app token 信息'; 
CREATE unique INDEX idx_op_app_token_1 ON op_app_token(app_name, app_user_key);
CREATE unique INDEX idx_op_app_token_2 ON op_app_token(app_token);

create table op_app_info 
(
   app_id               int(11)       not null AUTO_INCREMENT,
   app_name             varchar(64)   not null,
   app_description      varchar(512)  null,
   create_date          datetime      null,
   PRIMARY KEY (app_id)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT '注册的App信息'; 
CREATE unique INDEX idx_op_app_info_1 ON op_app_info(app_name);
