import { useContext, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom"
import { findUserById } from "../../services/userService";
import { getSomeNotesByDueDateDesc } from "../../services/noteService";
import ProfileDetails from "./ProfileDetails";
import ProfileNotes from "./ProfileNotes";
import AuthContext from "../../contexts/authContext";

export default function Profile() {

    const { user } = useContext(AuthContext);
    const { id } = useParams();
    const navigate = useNavigate();

    const [profileDetails, setProfileDetails] = useState({});
    const [expiringNotes, setExpiringNotes] = useState([]);

    async function loadUser() {
        const userData = await findUserById(id);

        if (user._id === id && userData) {
            setProfileDetails({...userData})

            await getExpiringEvents(userData._id, 4)

        } else {
            navigate('/404');
        }
    }

    async function getExpiringEvents(userId, numberOfEvents) {
        const expiringEvents = await getSomeNotesByDueDateDesc(userId, numberOfEvents);

        setExpiringNotes(expiringEvents);
    }

    useEffect(() => {
        loadUser();
    }, [id, user._id]);

    return (
        <div className="content profile">
            <ProfileDetails profileDetails={profileDetails} />

            <ProfileNotes expiringNotes={expiringNotes} />

            <div className="latest-checklists"></div>
        </div>

    )
}