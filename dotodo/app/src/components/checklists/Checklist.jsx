import { useContext, useEffect, useState } from "react";
import ChecklistItem from "./ChecklistItem";
import { addChecklistItem, deleteChecklistItem } from "../../services/checklistService";
import AuthContext from "../../contexts/authContext";

export default function Checklist({ checklist, deleteChecklist }) {

    const [activeChecklistId, setActiveChecklistId] = useState('');
    const [tasks, setTasks] = useState(checklist.tasks || []);
    const [newTodoItem, setNewTodoItem] = useState('');
    const [title, setTitle] = useState(checklist.title);

    const { user } = useContext(AuthContext);

    async function addTask(text) {
        if (text) {
            const newTask = {
                content: text,
                status: "Not started",
            };
            setTasks([...tasks, newTask]);
            setNewTodoItem('');
        }
    }

    async function deleteTask(taskId) {
        await deleteChecklistItem(user._id, checklist._id, taskId)
        setTasks(tasks.filter(task => task._id !== taskId));
    }

    useEffect(() => {
        if (checklist.elements) {
            setTasks(Object.values(checklist.elements));
        }
    }, [checklist.elements]);

    const handleXmarkClick = (event) => {
        event.stopPropagation();
        setActiveChecklistId("");
    };

    const handleTitleChange = (e) => {
        setTitle(e.target.value);
    };

    useEffect(() => {
        if (title) {
            checklist[title] = title;
        }

    }, [])

    const handleTitleBlur = () => {

    };

    return (
        <div className={activeChecklistId === checklist._id ? "checklist active" : "checklist"}
            onClick={() => setActiveChecklistId(checklist._id)}>
            {activeChecklistId === checklist._id &&
                <button className="xmark" onClick={handleXmarkClick}>
                    <i className="fa-solid fa-xmark" />
                </button>}

            <button className="delete-checklist" onClick={() => deleteChecklist(checklist._id)}>
                <i className="fa-solid fa-trash-can" />
            </button>

            <input className="title" value={title} onChange={handleTitleChange} onBlur={handleTitleBlur} />

            <div className="new-todo-item">
                <input className="new-todo-item-text" value={newTodoItem} onChange={e => setNewTodoItem(e.target.value)} />
                <button onClick={() => addTask(newTodoItem)}><i className="fa-solid fa-square-plus" /></button>
            </div>

            {tasks.map(task => (
                <ChecklistItem
                    key={task._id}
                    task={task}
                    checklist={checklist}
                    deleteTask={deleteTask} />
            ))}
        </div>
    );
}
