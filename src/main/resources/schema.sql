create table if not exists USERS
(
    USER_ID bigint generated always as identity primary key,
    NAME    varchar(50) not null,
    EMAIL   varchar(50) not null unique
    );

create table if not exists ITEMS
(
    ITEM_ID      bigint generated always as identity primary key,
    NAME         varchar(50) not null,
    DESCRIPTION  varchar(255) not null,
    IS_AVAILABLE boolean not null,
    OWNER_ID     bigint references USERS (USER_ID)
    );

create table if not exists BOOKINGS
(
    BOOKING_ID   bigint generated always as identity primary key,
    ITEM_ID      bigint references ITEMS (ITEM_ID),
    BOOKER_ID    bigint references USERS (USER_ID),
    START_DATE   timestamp without time zone,
    END_DATE     timestamp without time zone,
    STATUS       varchar(20) not null
    );

create table if not exists COMMENTS
(
    COMMENT_ID bigint generated always as identity primary key,
    TEXT       varchar(255) not null,
    ITEM_ID    bigint references ITEMS (ITEM_ID),
    AUTHOR_ID  bigint references USERS (USER_ID),
    CREATED    timestamp without time zone
    );