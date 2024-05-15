import { useContext, useState } from "react";
import { editChecklistItem } from "../../services/checklistService";
import AuthContext from "../../contexts/authContext";

export default function ChecklistItem({ task, checklist, deleteTask }) {

    const { user } = useContext(AuthContext);

    const [formValues, setFormValues] = useState({ ...task });

    const changeHandler = (e) => {
        setFormValues(state => ({
            ...state,
            [e.target.name]: e.target.value,
        }));
    }

    const updateValue = async () => {
        await editChecklistItem(user._id, checklist._id, task._id, formValues)
    }

    const handleTickChange = async () => {
        const newStatus = formValues.taskStatus !== 'Done' ? 'Done' : 'In Progress';
        setFormValues((state) => ({
            ...state,
            taskStatus: newStatus
        }));
        await editChecklistItem(user._id, checklist._id, task._id, { status: newStatus });
    };

    return (
        <form method="post" className="todo-item">
            <input
                className="tick"
                type="checkbox"
                checked={formValues.taskStatus === 'Done'}
                onChange={handleTickChange}
            />
            <input
                className={`todo-item-text ${formValues.taskStatus === 'Done' ? 'done' : ''}`}
                id="content"
                name="content"
                type="text"
                value={formValues.content}
                onChange={changeHandler}
                onBlur={updateValue}
            />
            <button className="delete" onClick={() => deleteTask(task._id)}>
                <i className="fa-solid fa-square-minus" />
            </button>
        </form>
    );
}