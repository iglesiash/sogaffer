import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {loginSchema, type LoginForm} from "../schemas/loginSchema";
import sorareLogo from "../assets/sorare-logo.png";
import "./LoginComponent.css";

const LoginComponent = () => {
    const {
        register,
        handleSubmit,
        formState: {errors},
    } = useForm<LoginForm>({
        resolver: zodResolver(loginSchema),
        defaultValues: {
            username: "",
            password: "",
        },
    });

    function onSubmit(data: LoginForm) {
        console.log(data.username);
        console.log(data.password);
    }

    // TODO: possible i18n for custom placeholders
    return (
        <div className="login-page">
            <form className="login-card" onSubmit={handleSubmit(onSubmit)}>
                <h1 className="login-title">
                    Inicia sesión con tus credenciales de {}
                    <img src={sorareLogo} alt="Sorare" className="login-title-logo"/>
                </h1>

                <div className="login-field">
                    <input
                        placeholder="Introduce tu email"
                        type="email"
                        id="username"
                        {...register("username")}
                    />
                    {errors.username && (<p className="login-error">{errors.username.message}</p>)}
                </div>

                <div className="login-field">
                    <input
                        placeholder="Introduce tu contraseña"
                        type="password"
                        id="password"
                        {...register("password")}
                    />
                    {errors.password && (<p className="login-error">{errors.password.message}</p>)}
                </div>

                <button className="login-submit" type="submit">
                    Iniciar sesión
                </button>
            </form>
        </div>
    );
};

export default LoginComponent;