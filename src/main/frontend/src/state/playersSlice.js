import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';

const API_URL = process.env.REACT_APP_API_URL;

export const getPlayerBySlug = createAsyncThunk(
    'players/getPlayer',
    async (playerSlug) => {
        const response = await fetch(`${API_URL}/players/${playerSlug}`, {
            headers: { 'Content-Type': 'application/json' },
        });
        return await response.json();
    }
);

export const getPlayersByLeague = createAsyncThunk(
    'players/getPlayers',
    async (leagueSlug) => {
        const response = await fetch(`${API_URL}/players?league=${leagueSlug}`, {
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
        resetPlayers: (state) => {
            state.players = [];
            state.player = null;
        }
    },
    extraReducers: (builder) => {
        // login
        builder
            // players
            .addCase(getPlayersByLeague.pending, (state, action) => {
                state.players = []
                state.loading = true;
                action.error = null;
            }).addCase(getPlayersByLeague.fulfilled, (state, action) => {
                state.players = action.payload;
                state.loading = false;
            }).addCase(getPlayersByLeague.rejected, (state, action) => {
                state.players = []
                state.loading = true;
                state.error = action.error;
            })
            // player
            .addCase(getPlayerBySlug.pending, (state, action) => {
                state.player = null;
                state.loading = true;
                action.error = null;
            }).addCase(getPlayerBySlug.fulfilled, (state, action) => {
                state.player = action.payload;
                state.loading = false;
            }).addCase(getPlayerBySlug.rejected, (state, action) => {
                state.player = null;
                state.loading = true;
                state.error = action.error;
            })
            ; // builder

    }, // initialState
}); // createSlice

export const {
    resetPlayers
} = playersSlice.actions;
