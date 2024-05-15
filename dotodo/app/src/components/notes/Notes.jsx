import { useContext, useEffect, useState } from "react"
import { addNote, deleteNote, getAllNotes } from "../../services/noteService";
import NoteItem from "./NoteItem";
import NoAccess from "../error/NoAccess";
import NavContext from "../../contexts/navContext";
import AuthContext from "../../contexts/authContext";
import NoContent from "../error/NoContent";
import EmptyNote from "./EmptyNote";

export default function Notes() {

    const { handleNavigationClick } = useContext(NavContext);
    const { user } = useContext(AuthContext);

    const [notes, setNotes] = useState([]);
    const [activeNoteId, setActiveNoteId] = useState('');
    const [makeNew, setMakeNew] = useState(false);
    const [text, setText] = useState('');

    async function createNote(title) {
        if (text) {
            const newNote = {
                title: title,
                status: "Not started",
                events: {}
            };

            await addNote(user._id, newNote);
            setNotes([...notes, newNote]);
            setText('');
        }
    }

    const deleteNoteHandler = async (id) => {
        await deleteNote(user._id, id);

        setNotes(notes.filter(note => note._id !== id));
    }

    useEffect(() => {
        if (user._id) {
            getAllNotes(user._id)
                .then(data => {
                    setNotes(data);
                });
        }
    }, [user]);

    return (
        <div className="content notes">

            {!user._id
                ? <NoAccess onItemClick={handleNavigationClick} />
                : notes.length > 0
                    ? <ul className="notes-list">
                        {notes.map(item =>
                            <NoteItem key={item._id} note={item} 
                            activeNoteId={activeNoteId} setActiveNoteId={setActiveNoteId}
                            deleteNote={deleteNoteHandler} />
                        )}
                        <EmptyNote setNotes={setNotes}></EmptyNote>
                    </ul>
                    : <div className="new-note">
                        <NoContent />

                        {!makeNew
                            ? <button className="add-note" onClick={() => setMakeNew(true)}>Add</button>
                            : <div className="new-note">
                                <input value={text} placeholder="Note title" onChange={e => setText(e.target.value)} />
                                <button className="add-note" onClick={() => createNote(text)}>Add</button>
                            </div>
                        }
                    </div>
            }
        </div>
    )
}