import Select, { createFilter } from 'react-select';
import { LEAGUES } from 'utils/Utils';
import { useIntl } from 'react-intl';
import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { getPlayersByLeague, resetPlayers } from 'state/playersSlice';
import { Link } from 'react-router-dom';
import { Spinner } from 'reactstrap';

export const SelectPlayerPage = () => {
    const { formatMessage: f } = useIntl();
    const dispatch = useDispatch();

    const [selectedLeague, setSelectedLeague] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');


    const SELECT_PLACEHOLDER = f({ id: 'select.placeholder' });
    const NO_OPTIONS = f({ id: 'select.noOptions' });

    const filter = createFilter({
        matchFrom: 'any',
        stringify: option => `${option.label}`,
    }); // createFilter
    const { players, loading } = useSelector((state) => state.players);

    /**
     * Returns the player list without the coaches and which match the criteria in the search bar
     * @returns the player list without the coaches and which match the criteria in the search bar
     */
    const filteredPlayers = () => {
        return players
            .filter(player => player.position !== "Coach")
            .filter(player => player.label.toLowerCase().includes(searchTerm.toLowerCase()));
    }


    /**
     * Fetches the league players whenever the option in the select changes
     */
    useEffect(() => {
        if (selectedLeague) {
            dispatch(resetPlayers());
            dispatch(getPlayersByLeague(selectedLeague.value));
        } //if
    }, [selectedLeague]);

    return (
        <>
            <div className='col-2'>

                <Select
                    filterOption={filter}
                    value={selectedLeague}
                    noOptionsMessage={() => NO_OPTIONS}
                    onChange={(value) => setSelectedLeague(value)}
                    placeholder={SELECT_PLACEHOLDER}
                    options={LEAGUES}
                />
            </div>

            {

                loading ?

                    <Spinner />

                    :
                    <>
                        <div className='col-2'>

                            <input
                                type="text"
                                placeholder={f({ id: 'search.placeholder' })} // Assuming you have a placeholder in your intl messages
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                                style={{ marginBottom: '20px', width: '100%' }} // Adjust styling as needed
                            />
                        </div>
                        <div style={{
                            display: 'grid',
                            gridTemplateColumns: 'repeat(10, 1fr)',
                            gap: '20px', // Adjusts spacing between images
                            marginTop: '2rem'
                        }}>
                            {filteredPlayers().map(player => (
                                <div key={player.value} style={{ textAlign: 'center' }}>
                                    <Link to={`/players/${player.value}`} style={{ textDecoration: 'none', color: 'inherit' }}>
                                        <img src={player.squaredPictureUrl} alt="" style={{ width: '100%', height: 'auto' }} />
                                        <div>{player.label}</div>
                                    </Link>
                                </div>
                            ))}
                        </div>

                    </>

            }
        </>
    );
}