import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {loginFormSchema, type LoginForm} from "../schemas/loginFormSchema.ts";
import sorareLogo from "../assets/sorare-logo.png";
import "./LoginComponent.css";
import {useAuth} from "../hooks/useAuth.ts";

const LoginComponent = () => {
    const {status, error, login} = useAuth();

    const {
        register,
        handleSubmit,
        formState: {errors},
    } = useForm<LoginForm>({
        resolver: zodResolver(loginFormSchema),
        defaultValues: {
            username: "",
            password: "",
        },
    });

    async function onSubmit(data: LoginForm) {
        await login(data.username, data.password);
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

                {error && (<p className="login-error">{error}</p>)}

                <button className="login-submit" type="submit" disabled={status === "loading"}>
                    {status === "loading" ? "Iniciando sesión..." : "Iniciar sesión"}
                </button>
            </form>
        </div>
    );
};

export default LoginComponent;