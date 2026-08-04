import apiClient from "../apiClient.js";
import { createRaffleRequest } from "../dtos/raffleRequest.js";

const RAFFLE_BASE = "/raffles";

const raffleService = {
    async create({ groupId, name, value, total }) {
        const body = createRaffleRequest({ groupId, name, value, total });
        return apiClient.post(RAFFLE_BASE, body);
    },

    async listByGroup(groupId) {
        return apiClient.get(`/raffles/group/${groupId}`);
    },

    async update(id, { groupId, name, value, total }) {
        const body = createRaffleRequest({ groupId, name, value, total });
        return apiClient.put(`${RAFFLE_BASE}/${id}`, body);
    },

    async remove(id) {
        return apiClient.delete(`${RAFFLE_BASE}/${id}`);
    }
};

export default raffleService;
