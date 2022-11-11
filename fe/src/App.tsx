import React from "react";
import { Route, Routes } from "react-router-dom";

import LoginPage from "./features/LoginPage";
import RegisterPage from "./features/RegisterPage";

function App() {
	return (
		<div className="App">
			<Routes>
				<Route path="/" element={<LoginPage />}></Route>
				<Route path="/register" element={<RegisterPage />}></Route>
			</Routes>
		</div>
	);
}

export default App;
