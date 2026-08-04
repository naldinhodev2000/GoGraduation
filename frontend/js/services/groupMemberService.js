import apiClient from "../apiClient.js";

const groupMemberService = {
    async getDetailed(groupId) {
        return apiClient.get(`/groups/${groupId}/members/detailed`);
    },

    async changeRole(groupId, userId, role) {
        return apiClient.patch(`/groups/${groupId}/users/${userId}/role`, role);
    }
};

export default groupMemberService;
