export function createMonthlyFeeRequest({ groupId, value, startDate, endDate }) {
    return {
        id: null,
        value,
        groupId,
        startDate,
        endDate
    };
}
