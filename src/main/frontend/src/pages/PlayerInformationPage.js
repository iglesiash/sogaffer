import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';
import { getPlayerBySlug } from 'state/playersSlice';
import { PlayerInformation } from '../components/PlayerInformation/PlayerInformation';
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
}; // PlayerInformationPage