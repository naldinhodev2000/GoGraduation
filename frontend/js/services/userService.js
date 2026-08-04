import apiClient from "../apiClient.js";
import { createUserRequest } from "../dtos/userRequest.js";

const userService = {
    async listAll() {
        return apiClient.get("/users");
    },

    async update(id, { name, email, telefone, login, senha }) {
        const body = createUserRequest({ name, email, telefone, login, senha });
        return apiClient.put(`/users/${id}`, body);
    },

    async remove(id) {
        return apiClient.delete(`/users/${id}`);
    },

    async getJoinedGroups() {
        return apiClient.get("/users/me/groups");
    }
};

export default userService;
