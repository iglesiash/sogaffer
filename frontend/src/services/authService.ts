import { loginDtoSchema, type LoginDto } from "../schemas/loginResponseSchema.ts";
import { LoginError } from "../errors/LoginError.ts";

const API_BASE_URL = import.meta.env.VITE_API_URL;

export async function loginRequest(email: string, password: string): Promise<LoginDto> {
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
    });

    if (!response.ok) {
        throw new LoginError("Credenciales incorrectas");
    }

    return loginDtoSchema.parse(await response.json());
}
