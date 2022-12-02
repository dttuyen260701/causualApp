let isProduction = false;
if (process.env.NODE_ENV === "production") {
	isProduction = true;
}
export const API_URL = isProduction ? "" : "https://d31f-2402-800-620e-f56a-5572-709-322d-9454.ap.ngrok.io";
