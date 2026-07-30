import apiClient from "../apiClient.js";
import { createExpenseRequest } from "../dtos/expenseRequest.js";

const expenseService = {
    async create({ groupId, description, value }) {
        const body = createExpenseRequest({ groupId, description, value });
        return apiClient.post("/expends", body);
    },

    async update(id, { groupId, description, value }) {
        const body = createExpenseRequest({ groupId, description, value });
        return apiClient.put(`/expends/${id}`, body);
    },

    async remove(id) {
        return apiClient.delete(`/expends/${id}`);
    },

    async listByGroupId(groupId) {
        return apiClient.get(`/expends/${groupId}/expenses`);
    }
};

export default expenseService;
