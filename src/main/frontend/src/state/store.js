import { configureStore } from '@reduxjs/toolkit';
import { thunk } from 'redux-thunk';
import { authSlice } from "./authSlice";

export const store = configureStore({
    reducer: {
        auth: authSlice.reducer
    },
    middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(thunk),
});