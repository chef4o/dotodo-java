import { useEffect, useState } from "react";
import { Routes, Route } from 'react-router-dom'
import Home from "./components/home/Home.jsx";
import AboutUs from "./components/AboutUs.jsx";
import News from "./components/News.jsx";
import Events from "./components/events/Events.jsx";
import Checklists from "./components/checklists/Checklists.jsx";
import Notes from "./components/notes/Notes.jsx";
import Nav from "./components/Nav.jsx";
import NotFound from "./components/error/NotFound.jsx";
import Footer from "./components/Footer.jsx";
import taskTypes from "./assets/taskTypes.js";
import { topNav, sideNav, bottomNav, connectNav } from "./assets/navElements.js"
import Contacts from "./components/Contacts.jsx";
import Profile from "./components/profile/Profile.jsx";
import NavContext from "./contexts/navContext.js";
import AuthContext from "./contexts/authContext.js";

const App = () => {

    const [selectedPageBg, setSelectedPageBg] = useState("home");

    const [showAuthModal, setShowAuthModal] = useState({
        login: false,
        register: false
    });
    const [user, setUser] = useState({});

    const guestAuthNavElements = topNav.filter(item => item.name != "logout");
    const loggedAuthNavElements = topNav.filter(item => item.name === "logout");
    const [topNavElements, setTopNavElements] = useState(guestAuthNavElements);

    const guestSideNavElements = sideNav.filter(item => item.name != "profile");
    const [sideNavElements, setSideNavElements] = useState(guestSideNavElements);

    const handleNavigationClick = (page) => {
        !topNav.map(item => item.name).includes(page) && setSelectedPageBg(page);

        if (page === 'logout') {
            setUser({});
        }

        setShowAuthModal((oldShowAuthModalState) => {
            return Object.keys(oldShowAuthModalState)
                .reduce((newShowAuthModalState, key) => {
                    newShowAuthModalState[key] = key === page;
                    return newShowAuthModalState;
                }, {});
        });
    };

    const hideAuthModalHandler = () => {
        setShowAuthModal({
            login: false,
            register: false
        });
    }

    useEffect(() => {
        if (user._id) {
            setTopNavElements(loggedAuthNavElements);
            setSideNavElements(sideNav);
        } else {
            setTopNavElements(guestAuthNavElements);
            setSideNavElements(guestSideNavElements);
        }
    }, [user]);

    return (
        <NavContext.Provider value={{ handleNavigationClick, selectedPageBg }}>
            <AuthContext.Provider value={{ user, setUser, showAuthModal, hideAuthModal: hideAuthModalHandler }}>
                <main className={selectedPageBg}>
                    <Nav topNav={topNavElements}
                        sideNavElements={sideNavElements}
                        connectNav={connectNav}
                        bottomNav={bottomNav} />

                    <Routes>
                        <Route path='/' element={<Home taskTypes={taskTypes} />} />
                        <Route path='/notes' element={<Notes />} />
                        <Route path='/checklists' element={<Checklists />} />
                        <Route path='/events' element={<Events />} />
                        <Route path='/profile/:id' element={<Profile />} />
                        <Route path='/about' element={<AboutUs />} />
                        <Route path='/contacts' element={<Contacts />} />
                        <Route path='/news' element={<News />} />
                        <Route path='*' element={<NotFound />} />
                    </Routes>

                    <Footer />
                </main>
            </AuthContext.Provider>
        </NavContext.Provider>
    );
}

export default App;