import { formatDate } from "utils/DateUtils";
import { translateSuspension } from "utils/PlayerUtils";
import { useIntl } from 'react-intl';

export const PlayerSuspensionContainer = ({ suspensions }) => {

    /**
     * Returns the active suspensions for that player 
     * @returns the active suspensions for that player
     */
    const getActiveSuspensions = () => {
        let activeSuspensions = [];
        suspensions.forEach(suspension => {
            if (suspension.active) {
                activeSuspensions.push(suspension);
            } // if
        }); // forEach
        return activeSuspensions;
    } // getActiveSuspensions

    const activeSuspensions = getActiveSuspensions();
    const { formatMessage: f } = useIntl();
    const ONGOING_SUSPENSIONS = f({id: 'player.ongoingSuspensions'});
    const NO_ONGOING_SUSPENSIONS = f({id: 'player.noOngoingSuspensions'});

    return (
        <div className='col-6'>
            <div>
                <div style={{ display: 'flex', alignItems: 'baseline' }}>
                    <i className='las la-ban' style={{ fontSize: '2rem', color: 'red', marginRight: '0.5rem' }} />
                    <h2>{ONGOING_SUSPENSIONS}</h2>
                </div>

                {
                    activeSuspensions.length > 0 ?

                        activeSuspensions.map(suspension => {
                            let render;
                            if (suspension.active) {
                                render = (
                                    <>
                                        <p>{`Razón: ${translateSuspension(f, suspension.reason)}`}</p>
                                        <p>{`Fecha inicio: ${formatDate(suspension.startDate, 'es')}`}</p>
                                        <p>{`Fecha fin: ${formatDate(suspension.endDate, 'es')}`}</p>
                                    </>
                                ); // render
                            } // if
                            return render;
                        }) // map

                        :

                        NO_ONGOING_SUSPENSIONS
                }
            </div>
        </div>
    ); // return
} // PlayerSuspensionContainer