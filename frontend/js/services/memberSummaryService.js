import apiClient from "../apiClient.js";

const memberSummaryService = {
    async getSummary(groupId, userId) {
        return apiClient.get(`/groups/${groupId}/members/${userId}/summary`);
    }
};

export default memberSummaryService;
