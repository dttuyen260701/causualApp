import React from "react";
import { Navigate, Route, Routes } from "react-router-dom";

import { AboutUs } from "./components/public/AboutUs/AboutUs";
import { Contact } from "./components/public/Contact/Contact";
import { MainPage } from "./components/public/MainPage/MainPage";
import { OurTeam } from "./components/public/OurTeam/OurTeam";
import LoginPage from "./features/LoginPage";
import PublicPage from "./features/PublicPage";
import RegisterPage from "./features/RegisterPage";

function App() {
	return (
		<div className="App">
			<Routes>
				<Route element={<PublicPage />}>
					<Route path="/main" element={<Navigate replace to="/public" />} />
					<Route path="/public" element={<MainPage />} />
					<Route path="/public/team" element={<OurTeam />} />
					<Route path="/public/about-us" element={<AboutUs />} />
					<Route path="/public/contact" element={<Contact />} />
				</Route>
				<Route path="/" element={<LoginPage />}></Route>
				<Route path="/register" element={<RegisterPage />}></Route>
			</Routes>
		</div>
	);
}

export default App;
