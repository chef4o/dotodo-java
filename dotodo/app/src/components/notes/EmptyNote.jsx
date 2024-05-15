import { useContext, useState } from "react";
import AuthContext from "../../contexts/authContext";
import { addNote } from "../../services/noteService";

export default function EmptyNote({ setNotes }) {
    const [text, setText] = useState('');
    const [title, setTitle] = useState('');

    const { user } = useContext(AuthContext);

    async function createNote() {
        if (title) {
            const newNote = {
                title: title,
                content: text,
                startDate: new Date()
            };

            await addNote(user._id, newNote);
            setNotes((state) => [...state, newNote]);
            setTitle('');
            setText('');
        }
    }

    const handleTitleChange = (e) => {
        setTitle(e.target.value);
    };

    return (
        <div className="note new">
            <input className="title" value={title} placeholder="Add title" onChange={handleTitleChange} />

            <input value={text} className="note-text" placeholder="Add text" onChange={e => setText(e.target.value)} />

            <button className="create" onClick={createNote}>Create</button>
        </div>
    );
}
