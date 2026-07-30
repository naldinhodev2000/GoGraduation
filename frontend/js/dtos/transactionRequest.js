export function createTransactionRequest({ value, description, type, groupId, userId }) {
    return {
        value,
        description,
        type,
        groupId,
        userId
    };
}
