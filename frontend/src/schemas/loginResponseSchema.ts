import {z} from "zod";

// TODO: i18n for custom messages
export const loginDtoSchema = z.object({
    accessToken: z.string(),
    expiresIn: z.number()
});

export type LoginDto = z.infer<typeof loginDtoSchema>;