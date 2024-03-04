import { formatDate, formatExpectedInjuryEndDate } from "utils/DateUtils";
import { translateInjury } from "utils/PlayerUtils";
import { useIntl } from 'react-intl';

export const PlayerInjuryContainer = ({ injuries }) => {

    const getActiveInjuries = () => {
        let activeInjuries = [];
        injuries.forEach(injury => {
            if (injury.active) {
                activeInjuries.push(injury);
            }
        });
        return activeInjuries;
    }

    const { formatMessage: f } = useIntl();
    const activeInjuries = getActiveInjuries();
    const ONGOING_INJURIES = f({id: 'player.ongoingInjuries'});
    const NO_ONGOING_INJURIES = f({id: 'player.noOngoingInjuries'});

    return (
        <div className='col-6'>
            <div style={{ display: 'flex', alignItems: 'baseline' }}>
                <i class='las la-ambulance' style={{ fontSize: '2rem', marginRight: '0.5rem' }} />
                <h2>{ONGOING_INJURIES}</h2>
            </div>

            {
                activeInjuries.length > 0 ?

                    activeInjuries.map(injury => {
                        let render;
                        if (injury.active) {
                            render = (
                                <>
                                    <p>{`Razón: ${translateInjury(f, injury.kind)}`}</p>
                                    <p>{`Fecha lesión: ${formatDate(injury.startDate, 'es')}`}</p>
                                    <p>{`Fecha estimada de vuelta: ${formatExpectedInjuryEndDate(injury.expectedEndDate)}`}</p>
                                </>
                            );
                        }
                        return render;
                    })

                    :

                    NO_ONGOING_INJURIES
            }
        </div>
    );
}