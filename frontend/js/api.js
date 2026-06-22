const API_URL = "http://localhost:8080/api";

async function makeRequest(endpoint, method = 'GET', data = null) {
    const token = localStorage.getItem('authToken'); 
    
    const config = {
        method: method,
        headers: {
            'Content-Type': 'application/json',
            ...(token && { 'Authorization': `Bearer ${token}` })
        }
    };

    if (data && method !== 'GET' && method !== 'DELETE') {
        config.body = JSON.stringify(data);
    }

    try {
        const response = await fetch(`${API_URL}${endpoint}`, config);

        if (response.status === 401 || response.status === 403) {
            localStorage.removeItem('authToken');
            window.location.replace('/index.html'); 
            return null;
        }

        if (response.status === 204) {
            return { success: true };
        }

        const responseData = await response.json();

        if (!response.ok) {
            throw new Error(responseData.message || `Server error: ${response.status}`);
        }

        return responseData;

    } catch (error) {
        console.error("[API Error]:", error.message);
        alert("A connection error occurred. Please try again later.");
        return null;
    }
}