//const API_BASE_URL = "http://localhost:8080";
const API_BASE_URL = "https://api-bora-formar.onrender.com";

function getToken() {
    return localStorage.getItem("token");
}

function setToken(token) {
    localStorage.setItem("token", token);
}

function clearToken() {
    localStorage.removeItem("token");
}

function friendlyMessage(status, rawMessage) {
    const known = {
        400: "Dados inválidos. Verifique os campos e tente novamente.",
        401: "Sua sessão expirou. Faça login novamente.",
        403: "Você não tem permissão para realizar esta ação.",
        404: "Não foi possível encontrar o que você procurava.",
        409: "Não foi possível concluir por um conflito de dados.",
        422: "Dados inválidos. Verifique os campos e tente novamente.",
        500: "Ocorreu um erro no servidor. Tente novamente em instantes.",
        502: "O servidor está indisponível no momento.",
        503: "O servidor está indisponível no momento."
    };

    if (known[status]) {
        return known[status];
    }

    if (status >= 500) {
        return "Ocorreu um erro no servidor. Tente novamente em instantes.";
    }

    if (rawMessage && rawMessage.length < 150 && !rawMessage.includes("Exception")) {
        return rawMessage;
    }

    return "Não foi possível concluir a solicitação. Tente novamente.";
}

async function request(endpoint, options = {}) {
    const token = getToken();

    const headers = {
        "Content-Type": "application/json",
        ...(token ? { "Authorization": `Bearer ${token}` } : {}),
        ...options.headers
    };

    let response;
    try {
        response = await fetch(`${API_BASE_URL}${endpoint}`, {
            ...options,
            headers
        });
    } catch (networkError) {
        console.error("Erro de rede:", networkError);
        throw new Error("Não foi possível conectar ao servidor. Verifique sua conexão.");
    }

    if (response.status === 204) {
        return null;
    }

    let data = null;
    const contentType = response.headers.get("content-type");
    if (contentType && contentType.includes("application/json")) {
        data = await response.json().catch(() => null);
    }

    if (!response.ok) {
        const rawMessage = data && (data.message || data.error);


        console.error(`[API] ${response.status} em ${endpoint}:`, rawMessage || "(sem corpo)");

        if (response.status === 401) {
            clearToken();
        }

        throw new Error(friendlyMessage(response.status, rawMessage));
    }

    return data;
}

const apiClient = {
    get(endpoint) {
        return request(endpoint, { method: "GET" });
    },
    post(endpoint, body) {
        return request(endpoint, { method: "POST", body: body !== undefined ? JSON.stringify(body) : undefined });
    },
    put(endpoint, body) {
        return request(endpoint, { method: "PUT", body: body !== undefined ? JSON.stringify(body) : undefined });
    },
    delete(endpoint) {
        return request(endpoint, { method: "DELETE" });
    },
    patch(endpoint, body) {
        return request(endpoint, { method: "PATCH", body: body !== undefined ? JSON.stringify(body) : undefined });
    },
    getToken,
    setToken,
    clearToken
};

export default apiClient;
