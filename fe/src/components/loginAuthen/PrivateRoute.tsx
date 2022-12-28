import React from "react";
import { Navigate } from "react-router-dom";

import { IPrivateRouteProps } from "./types";

const PrivateRoute = ({ navigatePath, component }: IPrivateRouteProps): JSX.Element => {
	if (localStorage.getItem("user") != null) {
		return component;
	}
	return <Navigate to={{ pathname: navigatePath ?? "/" }} />;
};

export default PrivateRoute;
