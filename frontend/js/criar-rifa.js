@import url('https://fonts.googleapis.com/css2?family=Sarala:wght@400;700&display=swap');


* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    font-family: "Sarala", sans-serif;
}


body {

    min-height: 100vh;

    background-color: #ffffff;

    color: #000;

}



/* ============================
   HEADER
============================ */


#header-content {

    display: flex;

    align-items: center;

    gap: 16px;

    padding: 16px 20px;

}


#back-link {

    text-decoration: none;

    color: #000;

    font-size: 15px;

    font-weight: 600;

}


#header-content h1 {

    font-size: 20px;

    font-weight: 700;

}



/* ============================
   CONTEÚDO
============================ */


#main-content {

    display: flex;

    justify-content: center;

    padding: 40px 20px;

}



.card {

    width: 100%;

    max-width: 360px;

    background: #ffffff;

}



.card h2 {

    text-align: center;

    font-size: 22px;

    margin-bottom: 32px;

}



/* ============================
   FORMULÁRIO
============================ */


#form-rifa {

    display: flex;

    flex-direction: column;

}



#form-rifa label {

    font-size: 14px;

    margin-bottom: 6px;

}



.form-item {

    width: 100%;

    height: 42px;

    border: none;

    border-radius: 10px;

    background-color: #9fb1af;

    padding: 0 12px;

    margin-bottom: 18px;

    font-size: 16px;

    color: #000;

    outline: none;

}



.form-item::placeholder {

    color: #333;

}



/* ============================
   ERRO
============================ */


#form-error {

    color: #b00020;

    font-size: 13px;

    margin-bottom: 12px;

}



.hidden {

    display: none;

}



/* ============================
   BOTÃO
============================ */


#button-criar {

    height: 42px;

    border: none;

    border-radius: 22px;

    background-color: #e0e0e0;

    color: #000;

    font-size: 15px;

    font-weight: 700;

    cursor: pointer;

    transition: 0.2s;

}



#button-criar:hover {

    background-color: #d0d0d0;

}



#button-criar:disabled {

    opacity: 0.6;

    cursor: not-allowed;

}



/* ============================
   MOBILE
============================ */


@media(max-width:600px){


    #main-content {

        padding: 30px 16px;

    }


    .card {

        max-width: 100%;

    }


}