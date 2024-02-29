import { configureStore } from '@reduxjs/toolkit';
import { thunk } from 'redux-thunk';
import { authSlice } from "./authSlice";
import { playersSlice } from './playersSlice';

export const store = configureStore({
    reducer: {
        auth: authSlice.reducer,
        players: playersSlice.reducer
    },
    middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(thunk),
});