import { formatDate } from "utils/DateUtils";
import unknownTeamCrest from 'images/unknown-team.png';

export const PlayerUpcomingGames = ({ games }) => {
    return (
        <div style={{ marginRight: '2rem', marginTop: '1rem' }}>
            <div style={{ display: 'flex', justifyContent: 'center', marginBottom: '10px' }}>
                <h2>Próximos partidos</h2>
            </div>
            {
                games.map(game => {
                    return (
                        <div style={{ marginBottom: '3rem' }}>
                            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
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
                                <div className="col-3">
                                    <img
                                        style={{ height: 'auto' }}
                                        alt='Escudo equipo visitante'
                                        src={game.awayTeam.pictureUrl || unknownTeamCrest}
                                    />
                                </div>
                            </div>
                            <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '1rem', alignItems: 'center', textAlign: 'center' }}>
                                <div className="col-4">
                                    {game.homeTeam.name}
                                </div>
                                <div className="col-4">
                                    {game.awayTeam.name}
                                </div>
                            </div>


                            <div style={{ marginTop: '10px', display: 'flex', flexDirection: 'column' }}>
                                <div>
                                    <i className="las la-map-marker" style={{ marginRight: '10px' }}></i>
                                    {game.venue}
                                </div>
                                <div>
                                    <i className="las la-calendar" style={{ marginRight: '10px' }} />
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