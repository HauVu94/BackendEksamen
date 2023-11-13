create table messages
(
    message_id          INTEGER IDENTITY primary key,
    message_subject     varchar(100),
    message_body        varchar(max),
    user_id             Integer references users(user_id),
    chat_id             Integer references chats(chat_id)
);


