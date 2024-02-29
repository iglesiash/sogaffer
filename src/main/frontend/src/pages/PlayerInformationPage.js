import { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { PlayerInformation } from "../components/LoginForm/PlayerInformation/PlayerInformation";
import { getPlayers } from "../state/playersSlice";


export const PlayerInformationPage = () => {

    const dispatch = useDispatch();

    useEffect(() => {
        // dispatch(getPlayers());
    }, []);

    return (
        <PlayerInformation />
    ); // return
};