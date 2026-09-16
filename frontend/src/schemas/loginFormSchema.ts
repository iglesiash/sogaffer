import { z } from "zod";

// TODO: i18n for custom messages
export const loginFormSchema = z.object({
    username: z.email("Introduce un email válido"),
    password: z
        .string()
        .trim()
        .min(1, "Introduce una contraseña"),
});

export type LoginForm = z.infer<typeof loginFormSchema>;