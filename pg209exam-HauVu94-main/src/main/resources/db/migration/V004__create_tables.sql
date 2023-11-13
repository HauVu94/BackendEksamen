create table chat_members
(
    user_id INTEGER not null,
    chat_id INTEGER not null,
    PRIMARY KEY (user_id, chat_id),
    FOREIGN KEY (chat_id) references chats(chat_id),
    FOREIGN KEY (user_id) references users(user_id)
)