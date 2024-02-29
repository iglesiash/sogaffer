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
export const getPlayer = createAsyncThunk(
    'players/getPlayer',
    async (playerSlug) => {
        const response = await fetch(`${API_URL}/players/${playerSlug}`, {
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
        }
    },
    extraReducers: (builder) => {
        // login
        builder
            .addCase(getPlayersByLeague.pending, (state, action) => {
                state.players = []
                state.loading = true;
            }).addCase(getPlayersByLeague.fulfilled, (state, action) => {
                state.players = action.payload;
                state.loading = false;
            }).addCase(getPlayersByLeague.rejected, (state, action) => {
                state.players = []
                state.loading = true;
            }); // builder
    }, // initialState
}); // createSlice

export const {
    resetPlayers
} = playersSlice.actions;
