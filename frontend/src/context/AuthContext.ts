import { createContext } from "react";
import type { AuthContextValue } from "../types/AuthContextValue.ts";

export const AuthContext = createContext<AuthContextValue | null>(null);
