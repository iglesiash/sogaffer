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
    } // filteredPlayers

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
        <div style={{ marginTop: '1rem' }}>
            <h1>
                {f({ id: "app.players" })}
            </h1>
            <div className='col-2' style={{ marginLeft: '1rem' }}>

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

                loading

                    ?

                    <Spinner />

                    :

                    <>
                        {
                            players.length > 0 ?
                                <div className='col-2' style={{ marginLeft: '1rem', marginTop: '2rem' }}>
                                    <input
                                        type="text"
                                        placeholder={f({ id: 'search.placeholder' })}
                                        value={searchTerm}
                                        onChange={(e) => setSearchTerm(e.target.value)}
                                        style={{ marginBottom: '1rem', width: '100%' }}
                                    />
                                </div>
                                :
                                null
                        }

                        <div style={{
                            display: 'grid',
                            gridTemplateColumns: 'repeat(10, 1fr)',
                            gap: '20px', // Adjusts spacing between images
                            marginTop: '2rem',
                            marginLeft: '1rem',
                            marginRight: '1rem'
                        }}>
                            {
                                filteredPlayers().map(player => (
                                    <div key={player.value} style={{ textAlign: 'center' }}>
                                        <Link to={`/players/${player.value}`} style={{ textDecoration: 'none', color: 'inherit' }}>
                                            <img src={player.squaredPictureUrl} alt="" style={{ width: '100%', height: 'auto' }} />
                                            <div>{player.label}</div>
                                        </Link>
                                    </div>
                                )) // map
                            }
                        </div>
                    </>
            }
        </div>
    ); //  return
} // SelectPlayerPage