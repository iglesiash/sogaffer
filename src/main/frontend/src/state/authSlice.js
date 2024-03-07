import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';

const API_URL = process.env.REACT_APP_API_URL;

// Example async thunk
export const login = createAsyncThunk(
    'auth/login',
    async (data, thunkAPI) => {
        const response = await fetch(`${API_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email: data.email, password: data.password })
        });
        return await response.json();
    }
);

export const authSlice = createSlice({
    name: 'auth',
    initialState: {
        currentUser: undefined,
        otpSessionChallenge: undefined,
        tcuToken: undefined,
        errors: [],

        loading: false,
    },
    reducers: {
        // Sync reducers here
    },
    extraReducers: (builder) => {
        // login
        builder
            .addCase(login.pending, (state) => {
                state.currentUser = undefined;
                state.otpSessionChallenge = undefined;
                state.tcuToken = undefined;
                state.errors = [];

                state.loading = true;
            }).addCase(login.fulfilled, (state, action) => {
                const signInData = action.payload.data.signIn;
                state.currentUser = signInData.currentUser;
                state.otpSessionChallenge = signInData.otpSessionChallenge;
                state.tcuToken = signInData.tcuToken;
                state.errors = signInData.errors;

                localStorage.setItem('access-token', state.currentUser.jwtToken.token);

                state.loading = false;
            }).addCase(login.rejected, (state) => {
                state.otpSessionChallenge = undefined;
                state.tcuToken = undefined;
                state.currentUser = undefined;
                state.errors = [];

                state.loading = false;
            }); // builder
    }, // initialState
}); // createSlice
