import apiClient from "../apiClient.js";
import { createExpenseRequest } from "../dtos/expenseRequest.js";


const expenseService = {


    async create({
        groupId,
        description,
        value
    }) {


        const body =
            createExpenseRequest({
                groupId,
                description,
                value
            });


        return apiClient.post(
            "/expenses",
            body
        );

    },



    async update(
        id,
        {
            groupId,
            description,
            value
        }
    ) {


        const body =
            createExpenseRequest({
                groupId,
                description,
                value
            });



        return apiClient.put(
            `/expenses/${id}`,
            body
        );

    },



    async remove(id) {


        return apiClient.delete(
            `/expenses/${id}`
        );

    },



    async listByGroupId(groupId) {


        return apiClient.get(
            `/expenses/group/${groupId}`
        );

    }


};


export default expenseService;