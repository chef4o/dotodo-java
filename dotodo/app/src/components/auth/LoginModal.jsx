import { useContext, useEffect, useRef, useState } from "react";
import { isPasswordValid } from "../../controllers/userController";
import { formEmptyFieldsHandler, emptyField } from "../../controllers/errorController";
import { getUser } from "../../controllers/userController";
import AuthContext from "../../contexts/authContext";
import NavContext from "../../contexts/navContext";

export default function LoginModal() {

    const { selectedPageBg } = useContext(NavContext);
    const { setUser, hideAuthModal } = useContext(AuthContext);

    const FORM_FIELDS = {
        username: 'username',
        password: 'password',
    }

    const ERROR_FIELDS = {
        ...FORM_FIELDS,
        login: 'login',
    }

    const formInitialState = Object.fromEntries(Object.keys(FORM_FIELDS).map(key => [key, '']));
    const [formValues, setFormValues] = useState(formInitialState);

    const validationInitialState = Object.fromEntries(Object.keys(ERROR_FIELDS).map(key => [key, '']));
    const [validationErrors, setValidationErrors] = useState(validationInitialState);

    const validateLogin = async () => {

        formEmptyFieldsHandler(formInitialState, formValues, ['login'], setValidationErrors);

        const currentUser = await getUser(formValues.username);

        if (currentUser && isPasswordValid(currentUser.password, formValues.password)) {
            setValidationErrors(formInitialState);
            hideAuthModal();
            setUser({
                _id: currentUser._id,
                username: currentUser.username,
                firstName: currentUser.firstName,
                email: currentUser.email
            });
        } else {
            !emptyField(formValues) && !validationErrors.login && setValidationErrors(state => ({
                ...state,
                login: 'Username or password is wrong'
            }));
        }
    }

    const usernameInputRef = useRef();
    useEffect(() => { usernameInputRef.current.focus(); }, []);

    const changeHandler = (e) => {
        setFormValues(state => ({
            ...state,
            [e.target.name]: e.target.value,
        }));
    }

    const submitFormHandler = () => {
        validateLogin();
    }

    return (
        <form method="post" className={`auth-form ${selectedPageBg} login`} action="/login">
            <button className="xmark" onClick={() => hideAuthModal()}><i className="fa-solid fa-xmark" /></button>

            <div className="auth-fields">
                <div className={FORM_FIELDS.username}>
                    <label htmlFor={FORM_FIELDS.username}>Username/Email</label>
                    <div className="form-input">
                        <i className="fa-solid fa-user" />
                        <input
                            ref={usernameInputRef}
                            id={FORM_FIELDS.username}
                            name={FORM_FIELDS.username}
                            type="text"
                            value={formValues.username}
                            onChange={changeHandler}
                            className={validationErrors.username && "error-field"} />
                    </div>
                    {validationErrors.username && <div className={`error auth ${FORM_FIELDS.username}`}>{validationErrors.username}</div>}
                </div>

                <div className={FORM_FIELDS.password}>
                    <label htmlFor={FORM_FIELDS.password}>Password</label>
                    <div className="form-input">
                        <i className="fa-solid fa-key" />
                        <input
                            id={FORM_FIELDS.password}
                            name={FORM_FIELDS.password}
                            type="password"
                            value={formValues.password}
                            onChange={changeHandler}
                            className={validationErrors.password && "error-field"} />
                    </div>
                    {validationErrors.password && <div className={`error auth ${FORM_FIELDS.password}`}>{validationErrors.password}</div>}
                </div>
            </div>

            {validationErrors.login && <div className="error auth login">{validationErrors.login}</div>}

            <button type="button" className="login" onClick={() => submitFormHandler()}>Login</button>
        </form>
    )
}