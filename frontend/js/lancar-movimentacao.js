import cashService from "./services/cashService.js";
import raffleService from "./services/raffleService.js";
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

const raffleSelect = document.getElementById("raffle");

const backLink = document.getElementById("back-link");

const valueInput = document.getElementById("value");

const descriptionInput = document.getElementById("description");



backLink.href = `sala.html?id=${groupId}`;



/* ============================
   TIPO ENTRADA / SAÍDA
============================ */


typeButtons.forEach(button => {


    button.addEventListener("click", () => {


        typeButtons.forEach(btn => {

            btn.classList.remove("selected");

        });


        button.classList.add("selected");


        typeInput.value =
            button.dataset.type;


    });


});



/* ============================
   CARREGAR RIFAS
============================ */


async function loadRaffles() {


    try {


        const raffles =
            await raffleService.listByGroup(groupId);



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


        console.log(
            "Não foi possível carregar rifas",
            error
        );


    }


}



/* ============================
   ENVIAR MOVIMENTAÇÃO
============================ */


form.addEventListener("submit", async(event)=>{


    event.preventDefault();


    errorMessage.classList.add("hidden");



    const value =
        Number(valueInput.value);



    const description =
        descriptionInput.value.trim();



    const type =
        typeInput.value;



    const raffleId =
        raffleSelect.value || null;




    if(value <= 0){


        errorMessage.textContent =
            "O valor deve ser maior que zero.";


        errorMessage.classList.remove("hidden");


        return;

    }



    if(!description){


        errorMessage.textContent =
            "Informe uma descrição.";


        errorMessage.classList.remove("hidden");


        return;

    }




    const button =
        document.getElementById("button-lancar");



    button.disabled = true;

    button.textContent =
        "Lançando...";



    try {



        await cashService.addTransaction({

            value,

            description,

            type,

            groupId,

            raffleId,

            subscriptionPaymentId: null

        });



        window.location.href =
            `sala.html?id=${groupId}`;




    } catch(error){



        errorMessage.textContent =
            error.message ||
            "Não foi possível lançar movimentação.";


        errorMessage.classList.remove("hidden");



        button.disabled = false;


        button.textContent =
            "Lançar movimentação";


    }



});



/* ============================
   INICIAR
============================ */


loadRaffles();