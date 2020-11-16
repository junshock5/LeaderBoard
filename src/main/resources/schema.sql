DROP TABLE IF EXISTS user;

CREATE TABLE user
(
    `id`              BIGINT          not null AUTO_INCREMENT COMMENT '아이디',
    `matchMakerRank`  INT             NULL        COMMENT '점수',
    `rank`            INT             NULL        COMMENT '순위',
    `tier`            varchar(100)    NULL        COMMENT '계급',
    PRIMARY KEY (id)
);