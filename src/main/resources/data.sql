insert into member (member_id, email, member_role, member_status, nickname, password, x, y)
values (1, 'test1@gmail.com', 'ROLE_MEMBER', 'ACTIVE', 'test1', '$2a$12$VcE9BaX/L2vJwwFhy.DV3eRFyplF1zzx0OpASbUlkFkKhSC/JzCEO', 1, 2)
on duplicate key update member_id = 1;

insert into member (member_id, email, member_role, member_status, nickname, password, x, y)
values (2, 'test2@gmail.com', 'ROLE_MEMBER', 'ACTIVE', 'test2', '$2a$12$VcE9BaX/L2vJwwFhy.DV3eRFyplF1zzx0OpASbUlkFkKhSC/JzCEO', 1, 2)
on duplicate key update member_id = 2;

insert into member (member_id, email, member_role, member_status, nickname, password, x, y)
values (3, 'test3@gmail.com', 'ROLE_MEMBER', 'ACTIVE', 'test3', '$2a$12$VcE9BaX/L2vJwwFhy.DV3eRFyplF1zzx0OpASbUlkFkKhSC/JzCEO', 1, 2)
on duplicate key update member_id = 3;

insert into chat_room (chat_room_id, register_date, update_date)
values (1, '2022-11-20', '2022-11-20') on duplicate key update chat_room_id = 1;

insert into chatroom_members (chat_room_id, member_id) values (1, 1);

insert into chatroom_members (chat_room_id, member_id) values (1, 2);