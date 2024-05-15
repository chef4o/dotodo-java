import { useContext, useEffect, useState } from 'react';
import { createContact } from '../services/contactService';
import AuthContext from '../contexts/authContext';

export default function Contacts() {

    const { user } = useContext(AuthContext);

    const [error, setError] = useState('');
    const [messageSent, setMessageSent] = useState(false);
    const [formData, setFormData] = useState({ name: '', email: '', phone: '', comment: '' });

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    }

    const isFormValid = () => {
        return formData.name !== '' && formData.email !== '' && formData.comment !== '';
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        if (isFormValid()) {
            setMessageSent(true);
            setError('');
        } else {
            setMessageSent(false);
            setError('Name, email, and message are mandatory.');
        }
    }

    useEffect(() => {
        console.log(user)
        if (user) {
            setFormData({
                name: user.firstName || '', 
                email: user.email || '',
                phone: user.phoneNumber || '',
                comment: ''
            })
        }
    }, [])

    useEffect(() => {
        if (messageSent) {
            createContact(formData);
        }
    }, [messageSent]);

    return (
        <div className="content contact">
            <h2>Contact Me</h2>
            <p>Please use this form to contact me for any question, request or suggestion you might have.</p>

            {messageSent
                ? <div>
                    <i className="fa-solid fa-envelope-circle-check"></i>
                    <h3>Thank you for your message! <br /> I'll get back to you in due course.</h3>
                </div>

                : <form onSubmit={handleSubmit}>
                    {error
                        ? <div className={`error contact`}>{error}</div>
                        : <h4>Send Us A Quick Message</h4>}

                    <input
                        type="text"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        placeholder="Name *"
                    />

                    <input
                        type="text"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        placeholder="Email Address *"
                    />

                    <input
                        type="text"
                        name="phone"
                        value={formData.phone}
                        onChange={handleChange}
                        placeholder="Phone Number"
                    />
                    <textarea
                        name="comment"
                        value={formData.comment}
                        onChange={handleChange}
                        cols="30"
                        rows="10"
                        placeholder="Your Comment"
                    ></textarea>

                    <input type="submit" className="submit" value="Submit Message" />
                </form>
            }
        </div>
    )
}
