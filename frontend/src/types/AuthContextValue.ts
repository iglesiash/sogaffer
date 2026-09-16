import type { AuthState } from "./AuthState.ts";

export interface AuthContextValue extends AuthState {
    login: (email: string, password: string) => Promise<void>;
}
