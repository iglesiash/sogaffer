import { PlayerSuspensionContainer } from "./PlayerSuspensionContainer";
import { PlayerImagesContainer } from "./PlayerImagesContainer";
import { PlayerInfoContainer } from "./PlayerInfoContainer";
import { PlayerInjuryContainer } from "./PlayerInjuryContainer";

export const PlayerBasicInformationContainer = ({ player }) => {
    return (
        <>
            <div className='flex'>
                <PlayerImagesContainer
                    squaredPictureUrl={player.squaredPictureUrl}
                    country={player.country}
                    activeClub={player.activeClub}
                />
                <PlayerInfoContainer
                    player={player}
                />
            </div >
            <div style={{ display: 'flex', marginLeft: '1rem', marginTop: '2rem' }}>
                <PlayerSuspensionContainer
                    suspensions={player.suspensions}
                />
                {/* Injuries */}
                <PlayerInjuryContainer
                    injuries={player.injuries}
                />
            </div>
        </>
    );
}; // PlayerBasicInformation