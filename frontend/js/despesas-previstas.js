import expenseService from "./services/expenseService.js";
import authService from "./services/authService.js";


/* ============================
   AUTENTICAÇÃO
============================ */


if (!authService.isAuthenticated()) {

    window.location.href =
        "login.html";

}



const params =
    new URLSearchParams(
        window.location.search
    );


const groupId =
    params.get("id");



if (!groupId) {

    window.location.href =
        "salas.html";

}



/* ============================
   ELEMENTOS
============================ */


const form =
    document.getElementById(
        "form-despesa"
    );


const descriptionInput =
    document.getElementById(
        "description"
    );


const valueInput =
    document.getElementById(
        "value"
    );


const errorMessage =
    document.getElementById(
        "form-error"
    );


const backLink =
    document.getElementById(
        "back-link"
    );



backLink.addEventListener(
    "click",
    (event)=>{

        event.preventDefault();

        window.history.back();

    }
);



/* ============================
   CADASTRAR DESPESA
============================ */


form.addEventListener(
    "submit",
    async(event)=>{


        event.preventDefault();


        errorMessage.classList.add(
            "hidden"
        );



        const description =
            descriptionInput.value.trim();



        const value =
            Number(
                valueInput.value
            );



        if(!description){


            errorMessage.textContent =
                "Informe a descrição da despesa.";


            errorMessage.classList.remove(
                "hidden"
            );


            return;

        }



        if(value <= 0){


            errorMessage.textContent =
                "O valor deve ser maior que zero.";


            errorMessage.classList.remove(
                "hidden"
            );


            return;

        }



        const button =
            document.getElementById(
                "button-salvar"
            );



        button.disabled = true;


        button.textContent =
            "Salvando...";



        try {


            console.log(
                "Enviando despesa:",
                {
                    groupId,
                    description,
                    value
                }
            );



            const response =
                await expenseService.create({

                    groupId,

                    description,

                    value

                });



            console.log(
                "Resposta backend:",
                response
            );



            window.history.back();



        } catch(error){



            console.error(
                "Erro ao salvar despesa:",
                error
            );



            errorMessage.textContent =

                error.message ||
                "Não foi possível cadastrar despesa.";



            errorMessage.classList.remove(
                "hidden"
            );



            button.disabled =
                false;



            button.textContent =
                "Salvar despesa";

        }


    }

);