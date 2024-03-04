import { PlayerImagesContainer } from './PlayerImagesContainer';
import { PlayerInfoContainer } from './PlayerInfoContainer';
import { PlayerInjuryContainer } from './PlayerInjuryContainer';
import { PlayerSuspensionContainer } from './PlayerSuspensionContainer';

export const PlayerBasicInformation = ({ player }) => {
    return (
        <>
            <h1>{player.label}</h1>
            <div style={{ display: 'flex' }}>
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