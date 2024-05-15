export default function NoAccess({ user, onItemClick }) {
    return (
        <div className="error-page 403">
            <i className="fa-solid fa-triangle-exclamation"></i>
            <h1>403 Access Forbidded</h1>
            <p>You cannot access this page unless you
                <a className="login" onClick={() => onItemClick('login')}> log in </a>  or
                <a className="register" onClick={() => onItemClick('register')}> register </a>
            </p>
        </div>
    )
}