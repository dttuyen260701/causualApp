let isProduction = false;
if (process.env.NODE_ENV === "production") {
	isProduction = true;
}
export const API_URL = isProduction ? "" : "https://6cb4-2402-800-620e-edca-d146-5581-f300-4c9b.ap.ngrok.io";
