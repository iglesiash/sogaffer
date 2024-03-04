import { PlayerBasicInformation } from './PlayerBasicInformation';
import { PlayerUpcomingGames } from './PlayerUpcomingGames';


export const PlayerInformation = ({ player }) => {

    return (
        player &&
        <div style={{ display: 'flex' }}>
            <div className='col-lg-9'>
                <PlayerBasicInformation
                    player={player}
                />
            </div>

            <div className='col-lg-3'>
                <PlayerUpcomingGames
                    games={player.activeClub.upcomingGames}
                />
            </div>
        </div>
    ); // return
} // PlayerInformation