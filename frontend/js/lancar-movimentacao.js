import cashService from "./services/cashService.js";
import raffleService from "./services/raffleService.js";
import subscriptionPaymentService from "./services/subscriptionPaymentService.js";
import authService from "./services/authService.js";

/* ============================
   AUTENTICAÇÃO
============================ */

if (!authService.isAuthenticated()) {
    window.location.href = "login.html";
}

const params = new URLSearchParams(window.location.search);
const groupId = params.get("id");

if (!groupId) {
    window.location.href = "salas.html";
}

/* ============================
   ELEMENTOS
============================ */

const form = document.getElementById("form-movimentacao");

const errorMessage = document.getElementById("form-error");

const typeInput = document.getElementById("type");

const typeButtons = document.querySelectorAll(".type-option");

const valueInput = document.getElementById("value");

const descriptionInput = document.getElementById("description");

const backLink = document.getElementById("back-link");

const originSelect = document.getElementById("origin");

const raffleContainer = document.getElementById("raffle-container");

const subscriptionContainer = document.getElementById("subscription-container");

const raffleSelect = document.getElementById("raffle");

const subscriptionSelect = document.getElementById("subscription-payment");

const button = document.getElementById("button-lancar");

backLink.href = `sala.html?id=${groupId}`;

/* ============================
   TIPO
============================ */

typeButtons.forEach(button => {

    button.addEventListener("click", () => {

        typeButtons.forEach(btn =>
            btn.classList.remove("selected"));

        button.classList.add("selected");

        typeInput.value = button.dataset.type;

    });

});

/* ============================
   ORIGEM
============================ */

originSelect.addEventListener("change", () => {

    raffleContainer.classList.add("hidden");
    subscriptionContainer.classList.add("hidden");

    raffleSelect.value = "";
    subscriptionSelect.value = "";

    switch (originSelect.value) {

        case "RAFFLE":

            raffleContainer.classList.remove("hidden");
            break;

        case "SUBSCRIPTION":

            subscriptionContainer.classList.remove("hidden");
            break;

    }

});

/* ============================
   CARREGAR RIFAS
============================ */

async function loadRaffles() {

    try {

        const raffles =
            await raffleService.listByGroup(groupId);

        raffleSelect.innerHTML =
            `<option value="">Selecione...</option>`;

        raffles.forEach(raffle => {

            const option =
                document.createElement("option");

            option.value =
                raffle.id;

            option.textContent =
                raffle.name;

            raffleSelect.appendChild(option);

        });

    } catch (error) {

        console.error(
            "Erro ao carregar rifas",
            error
        );

    }

}

/* ============================
   CARREGAR PAGAMENTOS
============================ */

async function loadSubscriptionPayments() {

    try {

        const payments =
            await subscriptionPaymentService.listByGroup(groupId);

        subscriptionSelect.innerHTML =
            `<option value="">Selecione...</option>`;

        payments.forEach(payment => {

            const option =
                document.createElement("option");

            option.value =
                payment.id;

            option.textContent =
                `${payment.user} - ${payment.reference}`;

            subscriptionSelect.appendChild(option);

        });

    } catch (error) {

        console.error(
            "Erro ao carregar pagamentos",
            error
        );

    }

}

/* ============================
   ENVIAR MOVIMENTAÇÃO
============================ */

form.addEventListener("submit", async (event) => {

    event.preventDefault();

    errorMessage.classList.add("hidden");

    const value = Number(valueInput.value);

    const description = descriptionInput.value.trim();

    const type = typeInput.value;

    let raffleId = null;

    let subscriptionPaymentId = null;

    switch (originSelect.value) {

        case "RAFFLE":

            raffleId = raffleSelect.value || null;

            if (!raffleId) {

                errorMessage.textContent =
                    "Selecione uma rifa.";

                errorMessage.classList.remove("hidden");

                return;

            }

            break;

        case "SUBSCRIPTION":

            subscriptionPaymentId =
                subscriptionSelect.value || null;

            if (!subscriptionPaymentId) {

                errorMessage.textContent =
                    "Selecione um pagamento de mensalidade.";

                errorMessage.classList.remove("hidden");

                return;

            }

            break;

    }

    if (value <= 0) {

        errorMessage.textContent =
            "O valor deve ser maior que zero.";

        errorMessage.classList.remove("hidden");

        return;

    }

    if (!description) {

        errorMessage.textContent =
            "Informe uma descrição.";

        errorMessage.classList.remove("hidden");

        return;

    }

    button.disabled = true;

    button.textContent = "Lançando...";

    try {

        await cashService.addTransaction({

            value,

            description,

            type,

            groupId,

            raffleId,

            subscriptionPaymentId

        });

        window.location.href =
            `sala.html?id=${groupId}`;

    } catch (error) {

        console.error(error);

        errorMessage.textContent =
            error.message ||
            "Não foi possível lançar a movimentação.";

        errorMessage.classList.remove("hidden");

        button.disabled = false;

        button.textContent =
            "Lançar movimentação";

    }

});

/* ============================
   INICIAR
============================ */

async function init() {

    await Promise.all([

        loadRaffles(),

        loadSubscriptionPayments()

    ]);

}

init();