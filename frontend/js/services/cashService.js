import apiClient from "../apiClient.js";
import { createTransactionRequest } from "../dtos/transactionRequest.js";

const cashService = {
    async getCash(groupId) {
        return apiClient.get(`/cash/${groupId}`);
    },

    async getRemainingToGoal(groupId) {
        return apiClient.get(`/cash/${groupId}/goal`);
    },

    async addTransaction({ value, description, type, groupId, raffleId, subscriptionPaymentId }) {
        const body = createTransactionRequest({ value, description, type, groupId, raffleId, subscriptionPaymentId });
        return apiClient.post("/cash", body);
    },

    async removeTransaction(id) {
        return apiClient.delete(`/cash/transactions/${id}`);
    }
};

export default cashService;
