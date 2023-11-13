import { useEffect, useState } from "react";
import "./App.css";

import {
  HashRouter,
  Link,
  Route,
  Routes,
  useNavigate,
  useParams,
} from "react-router-dom";
import Dropdown from "react-bootstrap/Dropdown";
import Select from "react-select";

// List all messages
function ListChats({ setChat }) {
  const navigate = useNavigate();
  const { userId } = useParams();
  const [loading, setLoading] = useState(true);
  const [chats, setChats] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      const data = await fetch("/api/users/chat/" + userId);
      setChats(await data.json());
      setLoading(false);
    };
    fetchData().catch(console.error);
  }, []);

  function handleClick(chat) {
    setChat(chat);
    navigate("/chat");
  }

  if (loading) {
    return (
      <div>
        Loading...
        <br />
        <Link to={"/"}> Something went wrong, back to main page </Link>
      </div>
    );
  }
  return (
    <div>
      {chats.map((m) => (
        <button
          style={{ border: "1px solid white" }}
          onClick={() => handleClick(m)}
        >
          <h2>{m.subject}</h2>
          {m.creator.username}
        </button>
      ))}
    </div>
  );
}

// User page
function UserInfo({ users, setChat, setActiveUser }) {
  const { userId } = useParams();


  const user = users.filter((u) => u.userId === parseInt(userId))[0];
  //console.log(JSON.stringify(users));
  //console.log("Selected user: " + JSON.stringify(user));
  setActiveUser(user);
  return (
    <>
      <h2>Welcome</h2>
      <h1>{user.username}</h1>
      <p>Mail:{user.email}<br/>Phone Number:{user.phonenumber}</p>
      <Link to={"alterUser"}>Change user credentials</Link>
      <h1>All your chats</h1>
      <ListChats setChat={setChat} />
      <Link to={"addnewchat"}> Add new chat</Link>
    </>
  );
}


// Main page
function FrontPage({ users }) {
  //console.log("users");

  return (
    <div>
      <h1>Welcome to the chat</h1>

      {/*meny for valg av bruker*/}
      <Dropdown>
        <Dropdown.Toggle variant="success" id="dropdown-basic">
          Choose your user
        </Dropdown.Toggle>

        <Dropdown.Menu id={"dropbox"}>
          {users.map((u) => (
            <>
              <Dropdown.Item href={"User"}>
                <Link to={"/" + u.userId}>{u.username}</Link>
              </Dropdown.Item>
              <Dropdown.Divider />
            </>
          ))}
        </Dropdown.Menu>
      </Dropdown>
    </div>
  );
}

// Add a new message
function NewMessage({ users, chat }) {
  const navigate = new useNavigate();
  const [body, setBody] = useState("");
  const [title, setTitle] = useState("");
  const [receiverId, setReceiverId] = useState();
  const chatId = chat.chatId;
  const { senderId } = useParams();

  async function handleSubmit(e) {
    e.preventDefault();
    await fetch("/api/messages", {
      method: "post",
      body: JSON.stringify({ title, body, receiverId, senderId, chatId }),
      headers: {
        "Content-Type": "application/json",
      },
    });
    navigate("/" + senderId);
  }

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <div>
          <label>
            Title:{" "}
            <input type="text" onChange={(e) => setTitle(e.target.value)} />
          </label>
        </div>
        <div>
          <label>
            body:{" "}
            <input type="text" onChange={(e) => setBody(e.target.value)} />
          </label>
        </div>
        <button>Submit</button>
      </form>
    </div>
  );
}

