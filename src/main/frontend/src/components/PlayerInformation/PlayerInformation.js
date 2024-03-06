import {
    PlayerBasicInformationContainer
} from './PlayerBasicInformation/PlayerBasicInformationContainer';
import { PlayerUpcomingGames } from './PlayerUpcomingGamesSidebar';

export const PlayerInformation = ({ player }) => {

    return (
        player &&
        <>
            <h1 style={{ marginTop: '1rem' }}>
                {player.label}
            </h1>
            <div className='flex'>
                <div className='col-lg-9'>
                    <PlayerBasicInformationContainer
                        player={player}
                    />
                </div>

                <div className='col-lg-3'>
                    <PlayerUpcomingGames
                        games={player.activeClub.upcomingGames}
                    />
                </div>
            </div>
        </>
    ); // return
} // PlayerInformation