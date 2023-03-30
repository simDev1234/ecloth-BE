insert into member (member_id, email, member_role, member_status, nickname, password, x, y)
values (1, 'test1@gmail.com', 'ROLE_MEMBER', 'ACTIVE', 'test1', '$2a$12$VcE9BaX/L2vJwwFhy.DV3eRFyplF1zzx0OpASbUlkFkKhSC/JzCEO', 1, 2)
on duplicate key update member_id = 1;

insert into member (member_id, email, member_role, member_status, nickname, password, x, y)
values (2, 'test2@gmail.com', 'ROLE_MEMBER', 'ACTIVE', 'test2', '$2a$12$VcE9BaX/L2vJwwFhy.DV3eRFyplF1zzx0OpASbUlkFkKhSC/JzCEO', 1, 2)
on duplicate key update member_id = 2;

insert into member (member_id, email, member_role, member_status, nickname, password, x, y)
values (3, 'test3@gmail.com', 'ROLE_MEMBER', 'ACTIVE', 'test3', '$2a$12$VcE9BaX/L2vJwwFhy.DV3eRFyplF1zzx0OpASbUlkFkKhSC/JzCEO', 1, 2)
on duplicate key update member_id = 3;