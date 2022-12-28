let isProduction = false;
if (process.env.NODE_ENV === "production") {
	isProduction = true;
}
export const API_URL = isProduction ? "" : "https://f427-14-250-209-246.ap.ngrok.io";
