import React from "react";
import { Navigate, Route, Routes } from "react-router-dom";

import { AccountInfo } from "./components/InfoManagement/AccountInfo";
import { PersonalInfo } from "./components/InfoManagement/PersonalInfo";
import { HomePage } from "./components/JobManagement/HomePage";
import { JobInfoManage } from "./components/JobManagement/JobInfoManage";
import PrivateRoute from "./components/loginAuthen/PrivateRoute";
import { AboutUs } from "./components/public/AboutUs/AboutUs";
import { Contact } from "./components/public/Contact/Contact";
import { MainPage } from "./components/public/MainPage/MainPage";
import { OurTeam } from "./components/public/OurTeam/OurTeam";
import { JobManagePage } from "./features/JobManagePage";
import LoginPage from "./features/LoginPage";
import PublicPage from "./features/PublicPage";
import RegisterPage from "./features/RegisterPage";
import { UpdateInfo } from "./features/UpdateInfo";

function App() {
	return (
		<div className="App">
			<Routes>
				<Route element={<PublicPage />}>
					<Route path="/" element={<Navigate replace to="/public" />} />
					<Route path="/public" element={<MainPage />} />
					<Route path="/public/team" element={<OurTeam />} />
					<Route path="/public/about-us" element={<AboutUs />} />
					<Route path="/public/contact" element={<Contact />} />
				</Route>
				<Route element={<PrivateRoute component={<JobManagePage />} />}>
					<Route path="/job-manage" element={<Navigate replace to="/job-manage/home" />} />
					<Route path="/job-manage/home" element={<PrivateRoute component={<HomePage />} />} />
					<Route path="/job-manage/job" element={<PrivateRoute component={<JobInfoManage />} />} />
					<Route element={<PrivateRoute component={<UpdateInfo />} />}>
						<Route path="/update-info" element={<Navigate replace to="/update-info/account" />} />
						<Route path="/update-info/account" element={<PrivateRoute component={<AccountInfo />} />} />
						<Route path="/update-info/personal" element={<PrivateRoute component={<PersonalInfo />} />} />
					</Route>
				</Route>
				<Route path="/login" element={<LoginPage />}></Route>
				<Route path="/register" element={<RegisterPage />}></Route>
			</Routes>
		</div>
	);
}

export default App;
