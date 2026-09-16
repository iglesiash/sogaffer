import type { AuthStatus } from "./AuthStatus.ts";

export interface AuthState {
    accessToken: string | null;
    expiresIn: number | null;
    status: AuthStatus;
    error: string | null;
}
