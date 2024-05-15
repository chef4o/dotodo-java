import { addChecklist, deleteChecklist, getAllChecklists } from "../../services/checklistService";
import Checklist from "./Checklist";
import { useContext, useEffect, useState } from "react";
import NoAccess from "../error/NoAccess";
import NoContent from "../error/NoContent";
import EmptyChecklist from "./EmptyChecklist";
import NavContext from "../../contexts/navContext";
import AuthContext from "../../contexts/authContext";

export default function Checklists() {

    const { handleNavigationClick } = useContext(NavContext);
    const { user } = useContext(AuthContext);

    const [checklists, setChecklists] = useState([]);
    const [makeNew, setMakeNew] = useState(false);

    const [text, setText] = useState('');

    async function createChecklist(title) {
        if (text) {
            const newChecklist = {
                title: title,
                status: "Not started",
                events: {}
            };

            await addChecklist(user._id, newChecklist);
            setChecklists([...checklists, newChecklist]);
            setText('');
        }
    }

    const deleteChecklistHandler = async (id) => {
        await deleteChecklist(user._id, id);

        setChecklists(checklists.filter(checklist => checklist._id !== id));
    }

    useEffect(() => {
        if (user._id) {
            getAllChecklists(user._id)
                .then(setChecklists);
        }
    }, [user, checklists]);

    return (
        <div className="content checklists">
            {!user._id
                ? <NoAccess onItemClick={handleNavigationClick} />
                : checklists.length > 0
                    ? <ul className="checklists-list">
                        {checklists.map(checklist =>
                            <Checklist 
                            key={checklist._id} 
                            checklist={checklist}
                            setChecklists={setChecklists}
                            deleteChecklist={deleteChecklistHandler} />
                        )}
                        <EmptyChecklist setChecklists={setChecklists}></EmptyChecklist>
                    </ul>
                    : <div className="no-items">

                        <NoContent />

                        {!makeNew
                            ? <button className="add-checklist" onClick={() => setMakeNew(true)}>Create</button>
                            : <div className="new-checklist">
                                <input
                                    className="new-checklist-title"
                                    value={text} placeholder="Checklist title"
                                    onChange={e => setText(e.target.value)} />
                                <button className="add-checklist" onClick={() => createChecklist(text)}>Add</button>
                            </div>
                        }
                    </div>
            }
        </div>
    )
}