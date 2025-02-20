CREATE TABLE member (
    id                int AUTO_INCREMENT   PRIMARY KEY,
    name              varchar(20)          NOT NULL,
    created_by        int                  NOT NULL,
    modified_by       int                  NULL,
    created_date      datetime             NOT NULL,
    modified_date     datetime             NOT NULL
) CHARSET = utf8mb4;