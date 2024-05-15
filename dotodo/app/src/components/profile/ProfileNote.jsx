export default function ProfileNote({ note, activeNoteId, setActiveNoteId }) {

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
        </div>
    );
}