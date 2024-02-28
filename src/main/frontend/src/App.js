import 'bootstrap/dist/css/bootstrap.min.css';
import { IntlProvider } from 'react-intl';
import { Provider } from 'react-redux';
import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import './App.css';
import { locales } from './i18n';
import { LoginPage } from './pages/LoginPage';
import { store } from './state/store';
import { PlayerInformationPage } from './pages/PlayerInformationPage';

function App() {
  return (
    <IntlProvider messages={locales["es"]}>
      <Provider store={store}>
        <Router>
          <Routes>
            <Route path='/login' element={<LoginPage />} />
            <Route path="/" element={<PlayerInformationPage />} />

          </Routes>
        </Router>
      </Provider>
    </IntlProvider>
  );
}

export default App;
