import 'bootstrap/dist/css/bootstrap.min.css';
import { PlayerInformationPage } from 'pages/PlayerInformationPage';
import { SelectPlayerPage } from 'pages/SelectPlayerPage';
import { IntlProvider } from 'react-intl';
import { Provider } from 'react-redux';
import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import "../node_modules/line-awesome/dist/line-awesome/css/line-awesome.css";
import './App.css';
import { locales } from './i18n';
import { LoginPage } from './pages/LoginPage';
import { store } from './state/store';


function App() {
  return (
    <IntlProvider messages={locales["es"]}>
      <Provider store={store}>
        <Router>
          <Routes>
            <Route path='/login' element={<LoginPage />} />
            <Route path="/players" element={<SelectPlayerPage />} />
            <Route path="/players/:playerSlug" element={<PlayerInformationPage />} />

          </Routes>
        </Router>
      </Provider>
    </IntlProvider>
  );
}

export default App;
