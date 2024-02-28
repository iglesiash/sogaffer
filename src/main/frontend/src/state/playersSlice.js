import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';

const API_URL = process.env.REACT_APP_API_URL;

// Example async thunk
export const getPlayerBySlug = createAsyncThunk(
    'players/getPlayer',
    async (playerSlug, thunkAPI) => {
        const response = await fetch(`${API_URL}/players/${playerSlug}`, {
            headers: { 'Content-Type': 'application/json' },
        });
        return await response.json();
    }
);

export const getPlayers = createAsyncThunk(
    'players/getPlayers',
    async (thunkAPI) => {
        const response = await fetch(`${API_URL}/players/`, {
            headers: { 'Content-Type': 'application/json' },
        });
        return await response.json();
    }
);

export const playersSlice = createSlice({
    name: 'players',
    initialState: {
        player: undefined,
        players: [],
        loading: false,
    },
    reducers: {
        // Sync reducers here
    },
    extraReducers: (builder) => {
        // login
        builder
            .addCase(getPlayers.pending, (state, action) => {
                state.currentUser = undefined;
                state.otpSessionChallenge = undefined;
                state.tcuToken = undefined;
                state.errors = [];

                state.loading = true;
            }).addCase(getPlayers.fulfilled, (state, action) => {
                const signInData = action.payload.data.signIn;
                state.currentUser = signInData.currentUser;
                state.otpSessionChallenge = signInData.otpSessionChallenge;
                state.tcuToken = signInData.tcuToken;
                state.errors = signInData.errors;

                state.loading = false;
            }).addCase(getPlayers.rejected, (state, action) => {
                state.otpSessionChallenge = undefined;
                state.tcuToken = undefined;
                state.currentUser = undefined;
                state.errors = action.error;

                state.loading = false;
            }); // builder
    }, // initialState
}); // createSlice