// list out message on user
function ListMessages({ chat, activeUser }) {
  const [message, setMessage] = useState();
  const [member, setMember] = useState();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      fetch("/api/chats/message/" + chat.chatId),
      fetch("/api/chats/member/" + chat.chatId),
    ])
        .then(([resUsers, resMessages]) =>
            Promise.all([resUsers.json(), resMessages.json()])
        )
        .then(([dataMessages, dataUsers]) => {
          console.log(dataUsers);
          // console.log(dataMessages);
          setMessage(dataMessages);
          setMember(dataUsers);
          setLoading(false);
        });
  }, []);

  if (loading) return <div>Loading</div>;

  return (
      <div>
        <div>
          {member.map((u) => (
              <p>Chat member: {u.username} </p>
          ))}
        </div>
        {message.map((m) => (
            <div style={{border: "1px solid white"}}>
              <h4>Subject: {m.subject}</h4>
              <p style={{fontWeight: "bold"}}>from: {m.user.username}</p>
              <div>
                <h4 style={{fontWeight: "bold"}}>Message: </h4> {m.body}
              </div>

            </div>
        ))}
        <Link to={"/" + activeUser.userId + "/message/new"}>New message</Link>
      </div>
  );
}
// add a new chat and select user
  function AddNewChat({users, chat}) {
    const navigate = useNavigate();
    const {creatorId} = useParams();
    const [message, setMessage] = useState();
    const [subject, setSubject] = useState();
    const [userId, setUserId] = useState([]);

    async function handleSubmit(e) {
      e.preventDefault();
      console.log(userId)
      await fetch("/api/chats", {
        method: "POST",
        body: JSON.stringify({creatorId, userId, subject, message}),
        headers: {
          "Content-Type": "application/json",
        },
      });
      navigate("/" + creatorId);
    }

    return (
        <>
          <h2>Start chat, choose one or more receivers</h2>
          <div>
            <form onSubmit={handleSubmit}>
              <div>
                <label>
                  Subject:{" "}
                  <input type="text" onChange={(e) => setSubject(e.target.value)}/>
                </label>
              </div>
              <div>
                <label>
                  Message:{" "}
                  <input type="text" onChange={(e) => setMessage(e.target.value)}/>
                </label>
              </div>
              <div>
                <center>
                  Choose receiver:{" "}
                  <Select options={users}
                          onChange={setUserId}
                          getOptionLabel={u => u.username}
                          getOptionValue={u => u.userId}
                          isMulti
                          id={"Select"}
                  ></Select>
                </center>
              </div>

              <button>Submit</button>
            </form>
          </div>
        </>
    );
  }


  function AlterUserInformation({users}) {
    const navigate = useNavigate();
    const [username, setUsername] = useState();
    const [email, setEmail] = useState();
    const [phonenumber, setPhonenumber] = useState();
    const {userId} = useParams();
    const [loading, setLoading] = useState(true);
    const [chats, setChats] = useState([]);

    async function handleSubmit(e) {
      e.preventDefault();
      console.log(userId);
      await fetch("/api/users/user/" + userId, {
        method: "POST",
        body: JSON.stringify({username, email, phonenumber}),
        headers: {
          "Content-Type": "application/json",
        },
      });
      navigate("/" + userId);
    }

    return (
        <div>
          <form onSubmit={handleSubmit}>
            <div>
              <label>
                Username:{" "}
                <input defaultValue={users.username} type="text"
                       onChange={(e) => setUsername(e.target.value)}/>

              </label>
            </div>
            <div>
              <label>
                Email:{" "}
                <input defaultValue={users.email} type="text"
                       onChange={(e) => setEmail(e.target.value)}/>
              </label>
            </div>
            <div>
              <label>
                Phone Number:{" "}
                <input defaultValue={users.phonenumber} type="text"
                       onChange={(e) => setPhonenumber(e.target.value)}/>
              </label>
            </div>
            <button>Submit</button>
          </form>
        </div>
    );
  }


  function App() {
    const [users, setUsers] = useState([]);
    const [chat, setChat] = useState([]);
    const [activeUser, setActiveUser] = useState();
    const [loading, setLoading] = useState(true);

    useEffect(() => {
      const fetchData = async () => {
        const res = await fetch("/api/users");
        const json = await res.json();
        setUsers(json);
        setLoading(false);
        // console.log(users);
      };
      fetchData().catch(console.error);
    }, []);

    if (loading) return <div>Loading</div>;

    return (
        <HashRouter>
          <Routes>
            <Route path={"/"} element={<FrontPage users={users}/>}></Route>
            <Route
                path={"/:userId"}
                element={
                  <UserInfo
                      users={users}
                      setChat={setChat}
                      setActiveUser={setActiveUser}
                  />
                }
            ></Route>
            <Route
                path={"/:userId/messenger/add"}
                element={<ListChats setChat={setChat}/>}
            ></Route>
            <Route
                path={"/chat"}
                element={<ListMessages chat={chat} activeUser={activeUser}/>}
            ></Route>
            <Route
                path={"/:senderId/message/new"}
                element={<NewMessage users={users} chat={chat}/>}
            ></Route>
            <Route
                path={"/:creatorId/addnewchat"}
                element={<AddNewChat users={users} chat={chat}/>}
            ></Route>
            <Route
                path={"/:userId/alterUser"}
                element={<AlterUserInformation users={users}/>}
            ></Route>
          </Routes>
        </HashRouter>
    );
  }

export default App;

