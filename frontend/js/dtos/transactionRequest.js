export function createTransactionRequest({ value, description, type, groupId, raffleId, subscriptionPaymentId }) {
    return {
        value,
        description,
        type,
        groupId,
        raffleId: raffleId || null,
        subscriptionPaymentId: subscriptionPaymentId || null
    };
}
