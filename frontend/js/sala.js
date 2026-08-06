import cashService from "./services/cashService.js";
import authService from "./services/authService.js";
import groupService from "./services/groupService.js";
import raffleService from "./services/raffleService.js";
import expenseService from "./services/expenseService.js";


/* ============================
   AUTENTICAÇÃO
============================ */

if (!authService.isAuthenticated()) {

    window.location.href = "login.html";

}


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


const menuToggle =
    document.getElementById("menu-toggle");


const sideMenu =
    document.getElementById("side-menu");


const menuOverlay =
    document.getElementById("menu-overlay");


const logoutButton =
    document.getElementById("logout-button");



const goalValue =
    document.getElementById("goal-value");


const balanceValue =
    document.getElementById("balance-value");



const expenseValue =
    document.getElementById("expense-value");


const projectedBalance =
    document.getElementById("projected-balance");



const groupName =
    document.getElementById("group-name");


const groupToken =
    document.getElementById("group-token");



const copyTokenButton =
    document.getElementById("copy-token-button");



const raffleList =
    document.getElementById("raffle-list");


const raffleEmpty =
    document.getElementById("raffle-empty");


const raffleTemplate =
    document.getElementById("raffle-template");



const expenseList =
    document.getElementById("expense-list");


const expenseEmpty =
    document.getElementById("expense-empty");


const expenseTemplate =
    document.getElementById("expense-template");



const errorMessage =
    document.getElementById("error-message");



/* ============================
   MENU
============================ */


menuToggle.addEventListener("click", () => {


    sideMenu.classList.toggle("open");


    menuOverlay.classList.toggle("open");


});



menuOverlay.addEventListener("click", () => {


    sideMenu.classList.remove("open");


    menuOverlay.classList.remove("open");


});



logoutButton.addEventListener("click", () => {


    authService.logout();


    window.location.href =
        "login.html";


});
/* ============================
   NAVEGAÇÃO
============================ */


document.getElementById("link-membros")
.addEventListener("click", () => {


    window.location.href =
        `membros.html?id=${groupId}`;


});



document.getElementById("link-lancar")
.addEventListener("click", () => {


    window.location.href =
        `lancar-movimentacao.html?id=${groupId}`;


});



document.getElementById("link-mensalidade")
.addEventListener("click", () => {


    window.location.href =
        `mensalidade.html?id=${groupId}`;


});



document.getElementById("link-expenses")
.addEventListener("click", () => {


    window.location.href =
        `despesas-previstas.html?id=${groupId}`;


});



document.getElementById("link-rifa")
.addEventListener("click", () => {


    window.location.href =
        `criar-rifa.html?id=${groupId}`;


});





/* ============================
   COPIAR TOKEN
============================ */


copyTokenButton.addEventListener("click", async () => {


    try {


        await navigator.clipboard.writeText(
            groupToken.textContent
        );


        copyTokenButton.textContent =
            "✅ Copiado!";



        setTimeout(() => {


            copyTokenButton.textContent =
                "📋 Copiar";


        }, 1500);



    } catch {


        alert(
            "Não foi possível copiar o código."
        );


    }


});





/* ============================
   DASHBOARD
============================ */


async function loadDashboard() {


    try {


        const dashboard =
            await cashService.getRemainingToGoal(
                groupId
            );



        goalValue.textContent =

            `R$ ${Number(dashboard.goal)
                .toLocaleString("pt-BR", {

                    minimumFractionDigits: 2,

                    maximumFractionDigits: 2

                })}`;




        balanceValue.textContent =

            `R$ ${Number(dashboard.balance)
                .toLocaleString("pt-BR", {

                    minimumFractionDigits: 2,

                    maximumFractionDigits: 2

                })}`;



    } catch(error) {


        errorMessage.textContent =

            error.message ||
            "Não foi possível carregar o dashboard.";


        errorMessage.classList.remove(
            "hidden"
        );


    }


}






