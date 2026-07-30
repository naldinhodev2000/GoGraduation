const API_BASE_URL = "http://localhost:8080";

function getToken() {
    return localStorage.getItem("token");
}

function setToken(token) {
    localStorage.setItem("token", token);
}

function clearToken() {
    localStorage.removeItem("token");
}

async function request(endpoint, options = {}) {
    const token = getToken();

    let formattedEndpoint = endpoint.trim();
    if (!formattedEndpoint.startsWith("/")) {
        formattedEndpoint = `/${formattedEndpoint}`;
    }
    if (formattedEndpoint.length > 1 && formattedEndpoint.endsWith("/")) {
        formattedEndpoint = formattedEndpoint.slice(0, -1);
    }

    const headers = { ...options.headers };

    if (token) {
        headers["Authorization"] = `Bearer ${token}`;
    }

    if (options.body) {
        headers["Content-Type"] = "application/json";
    }

    const response = await fetch(`${API_BASE_URL}${formattedEndpoint}`, {
        ...options,
        headers
    });

    if (response.status === 204) {
        return null;
    }

    let data = null;
    const contentType = response.headers.get("content-type");
    if (contentType && contentType.includes("application/json")) {
        data = await response.json().catch(() => null);
    }

    if (!response.ok) {
        const message = (data && (data.message || data.error)) || `Erro ${response.status} ao acessar ${formattedEndpoint}`;

        if (response.status === 401) {
            clearToken();
        }

        throw new Error(message);
    }

    return data;
}

const apiClient = {
    get(endpoint) {
        return request(endpoint, { method: "GET" });
    },
    post(endpoint, body) {
        return request(endpoint, {
            method: "POST",
            body: body ? JSON.stringify(body) : JSON.stringify({}) // Envia {} vazio em vez de undefined se não houver body
        });
    },
    put(endpoint, body) {
        return request(endpoint, {
            method: "PUT",
            body: body ? JSON.stringify(body) : JSON.stringify({})
        });
    },
    delete(endpoint) {
        return request(endpoint, { method: "DELETE" });
    },
    getToken,
    setToken,
    clearToken
};

export default apiClient;
