import { yupResolver } from '@hookform/resolvers/yup';
import { useForm } from 'react-hook-form';
import { useIntl } from 'react-intl';
import { useDispatch, useSelector } from 'react-redux';
import { object, string } from 'yup';
import { login } from '../../state/authSlice';

export const LoginForm = () => {
    const { formatMessage: f } = useIntl();

    // Custom messages
    const REQUIRED_LABEL = f({ id: 'app.error.required' });
    const INVALID_EMAIL_LABEL = f({ id: 'app.error.email' });
    const EMAIL_LABEL = f({ id: 'app.email' });
    const PASSWORD_LABEL = f({ id: 'app.password' });
    const SIGN_IN_LABEL = f({ id: 'app.signIn' });
    const SIGNING_IN_LABEL = f({ id: 'app.signingIn' });

    const dispatch = useDispatch();
    const { errors } = useSelector(state => state.auth);

    const loginSchema = object({
        email: string().email(INVALID_EMAIL_LABEL).required(REQUIRED_LABEL),
        password: string().required(REQUIRED_LABEL)
    }); // loginSchema

    const methods = useForm({
        resolver: yupResolver(loginSchema),
    }); // useForm
    const { register, handleSubmit, formState: { errors: formErrors } } = methods;

    /**
     * Submits the login and sends the data to the API
     * @param {object} data: array with the form values
     */
    const onSubmit = (data) => {
        dispatch(login(data));
    }

    const { loading } = useSelector(state => state.auth);

    return (
        <>
            <form className="login-form" onSubmit={handleSubmit(onSubmit)}>
                <div className='input-container'>
                    <input placeholder={EMAIL_LABEL} {...register('email')} />
                    {formErrors.email && <div className="error-message">{formErrors.email.message}</div>}
                </div>

                <div className='input-container'>
                    <input type='password' placeholder={PASSWORD_LABEL}  {...register('password')} />
                    {formErrors.password && <div className="error-message">{formErrors.password.message}</div>}
                </div>

                <button type='submit' disabled={loading} className="btn btn-primary" style={{ marginTop: "10px" }}>
                    {loading ? SIGNING_IN_LABEL : SIGN_IN_LABEL}
                </button>
            </form>
            {
                errors?.map(error => {
                    let formError = null;
                    if (error.message === 'invalid') {
                        formError = (
                            <div className='error-message' key={error.id}>
                                {f({ id: 'app.error.credentials' })}
                            </div>
                        ); // formError
                    } // if
                    return formError;
                }) // map
            }
        </>
    ); // return
} // LoginForm