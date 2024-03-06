import { formatDate } from "utils/DateUtils";
import unknownTeamCrest from 'images/unknown-team.png';
import styles from './PlayerUpcomingGamesSidebar.module.scss';
import { useIntl } from 'react-intl';

export const PlayerUpcomingGames = ({ games }) => {

    const { formatMessage: f } = useIntl();
    return (
        <div className={styles.container}>
            <div className='center-h'>
                <h2>{f({id: 'player.upcomingGames'})}</h2>
            </div>
            {
                games.map(game => {
                    return (
                        <div style={{ marginBottom: '3rem' }}>
                            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                {/* Home team */}
                                <div className="col-3">
                                    <img
                                        style={{ height: 'auto' }}
                                        alt='Escudo equipo local'
                                        src={game.homeTeam.pictureUrl || unknownTeamCrest}
                                    />
                                </div>
                                <div className="col-1" style={{ textAlign: 'center' }}>
                                    {'-'}
                                </div>

                                {/* Away team */}
                                <div className="col-3">
                                    <img
                                        style={{ height: 'auto' }}
                                        alt='Escudo equipo visitante'
                                        src={game.awayTeam.pictureUrl || unknownTeamCrest}
                                    />
                                </div>
                            </div>
                            {/* Team names */}
                            <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '1rem', alignItems: 'center', textAlign: 'center' }}>
                                <div className="col-3">
                                    {game.homeTeam.name}
                                </div>
                                <div className="col-3">
                                    {game.awayTeam.name}
                                </div>
                            </div>


                            <div style={{ marginTop: '10px', display: 'flex', flexDirection: 'column' }}>
                                <div>
                                    <i className="las la-map-marker mr-1rem"></i>
                                    {game.venue}
                                </div>
                                <div>
                                    <i className="las la-calendar mr-1rem" />
                                    {formatDate(game.date, 'es', { dateStyle: 'long', timeStyle: 'short' }) + "h"}
                                </div>
                            </div>
                        </div>
                    ); // return
                }) // map
            }
        </div>
    ); // return
} // PlayerUpcomingGames