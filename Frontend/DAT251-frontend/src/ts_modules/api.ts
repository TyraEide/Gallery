import type {UUID} from "node:crypto";

const base_url = import.meta.env.VITE_API_URL;

export interface User {id: UUID, username: string, email: string}

export function api_url(path:string){
    return base_url + path;
}

export function set_jwt_token(token:string){
    localStorage.setItem("token", token)
}

export function jwt_token_header(){
    const token     = localStorage.getItem("token");
    const headers   = new Headers({});
    headers.set("content-type", "application/json");
    headers.set("Authorization", "Bearer " + token);
    return headers
}

export function set_logged_in_user(user:any) {
    localStorage.setItem("user", user)
}

export function get_logged_in_user(): User | null {
    const user = localStorage.getItem("user");
    if (user) {
        try {
            return JSON.parse(user);
        } catch (e) {
            console.log("Failed to parse user from local storage: ", e);
            return null;
        }
    }
    return null;
}