/* ============================
   DADOS DA SALA
============================ */


async function loadGroupInfo() {


    try {


        const group =
            await groupService.getById(
                groupId
            );



        groupName.textContent =
            group.name;



        groupToken.textContent =
            group.token;



    } catch(error) {


        groupName.textContent =
            "Sala";



        groupToken.textContent =
            "--";



        errorMessage.textContent =

            error.message ||
            "Não foi possível carregar a sala.";



        errorMessage.classList.remove(
            "hidden"
        );


    }


}
/* ============================
   RIFAS
============================ */


async function loadRaffles() {


    try {


        raffleList.innerHTML = "";


        raffleEmpty.classList.add(
            "hidden"
        );



        const raffles =
            await raffleService.listByGroup(
                groupId
            );



        if (!raffles || raffles.length === 0) {


            raffleEmpty.classList.remove(
                "hidden"
            );


            return;


        }



        raffles.forEach((raffle) => {



            const clone =
                raffleTemplate.content.cloneNode(
                    true
                );



            clone.querySelector(
                ".raffle-name"
            ).textContent =
                raffle.name;



            clone.querySelector(
                ".raffle-price"
            ).textContent =

                `R$ ${Number(raffle.value)
                    .toLocaleString("pt-BR", {

                        minimumFractionDigits: 2,

                        maximumFractionDigits: 2

                    })} por número`;



            const card =
                clone.querySelector(
                    ".raffle-card"
                );



            card.addEventListener(
                "click",
                () => {

                    console.log(
                        raffle.id
                    );

                }
            );



            raffleList.appendChild(
                clone
            );


        });



    } catch(error) {


        raffleEmpty.classList.remove(
            "hidden"
        );


        raffleEmpty.innerHTML = `

            <div class="empty-icon">
                ⚠️
            </div>

            <h3>
                Erro
            </h3>

            <p>
                Não foi possível carregar as rifas.
            </p>

        `;


    }


}





/* ============================
   DESPESAS PREVISTAS
============================ */


async function loadExpenses() {


    try {


        expenseList.innerHTML = "";


        expenseEmpty.classList.add(
            "hidden"
        );



        const expenses =
            await expenseService.listByGroupId(
                groupId
            );



        let totalExpenses = 0;



        if (!expenses || expenses.length === 0) {


            expenseValue.textContent =
                "R$ 0,00";


            projectedBalance.textContent =
                balanceValue.textContent;


            expenseEmpty.classList.remove(
                "hidden"
            );


            return;

        }



        expenses.forEach((expense)=>{


            totalExpenses +=
                Number(expense.value);



            const clone =
                expenseTemplate.content
                .cloneNode(true);



            clone.querySelector(
                ".expense-name"
            ).textContent =
                expense.description;



            clone.querySelector(
                ".expense-value"
            ).textContent =

                `R$ ${
                    Number(expense.value)
                    .toLocaleString("pt-BR",{
                        minimumFractionDigits:2,
                        maximumFractionDigits:2
                    })
                }`;



            expenseList.appendChild(
                clone
            );


        });



        expenseValue.textContent =

            `R$ ${
                totalExpenses
                .toLocaleString("pt-BR",{
                    minimumFractionDigits:2,
                    maximumFractionDigits:2
                })
            }`;




        const dashboard =
            await cashService.getRemainingToGoal(
                groupId
            );



        const projected =
            Number(dashboard.balance)
            -
            totalExpenses;



        projectedBalance.textContent =

            `R$ ${
                projected
                .toLocaleString("pt-BR",{
                    minimumFractionDigits:2,
                    maximumFractionDigits:2
                })
            }`;



    } catch(error) {


        console.log(
            "Erro despesas:",
            error
        );


    }

}




/* ============================
   INICIALIZAÇÃO
============================ */


async function initializePage() {


    await loadDashboard();


    await loadGroupInfo();


    await loadRaffles();


    await loadExpenses();


}



initializePage();