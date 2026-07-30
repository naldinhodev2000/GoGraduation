import apiClient from "../apiClient.js";
import { createTransactionRequest } from "../dtos/transactionRequest.js";

const cashService = {
    async getCash(groupId) {
        return apiClient.get(`/cash/${groupId}`);
    },
    async getRemainingToGoal(groupId) {
        return apiClient.get(`/cash/${groupId}/goal`);
    },
    async addTransaction({ value, description, type, groupId, userId }) {
        const body = createTransactionRequest({ value, description, type, groupId, userId });
        return apiClient.post("/cash", body);
    },
    async removeTransaction(id) {
        return apiClient.delete(`/cash/${id}`);
    }
};

export default cashService;
