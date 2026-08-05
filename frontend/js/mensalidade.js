import monthlyFeeService from "./services/monthlyFeeService.js";
import authService from "./services/authService.js";


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


const form =
    document.getElementById("form-mensalidade");


const errorMessage =
    document.getElementById("form-error");


const feeList =
    document.getElementById("fee-list");


const feeEmpty =
    document.getElementById("fee-empty");


const backLink =
    document.getElementById("back-link");



backLink.href =
    `sala.html?id=${groupId}`;



/* ============================
   FORMATAR DATA
============================ */


function formatDate(dateStr) {


    if (!dateStr) {

        return "Sem data final";

    }


    const [year, month, day] =
        dateStr.split("-");


    return `${day}/${month}/${year}`;

}



/* ============================
   FORMATAR VALOR
============================ */


function formatMoney(value) {


    return Number(value).toLocaleString(
        "pt-BR",
        {
            style: "currency",
            currency: "BRL"
        }
    );

}



/* ============================
   CARREGAR MENSALIDADES
============================ */


async function loadFees() {


    try {


        feeList.innerHTML = "";

        feeEmpty.classList.add("hidden");



        const fees =
            await monthlyFeeService.listByGroupId(groupId);



        if (!fees || fees.length === 0) {


            feeEmpty.classList.remove("hidden");

            return;

        }




        fees.forEach(fee => {



            const item =
                document.createElement("li");


            item.className =
                "fee-card";



            item.innerHTML = `

                <div class="fee-value">

                    ${formatMoney(fee.value)}

                </div>


                <div class="fee-period">

                    ${formatDate(fee.startDate)}
                    até
                    ${formatDate(fee.endDate)}

                </div>

            `;



            feeList.appendChild(item);



        });



    } catch(error) {


        feeEmpty.textContent =
            error.message ||
            "Não foi possível carregar as mensalidades.";


        feeEmpty.classList.remove("hidden");


    }


}



/* ============================
   CRIAR MENSALIDADE
============================ */


form.addEventListener(
    "submit",
    async(event)=>{


    event.preventDefault();



    errorMessage.classList.add("hidden");



    const value =
        Number(document.getElementById("value").value);



    const startDate =
        document.getElementById("start-date").value;



    const endDate =
        document.getElementById("end-date").value || null;




    if(value <= 0){


        errorMessage.textContent =
            "O valor deve ser maior que zero.";


        errorMessage.classList.remove("hidden");


        return;

    }




    const button =
        document.getElementById("button-criar");



    button.disabled = true;


    button.textContent =
        "Criando...";




    try {



        await monthlyFeeService.create({

            groupId,

            value,

            startDate,

            endDate

        });



        window.location.href =
            `sala.html?id=${groupId}`;




    } catch(error) {



        errorMessage.textContent =
            error.message ||
            "Não foi possível criar a mensalidade.";


        errorMessage.classList.remove("hidden");



        button.disabled = false;


        button.textContent =
            "Criar mensalidade";


    }



});



/* ============================
   INICIAR
============================ */


loadFees();