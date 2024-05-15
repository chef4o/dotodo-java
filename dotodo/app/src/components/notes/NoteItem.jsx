export default function NoteItem({ note, activeNoteId, setActiveNoteId, deleteNote }) {

    const handleXmarkClick = (event) => {
        event.stopPropagation();
        setActiveNoteId("");
    };

    return (
        <div className={activeNoteId === note._id ? "note active" : "note"} onClick={() => setActiveNoteId(note._id)}>
            {activeNoteId === note._id &&
                <button className="xmark" onClick={handleXmarkClick}>
                    <i className="fa-solid fa-xmark" />
                </button>}
            <h3>{note.title}</h3>
            <p>{note.content}</p>
            <button className="delete-btn" onClick={() => deleteNote(note._id)}>Delete</button>
        </div>
    );
}