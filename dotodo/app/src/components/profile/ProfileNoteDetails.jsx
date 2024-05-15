export default function ProfileNoteDetails({ note, activeNoteId, setActiveNoteId }) {

    const handleXmarkClick = (event) => {
        event.stopPropagation();
        setActiveNoteId("");
    };

    return (
        <div className={activeNoteId === note._id ? "note-details active" : "note"} onClick={() => setActiveNoteId(note._id)}>
            {activeNoteId === note._id &&
                <button className="xmark" onClick={handleXmarkClick}>
                    <i className="fa-solid fa-xmark" />
                </button>}

            <div className="heading-row">
                <h3>{note.title}</h3>
                <p>{`Status: ${note.isArchived ? 'Archived' : 'Active'}`}</p>
            </div>

            <div className="dates-row">
                <p>{`Initiated on: ${note.startDate}`}</p>
                {!note.completedOn
                    ? <p>{`Due date: ${note.dueDate}`}</p>
                    : <p>{`Completed on: ${note.completedOn}`}</p>}
            </div>

            <p className="expiring-notes-content">{note.content}</p>
        </div>
    );
}