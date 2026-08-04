import apiClient from "../apiClient.js";
import { createLoginRequest } from "../dtos/loginRequest.js";
import { createUserRequest } from "../dtos/userRequest.js";

const authService = {
    async login(user, password) {
        const body = createLoginRequest(user, password);
        const response = await apiClient.post("/auth/login", body);

        if (response?.token) {
            apiClient.setToken(response.token);
        }

        return response;
    },

    async register({ name, email, telefone, login, senha }) {
        const body = createUserRequest({ name, email, telefone, login, senha });
        return apiClient.post("/auth/register", body);
    },

    logout() {
        apiClient.clearToken();
    },

    isAuthenticated() {
        return !!apiClient.getToken();
    }
};

export default authService;
