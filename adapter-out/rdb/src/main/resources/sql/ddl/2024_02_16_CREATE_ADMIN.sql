CREATE TABLE admin_role
(
    admin_role_id bigint       NOT NULL AUTO_INCREMENT COMMENT 'PK',
    users_id      bigint       NOT NULL COMMENT '어드민 ID',
    role          varchar(50)  NOT NULL COMMENT '가지고 있는 역할',
    state         varchar(20)  NOT NULL COMMENT '역할 상태',
    memo          varchar(100) NOT NULL COMMENT '역할 요청 시 메모',
    created_at    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '최근 수정 일시',
    PRIMARY KEY PK_adminrole (admin_role_id)
) COMMENT '어드민 페이지 권한 정보';
