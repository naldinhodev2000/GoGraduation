function decodeToken(token) {
    const payload = token.split(".")[1];
    const normalized = payload.replace(/-/g, "+").replace(/_/g, "/");
    return JSON.parse(atob(normalized));
}

export function getCurrentUserId() {
    const token = localStorage.getItem("token");
    if (!token) {
        return null;
    }
    const payload = decodeToken(token);
    return payload.sub || payload.userId || payload.id || null;
}
