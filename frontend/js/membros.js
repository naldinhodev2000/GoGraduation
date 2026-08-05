import groupService from "./services/groupService.js";
import groupMemberService from "./services/groupMemberService.js";
import memberSummaryService from "./services/memberSummaryService.js";
import subscriptionPaymentService from "./services/subscriptionPaymentService.js";


/* ============================
   ID DA SALA
============================ */

const params =
    new URLSearchParams(window.location.search);

const groupId =
    params.get("id");


if (!groupId) {
    window.location.href = "salas.html";
}


/* ============================
   ELEMENTOS
============================ */

const memberList =
    document.getElementById("member-list");

const emptyState =
    document.getElementById("empty-state");

const errorMessage =
    document.getElementById("error-message");

const memberTemplate =
    document.getElementById("member-template");

const backLink =
    document.getElementById("back-link");


backLink.href =
    `sala.html?id=${groupId}`;


let isAdmin = false;


/* ============================
   STATUS
============================ */

function statusLabel(summary) {

    if (summary.overdue) {
        return "Atrasado";
    }


    if (summary.subscriptionStatus === "ACTIVE") {
        return "Em dia";
    }


    if (!summary.subscriptionStatus) {
        return "Sem mensalidade";
    }


    return summary.subscriptionStatus;
}


/* ============================
   FORM PAGAMENTO
============================ */

function buildPaymentForm(summary) {


    if (!summary.subscriptionId || !summary.overdue) {
        return "";
    }


    return `

        <details class="payment-form">

            <summary>
                Registrar pagamento
            </summary>


            <label>
                Valor pago
            </label>

            <input 
                class="pay-value"
                type="number"
                step="0.01"
            >



            <label>
                Mês referência
            </label>

            <input
                class="pay-reference"
                type="month"
            >



            <label>
                Comprovante
            </label>

            <input
                class="pay-proof"
                type="file"
                accept="image/*"
            >



            <label>
                Observação
            </label>

            <textarea
                class="pay-note"
                placeholder="Ex: pago via Pix"
            ></textarea>



            <button
                class="pay-button"
                type="button">

                Confirmar pagamento

            </button>



            <p class="pay-error hidden"></p>


        </details>

    `;
}



/* ============================
   CONTROLE ADMIN
============================ */

function buildAdminControls(member) {


    if (!isAdmin) {
        return "";
    }


    if (member.role === "ADMIN") {

        return `

            <button
                class="role-button"
                data-action="demote">

                Remover administrador

            </button>

        `;

    }


    return `

        <button
            class="role-button"
            data-action="promote">

            Tornar administrador

        </button>

    `;

}


/* ============================
   PAGAMENTO EVENTO
============================ */

function attachPaymentHandler(card, summary) {


    const button =
        card.querySelector(".pay-button");


    if (!button) {
        return;
    }



    button.addEventListener("click", async () => {


        const value =
            card.querySelector(".pay-value").value;


        const reference =
            card.querySelector(".pay-reference").value;


        const proof =
            card.querySelector(".pay-proof");


        const note =
            card.querySelector(".pay-note").value;


        const error =
            card.querySelector(".pay-error");



        if (!value || !reference) {

            error.textContent =
                "Preencha valor e referência.";

            error.classList.remove("hidden");

            return;
        }



        button.disabled = true;

        button.textContent =
            "Enviando...";



        try {


            let proofImage = null;



            if (proof.files.length > 0) {

                proofImage =
                    await subscriptionPaymentService
                    .fileToBase64(
                        proof.files[0]
                    );

            }



            await subscriptionPaymentService.create(

                summary.subscriptionId,

                {
                    value,

                    date:
                        new Date()
                        .toISOString(),

                    reference:
                        `${reference}-01`,

                    proofImage,

                    note
                }

            );



            window.location.reload();



        } catch(error) {


            error.textContent =
                error.message;


            error.classList.remove("hidden");


            button.disabled = false;


            button.textContent =
                "Confirmar pagamento";


        }


    });


}

/* ============================
   ALTERAR CARGO
============================ */

function attachRoleHandler(card, member) {

    const button =
        card.querySelector(".role-button");


    if (!button) {
        return;
    }


    button.addEventListener("click", async () => {


        const action =
            button.dataset.action;


        const newRole =
            action === "promote"
                ? "ADMIN"
                : "MEMBER";


        button.disabled = true;



        try {

            await groupMemberService.changeRole(
                groupId,
                member.userId,
                newRole
            );


            window.location.reload();


        } catch(error) {


            alert(error.message);


            button.disabled = false;

        }


    });

}



/* ============================
   CRIAR CARD DO MEMBRO
============================ */

async function createMemberCard(member) {


    const clone =
        memberTemplate.content
        .cloneNode(true);



    const card =
        clone.querySelector(".member-card");



    const name =
        clone.querySelector(".member-name");


    const role =
        clone.querySelector(".member-role");


    const status =
        clone.querySelector(".member-status");


    const raffles =
        clone.querySelector(".member-raffles");


    const debt =
        clone.querySelector(".member-debt");


    const paymentContainer =
        clone.querySelector(".payment-container");


    const adminActions =
        clone.querySelector(".admin-actions");



    name.textContent =
        member.name;


    role.textContent =
        member.role === "ADMIN"
            ? "Administrador"
            : "Membro";



    try {


        const summary =
            await memberSummaryService.getSummary(
                groupId,
                member.userId
            );


        summary.role =
            member.role;



        status.textContent =
            statusLabel(summary);



        if (summary.overdue) {

            status.classList.add(
                "status-late"
            );

        } else {

            status.classList.add(
                "status-ok"
            );

        }



        raffles.textContent =
            summary.rafflesSold;



        debt.textContent =
            `R$ ${Number(
                summary.raffleAmountDue
            ).toFixed(2)}`;



        paymentContainer.innerHTML =
            buildPaymentForm(summary);



        adminActions.innerHTML =
            buildAdminControls(member);



        attachPaymentHandler(
            card,
            summary
        );


    } catch(error) {


        status.textContent =
            "Erro ao carregar";


    }



    attachRoleHandler(
        card,
        member
    );


    return clone;

}



/* ============================
   CARREGAR MEMBROS
============================ */

async function loadMembers() {


    try {


        const members =
            await groupMemberService
            .getDetailed(groupId);



        try {


            const role =
                await groupService
                .getMyRole(groupId);



            isAdmin =
                role === "ADMIN";



        } catch {


            isAdmin = false;

        }




        if (!members || members.length === 0) {


            emptyState
            .classList
            .remove("hidden");


            return;

        }



        for (const member of members) {


            const card =
                await createMemberCard(
                    member
                );


            memberList.appendChild(
                card
            );

        }



    } catch(error) {


        errorMessage.textContent =
            error.message ||
            "Não foi possível carregar os membros.";


        errorMessage
        .classList
        .remove("hidden");


    }

}



/* ============================
   INICIAR
============================ */

loadMembers();