import './App.css'
import LoginPage from "./LoginPage.tsx";
import {BrowserRouter, Link, Route, Routes} from "react-router";

function App() {

    return (
        <BrowserRouter>
            {/* Navigation */}
            <nav>
                <Link to="/login"></Link>
            </nav>

            {/* Routes */}
            <Routes>
                <Route path = "/login" element={<LoginPage/>}/>
            </Routes>
        </BrowserRouter>
    )
}

export default App
