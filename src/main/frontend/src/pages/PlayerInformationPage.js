import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';
import { PlayerInformation } from '../components/PlayerInformation/PlayerInformation';
import { getPlayerBySlug } from '../state/playersSlice';
export const PlayerInformationPage = () => {

    const { playerSlug } = useParams();

    const { player } = useSelector((state) => state.players);

    const dispatch = useDispatch();

    /**
     * Fetches the player whenever its slug is selected
     */
    useEffect(() => {
        dispatch(getPlayerBySlug(playerSlug));
    }, []);

    return (
        <>
            <PlayerInformation
                player={player}
            />
        </>
    ); // return
};