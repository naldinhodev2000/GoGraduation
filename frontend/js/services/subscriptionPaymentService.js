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
        return apiClient.post(`/subscription-payments/${subscriptionId}`, payment);
    }
};

export default subscriptionPaymentService;
