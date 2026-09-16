import { useMemo, useState, type ReactNode } from "react";
import { AuthContext } from "./AuthContext.ts";
import { loginRequest } from "../services/authService.ts";
import { LoginError } from "../errors/LoginError.ts";
import type { AuthState } from "../types/AuthState.ts";

const initialState: AuthState = {
    accessToken: null,
    expiresIn: null,
    status: "idle",
    error: null,
};

export function AuthProvider({ children }: { children: ReactNode }) {
    const [state, setState] = useState<AuthState>(initialState);

    async function login(email: string, password: string) {
        setState((prev) => ({ ...prev, status: "loading", error: null }));

        try {
            const { accessToken, expiresIn } = await loginRequest(email, password);
            setState((prev) => ({ ...prev, status: "idle", accessToken, expiresIn }));
        } catch (err) {
            const message = err instanceof LoginError ? err.message : "No se pudo iniciar sesión";
            setState((prev) => ({ ...prev, status: "failed", error: message }));
        }
    }

    const value = useMemo(() => ({ ...state, login }), [state]);

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
