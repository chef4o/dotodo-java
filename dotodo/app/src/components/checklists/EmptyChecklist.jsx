import { useContext, useState } from "react";
import { addChecklist } from "../../services/checklistService";
import AuthContext from "../../contexts/authContext";

export default function EmptyChecklist({ setChecklists }) {
    const [tasks, setTasks] = useState([]);
    const [text, setText] = useState('');
    const [title, setTitle] = useState('');

    const { user } = useContext(AuthContext);

    async function createChecklist() {
        if (title) {

            const newChecklist = {
                title: title,
                status: "Not started",
                events: tasks
            };

            await addChecklist(user._id, newChecklist);
            setChecklists((state) => [...state, newChecklist]);
            setTitle('');
            setText('');
            setTasks([]);
        }
    }

    function addTask(text) {
        if (text) {
            const newTask = {
                content: text,
                status: "Not started",
            };
            setTasks((state) => [...state, newTask]);
            setText('');
        }
    }

    const handleTitleChange = (e) => {
        setTitle(e.target.value);
    };  

    return (
        <div className="checklist new">
            <input className="title" value={title} placeholder="Add title" onChange={handleTitleChange} />

            <div className="new-todo-item">
                <input className="new-todo-item-text" value={text} onChange={e => setText(e.target.value)} />
                <button onClick={() => addTask(text)}><i className="fa-solid fa-square-plus" /></button>
            </div>

            {Object.values(tasks).map(task =>
                <div key={task.content} className="todo-item">
                    <input
                        className="tick"
                        type="checkbox"
                    />
                    <p>{task.content}</p>
                </div>
            )}

            <button className="create" onClick={createChecklist}>Create</button>

        </div>
    );
}
