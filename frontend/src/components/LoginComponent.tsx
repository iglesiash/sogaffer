import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {loginSchema, type LoginForm} from "../schemas/loginSchema";

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
        <form onSubmit={handleSubmit(onSubmit)}>
            <div>
                <input
                    placeholder="Introduce tu email"
                    type="email"
                    id="username"
                    {...register("username")}
                />
                {errors.username && (<p>{errors.username.message}</p>)}
            </div>

            <div>
                <input
                    placeholder="Introduce tu contraseña"
                    type="password"
                    id="password"
                    {...register("password")}
                />
                {errors.password && (<p>{errors.password.message}</p>)}
            </div>

            <button type="submit">
                Iniciar sesión
            </button>
        </form>
    );
};

export default LoginComponent;