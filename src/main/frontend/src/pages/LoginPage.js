import { useEffect } from "react";
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import loginImage from '../images/login-image.jpg';
import { useIntl } from 'react-intl';
import { LoginForm } from "../components/LoginForm/LoginForm";

export const LoginPage = () => {
    const { formatMessage: f } = useIntl();
    const LOGIN_LABEL = f({ id: 'app.login' });

    const { currentUser } = useSelector(state => state.auth);
    const navigate = useNavigate();

    useEffect(() => {
        currentUser && navigate('/home');
    }, [currentUser, navigate]);

    return (
        <div className='page-container'>
            {/* Image */}
            <div className="col-md-6">
                <img
                    className="login-image"
                    src={loginImage}
                    alt="Imagen que representa un campo de fútbol" />
            </div>

            {/* Form */}
            <div className="col-sm-12 col-md-6 login-header">
                {/* TODO: add logo when its ready */}
                <h1>
                    {LOGIN_LABEL}
                </h1>

                <LoginForm />
            </div>
        </div>
    );
}
