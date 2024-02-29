import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import Select from 'react-select';
import { LEAGUES } from '../../../utils/Utils';
import { getPlayer, getPlayersByLeague, resetPlayers } from '../../../state/playersSlice';

export const PlayerInformation = () => {

    const [leagueValue, setLeagueValue] = useState(null);
    const { players , loading} = useSelector((state) => state.players);
    const dispatch = useDispatch();

    const [defaultPlayer, setDefaultPlayer] = useState(null);

    useEffect(() => {
        if (leagueValue) {
            dispatch(resetPlayers());
            setDefaultPlayer(null);
            dispatch(getPlayersByLeague(leagueValue));
        } //if
    }, [leagueValue]);

    useEffect (() => {
        if (defaultPlayer) {
            dispatch(getPlayer(defaultPlayer.value));
        } // if
    }, [defaultPlayer])

    return (
        <>
            {
                LEAGUES.map(league => {
                    return (
                        <button value={league['icon']} onClick={() => setLeagueValue(league['value'])}>
                            {league['icon']}
                        </button>
                    )
                })
            }
            <Select
                options={players}
                defaultValue={defaultPlayer}
                isDisabled={loading}
                onChange={(value) => setDefaultPlayer(value)}
            />
        </>
    );
}