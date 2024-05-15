export default function ProfileDetails({ profileDetails, activeNoteId, setActiveNoteId }) {

    const formatAddress = (address) => {
        return Object.values(address)
            .filter(value => value)
            .join(', ');
    };

    return (
        <div className="user-details">
            <div className="profile-details">
                <div className="personal">
                    {(profileDetails.firstName || profileDetails.lastName) &&
                        <p className="name">{`Name: ${profileDetails.firstName} ${profileDetails.lastName}`}</p>}
                    <p className="username">{`Username: ${profileDetails.username}`}</p>
                    {profileDetails.dateOfBirth &&
                        <p className="dateOfBirth">{`Birthday: ${profileDetails.dateOfBirth}`}</p>}
                </div>

                <div className="profile-image">
                    <img src={profileDetails.imageUrl} alt="" />
                </div>

                <div className="contact">
                    <p className="email">{`Email: ${profileDetails.email}`}</p>
                    {profileDetails.phoneNumber &&
                        <p className="phoneNumber">{`Phone: ${profileDetails.phoneNumber}`}</p>}
                    {profileDetails.address &&
                        <p className="address">{`Address: ${formatAddress(profileDetails.address)}`}</p>}
                </div>
            </div>
        </div>
    )
}