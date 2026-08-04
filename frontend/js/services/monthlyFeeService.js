import apiClient from "../apiClient.js";
import { createMonthlyFeeRequest } from "../dtos/monthlyFeeRequest.js";

const monthlyFeeService = {
    async create({ groupId, value, startDate, endDate }) {
        const body = createMonthlyFeeRequest({ groupId, value, startDate, endDate });
        return apiClient.post("/monthly-fees", body);
    },
    async update(id, { groupId, value, startDate, endDate }) {
        const body = createMonthlyFeeRequest({ groupId, value, startDate, endDate });
        return apiClient.put(`/monthly-fees/${id}`, body);
    },
    async listByGroupId(groupId) {
        return apiClient.get(`/monthly-fees/group/${groupId}`);
    }
};

export default monthlyFeeService;
