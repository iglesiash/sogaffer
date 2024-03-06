import { useIntl } from 'react-intl';
import { showPlayerBirthInformation } from "utils/DateUtils";
import { translateBestFoot, translatePlayingStatus, translatePosition } from "utils/PlayerUtils";

export const PlayerInfoContainer = ({ player }) => {

    const { formatMessage: f } = useIntl();
    const UNDETERMINED = f({id: 'player.shirtNumber.unknown'});
    return (

        <div className='col-6 container'>
            <div>
                <i className='las la-birthday-cake' style={{ marginRight: '0.5rem' }}></i>
                {showPlayerBirthInformation(player.birthDate, player.age)}
            </div>
            <div>
                <i className='las la-ruler-vertical' style={{ marginRight: '0.5rem' }} />
                {(player.height || '-') + ' cm'}
            </div>
            <div>
                <i className='las la-weight' style={{ marginRight: '0.5rem' }} />
                {(player.weight || '-') + ' kg'}
            </div>
            <div>
                <i className='las la-futbol' style={{ marginRight: '0.5rem' }} />
                {translatePosition(f, player.position)}
            </div>
            <div>
                <i className='las la-shoe-prints' style={{ marginRight: '0.5rem' }} />
                {translateBestFoot(f, player.bestFoot)}
            </div>
            <div style={{ marginRight: '0.5rem' }}>
                <i className='las la-clock' style={{ marginRight: '0.5rem' }} />
                {translatePlayingStatus(f, player.playingStatus)}
            </div>
            <div>
                <i className='las la-tshirt' style={{ marginRight: '0.5rem' }} />
                {player.shirtNumber || UNDETERMINED}
            </div>
        </div>
    ) // return
} // PlayerInfoContainer