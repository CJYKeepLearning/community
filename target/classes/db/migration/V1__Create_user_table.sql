create table USER
(
    ID           INT auto_increment,
    NAME         VARCHAR(50),
    TOKEN        CHAR(36),
    GMT_CREATE   BIGINT,
    GMT_MODIFIED BIGINT,
    ACCOUNT_ID   VARCHAR(100),
    constraint USER_PK
    primary key (ID)
);