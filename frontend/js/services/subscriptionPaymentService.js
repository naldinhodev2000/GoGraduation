import apiClient from "../apiClient.js";

function fileToBase64(file) {

    return new Promise((resolve, reject) => {

        const reader = new FileReader();

        reader.onload = () => resolve(reader.result);

        reader.onerror = reject;

        reader.readAsDataURL(file);

    });

}

const subscriptionPaymentService = {

    fileToBase64,

    async create(subscriptionId, payment) {

        return apiClient.post(
            `/subscription-payments/${subscriptionId}`,
            payment
        );

    },

    async listByGroup(groupId) {

        return apiClient.get(
            `/subscription-payments/group/${groupId}`
        );

    },

    async listBySubscription(subscriptionId) {

        return apiClient.get(
            `/subscription-payments/subscription/${subscriptionId}`
        );

    },

    async remove(paymentId) {

        return apiClient.delete(
            `/subscription-payments/${paymentId}`
        );

    }

};

export default subscriptionPaymentService;