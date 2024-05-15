import PropTypes from "prop-types";
import { useContext } from "react";
import { Link } from "react-router-dom";
import NavContext from "../../contexts/navContext";

export default function TaskType({ name, headerText }) {

  const { handleNavigationClick } = useContext(NavContext);

    return (
        <Link to={name} onClick={() => handleNavigationClick(name.toLocaleLowerCase())}>
            <li className={name.toLocaleLowerCase()}>
                <h4>{name}</h4>
                <h3 className="header-text">{headerText}</h3>
            </li>
        </Link>
    )
}

TaskType.propTypes = {
    name: PropTypes.string.isRequired,
    headerText: PropTypes.string.isRequired,
};