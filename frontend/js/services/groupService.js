import apiClient from "../apiClient.js";
import { createGroupRequest } from "../dtos/groupRequest.js";

const groupService = {
    async create({ name, goal, team, course, createdBy }) {
        const body = createGroupRequest({ name, goal, team, course, createdBy });
        return apiClient.post("/groups", body);
    },

    async listAll() {
        return apiClient.get("/groups");
    },

    async update(id, { name, goal, team, course, createdBy }) {
        const body = createGroupRequest({ name, goal, team, course, createdBy });
        return apiClient.put(`/groups/${id}`, body);
    },

    async remove(id) {
        return apiClient.delete(`/groups/${id}`);
    },

    async addUser(groupId, userId) {
        return apiClient.post(`/groups/${groupId}/users`, { idUser: userId });
    },

    async removeUser(groupId, userId) {
        return apiClient.delete(`/groups/${groupId}/users/${userId}`);
    },

    async joinByToken(token) {
        return apiClient.post("/groups/join", { token });
    },

    async getMembers(groupId) {
        return apiClient.get(`/groups/${groupId}/members`);
    }
};

export default groupService;
