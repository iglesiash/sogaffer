import unknownTeam from 'images/unknown-team.png';
import { useIntl } from 'react-intl';
export const PlayerImagesContainer = ({ squaredPictureUrl, country, activeClub }) => {

    const { formatMessage: f } = useIntl();

    const IMAGE_ALT = f({ id: 'player.image' });
    const COUNTRY_ALT = f({ id: 'player.country' });
    const CLUB_ALT = f({ id: 'player.club' });
    const LEAGUE_ALT = f({ id: 'player.league' });
    
    return (
        <div className='col-6 ml-1rem'>
            <div style={{ position: 'relative' }}>
                <div style={{ display: 'flex', justifyContent: 'center' }}>
                    <img
                        src={squaredPictureUrl}
                        alt={IMAGE_ALT}
                        style={{ height: 'auto', width: '70%' }}
                    />
                </div>
                <img
                    src={country.flagUrl}
                    alt={COUNTRY_ALT}
                    style={{
                        position: 'absolute',
                        border: '1px solid black',
                        top: '1.5rem',
                        right: '7rem',
                        width: '3.5rem',
                        height: 'auto',
                    }}
                />
            </div>

            <div style={{ display: 'flex', justifyContent: 'space-evenly', alignItems: 'center', marginTop: '1rem' }}>
                <img
                    alt={CLUB_ALT}
                    src={activeClub.pictureUrl || unknownTeam}
                    style={{ height: '4rem', width: 'auto' }} />
                <img
                    alt={LEAGUE_ALT}
                    src={activeClub.domesticLeague.logoUrl}
                    style={{ height: '4rem', width: 'auto' }} />
            </div>
        </div>
    ); // return
} // PlayerImagesContainer