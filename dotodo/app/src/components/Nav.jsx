import { useContext } from "react";
import LoginModal from "./auth/LoginModal";
import RegisterModal from "./auth/RegisterModal";
import { Link } from 'react-router-dom'
import NavContext from "../contexts/navContext";
import AuthContext from "../contexts/authContext";

export default function Nav({ topNav, sideNavElements, connectNav, bottomNav }) {

  const { handleNavigationClick, selectedPageBg } = useContext(NavContext);
  const { user, showAuthModal, hideAuthModal } = useContext(AuthContext);

  return (

    <div className="nav">
      {(showAuthModal.login || showAuthModal.register) && <div className="backdrop" onClick={hideAuthModal} />}

      {showAuthModal.login && <LoginModal />}

      {showAuthModal.register && <RegisterModal />}

      <div className="top-bar">
        {user.firstName
          ? <p className="greeting">Hello, {user.firstName}</p>
          : user.username && <p className="greeting">Hello, {user.username}</p>}

        <ul className="top-bar-controls">
          {topNav.map(item =>
            <li key={item.name}><div onClick={() => handleNavigationClick(item.name)}>{item.name}</div></li>
          )}
        </ul>
      </div>

      <div className="sidebar">
        <div className="navigation">
          <Link to="/" id="logo" onClick={() => handleNavigationClick("home")}>
            <img src="/images/logo_dark.png" alt="DOTODO" />
          </Link>
          <ul className="menu">
            {sideNavElements.map(item =>
              <li key={item.name} className={selectedPageBg === item.name ? `${item.name} active` : item.name}>
                <Link to={item.name === 'profile' ? `${item.href}/${user._id}` : item.href}
                  onClick={() => handleNavigationClick(item.name)}>
                  <i className={item.icon}></i><br />{item.name}
                </Link>
              </li>
            )}
          </ul>
        </div>

        <div className="connect">
          {connectNav.map(item =>
            <a key={item.id} href={item.url} id={item.id} target="_blank" rel="noreferrer"><i className={item.icon}></i></a>
          )}
        </div>
      </div>

      <ul className="bottom-bar">
        {bottomNav.map(item =>
          <li key={item.name}>
            <Link to={item.name} onClick={() => handleNavigationClick(item.name)}>{item.name}</Link>
          </li>
        )}
      </ul>
    </div>
  );
}