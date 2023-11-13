create table chats
(
    chat_id INTEGER IDENTITY primary key,
    subject varchar(100),
    creator_id integer references users(user_id)
);

